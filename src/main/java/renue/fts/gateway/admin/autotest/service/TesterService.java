package renue.fts.gateway.admin.autotest.service;

import org.apache.log4j.Logger;
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
import renue.fts.gateway.admin.autotest.web.controller.ResposeDocumentController;
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

    private static final Logger log = Logger.getLogger(TesterService.class);

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
    private HashMap<String, ValidationResult> responseInfo;

    public VariableContainer getVariableContainer() {
        return variableContainer;
    }

    private Iterator<Step> stepIterator;
    private Step currentStep;
    private Iterator<Response> responseIterator;
    private Response currentResponse;
    private EnvelopeType currentRequestDocument;

    public boolean isHasNextRespronse() {
        return hasNextRespronse;
    }

    private boolean hasNextRespronse = false;
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
        responseInfo = new HashMap<>();

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
        BodyType signedDocument = signatureService.sign(doc);
        return envelopeCreator.createEnvelope(signedDocument, currentStep);
    }

    /**
     * Process Response.
     *
     * @param envelopeType Handle responseInfo EnvelopeType document.
     */
    public void processResponse(final EnvelopeType envelopeType) throws IOException, IllegalAccessException,
            InterruptedException {
        log.info("Начинаем обрабатывать ответ");
        if (currentStep == null) {
            log.warn("При прошлой передаче транзакций была ошибка");
            return;
        }
        if (!responseIterator.hasNext()) {
            System.out.println("Ожидаемых ответов нет.");
            return;
        }
        hasNextRespronse = false;
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

        ValidationResult validationResult = validateResponse(envelopeType, response);
        responseInfo.put(response.getResponseName() == null ? "Без названия" : response
                .getResponseName(), validationResult);

        for(int index=0;index<processingResult.size();index++){
            if(processingResult.get(index).getTransactionName().equals(currentStep.getName())){
                processingResult.get(index).setResponseInfo(responseInfo);
                return;
            }
        }
        TransactionInfo transactionInfo = new TransactionInfo(
                currentStep.getName() == null ? "Без названия" : currentStep.getName(), responseInfo);

        processingResult.add(transactionInfo);
        this.responseEnvelopeDocument.put(response.getResponseName(), envelopeType);

        if (validationResult.isValid()) {
            if (responseIterator.hasNext()) {
                hasNextRespronse = true;
                waitForSecondResponse();
                return;
            }
            processStep();
        }
    }

    /**
     * @param envelopeType
     * @param response
     * @return
     * @throws IllegalAccessException
     */
    private ValidationResult validateResponse(final EnvelopeType envelopeType, final Response response) throws
            IllegalAccessException {
        log.info("Начинаем обрабатвать ответ");
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
        log.info("Закончили обрабатвать ответ");
        return validationResult;
    }

    /**
     * @throws InterruptedException
     */
    private void waitForSecondResponse() throws InterruptedException {
        System.out.println("Waiting For Response ...");
        Thread.sleep(50000);
        if (responseIterator.hasNext()) {
            ValidationResult badResult = new ValidationResult();
            badResult.setValidationTime(DateTime.now());
            badResult.setValid(false);
           // HashMap<String, ValidationResult> responseInfo = new HashMap<>();
            responseInfo.put("Ответ не пришел", badResult);

            for(int index=0;index<processingResult.size();index++){
                if(processingResult.get(index).getTransactionName().equals(currentStep.getName())){
                    processingResult.get(index).setResponseInfo(responseInfo);
                    return;
                }
            }
            processingResult.add(new TransactionInfo(currentStep.getName(),responseInfo));
        }
    }


}
