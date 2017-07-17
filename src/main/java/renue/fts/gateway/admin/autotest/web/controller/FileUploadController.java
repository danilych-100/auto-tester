package renue.fts.gateway.admin.autotest.web.controller;

//import org.springframework.stereotype.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.service.TesterService;


/**
 * Controller for uploading file from file system.
 */
@Controller
public class FileUploadController {

    @Autowired
    private TesterService testerService;

    /**
     * Mapped string info to /upload.
     * @return string information about providing upload.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    /**
     * Mapping of uploading.
     *
     * @param file File from file system.
     * @return Information about upload result.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") final MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                ScenariosDescription scenariosDescription = mapper.readValue(bytes, ScenariosDescription.class);

                String st = bytes.toString();
                testerService.startProcess(scenariosDescription);

                return "Вы удачно загрузили " + "application.yml"+"<br>"+
                        "Чтобы увидеть лог вернитесь на главную страницу(localhost:8080) и нажмите Check log for ... или перейдите localhost:8080/log" +"<br>"+
                        "Чтобы увидеть весь ответный документ вернитесь на главную страницу(localhost:8080) и нажмите Check all response ... или перейдите localhost:8080/responseDoc";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + "application.yml" + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + "application.yml" + " потому что файл пустой.";
        }
    }

}
