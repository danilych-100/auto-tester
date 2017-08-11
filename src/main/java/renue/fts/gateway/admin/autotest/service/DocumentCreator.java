package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.documentutil.RequestDocumentHeaderChecker;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.documentvariable.VariableType;
import renue.fts.gateway.admin.autotest.scenarios.Body;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import renue.fts.gateway.admin.autotest.utils.ReflectionUtility;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Create BaseDocType document.
 */
@Service
class DocumentCreator {

    @Autowired
    private VariableContainer variableContainer;

    /**
     * Create Document.
     *
     * @param currentStep Current step of transaction.
     * @return BaseDocType document.
     */
    BaseDocType createDocument(final Step currentStep) {
        RequestDocumentHeaderChecker requestDocumentHeaderChecker = new RequestDocumentHeaderChecker(
                currentStep.getRequest().getHeader().getGwHeader().getMessageType());

        BaseDocType baseDocType = requestDocumentHeaderChecker.getDocumentTypeByHeader();
        fillBaseDocType(baseDocType, currentStep.getRequest().getBody());
        baseDocType.setDocumentID(UUID.randomUUID().toString());
        baseDocType.setRefDocumentID(UUID.randomUUID().toString());

        return baseDocType;
    }


    /**
     * Fill BaseDocType.
     *
     * @param baseDocType Creating BaseDocType document.
     * @param body        Body from configuration.
     */
    private void fillBaseDocType(final BaseDocType baseDocType, final Body body) {
        Field[] baseFields = ReflectionUtility.getAllFields(baseDocType.getClass(), new ArrayList<>());
        for (Field baseField : baseFields) {
            baseField.setAccessible(true);
            try {
                for (Field bodyField : body.getClass().getDeclaredFields()) {
                    bodyField.setAccessible(true);
                    if (bodyField.get(body) != null) {
                        if (baseField.getName().equals(bodyField.getName())) {
                            try {
                                DocumentVariable documentVariable = variableContainer
                                        .getDocumentVariableFromContainer((String) bodyField.get(body));
                                if (documentVariable != null) {
                                    baseField.set(baseDocType, documentVariable.getValue());
                                    continue;
                                }
                            } catch (Exception e) {
                                baseField.set(baseDocType, bodyField.get(body));
                                continue;
                            }
                            baseField.set(baseDocType, bodyField.get(body));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
