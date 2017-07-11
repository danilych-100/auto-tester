package renue.fts.gateway.admin.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.MarshallingException;
import org.springframework.oxm.jibx.JibxMarshaller;
import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.config.TestStepConfig;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Danil on 07.07.2017.
 */
@Service
public class MarshallingService {

    @Autowired
    private JibxMarshaller marshaller;

    @Autowired
    private TestStepConfig testStepConfig;

    /**
     * adsda.
     *
     * @param envelopeType
     * @return
     */
    public byte[] marshall(final EnvelopeType envelopeType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(out);

        marshaller.marshal(envelopeType, result);
        System.out.println("Замаршелили");

        byte[] bytes = out.toByteArray();
        printForPeople(bytes);
        return bytes;
    }

    /**Print bytes string in people comfortable view.
     * @param bytes
     */
    private void printForPeople(final byte[] bytes) {
        String stringBytes = new String(bytes);
        for (int i = 0; i < stringBytes.length(); i++) {
            char simbol = stringBytes.charAt(i);
            System.out.print(simbol);
            if (simbol == '>') {
                System.out.println();
            }
        }
    }

    /**
     * @param bytes
     * @return
     * @throws MarshallingException
     */
    public Object unmarshall(final byte[] bytes) throws Exception {
        try {
            EnvelopeType envelopeType = (EnvelopeType) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(bytes)));
            System.out.println("Размаршелили");
            return envelopeType;

        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
