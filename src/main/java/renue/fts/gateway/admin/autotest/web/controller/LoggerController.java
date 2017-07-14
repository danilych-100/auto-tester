package renue.fts.gateway.admin.autotest.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.service.TesterService;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;

import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by Danil on 13.07.2017.
 */
@Controller
public class LoggerController {

    @Autowired
    private TesterService testerService;


    /**
     * @return
     */
    @RequestMapping(value = "/log")
    public @ResponseBody
    String logResult(final HttpServletResponse response) {
        response.setHeader("Refresh", "3; URL=http://localhost:8080/log");

        Map<String, ValidationResult> validationResult = testerService.getProcessingResult();
        if(validationResult == null){
            return DateTime.now().toString() + ": <br>"+" Пока логгировать нечего. Отправте файл.";
        }
        String webLog = DateTime.now().toString() + ": <br>"+ " <br>";
        for (Map.Entry entryStepResponse : validationResult.entrySet()) {

            webLog+= " <br> "+"Response Name: " + entryStepResponse.getKey()+ " <br>"+ " <br>";

            Map<String,String> resultFields = ((ValidationResult) entryStepResponse.getValue()).getFieldResult();

            for (Map.Entry entry : resultFields.entrySet()) {
                webLog += (entry.getKey() + ":    " + entry.getValue() + "<br>");
            }
        }

        return webLog;
    }
}
