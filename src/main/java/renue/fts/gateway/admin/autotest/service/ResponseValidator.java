package renue.fts.gateway.admin.autotest.service;


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
                if (expectedField.getName() != responseField.getName()) {
                    continue;
                }
                expectedField.setAccessible(true);
                responseField.setAccessible(true);
                try {
                    Object expFieldValue = expectedField.get(expectedResponse.getBody());
                    Object respFieldvalue = responseField.get(baseDocType);
                    if (expFieldValue instanceof ArrayList) {
                        if (respFieldvalue instanceof ArrayList) {
                            validateListField(validationResult,
                                              (ArrayList) expFieldValue,
                                              (ArrayList) respFieldvalue);
                        }
                        continue;
                    }
                    if (expFieldValue == null) {
                        continue;
                    }
                    if (expFieldValue.equals(respFieldvalue)) {
                        validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
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
            for (Field expectedListField : expFieldValue.get(index)
                    .getClass()
                    .getDeclaredFields()) {

                Field responseListField = ReflectionUtils.findField(respFieldvalue.get(
                        index).getClass(), expectedListField.getName());
                expectedListField.setAccessible(true);
                responseListField.setAccessible(true);

                if (expectedListField.get(expFieldValue.get(index))
                        .equals(responseListField.get(respFieldvalue.get(index)))) {
                    validationResult.getFieldResult()
                            .put(expectedListField.getName(), "Совпадение с ожидаемым");
                } else {
                    validationResult.setValid(false);
                    validationResult.getFieldResult()
                            .put(expectedListField.getName(),
                                 "Ожидалось: " + expectedListField.get(expFieldValue.get(index)) + " Пришло: " +
                                         (String) responseListField.get(
                                                 respFieldvalue.get(index)));
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
                                 final ValidationResult validationResult) {
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
                                        final ValidationResult validationResult) {
        for (Field expectedField : expectedRoutingInf.getClass().getSuperclass().getDeclaredFields()) {
            Field responseField = ReflectionUtils.findField(responseRoutingInf.getClass(), expectedField.getName());
            expectedField.setAccessible(true);
            responseField.setAccessible(true);
            try {
                Object expFieldValue = expectedField.get(expectedRoutingInf);
                Object respFieldvalue = responseField.get(responseRoutingInf);
                if (expFieldValue == null) {
                    continue;
                }
                if (expFieldValue.equals(respFieldvalue)) {
                    validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
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

    /**
     * ValidateGWHeaderType.
     *
     * @param expectedGWHeader
     * @param responseGWHeader
     * @param validationResult
     */
    private void validateGWHeaderType(final GWHeaderType expectedGWHeader,
                                      final GWHeaderType responseGWHeader,
                                      final ValidationResult validationResult) {
        for (Field expectedField : expectedGWHeader.getClass().getDeclaredFields()) {
            expectedField.setAccessible(true);
            for (Field responseField : responseGWHeader.getClass().getDeclaredFields()) {
                responseField.setAccessible(true);
                if (expectedField.getName().equals(responseField.getName())) {
                    try {
                        if (expectedField.get(expectedGWHeader).equals(responseField.get(responseGWHeader))) {
                            validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                        } else {
                            validationResult.setValid(false);
                            validationResult.getFieldResult()
                                    .put(expectedField.getName(),
                                         "Ожидалось: " + expectedField.get(expectedGWHeader) + " Пришло: " +
                                                 (String) responseField.get(responseGWHeader));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
