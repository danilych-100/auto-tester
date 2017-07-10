package renue.fts.gateway.admin.autotest.web.controller;

//import org.springframework.stereotype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Danil on 06.07.2017.
 */
//CHECKSTYLE:OFF

@Controller
public class FileUploadController {

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") final MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources\\application.yml")));
                stream.write(bytes);
                stream.close();
                return "Вы удачно загрузили " + "application.yml" + " в " +"C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources\\application.yml";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + "application.yml" + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + "application.yml" + " потому что файл пустой.";
        }
    }

}
