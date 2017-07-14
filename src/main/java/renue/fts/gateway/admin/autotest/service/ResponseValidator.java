package renue.fts.gateway.admin.autotest.service;


import org.springframework.beans.factory.annotation.Autowired;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.documentvariable.VariableType;
import renue.fts.gateway.admin.autotest.helper.EnvelopeHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import renue.fts.gateway.admin.autotest.scenarios.Response;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class ResponseValidator {

    @Autowired
    private VariableContainer variableContainer;

    /**
     * validate expectedResponse.
     *
     * @param expectedResponse
     * @param envelopeType
     * @return
     */
    public ValidationResult validate(final Response expectedResponse, final EnvelopeType envelopeType) throws
            IllegalAccessException {
        ValidationResult validationResult = new ValidationResult();
        BaseDocType responseDocument = EnvelopeHelper.getDocument(envelopeType.getBody());

        validateHeaders(expectedResponse, envelopeType.getHeader(), validationResult);
        validateDocument(expectedResponse, responseDocument, validationResult);
        return validationResult;
    }

    /**
     * Validate Body document.
     *
     * @param expectedResponse
     * @param baseDocType
     * @param validationResult
     */
    private void validateDocument(final Response expectedResponse,
                                  final BaseDocType baseDocType,
                                  final ValidationResult validationResult) {
        for (Field expectedField : expectedResponse.getBody().getClass().getDeclaredFields()) {
            for (Field responseField : baseDocType.getClass().getDeclaredFields()) {
                if (expectedField.getName() != responseField.getName() || expectedField.getName() == "JiBX_bindingList") {
                    continue;
                }
                expectedField.setAccessible(true);
                responseField.setAccessible(true);
                try {
                    Object expFieldValue = expectedField.get(expectedResponse.getBody());
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
                                                              "  Это переменная: " + varName + " Пришла: " + respFieldvalue);
                    } else {
                        validationResult.setValid(false);
                        validationResult.getFieldResult()
                                .put(expectedField.getName(), "Ожидалось: " + expFieldValue + " Пришло: " +
                                        (String) respFieldvalue);
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
     * @param validationResult
     * @param expFieldValue
     * @param respFieldvalue
     * @throws IllegalAccessException
     */
    private void validateListField(final ValidationResult validationResult,
                                   final ArrayList expFieldValue,
                                   final ArrayList respFieldvalue) throws IllegalAccessException {
        for (int index = 0; index < expFieldValue.size(); index++) {
            for (Field expectedListField : expFieldValue.get(index).getClass().getDeclaredFields()) {

                Field responseListField = ReflectionUtils.findField(respFieldvalue.get(index).getClass(), expectedListField.getName());
                expectedListField.setAccessible(true);
                responseListField.setAccessible(true);
                if(expectedListField.getName() == "JiBX_bindingList"){
                    continue;
                }
                if (expectedListField.get(expFieldValue.get(index)).equals(responseListField.get(respFieldvalue.get(index)))) {
                    validationResult.getFieldResult().put(expectedListField.getName(), "Совпадение с ожидаемым");

                } else if (DocumentVariable.isReceivedDocumentVariable((String) expectedListField.get(expFieldValue.get(index)))) {
                    String varName = ((String) expectedListField.get(expFieldValue.get(index))).split("\\.")[1].replaceFirst("\\)", "");
                    variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE, (String) responseListField.get(respFieldvalue.get(index))));

                } else {
                    validationResult.setValid(false);
                    validationResult.getFieldResult().put(expectedListField.getName(),
                                 "Ожидалось: " + expectedListField.get(expFieldValue.get(index)) + " Пришло: " +
                                          responseListField.get(respFieldvalue.get(index)));
                }
            }
        }
    }

    /**
     * Validate headers.
     *
     * @param expectedResponse
     * @param responseHeaderType
     * @param validationResult
     */
    private void validateHeaders(final Response expectedResponse,
                                 final HeaderType responseHeaderType,
                                 final ValidationResult validationResult) throws IllegalAccessException {
        RoutingInfType respRoutingInfType = EnvelopeHelper.getRoutingInf(responseHeaderType);
        GWHeaderType respGWHeaderType = EnvelopeHelper.getGWHeaderType(responseHeaderType);

        validateRoutingInfType(expectedResponse.getHeader().getRoutingInf(), respRoutingInfType, validationResult);
        validateGWHeaderType(expectedResponse.getHeader().getGwHeader(), respGWHeaderType, validationResult);
    }

    /**
     * Validate RoutingInfType.
     *
     * @param expectedRoutingInf
     * @param responseRoutingInf
     * @param validationResult
     */
    private void validateRoutingInfType(final RoutingInfType expectedRoutingInf,
                                        final RoutingInfType responseRoutingInf,
                                        final ValidationResult validationResult) throws IllegalAccessException {
        for (Field expectedField : expectedRoutingInf.getClass().getSuperclass().getDeclaredFields()) {
            Field responseField = ReflectionUtils.findField(responseRoutingInf.getClass(), expectedField.getName());
            expectedField.setAccessible(true);
            responseField.setAccessible(true);
            Object expFieldValue = expectedField.get(expectedRoutingInf);
            Object respFieldvalue = responseField.get(responseRoutingInf);
            if (expFieldValue == null || expectedField.getName() == "JiBX_bindingList") {
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
     * @param expectedGWHeader
     * @param responseGWHeader
     * @param validationResult
     */
    private void validateGWHeaderType(final GWHeaderType expectedGWHeader,
                                      final GWHeaderType responseGWHeader,
                                      final ValidationResult validationResult) throws IllegalAccessException {
        for (Field expectedField : expectedGWHeader.getClass().getDeclaredFields()) {
            expectedField.setAccessible(true);
            for (Field responseField : responseGWHeader.getClass().getDeclaredFields()) {
                responseField.setAccessible(true);
                if (expectedField.getName().equals(responseField.getName()) && expectedField.getName() != "JiBX_bindingList"
                        && responseField.getName()!= "JiBX_bindingList") {
                    if (DocumentVariable.isReceivedDocumentVariable((String) expectedField.get(expectedGWHeader))) {
                        String varName = ((String) expectedField.get(expectedGWHeader)).split("\\.")[1]
                                .replaceFirst("\\)", "");
                        variableContainer.addVariable(new DocumentVariable(varName, VariableType.RECEIVE,
                                                                           (String) responseField.get(responseGWHeader)));
                        validationResult.getFieldResult().put(expectedField.getName(), "  Это переменная: " + varName + " Пришла: "
                                        + responseField.get(responseGWHeader));
                        continue;
                    }
                    if (expectedField.get(expectedGWHeader).equals(responseField.get(responseGWHeader))) {
                        validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                    } else {
                        validationResult.setValid(false);
                        validationResult.getFieldResult().put(expectedField.getName(),
                                                              "Ожидалось: " + expectedField.get(expectedGWHeader) + " Пришло: " + responseField
                                                                      .get(responseGWHeader));
                    }
                }
            }
        }
    }
}
