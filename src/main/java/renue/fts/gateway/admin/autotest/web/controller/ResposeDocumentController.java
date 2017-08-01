package renue.fts.gateway.admin.autotest.web.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import renue.fts.gateway.admin.autotest.service.TesterService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Map;

/**
 * Response Document controller.
 */
@Controller
public class ResposeDocumentController {

    @Autowired
    private TesterService testerService;

    @Autowired
    private MarshallingService marshaller;

    /**
     * Mapped /responseDoc for output responseInfo document.
     *
     * @param httpResponse
     * @return
     */
    @RequestMapping(value = "/responseDoc")
    public @ResponseBody
    String logResult(final HttpServletResponse httpResponse) {

        String output = "";
        for (Map.Entry response : testerService.getResponseEnvelopeDocument().entrySet()) {
            output += "Сценарий :  " + response.getKey() + " <br> ";
            output += "Пришедший документ : " + " <br> ";
            try {
                String xmlString = getTransformedEnvelopDoc((EnvelopeType) response.getValue());
                output += "<pre>" + StringEscapeUtils.escapeHtml4(xmlString) + "</pre>" +"<br>";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
        if (output.equals("")) {
            return "Пока пришедших документов нет. Ожидайте...";
        }

        output+="<form action=\"/newLogger\">\n" +
                "        <input type=\"submit\" value=\"Посмотреть лог\" />\n" +
                "    </form>\n" +
                "    <form action=\"/\">\n" +
                "        <input type=\"submit\" value=\"Назад в главное меню\" />\n" +
                "    </form>";
        return output;
    }

    /**
     * Get Envelope document transformed into String.
     *
     * @param envelopeType
     * @return
     * @throws TransformerException
     * @throws IOException
     */
    private String getTransformedEnvelopDoc(final EnvelopeType envelopeType) throws TransformerException,
            IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(new StreamSource(new ByteArrayInputStream(marshaller.marshall(envelopeType))), result);
        String xmlString = result.getWriter().toString();
        return xmlString;
    }
}
