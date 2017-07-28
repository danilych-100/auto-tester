package renue.fts.gateway.admin.autotest.service;

import org.javatuples.Triplet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import renue.fts.gateway.admin.autotest.scenarios.Response;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import renue.fts.gateway.admin.autotest.transaction.TransactionInfo;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Service for processing steps.
 */
@Service
public class TesterService {

    @Autowired
    private MyMessageSender messageSender;
    @Autowired
    private DocumentCreator documentCreator;
    @Autowired
    private SignatureService signatureService;
    @Autowired
    private ResponseValidator responseValidator;
    @Autowired
    private EnvelopeCreator envelopeCreator;

    @Autowired
    private VariableContainer variableContainer;

    public VariableContainer getVariableContainer() {
        return variableContainer;
    }

    private Iterator<Step> stepIterator;
    private Step currentStep;
    private Iterator<Response> responseIterator;
    private Response currentResponse;
    private EnvelopeType currentRequestDocument;

    private List<TransactionInfo> processingResult;

    public List<TransactionInfo> getProcessingResult() {
        return processingResult;
    }

    private Map<String, EnvelopeType> responseEnvelopeDocument = new HashMap<>();

    public Map<String, EnvelopeType> getResponseEnvelopeDocument() {
        return responseEnvelopeDocument;
    }


    /**
     * Start process.
     *
     * @param scenariosDescription Description of transaction steps.
     */
    public void startProcess(final ScenariosDescription scenariosDescription) throws IOException {
        processingResult = new ArrayList<>();
        stepIterator = scenariosDescription.getSteps()
                .values()
                .iterator();
        processStep();
    }

    /**
     * Process step.
     */
    private void processStep() throws IOException {
        if (!stepIterator.hasNext()) {
            System.out.println("Больше транзакций нет!");
            return;
        }

        currentStep = stepIterator.next();
        responseIterator = currentStep.getResponses().iterator();
        EnvelopeType envelope = createEnvelope();
        currentRequestDocument = envelope;

        messageSender.sendMessage(envelope);
    }

    /**
     * Create Envelope doc.
     *
     * @return EnvelopeType document.
     * @throws IOException throw IO exception.
     */
    private EnvelopeType createEnvelope() throws IOException {
        BaseDocType doc = documentCreator.createDocument(currentStep);
        System.out.println("norm6");
        BodyType signedDocument = signatureService.sign(doc);
        System.out.println("norm66");
        return envelopeCreator.createEnvelope(signedDocument, currentStep);
    }

    /**
     * Process Response.
     *
     * @param envelopeType Handle response EnvelopeType document.
     */
    public void processResponse(final EnvelopeType envelopeType) throws IOException, IllegalAccessException,
            InterruptedException {
        if (currentStep == null) {
            System.out.println(
                    "При прошлой передаче транзакций, ход выполнения программы был прерван. Прием сообщений остановлен.");
            return;
        }
        if (!responseIterator.hasNext()) {
            System.out.println("Ожидаемых ответов нет.");
            return;
        }
        currentResponse = responseIterator.next();
        proccessResponse(envelopeType, currentResponse);
    }

    /**
     * ProcessResponses.
     *
     * @param envelopeType
     * @param response
     */
    private void proccessResponse(final EnvelopeType envelopeType, final Response response) throws
            IllegalAccessException, IOException, InterruptedException {


        ValidationResult validationResult = responseValidator.validate(response, envelopeType);
        validationResult.setValidationTime(DateTime.now());

        String requestEnvelopeID = ((RoutingInfType) currentRequestDocument.getHeader().getAnyList().get(0))
                .getEnvelopeID();
        String responseInitialEnvelopeID = ((RoutingInfType) envelopeType.getHeader().getAnyList().get(0))
                .getInitialEnvelopeID();
        if (!requestEnvelopeID.equals(responseInitialEnvelopeID)) {
            validationResult.setValid(false);
            validationResult.getFieldResult().put("envelopeID",
                                                  " EnvelopeID не совпадает с InitialEnvelopeID: EnvelopeID: " + requestEnvelopeID + " InitialEnvelopeID: " + responseInitialEnvelopeID);
        }


        TransactionInfo transactionInfo = new TransactionInfo(currentStep.getName(), response.getResponseName(),
                                                              validationResult);
        processingResult.add(transactionInfo);
        this.responseEnvelopeDocument.put(response.getResponseName(), envelopeType);

        if (validationResult.isValid()) {
            if (responseIterator.hasNext()) {
                System.out.println("Waiting For Response ...");
                Thread.sleep(50000);
                if (responseIterator.hasNext()) {
                    ValidationResult badResult = new ValidationResult();
                    badResult.setValidationTime(DateTime.now());
                    processingResult
                            .add(new TransactionInfo(currentStep.getName(), "Not all response here", badResult));
                }
                return;

            }
            processStep();
        }
    }

}
