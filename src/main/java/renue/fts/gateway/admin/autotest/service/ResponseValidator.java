package renue.fts.gateway.admin.autotest.service;


import com.sun.webkit.dom.RangeImpl;
import helper.EnvelopeHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.stereotype.Service;
import org.w3c.dom.ranges.Range;
import renue.fts.gateway.admin.autotest.documentutil.RequestDocumentHeaderChecker;
import renue.fts.gateway.admin.autotest.scenarios.Header;
import renue.fts.gateway.admin.autotest.scenarios.Response;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.ApplicationInfType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;
import ru.kontur.fts.eps.schemas.marshaller.EnvelopeMarshaller;
import ru.kontur.fts.eps.schemas.marshaller.EnvelopeUnmarshaller;

import java.io.File;
import java.lang.reflect.Field;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class ResponseValidator {
    /**
     * validate response.
     *
     * @param response
     * @param envelopeType
     * @return
     */
    public ValidationResult validate(final Response response, final EnvelopeType envelopeType) throws
            IllegalAccessException {
        ValidationResult validationResult = new ValidationResult();
        validateHeaders(response, envelopeType.getHeader(), validationResult);

        RequestDocumentHeaderChecker documentHeaderChecker = new RequestDocumentHeaderChecker(EnvelopeHelper.getGWHeaderType(
                envelopeType.getHeader()).getMessageType());
        ru.kontur.fts.eps.schemas.admin.intexchcommonaggregatetypescust.BaseDocType document = EnvelopeHelper.getDocument(
                envelopeType.getBody());


       /* Field[] fields = response.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            final int indexField = i;
            fields[indexField].setAccessible(true);
            for (Field highResponseField : fields[indexField].get(response).getClass().getDeclaredFields()) {
                highResponseField.setAccessible(true);
                for (Field expectedResponseField : highResponseField.get(response.getHeader())
                        .getClass()
                        .getDeclaredFields()) {
                    expectedResponseField.setAccessible(true);
                    for (Object element : envelopeType.getHeader().getAnyList()) {
                        Arrays.stream(element.getClass().getDeclaredFields()).forEach(field -> {

                            field.setAccessible(true);

                            if (expectedResponseField.getName() == field.getName()) {
                                try {
                                    validationResult.setValid(expectedResponseField.get(fields[indexField]) == field.get(
                                            element));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }*/

        return validationResult;
    }


    /**
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
     * @param expectedRoutingInf
     * @param responseRoutingInf
     * @param validationResult
     */
    private void validateRoutingInfType(final RoutingInfType expectedRoutingInf,
                                        final RoutingInfType responseRoutingInf,
                                        final ValidationResult validationResult) {
        Field[] expectedFields = expectedRoutingInf.getClass().getDeclaredFields();
        for (Field expectedField : expectedFields) {
            expectedField.setAccessible(true);
            for (Field responseField : responseRoutingInf.getClass().getDeclaredFields()) {
                responseField.setAccessible(true);
                if (expectedField.getName().equals(responseField.getName())) {
                    try {
                        if (expectedField.get(expectedRoutingInf) == responseField.get(responseRoutingInf)) {
                            validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                        } else {

                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
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
                        if (expectedField.get(expectedGWHeader) == responseField.get(responseGWHeader)) {
                            validationResult.getFieldResult().put(expectedField.getName(), "Совпадение с ожидаемым");
                        } else {

                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
