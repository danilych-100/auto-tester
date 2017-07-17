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
 * Created by Danil on 17.07.2017.
 */
@Controller
public class ResposeDocumentController {

    @Autowired
    private TesterService testerService;

    @Autowired
    private MarshallingService marshaller;

    /**
     * Mapped /responseDoc for output response document.
     *
     * @param httpResponse
     * @return
     */
    @RequestMapping(value = "/responseDoc")
    public @ResponseBody
    String logResult(final HttpServletResponse httpResponse) {

        String output = "";
        for (Map.Entry response : testerService.getResponseEvelopeDocument().entrySet()) {
            output += "Transation name:  " + response.getKey() + " <br> ";
            output += "Response Document: " + " <br> ";
            try {
                String xmlString = getTransformedEnvelopDoc((EnvelopeType) response.getValue());
                output += "<pre>" + StringEscapeUtils.escapeHtml4(xmlString) + "</pre>";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
        if (output.equals("")) {
            return "Wait for response document";
        }
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
