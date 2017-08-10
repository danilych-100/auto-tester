package renue.fts.gateway.admin.autotest.web.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import renue.fts.gateway.admin.autotest.service.TesterService;
import renue.fts.gateway.admin.autotest.transaction.TransactionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danil on 31.07.2017.
 */
@Controller
public class NewLoggerController {

    @Autowired
    private TesterService testerService;

    @RequestMapping(value = "/newLogger")
    String logResult(final HttpServletResponse response, final HttpServletRequest req, final Model model) {
        String scheme = req.getScheme();
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();
        response.setHeader("Refresh", "60; "+"URL="+scheme+"://"+serverName+":"+serverPort+"/newLogger");

        List<TransactionInfo> transactionInfos = testerService.getProcessingResult();
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("transactionInfos",transactionInfos);
        model.addAllAttributes(input);
        model.addAttribute("hasNextResponse",testerService.isHasNextRespronse());

        return "newLogger";
    }
}
