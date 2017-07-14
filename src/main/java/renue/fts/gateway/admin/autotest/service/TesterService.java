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
import java.util.List;
import java.util.Map;

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
    private ResponseValidator responseValidator;
    @Autowired
    private EnvelopeCreator envelopeCreator;
    @Autowired
    private VariableContainer variableContainer;


    private Iterator<Step> stepIterator;
    private Step currentStep;
    private  Map<String,ValidationResult> processingResult;
    private ScenariosDescription scenariosDescription;

    public Map<String,ValidationResult> getProcessingResult() {
        return processingResult;
    }


    /**
     * Start process.
     * @param scenariosDescription
     */
    //CHECKSTYLE:OFF
    public void startProcess(final ScenariosDescription scenariosDescription) throws IOException {
        this.scenariosDescription=scenariosDescription;
        processingResult = new HashMap<>();
        stepIterator=scenariosDescription.getSteps()
                .values()
                .iterator();
        processStep();
    }

    /**
     * Process step.
     */
    private void processStep() throws IOException {
        if(!stepIterator.hasNext()){
            System.out.println("Больше транзакций нет!");
            return;
        }

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
    public void processResponse(final EnvelopeType envelopeType) throws IOException, IllegalAccessException {
        if(currentStep == null){
            System.out.println("При прошлой передаче транзакций, ход выполнения программы был прерван. Прием сообщений остановлен.");
            return;
        }
        processingResult.put(currentStep.getName(),responseValidator.validate(currentStep.getResponse(), envelopeType));

        variableContainer.getDocumentVariables().values().forEach(el-> System.out.println(el.getName()+":  "+el.getValue()+ "  Type: "+el.getVariableType()));

        if(processingResult.get(currentStep.getName()).isValid()) {
            processStep();
        }
        else{
            /*System.out.println("Ошибка валидации: ");
            System.out.println(processingResult.getFieldResult().toString());
            processingResult.getFieldResult().forEach((name,field)-> System.out.println(name+": "+field));*/
            return;
        }
    }
}
