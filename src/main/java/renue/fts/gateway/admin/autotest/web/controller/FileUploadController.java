package renue.fts.gateway.admin.autotest.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    String handleFileUpload(@RequestParam("file") final MultipartFile file, final Model model) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                ScenariosDescription scenariosDescription = mapper.readValue(bytes, ScenariosDescription.class);
                testerService.startProcess(scenariosDescription);


                model.addAttribute("uploadResult","Вы удачно загрузили YML конфиг. Нажмите на кнопку Обновить, через несколько мгновений, чтобы увидеть лог ответа.");
                return "newLogger";
            } catch (Exception e) {
                model.addAttribute("uploadResult","Вам не удалось загрузить YML конфиг. Возникла какая-то ошибка. Все тщательно проверте и отправте конфиг снова.");
                return "newLogger";
            }
        } else {
            model.addAttribute("uploadResult","Вам не удалось загрузить YML конфиг, потому что файл пуст.");
            return "newLogger";
        }
    }

}
