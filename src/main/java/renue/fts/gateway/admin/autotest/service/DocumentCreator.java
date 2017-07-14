package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import renue.fts.gateway.admin.autotest.documentutil.RequestDocumentHeaderChecker;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.documentvariable.VariableContainer;
import renue.fts.gateway.admin.autotest.documentvariable.VariableType;
import renue.fts.gateway.admin.autotest.scenarios.Body;
import renue.fts.gateway.admin.autotest.scenarios.Step;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class DocumentCreator {

    @Autowired
    private VariableContainer variableContainer;

    /**
     * Create Document.
     *
     * @param currentStep
     * @return
     */
    public BaseDocType createDocument(final Step currentStep) {
        RequestDocumentHeaderChecker requestDocumentHeaderChecker = new RequestDocumentHeaderChecker(
                currentStep.getRequest().getHeader().getGwHeader().getMessageType());

        BaseDocType baseDocType = requestDocumentHeaderChecker.getDocumentTypeByHeader();
        fillBaseDocType(baseDocType, currentStep.getRequest().getBody());

        if (variableContainer.getDocumentVariables().values().stream().filter(var -> var.getName() == "documentID")
                .count() == 0) {
            baseDocType.setDocumentID(UUID.randomUUID().toString());
        }
        if (variableContainer.getDocumentVariables().values().stream().filter(var -> var.getName() == "refDocumentID")
                .count() == 0) {
            baseDocType.setRefDocumentID(UUID.randomUUID().toString());
        }

        return baseDocType;
    }


    /**
     * Fill BaseDocType.
     *
     * @param baseDocType
     * @param body
     */
    private void fillBaseDocType(final BaseDocType baseDocType, final Body body) {
        Field[] baseFields = getAllFields(baseDocType.getClass(), new ArrayList<>());
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
                                if (DocumentVariable.isGenerateDocumentVariable((String) bodyField.get(body))) {
                                    String varName = ((String) bodyField.get(body)).split("\\.")[1]
                                            .replaceFirst("\\)", "");
                                    String varValue = UUID.randomUUID().toString();
                                    variableContainer.addVariable(
                                            new DocumentVariable(varName, VariableType.GENERATED, varValue));

                                    baseField.set(baseDocType, varValue);
                                }
                            } catch (ClassCastException e) {
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

    /**
     * @param clazz
     * @param fields
     * @return
     */
    private static Field[] getAllFields(final Class clazz, final List<Field> fields) {
        if (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields.toArray(new Field[fields.size()]);
    }
}
