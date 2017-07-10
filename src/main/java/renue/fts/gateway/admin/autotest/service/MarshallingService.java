package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.MarshallingException;
import org.springframework.oxm.jibx.JibxMarshaller;
import org.springframework.stereotype.Service;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import ru.kontur.fts.eps.schemas.common.HeaderType;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Danil on 07.07.2017.
 */
@Service
public class MarshallingService {

    @Autowired
    private JibxMarshaller marshaller;

    /**
     *
     * @param envelopeType
     * @return
     */
    public byte[] marshall(final EnvelopeType envelopeType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(out);

        HeaderType headers = new HeaderType();
        BodyType body = new BodyType();
        envelopeType.setHeader(headers);
        envelopeType.setBody(body);

        marshaller.marshal(envelopeType, result);

        System.out.println("Замаршелили");
        envelopeType.getHeader().getAnyList().forEach(elem-> System.out.println(elem));

        byte[] bytes = out.toByteArray();
        System.out.println(new String(bytes));
        return bytes;
    }

    /**
     *
     * @param bytes
     * @return
     * @throws MarshallingException
     */
    public Object unmarshall(final byte[] bytes) throws Exception {
        try {
            EnvelopeType envelopeType = (EnvelopeType) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(bytes)));

            System.out.println("Размаршелили");
            envelopeType.getHeader().getAnyList().forEach(elem-> System.out.println(elem));
            return envelopeType;

        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
