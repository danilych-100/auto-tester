package renue.fts.gateway.admin.autotest.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.documentvariable.VariableType;
import renue.fts.gateway.admin.autotest.scenarios.MyRoutingInfo;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class EnvelopeCreator {


    @Autowired
    private VariableContainer variableContainer;

    /**
     * Create EnvelopeType doc.
     *
     * @param signedDocument
     * @param currentStep
     * @return
     */
    public EnvelopeType createEnvelope(final BodyType signedDocument, final Step currentStep) {
        EnvelopeType envelopeType = new EnvelopeType();

        HeaderType headerType = setUpHeaderFromStep(currentStep);
        envelopeType.setHeader(headerType);
        envelopeType.setBody(signedDocument);
        return envelopeType;
    }

    /**
     * SetUp headers.
     *
     * @param currentStep
     * @return
     */
    private HeaderType setUpHeaderFromStep(final Step currentStep) {
        GWHeaderType gwHeader = new GWHeaderType();
        String mt = currentStep.getRequest().getHeader().getGwHeader().getMessageType();
        String ib = currentStep.getRequest().getHeader().getGwHeader().getInfoBrokerId();
        gwHeader.setMessageType(mt);
        gwHeader.setInfoBrokerId(ib);

        MyRoutingInfo routingInf = new MyRoutingInfo();
        routingInf.setSenderInformation(currentStep.getRequest().getHeader().getRoutingInf().getSenderInformation());
        routingInf.setReceiverInformationString(
                currentStep.getRequest().getHeader().getRoutingInf().getReceiverInformationString());
        routingInf.setPreparationDateTime(DateTime.now());

        checkAndSetupRoutingInfoVariables(routingInf, currentStep.getRequest().getHeader().getRoutingInf(),
                                          "EnvelopeID");
        checkAndSetupRoutingInfoVariables(routingInf, currentStep.getRequest().getHeader().getRoutingInf(),
                                          "InitialEnvelopeID");


        HeaderType headerType = new HeaderType();
        headerType.setAnyList(Arrays.<Object>asList(routingInf, gwHeader));

        return headerType;
    }

    /**
     * Исходя из имени поля, вызываем для них гет и сет методы для установки значения в поля.
     *
     * @param requestInfType
     * @param currentRoutingInfType
     * @param nameVariableField
     */
    private void checkAndSetupRoutingInfoVariables(final RoutingInfType requestInfType,
                                                   final RoutingInfType currentRoutingInfType,
                                                   final String nameVariableField) {
        try {
            Method methodGet = ReflectionUtils
                    .findMethod(currentRoutingInfType.getClass(), "get" + nameVariableField, null);
            Method methodSet = ReflectionUtils
                    .findMethod(requestInfType.getClass(), "set" + nameVariableField, String.class);
            String inputVariable = (String) methodGet.invoke(currentRoutingInfType);
            if (inputVariable == null) {
                methodSet.invoke(requestInfType, UUID.randomUUID().toString());
                return;
            }
            DocumentVariable documentVariable = variableContainer.getDocumentVariableFromContainer(inputVariable);
            if (documentVariable != null) {
                methodSet.invoke(requestInfType, documentVariable.getValue());
                return;
            }
            String varValue = UUID.randomUUID().toString();
            methodSet.invoke(requestInfType, varValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
