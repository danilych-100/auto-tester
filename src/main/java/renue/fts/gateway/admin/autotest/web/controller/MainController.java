package renue.fts.gateway.admin.autotest.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Danil on 31.07.2017.
 */
@Controller
public class MainController {
    /**
     *
     * @param mv
     * @return
     */
    @GetMapping("/")
    String mainPage(final ModelAndView mv) {
        return "index";
    }
}
