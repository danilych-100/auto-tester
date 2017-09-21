package renue.fts.gateway.admin.autotest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jibx.JibxMarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import renue.fts.gateway.admin.autotest.dto.signDocument.OkResponse;
import renue.fts.gateway.admin.autotest.dto.signDocument.Request;
import renue.fts.gateway.admin.autotest.dto.signDocument.Response;
import renue.fts.gateway.admin.autotest.dto.signDocument.UnsignedDocument;
import renue.fts.gateway.admin.autotest.scenarios.Body;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Service for signing BaseDocType document.
 */
@Service
public class SignatureService {

    private static final Logger log = Logger.getLogger(SignatureService.class);

    @Value("${crypto.url}")
    private String cryptoURL;

    @Value("${crypto.needUse}")
    private boolean isNeedUseCrypto;

    @Autowired
    private JibxMarshaller marshaller;

    /**
     * Sign doc.
     *
     * @param doc
     * @return
     */
    //CHECKSTYLE:OFF
    public BodyType sign(final BaseDocType doc) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(out);
        try {
            log.info("Начинаем маршалить тело");
            marshaller.marshal(doc, result);
        } catch (IOException e) {
            log.error("Ошибка маршаллинга тела", e);
        }
        BodyType bodyType = new BodyType();
        log.info("Начинаем подпись тела документа");
        if (isNeedUseCrypto) {
            byte[] autoSignedDocument = autoSign(out.toByteArray());
            System.out.println(new String(autoSignedDocument));
            log.info("Начинаем анмаршалить подписанное тело документа");
            bodyType.setAnyList(Arrays.<Object>asList(marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    autoSignedDocument)))));
            log.info("Закончили анмаршалить подписанное тело документа");
        } else {
            return manualSign(out.toString());
        }
        log.info("Закончили подписывать");
        return bodyType;
    }

    private BodyType manualSign(final String doc) throws IOException {

        String signStart = "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "\t\t\t<SignedInfo>\n" +
                "\t\t\t\t<CanonicalizationMethod Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"/>\n" +
                "\t\t\t\t<Reference URI=\"#KeyInfo\">\n" +
                "\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t<Transform Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>\n" +
                "\t\t\t\t\t<DigestValue>PvAsEKzS/M/pfjZaLLauG9kz8ThzW9jDIBTHftHoClQ=</DigestValue>\n" +
                "\t\t\t\t</Reference>\n" +
                "\t\t\t\t<Reference URI=\"#Object\">\n" +
                "\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t<Transform Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>\n" +
                "\t\t\t\t\t<DigestValue>ZZeMAEjE5scWJAHWfoJ4ATjx9z+uMeBVsF0qcjc3Ack=</DigestValue>\n" +
                "\t\t\t\t</Reference>\n" +
                "\t\t\t</SignedInfo>\n" +
                "\t\t\t<SignatureValue>MIIE1AYJKoZIhvcNAQcCoIIExTCCBMECAQExDDAKBgYqhQMCAgkFADALBgkqhkiG9w0BBwExggSfMIIEmwIBATCCAbowggGjMSAwHgYJKoZIhvcNAQkBFhF2dWNAY2EuY3VzdG9tcy5ydTEaMBgGCCqFAwOBAwEBEgwwMDc3MzA2NTQ0NzExGDAWBgUqhQNkARINMTExNzc0Njg4OTk0MTF7MHkGA1UECgxy0KbQtdC90YLRgNCw0LvRjNC90L7QtSDQuNC90YTQvtGA0LzQsNGG0LjQvtC90L3Qvi3RgtC10YXQvdC40YfQtdGB0LrQvtC1INGC0LDQvNC+0LbQtdC90L3QvtC1INGD0L/RgNCw0LLQu9C10L3QuNC1MTowOAYDVQQJDDHQv9C7LtCa0L7QvNGB0L7QvNC+0LvRjNGB0LrQsNGPINC0LjEg0LrQvtGA0L8uINCQMRUwEwYDVQQHDAzQnNC+0YHQutCy0LAxGzAZBgNVBAgMEjc3INCzLtCc0L7RgdC60LLQsDELMAkGA1UEBhMCUlUxTzBNBgNVBAMMRtCT0L7Qu9C+0LLQvdC+0Lkg0YPQtNC+0YHRgtC+0LLQtdGA0Y/RjtGJ0LjQuSDRhtC10L3RgtGAINCh0JLQo9CmINCi0J4CEQLhstl6xAzOgOcR6kEwc3qEMAoGBiqFAwICCQUAoIICfDAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNzA3MjYwNzUzMDFaMC8GCSqGSIb3DQEJBDEiBCA+LD2UWAHMiu6C+QxM9N62htYDQChU/uautp49qRG3IjCCAg8GCyqGSIb3DQEJEAIvMYIB/jCCAfowggH2MIIB8jAIBgYqhQMCAgkEIIwUgzfUXWxm42tugh/0Tx6baOWMIgiYM3oV8viHFI4uMIIBwjCCAaukggGnMIIBozEgMB4GCSqGSIb3DQEJARYRdnVjQGNhLmN1c3RvbXMucnUxGjAYBggqhQMDgQMBARIMMDA3NzMwNjU0NDcxMRgwFgYFKoUDZAESDTExMTc3NDY4ODk5NDExezB5BgNVBAoMctCm0LXQvdGC0YDQsNC70YzQvdC+0LUg0LjQvdGE0L7RgNC80LDRhtC40L7QvdC90L4t0YLQtdGF0L3QuNGH0LXRgdC60L7QtSDRgtCw0LzQvtC20LXQvdC90L7QtSDRg9C/0YDQsNCy0LvQtdC90LjQtTE6MDgGA1UECQwx0L/Quy7QmtC+0LzRgdC+0LzQvtC70YzRgdC60LDRjyDQtC4xINC60L7RgNC/LiDQkDEVMBMGA1UEBwwM0JzQvtGB0LrQstCwMRswGQYDVQQIDBI3NyDQsy7QnNC+0YHQutCy0LAxCzAJBgNVBAYTAlJVMU8wTQYDVQQDDEbQk9C+0LvQvtCy0L3QvtC5INGD0LTQvtGB0YLQvtCy0LXRgNGP0Y7RidC40Lkg0YbQtdC90YLRgCDQodCS0KPQpiDQotCeAhEC4bLZesQMzoDnEepBMHN6hDAKBgYqhQMCAhMFAARAZo24NtdU++a6+Ky0AR6moY3+smOSyIYy436c06Ny6nIqZn6ZK9VMmdYNyqiPvmV+g7DwVeIe8J8LVxXiHDWwTg==</SignatureValue>\n" +
                "\t\t\t<KeyInfo Id=\"KeyInfo\">\n" +
                "\t\t\t\t<X509Data>\n" +
                "\t\t\t\t\t<X509Certificate>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</X509Certificate>\n" +
                "\t\t\t\t</X509Data>\n" +
                "\t\t\t</KeyInfo>\n" +
                "\t\t\t<Object Id=\"Object\">";
        String finalSign = "</Object>\n" +
                "\t\t</Signature>";

        return buildCompleteBody(signStart.getBytes(), doc, finalSign.getBytes());
    }

    public byte[] autoSign(byte[] document) {
        try {
            byte[] encoded = Base64.encodeBase64(document);
            String documentStr = new String(encoded, "UTF-8");
            UnsignedDocument unsignedDocument = new UnsignedDocument();
            unsignedDocument.setBytes(documentStr);

            Request request = new Request();
            request.setDoSign(unsignedDocument);

            Response response = send(request, new URI(cryptoURL));
            OkResponse okResponse = ((OkResponse) response);
            return Base64.decodeBase64(okResponse.getDidSign().getBytes().getBytes("UTF-8"));
        } catch (URISyntaxException e) {
            log.error("Ошибка при кривом синтаксисе подписи",e);
        }  catch (IOException e) {
            log.error("Ошибка отправки на подпись",e);
        }
        return new byte[0];
    }

    public Response send(Object request, URI uri) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String responseStr = restTemplate.postForObject(uri, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(responseStr, Response.class);
        } catch (IOException e) {
            log.error("ошибка при отправке дока на подпись",e);
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Build body.
     *
     * @param signStart
     * @param stringBaseDocType
     * @param signFinal
     * @return
     * @throws IOException
     */
    private BodyType buildCompleteBody(final byte[] signStart,
                                       final String stringBaseDocType,
                                       final byte[] signFinal) throws IOException {

        byte[] docWithSign = joinSignWithDoc(signStart, stringBaseDocType, signFinal);

        BodyType bodyType = new BodyType();
        bodyType.setAnyList(Arrays.<Object>asList(marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                docWithSign)))));
        return bodyType;
    }

    /**
     * Join 3 bytes array of doc.
     *
     * @param signStart
     * @param stringBaseDocType
     * @param signFinal
     * @return
     *///CHECKSTYLE:OFF
    private byte[] joinSignWithDoc(final byte[] signStart, final String stringBaseDocType, final byte[] signFinal) {
        // 38 - индекс начиная с которого идет нормальный docType
        int correctDocStartIndex = 38;
        String normilizeBaseDoc = stringBaseDocType.substring(correctDocStartIndex);
        return ArrayUtils.addAll(ArrayUtils.addAll(signStart, normilizeBaseDoc.getBytes()), signFinal);
    }

}
