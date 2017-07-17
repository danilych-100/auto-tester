package renue.fts.gateway.admin.autotest.service;


import org.springframework.beans.factory.annotation.Autowired;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.documentvariable.VariableType;
import renue.fts.gateway.admin.autotest.helper.EnvelopeHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import renue.fts.gateway.admin.autotest.scenarios.Body;
import renue.fts.gateway.admin.autotest.scenarios.Response;
import renue.fts.gateway.admin.autotest.utils.ReflectionUtility;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.*;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Response validation. A lot of reflection API.
 */
@Service
class ResponseValidator {

    @Autowired
    private VariableContainer variableContainer;

    /**
     * validate expectedResponse.
     *
     * @param expectedResponse Expected Response from configuration.
     * @param envelopeType     Responses EnvelopeType document.
     * @return Validation result.
     */
    ValidationResult validate(final Response expectedResponse, final EnvelopeType envelopeType) throws
            IllegalAccessException {
        ValidationResult validationResult = new ValidationResult();

        SignatureType responseDocumentSignatureType = EnvelopeHelper.getDocumentSignatureType(envelopeType.getBody());
        validateHeaders(expectedResponse, envelopeType.getHeader(), validationResult);

        if (expectedResponse.getBody() != null) {
            validateDocument(expectedResponse.getBody(),
                             (BaseDocType) responseDocumentSignatureType.getObject().getAny(),
                             validationResult);
        }
       /* if (expectedResponse.getBody().getSignature() != null) {
            validateSignatureInfo(expectedResponse.getBody().getSignature().getSignedInfoType(),
                                  responseDocumentSignatureType.getSignedInfo(), validationResult);

            validateKeyInfo(expectedResponse.getBody().getSignature().getKeyInfo(),
                            responseDocumentSignatureType.getKeyInfo(), validationResult);

            if (expectedResponse.getBody().getSignature().getSignatureValue() == responseDocumentSignatureType
                    .getSignatureValue()) {
                validationResult.getFieldResult().put("signatureValue", "Совпадение с ожидаемым");
            } else{
                validationResult.setValid(false);
                validationResult.getFieldResult()
                        .put("signatureValue",
                             "Ожидалось: " + expectedResponse.getBody().getSignature().getSignatureValue()
                                     + " Пришло: " + responseDocumentSignatureType.getSignatureValue());
            }
        }*/

        return validationResult;
    }

