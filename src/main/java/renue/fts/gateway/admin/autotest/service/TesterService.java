package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Danil on 12.07.2017.
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
    private EnvelopeCreator envelopeCreator;

    private Iterator<Step> stepIterator;
    private Step currentStep;
    private ValidationResult processingResult;
    private ScenariosDescription scenariosDescription;
    private ResponseValidator responseValidator;

    /**
     * Start process.
     * @param scenariosDescription
     */
    //CHECKSTYLE:OFF
    public void startProcess(final ScenariosDescription scenariosDescription) throws IOException {
        this.scenariosDescription=scenariosDescription;
        stepIterator=scenariosDescription.getSteps()
                .values()
                .iterator();
        processStep();
    }

    /**
     * Process step.
     */
    private void processStep() throws IOException {
        currentStep=stepIterator.next();

        EnvelopeType envelope = createEnvelope();
        messageSender.sendMessage(envelope);
    }

    /**
     * create Envelope doc.
     * @return
     * @throws IOException
     */
    private EnvelopeType createEnvelope() throws IOException {
        BaseDocType doc = documentCreator.createDocument(currentStep);
        BodyType signedDocument = signatureService.sign(doc);
        return envelopeCreator.createEnvelope(signedDocument, currentStep);
    }

    /**
     * Process REsponse.
     * @param envelopeType
     */
    public void processResponse(final EnvelopeType envelopeType) throws IOException {
        ValidationResult validateResult = responseValidator.validate(currentStep.getResponse(), envelopeType);
        if(validateResult.isValid()) {
            processStep();
        }
        else{
            validateResult=validateResult;
            return;
        }
    }
}
