package renue.fts.gateway.admin.autotest.web.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import renue.fts.gateway.admin.autotest.scenarios.ScenariosDescription;
import renue.fts.gateway.admin.autotest.service.TesterService;

import java.io.IOException;


/**
 * Controller for uploading file from file system.
 */
//CHECKSTYLE:OFF
@Controller
public class FileUploadController {
    private static final Logger log = Logger.getLogger(FileUploadController.class);

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
        log.info("Получили файл с морды");
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                log.info("Запускаем парсер yml");
                ScenariosDescription scenariosDescription = mapper.readValue(bytes, ScenariosDescription.class);
                log.info("Парсинг yml завершен успешно");
                log.info("Начинаем обработку сценария, для отправки");
                testerService.startProcess(scenariosDescription);


                model.addAttribute("uploadResult","Вы удачно загрузили YML конфиг. Нажмите на кнопку Обновить, через несколько мгновений, чтобы увидеть лог ответа.");
                return "newLogger";
            } catch (IOException e) {
                log.error("ошибка обработки данных",e);
                model.addAttribute("uploadResult","Вам не удалось загрузить YML конфиг. Возникла какая-то ошибка. Все тщательно проверте и отправте конфиг снова.");
                return "newLogger";
            }
        } else {
            model.addAttribute("uploadResult","Вам не удалось загрузить YML конфиг, потому что файл пуст.");
            return "newLogger";
        }
    }

}
