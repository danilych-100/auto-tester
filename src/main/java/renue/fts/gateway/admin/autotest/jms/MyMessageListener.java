package renue.fts.gateway.admin.autotest.jms;

import com.ibm.jms.JMSBytesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import renue.fts.gateway.admin.autotest.service.TesterService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import java.io.*;

/**
 * Message listener.
 */
@Component
public class MyMessageListener implements javax.jms.MessageListener {

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private MyMessageSender producer;

    @Autowired
    private TesterService testerService;

    @Override
    public void onMessage(final javax.jms.Message message) {
        if (message instanceof BytesMessage) {
            System.out.println("Message has been consumed");
            try {
                byte[] answer = parseInputMessage(message);
                System.out.println(new String(answer));
                downloadEnvelopeToFS(answer);
                EnvelopeType envelopeType = (EnvelopeType) marshallingService.unmarshall(answer);
                testerService.processResponse(envelopeType);
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parse input mess.
     * @param message
     * @return
     * @throws JMSException
     */
    public static byte[] parseInputMessage(final Message message) throws JMSException {
        byte[] body = new byte[((int) ((JMSBytesMessage) message).getBodyLength())];
        ((JMSBytesMessage) message).readBytes(body);
        return body;
    }

    /**
     * donwload Envelope.
     * @param envelopeTypebytes
     */
    private void downloadEnvelopeToFS(final byte[] envelopeTypebytes) {
        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(
                    "C:\\Users\\Danil\\Desktop\\answer.xml")));
            outputStream.write(envelopeTypebytes);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
