package renue.fts.gateway.admin.autotest.service;

import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.documentutil.RequestDocumentHeaderChecker;
import renue.fts.gateway.admin.autotest.scenarios.Body;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class DocumentCreator {

    /**
     * Create Document.
     * @param currentStep
     * @return
     */
    public BaseDocType createDocument(final Step currentStep) {
        RequestDocumentHeaderChecker requestDocumentHeaderChecker = new RequestDocumentHeaderChecker(
                currentStep.getRequest().getHeader().getGwHeader().getMessageType());

        BaseDocType baseDocType = requestDocumentHeaderChecker.getDocumentTypeByHeader();

        baseDocType.setRefDocumentID(UUID.randomUUID().toString());
        baseDocType.setDocumentID(UUID.randomUUID().toString());

        fillBaseDocType(baseDocType,currentStep.getRequest().getBody());

        return baseDocType;
    }


    /**
     * Fill BaseDocType.
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
}
