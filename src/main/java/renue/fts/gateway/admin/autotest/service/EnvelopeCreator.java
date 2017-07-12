package renue.fts.gateway.admin.autotest.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.scenarios.MyRoutingInfo;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class EnvelopeCreator {

    /**
     * Create EnvelopeType doc.
     * @param signedDocument
     * @param currentStep
     * @return
     */
    public EnvelopeType createEnvelope(final BodyType signedDocument,final Step currentStep) {
        EnvelopeType envelopeType = new EnvelopeType();

        HeaderType headerType = setUpHeaderFromStep(currentStep);
        envelopeType.setHeader(headerType);

        envelopeType.setBody(signedDocument);

        return envelopeType;

    }

    /**
     * SetUp headers.
     * @param currentStep
     * @return
     */
    private HeaderType setUpHeaderFromStep(final Step currentStep) {
        GWHeaderType gwHeader = new GWHeaderType();
        String mt = currentStep
                .getRequest()
                .getHeader()
                .getGwHeader()
                .getMessageType();
        String ib = currentStep
                .getRequest()
                .getHeader()
                .getGwHeader()
                .getInfoBrokerId();
        gwHeader.setMessageType(mt);
        gwHeader.setInfoBrokerId(ib);

        MyRoutingInfo routingInf = new MyRoutingInfo();
        routingInf.setSenderInformation(currentStep
                                                .getRequest()
                                                .getHeader()
                                                .getRoutingInf()
                                                .getSenderInformation());
        routingInf.setReceiverInformationString(currentStep
                                                        .getRequest()
                                                        .getHeader()
                                                        .getRoutingInf()
                                                        .getReceiverInformationString());

        routingInf.setEnvelopeID(UUID.randomUUID().toString());
        routingInf.setPreparationDateTime(DateTime.now());

        HeaderType headerType = new HeaderType();
        headerType.setAnyList(Arrays.<Object>asList(routingInf, gwHeader));

        return headerType;
    }
}
