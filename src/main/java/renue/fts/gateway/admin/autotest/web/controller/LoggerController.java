package renue.fts.gateway.admin.autotest.web.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.service.TesterService;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Logger conroller.
 */
@Controller
public class LoggerController {

    @Autowired
    private TesterService testerService;


    /**
     * Controller for /log uri. Show log depends on response result.
     *
     * @return html string.
     */
    @RequestMapping(value = "/log")
    public @ResponseBody
    String logResult(final HttpServletResponse response) {
        response.setHeader("Refresh", "3; URL=http://localhost:8080/log");

        Map<String, ValidationResult> validationResult = testerService.getProcessingResult();
        if (validationResult == null) {
            return "Current time: "+DateTime.now().toString() + ": <br>"
                    + " Пока логгировать нечего. Отправте файл. Или проверте корректность введенных данных";
        }

        StringBuilder webLog = new StringBuilder(
                DateTime.now().toString() + ": <br>" + " <br>" + "Список переменныx: " + " <br>");

        for (Map.Entry variables : testerService.getVariableContainer().getDocumentVariables().entrySet()) {
            webLog.append("<br> " + "Переменная:  ").append(variables.getKey()).append(" со значением:  ")
                    .append(((DocumentVariable) variables.getValue()).getValue()).append(" <br>");
        }

        for (Map.Entry entryStepResponse : validationResult.entrySet()) {
            webLog.append(" <br> " + "Transaction Name: ").append(entryStepResponse.getKey()).append(" <br>")
                    .append(" <br>");

            Map<String, String> resultFields = ((ValidationResult) entryStepResponse.getValue()).getFieldResult();
            for (Map.Entry entry : resultFields.entrySet()) {
                webLog.append(entry.getKey()).append(":    ").append(entry.getValue()).append("<br>");
            }
        }
        if(validationResult.containsKey("Not all response here")){
            webLog.append("Не все ожидаемые ответы пришли.").append("<br>");
        }
        return webLog.toString();
    }
}
