package renue.fts.gateway.admin.autotest.service;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jibx.JibxMarshaller;
import org.springframework.stereotype.Service;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class SignatureService {

    @Autowired
    private JibxMarshaller marshaller;

    /**
     * Sign doc.
     * @param doc
     * @return
     */
    //CHECKSTYLE:OFF
    public BodyType sign(final BaseDocType doc) throws IOException {

        String signStart = "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "\t\t\t<SignedInfo>\n" +
                "\t\t\t\t<CanonicalizationMethod Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"/>\n" +
                "\t\t\t\t<Reference URI=\"#KeyInfo\">\n" +
                "\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t<Transform Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>\n" +
                "\t\t\t\t\t<DigestValue>0ddSE9AR1F+T4LiQLzI6c7E08eXgmfnA+3HyovT8pPE=</DigestValue>\n" +
                "\t\t\t\t</Reference>\n" +
                "\t\t\t\t<Reference URI=\"#Object\">\n" +
                "\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t<Transform Algorithm=\"urn:xml-dsig:transformation:v1.1\"/>\n" +
                "\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>\n" +
                "\t\t\t\t\t<DigestValue>ZZeMAEjE5scWJAHWfoJ4ATjx9z+uMeBVsF0qcjc3Ack=</DigestValue>\n" +
                "\t\t\t\t</Reference>\n" +
                "\t\t\t</SignedInfo>\n" +
                "\t\t\t<SignatureValue>MIIE1AYJKoZIhvcNAQcCoIIExTCCBMECAQExDDAKBgYqhQMCAgkFADALBgkqhkiG9w0BBwExggSfMIIEmwIBATCCAbowggGjMSAwHgYJKoZIhvcNAQkBFhF2dWNAY2EuY3VzdG9tcy5ydTEaMBgGCCqFAwOBAwEBEgwwMDc3MzA2NTQ0NzExGDAWBgUqhQNkARINMTExNzc0Njg4OTk0MTF7MHkGA1UECgxy0KbQtdC90YLRgNCw0LvRjNC90L7QtSDQuNC90YTQvtGA0LzQsNGG0LjQvtC90L3Qvi3RgtC10YXQvdC40YfQtdGB0LrQvtC1INGC0LDQvNC+0LbQtdC90L3QvtC1INGD0L/RgNCw0LLQu9C10L3QuNC1MTowOAYDVQQJDDHQv9C7LtCa0L7QvNGB0L7QvNC+0LvRjNGB0LrQsNGPINC0LjEg0LrQvtGA0L8uINCQMRUwEwYDVQQHDAzQnNC+0YHQutCy0LAxGzAZBgNVBAgMEjc3INCzLtCc0L7RgdC60LLQsDELMAkGA1UEBhMCUlUxTzBNBgNVBAMMRtCT0L7Qu9C+0LLQvdC+0Lkg0YPQtNC+0YHRgtC+0LLQtdGA0Y/RjtGJ0LjQuSDRhtC10L3RgtGAINCh0JLQo9CmINCi0J4CEQLhstl6xAzQgOcRkEdCraMnMAoGBiqFAwICCQUAoIICfDAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNzA2MTYxNDU2MjVaMC8GCSqGSIb3DQEJBDEiBCA4MLyjualCwH7zNvIrnUDkawgvcWoffpKyp/pDh04mdjCCAg8GCyqGSIb3DQEJEAIvMYIB/jCCAfowggH2MIIB8jAIBgYqhQMCAgkEIBkg++denSJ9wur6ea4O2yM0nEX028MtbzbZK9pP2jahMIIBwjCCAaukggGnMIIBozEgMB4GCSqGSIb3DQEJARYRdnVjQGNhLmN1c3RvbXMucnUxGjAYBggqhQMDgQMBARIMMDA3NzMwNjU0NDcxMRgwFgYFKoUDZAESDTExMTc3NDY4ODk5NDExezB5BgNVBAoMctCm0LXQvdGC0YDQsNC70YzQvdC+0LUg0LjQvdGE0L7RgNC80LDRhtC40L7QvdC90L4t0YLQtdGF0L3QuNGH0LXRgdC60L7QtSDRgtCw0LzQvtC20LXQvdC90L7QtSDRg9C/0YDQsNCy0LvQtdC90LjQtTE6MDgGA1UECQwx0L/Quy7QmtC+0LzRgdC+0LzQvtC70YzRgdC60LDRjyDQtC4xINC60L7RgNC/LiDQkDEVMBMGA1UEBwwM0JzQvtGB0LrQstCwMRswGQYDVQQIDBI3NyDQsy7QnNC+0YHQutCy0LAxCzAJBgNVBAYTAlJVMU8wTQYDVQQDDEbQk9C+0LvQvtCy0L3QvtC5INGD0LTQvtGB0YLQvtCy0LXRgNGP0Y7RidC40Lkg0YbQtdC90YLRgCDQodCS0KPQpiDQotCeAhEC4bLZesQM0IDnEZBHQq2jJzAKBgYqhQMCAhMFAARAvO6LXdXft7ejwjcTw93sTChaWZK4J3ywlk41bUVpV5LL2RXWDqykuyghE1DPZV45C4WG6KydY4fnts5zE8ScpQ==</SignatureValue>\n" +
                "\t\t\t<KeyInfo Id=\"KeyInfo\">\n" +
                "\t\t\t\t<X509Data>\n" +
                "\t\t\t\t\t<X509Certificate>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LzEyOC0yODgxINC+0YIgMTIuMDQuMjAxNjA2BgUqhQNkbwQtDCsi0JrRgNC40L/RgtC+0J/RgNC+IENTUCIgKNCy0LXRgNGB0LjRjyA0LjApMIGiBgNVHR8EgZowgZcwOqA4oDaGNGh0dHA6Ly92dWMuY3VzdG9tcy5ydS9jZHAvZ29sb3Zub3lfdWNfc3Z1Y3RvXzEuMi5jcmwwWaBXoFWGU2h0dHA6Ly9zdnVjdG9zLmduaXZjLmVhaXMuY3VzdG9tcy5ydS9pbWFnZXMvc3Rvcmllcy9maWxlL2dvbG92bm95X3VjX3N2dWN0b18xLjIuY3JsMIGzBggrBgEFBQcBAQSBpjCBozBABggrBgEFBQcwAoY0aHR0cDovL3Z1Yy5jdXN0b21zLnJ1L2NkcC9nb2xvdm5veV91Y19zdnVjdG9fMS4yLmNydDBfBggrBgEFBQcwAoZTaHR0cDovL3N2dWN0b3MuZ25pdmMuZWFpcy5jdXN0b21zLnJ1L2ltYWdlcy9zdG9yaWVzL2ZpbGUvZ29sb3Zub3lfdWNfc3Z1Y3RvXzEuMi5jcnQwCAYGKoUDAgIDA0EAsQBKt4P6blgCNK30fVrsVN4Di+00TREpjiJohH3TIEeWASLg5jsbBHAOSBsXAvLfJGKraB9efu+RUeGUQGR7Hw==</X509Certificate>\n" +
                "\t\t\t\t</X509Data>\n" +
                "\t\t\t</KeyInfo>\n" +
                "\t\t\t<Object Id=\"Object\">";
        String finalSign = "</Object>\n" +
                "\t\t</Signature>";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(out);

        try {
            marshaller.marshal(doc, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringBaseDoc = out.toString();
        return buildCompleteBody(signStart.getBytes(),stringBaseDoc,finalSign.getBytes());
    }

    /**
     * Build body.
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