    /**
     * Validate keyInfoType.
     * @param expectedResponse
     * @param keyInfo
     * @param validationResult
     */
    private void validateKeyInfo(final KeyInfoType expectedResponse, final KeyInfoType keyInfo,
                                 final ValidationResult validationResult) throws
            IllegalAccessException {
        for (Field expectedField : ReflectionUtility.getAllFields(expectedResponse.getClass(), new ArrayList<>())) {

            Field responseField = ReflectionUtils.findField(keyInfo.getClass(), expectedField.getName());
            expectedField.setAccessible(true);
            responseField.setAccessible(true);
            if (Objects.equals(expectedField.getName(), "JiBX_bindingList") || Objects
                    .equals(expectedField.get(expectedResponse), null)) {
                continue;
            }

            if (expectedField.get(expectedResponse).equals(responseField.get(keyInfo))) {
                validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
            } else if (DocumentVariable.isReceivedDocumentVariable((String) expectedField.get(expectedResponse))) {
                String varName = ((String) expectedField.get(expectedResponse)).split("\\.")[1]
                        .replaceFirst("\\)", "");
                variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE,
                                                                   (String) expectedField.get(expectedResponse)));
            } else {
                validationResult.setValid(false);
                validationResult.getFieldResult().put(expectedField.getName(),
                                                      "Ожидалось: " + expectedField
                                                              .get(expectedResponse) + " Пришло: " +
                                                              responseField.get(keyInfo));
            }
        }
    }

    /**
     * Validate SignInfoType.
     * @param expectedResponse
     * @param signedInfo
     * @param validationResult
     */
    private void validateSignatureInfo(final SignedInfoType expectedResponse, final SignedInfoType signedInfo,
                                       final ValidationResult validationResult) throws IllegalAccessException {

        for (Field expectedField : ReflectionUtility.getAllFields(expectedResponse.getClass(), new ArrayList<>())) {

            Field responseField = ReflectionUtils.findField(signedInfo.getClass(), expectedField.getName());
            expectedField.setAccessible(true);
            responseField.setAccessible(true);

            Object expectedFieldValue = expectedField.get(expectedResponse);
            Object responseFieldValue = responseField.get(signedInfo);
            if (Objects.equals(expectedField.getName(), "JiBX_bindingList") || Objects
                    .equals(expectedFieldValue, null)) {
                continue;
            }
            try {
                if (expectedFieldValue.equals(responseFieldValue)) {
                    validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                } else if (DocumentVariable.isReceivedDocumentVariable((String) expectedFieldValue)) {
                    String varName = ((String) expectedFieldValue).split("\\.")[1]
                            .replaceFirst("\\)", "");
                    variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE,
                                                                       (String) expectedFieldValue));
                } else {
                    validationResult.setValid(false);
                    validationResult.getFieldResult().put(expectedField.getName(),
                                                          "Ожидалось: " + expectedFieldValue + " Пришло: " +
                                                                  responseFieldValue);
                }
            } catch (ClassCastException e) {
                validationResult.setValid(false);
                validationResult.getFieldResult().put(expectedField.getName(),
                                                      "Ожидалось: " + expectedFieldValue + " Пришло: " +
                                                              responseFieldValue);
            }

        }

    }

    /**
     * Validate Body document.
     *
     * @param expectedResponseBody Expected Response from configuration.
     * @param baseDocType          Responses BaseDocType document.
     * @param validationResult     Validation result.
     */
    private void validateDocument(final Body expectedResponseBody,
                                  final BaseDocType baseDocType,
                                  final ValidationResult validationResult) {
        for (Field expectedField : ReflectionUtility
                .getAllFields(expectedResponseBody.getClass(), new ArrayList<>())) {
            for (Field responseField : ReflectionUtility.getAllFields(baseDocType.getClass(), new ArrayList<>())) {
                if (!Objects.equals(expectedField.getName(), responseField.getName()) ||
                        Objects.equals(expectedField.getName(), "JiBX_bindingList")) {
                    continue;
                }
                expectedField.setAccessible(true);
                responseField.setAccessible(true);
                try {
                    Object expFieldValue = expectedField.get(expectedResponseBody);
                    Object respFieldvalue = responseField.get(baseDocType);
                    if (expFieldValue instanceof ArrayList) {
                        if (respFieldvalue instanceof ArrayList) {
                            validateListField(validationResult, (ArrayList) expFieldValue, (ArrayList) respFieldvalue);
                        }
                        continue;
                    }
                    if (expFieldValue == null) {
                        continue;
                    }
                    if (expFieldValue.equals(respFieldvalue)) {
                        validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                    } else if (DocumentVariable.isReceivedDocumentVariable((String) expFieldValue)) {

                        String varName = ((String) expFieldValue).split("\\.")[1].replaceFirst("\\)", "");
                        variableContainer.addVariable(
                                new DocumentVariable(varName, VariableType.RECEIVE, (String) respFieldvalue));
                        validationResult.getFieldResult().put(expectedField.getName(),
                                                              "  Это переменная:  " + varName + " Пришла: " + respFieldvalue);
                    } else {
                        validationResult.setValid(false);
                        validationResult.getFieldResult()
                                .put(expectedField.getName(),
                                     "Ожидалось: " + expFieldValue + " Пришло: " + respFieldvalue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Валидируем поля, которые являются списками.
     * Берем поля которые нам нужны из ожидаемых и сравниваем с соответсвующими в пришедшем.
     *
     * @param validationResult Validation result.
     * @param expFieldValue    List of expected field from config.
     * @param respFieldvalue   List of responses field from config.
     * @throws IllegalAccessException throw exception.
     */
    private void validateListField(final ValidationResult validationResult,
                                   final ArrayList expFieldValue,
                                   final ArrayList respFieldvalue) throws IllegalAccessException {
        for (int index = 0; index < expFieldValue.size(); index++) {
            for (Field expectedListField : ReflectionUtility
                    .getAllFields(expFieldValue.get(index).getClass(), new ArrayList<>())) {

                Field responseListField = ReflectionUtils
                        .findField(respFieldvalue.get(index).getClass(), expectedListField.getName());
                expectedListField.setAccessible(true);
                responseListField.setAccessible(true);
                if (Objects.equals(expectedListField.getName(), "JiBX_bindingList")) {
                    continue;
                }
                if (expectedListField.get(expFieldValue.get(index))
                        .equals(responseListField.get(respFieldvalue.get(index)))) {
                    validationResult.getFieldResult().put(expectedListField.getName(), "Совпадение с ожидаемым");

                } else if (DocumentVariable
                        .isReceivedDocumentVariable((String) expectedListField.get(expFieldValue.get(index)))) {
                    String varName = ((String) expectedListField.get(expFieldValue.get(index))).split("\\.")[1]
                            .replaceFirst("\\)", "");
                    variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE,
                                                                       (String) responseListField
                                                                               .get(respFieldvalue.get(index))));

                } else {
                    validationResult.setValid(false);
                    validationResult.getFieldResult().put(expectedListField.getName(),
                                                          "Ожидалось: " + expectedListField
                                                                  .get(expFieldValue.get(index)) + " Пришло: " +
                                                                  responseListField.get(respFieldvalue.get(index)));
                }
            }
        }
    }

    /**
     * Validate headers.
     *
     * @param expectedResponse   Expected Response from configuration.
     * @param responseHeaderType Response headers from configuration.
     * @param validationResult   Validation results.
     */
    private void validateHeaders(final Response expectedResponse,
                                 final HeaderType responseHeaderType,
                                 final ValidationResult validationResult) throws IllegalAccessException {
        RoutingInfType respRoutingInfType = EnvelopeHelper.getRoutingInf(responseHeaderType);
        GWHeaderType respGWHeaderType = EnvelopeHelper.getGWHeaderType(responseHeaderType);
        if (expectedResponse.getHeader().getRoutingInf() != null) {
            validateRoutingInfType(expectedResponse.getHeader().getRoutingInf(), respRoutingInfType, validationResult);
        }
        if (expectedResponse.getHeader().getGwHeader() != null) {
            validateGWHeaderType(expectedResponse.getHeader().getGwHeader(), respGWHeaderType, validationResult);
        }
    }

    /**
     * Validate RoutingInfType.
     *
     * @param expectedRoutingInf Expected Routing Information from configuration.
     * @param responseRoutingInf Response Routing Information from configuration.
     * @param validationResult   Validation results.
     */
    private void validateRoutingInfType(final RoutingInfType expectedRoutingInf,
                                        final RoutingInfType responseRoutingInf,
                                        final ValidationResult validationResult) throws IllegalAccessException {
        for (Field expectedField : ReflectionUtility.getAllFields(expectedRoutingInf.getClass(), new ArrayList<>())) {
            Field responseField = ReflectionUtils.findField(responseRoutingInf.getClass(), expectedField.getName());
            expectedField.setAccessible(true);
            responseField.setAccessible(true);
            Object expFieldValue = expectedField.get(expectedRoutingInf);
            Object respFieldvalue = responseField.get(responseRoutingInf);
            if (expFieldValue == null || Objects.equals(expectedField.getName(), "JiBX_bindingList")) {
                continue;
            }
            if (expFieldValue.equals(respFieldvalue)) {
                validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
            } else if (DocumentVariable.isReceivedDocumentVariable((String) expFieldValue)) {
                String varName = ((String) expFieldValue).split("\\.")[1].replaceFirst("\\)", "");
                variableContainer.addVariable(new DocumentVariable(varName,
                                                                   VariableType.RECEIVE,
                                                                   (String) respFieldvalue));
                validationResult.getFieldResult()
                        .put(expectedField.getName(),
                             "  Это переменная: " + varName + " Пришла: " + respFieldvalue);

            } else {
                validationResult.setValid(false);
                validationResult.getFieldResult()
                        .put(expectedField.getName(), "Ожидалось: " + expFieldValue + " Пришло: " + respFieldvalue);
            }
        }
    }


    /**
     * ValidateGWHeaderType.
     *
     * @param expectedGWHeader Expected GWHeaders from configuration.
     * @param responseGWHeader Response GWHeaders from configuration.
     * @param validationResult Validation results.
     */
    private void validateGWHeaderType(final GWHeaderType expectedGWHeader,
                                      final GWHeaderType responseGWHeader,
                                      final ValidationResult validationResult) throws IllegalAccessException {
        for (Field expectedField : ReflectionUtility.getAllFields(expectedGWHeader.getClass(), new ArrayList<>())) {
            expectedField.setAccessible(true);
            for (Field responseField : ReflectionUtility.getAllFields(responseGWHeader.getClass(), new ArrayList<>())) {
                responseField.setAccessible(true);
                if (expectedField.getName().equals(responseField.getName()) &&
                        !Objects.equals(expectedField.getName(), "JiBX_bindingList")
                        && !Objects.equals(responseField.getName(), "JiBX_bindingList")) {

                    if (DocumentVariable.isReceivedDocumentVariable((String) expectedField.get(expectedGWHeader))) {
                        String varName = ((String) expectedField.get(expectedGWHeader)).split("\\.")[1]
                                .replaceFirst("\\)", "");
                        variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE,
                                                                           (String) responseField
                                                                                   .get(responseGWHeader)));
                        validationResult.getFieldResult()
                                .put(expectedField.getName(), "  Это переменная: " + varName + " Пришла: "
                                        + responseField.get(responseGWHeader));
                        continue;
                    }
                    if (expectedField.get(expectedGWHeader).equals(responseField.get(responseGWHeader))) {
                        validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                    } else {
                        validationResult.setValid(false);
                        validationResult.getFieldResult().put(expectedField.getName(),
                                                              "Ожидалось: " + expectedField
                                                                      .get(expectedGWHeader) + " Пришло: " + responseField
                                                                      .get(responseGWHeader));
                    }
                }
            }
        }
    }
}
