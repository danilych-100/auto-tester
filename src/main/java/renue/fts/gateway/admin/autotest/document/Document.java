package renue.fts.gateway.admin.autotest.document;

import lombok.Data;
import org.joda.time.DateTime;
import renue.fts.gateway.admin.autotest.config.TestStepConfig;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Danil on 10.07.2017.
 */
@Data
public class Document {

    private TestStepConfig testStepConfig;

    private HeaderType headerType = new HeaderType();
    private BodyType bodyType = new BodyType();

    public Document(final TestStepConfig testStepConfig) {
        this.testStepConfig = testStepConfig;
        setUpHeaderFromConfig();
        setUpBodyFromConfig();
    }

    /**
     * SetUp Body.
     */
    private void setUpBodyFromConfig() {
        DocumentHeaderChecker documentHeaderChecker = new DocumentHeaderChecker(testStepConfig.getSteps().getStep1()
                .getRequest()
                .getHeader()
                .getMessageType());

        BaseDocType baseDocType = documentHeaderChecker.getDocumentTypeByHeader();
        baseDocType.setDocumentID(testStepConfig.getSteps().getStep1()
                .getRequest()
                .getBody()
                .getDocumentID());
        baseDocType.setRefDocumentID(testStepConfig.getSteps().getStep1()
                .getRequest()
                .getBody()
                .getRefDocumentID());
        fillBaseDocType(baseDocType, testStepConfig.getSteps().getStep1().getRequest().getBody());

        bodyType.setAnyList(Arrays.<Object>asList(baseDocType));
    }

    /**
     * Fill BaseDOcType.
     *
     * @param baseDocType
     * @param body
     */
    private void fillBaseDocType(final BaseDocType baseDocType, final Body body) {
        for (Field baseField : baseDocType.getClass().getDeclaredFields()) {
            baseField.setAccessible(true);
            try {
                for (Field bodyField : body.getClass().getDeclaredFields()) {
                    bodyField.setAccessible(true);
                    if (bodyField.get(body) != null) {
                        if (baseField.getName().equals(bodyField.getName())) {
                            baseField.set(baseDocType, bodyField.get(body));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * SetUp Header.
     */
    private void setUpHeaderFromConfig() {
        GWHeaderType gwHeader = new GWHeaderType();
        String mt = testStepConfig.getSteps().getStep1()
                .getRequest()
                .getHeader()
                .getMessageType();
        String ib = testStepConfig.getSteps().getStep1()
                .getRequest()
                .getHeader()
                .getInfoBrokerId();
        gwHeader.setMessageType(mt);
        gwHeader.setInfoBrokerId(ib);

        RoutingInfType routingInf = new RoutingInfType();
        routingInf.setEnvelopeID("21231312");
        routingInf.setInitialEnvelopeID("131234");
        routingInf.setSenderInformation("wmq://RU.FTS.GW/TEST.INCOME");
        routingInf.setReceiverInformationList(Collections.singletonList("wmq://RU.FTS.ASVD.EPS/GW.EPS.DECL.FROM"));
        routingInf.setPreparationDateTime(DateTime.parse("2017-03-01"));

        headerType.setAnyList(Arrays.<Object>asList(routingInf,gwHeader));
    }


}
