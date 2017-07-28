package renue.fts.gateway.admin.autotest.web.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import renue.fts.gateway.admin.autotest.documentvariable.DocumentVariable;
import renue.fts.gateway.admin.autotest.service.TesterService;
import renue.fts.gateway.admin.autotest.transaction.TransactionInfo;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    String logResult(final HttpServletResponse response,final HttpServletRequest req) {
        String scheme = req.getScheme();
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 8090
        response.setHeader("Refresh", "10; "+"URL="+scheme+"://"+serverName+":"+serverPort+"/log");

        List<TransactionInfo> transactionInfos = testerService.getProcessingResult();
        if (transactionInfos == null) {
            return "Current time: " + DateTime.now().toString() + ": <br>"
                    + " Пока логгировать нечего. Отправте файл. Или проверте корректность введенных данных";
        }

        StringBuilder webLog = new StringBuilder(
                "Current time: " + DateTime.now().toString());

        webLog.append("<br> <h3>Транзакции: </h3>" + "<br> ");
        webLog.append("<ol>");

        for (TransactionInfo transactionInfo : transactionInfos) {

            webLog.append("<li>");
            webLog.append(" <br> " + "<u><em>Название транзакции: </em></u>").append(transactionInfo.getTransactionName())
                    .append(" <br>").append("<br>");


            webLog.append("<u>Название ожидаемого ответа: </u>").append(transactionInfo.getResponseTransactionName())
                    .append(" <br>");

            webLog.append("Дата и время обработки ответа: " + (transactionInfo.getTransactionResult())
                    .getValidationTime().toString() + " <br> ").append(" <br> ");

            if(transactionInfo.getResponseTransactionName().equals("Not all response here")){
                webLog.append("<br>").append("Не все ожидаемые ответы пришли.").append("<br>");
                break;
            }

            if(!transactionInfo.getTransactionResult().isValid()){
                webLog.append("<strong>Несовпадение с ожидаемым результатом!!!</strong>").append(" <br> ");
            }
            else {
                webLog.append("Совпадение с ожидаемым результатом").append(" <br> ");
            }
            webLog.append("<u>Поля ответного документа :</u>" + " <br> ");

            Map<String, String> resultFields = (transactionInfo.getTransactionResult())
                    .getFieldResult();
            webLog.append("<ul>");
            for (Map.Entry entry : resultFields.entrySet()) {
                webLog.append("<li>");
                webLog.append(entry.getKey()).append(":    ").append(entry.getValue()).append("<br>");
                webLog.append("</li>");
            }
            webLog.append("</ul>");
            webLog.append("</li>");

        }
        webLog.append("</ol>");
      /*  if (transactionInfos.stream()
                .filter(transactionInfo -> transactionInfo.getResponseTransactionName().equals("Not all response here"))
                .count() > 0) {
            webLog.append("Не все ожидаемые ответы пришли.").append("<br>");
        }*/
        return webLog.toString();
    }
}
