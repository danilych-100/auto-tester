package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private Map<String, ValidationResult> processingResult;
    private Map<String,EnvelopeType> responseEvelopeDocument = new HashMap<>();

    public Map<String,EnvelopeType> getResponseEvelopeDocument() {
        return responseEvelopeDocument;
    }

    public Map<String, ValidationResult> getProcessingResult() {
        return processingResult;
    }


    /**
     * Start process.
     *
     * @param scenariosDescription Description of transaction steps.
     */
    public void startProcess(final ScenariosDescription scenariosDescription) throws IOException {
        processingResult = new HashMap<>();
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

        EnvelopeType envelope = createEnvelope();
        messageSender.sendMessage(envelope);
    }

    /**
     * create Envelope doc.
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
     * Process REsponse.
     *
     * @param envelopeType Handle response EnvelopeType document.
     */
    public void processResponse(final EnvelopeType envelopeType) throws IOException, IllegalAccessException {
        if (currentStep == null) {
            System.out.println(
                    "При прошлой передаче транзакций, ход выполнения программы был прерван. Прием сообщений остановлен.");
            return;
        }
        this.responseEvelopeDocument.put(currentStep.getName(),envelopeType);
        processingResult
                .put(currentStep.getName(), responseValidator.validate(currentStep.getResponse(), envelopeType));
        if (processingResult.get(currentStep.getName()).isValid()) {
            processStep();
        }
    }


}
