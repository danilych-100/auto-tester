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
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Message listener.
 */
@Component
public class MyMessageListener implements javax.jms.MessageListener {

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private TesterService testerService;

    @Override
    public void onMessage(final javax.jms.Message message) {
        if (message instanceof BytesMessage) {
            System.out.println("Message has been consumed");
            try {
                byte[] answer = parseInputMessage(message);
                System.out.println(new String(answer));

                EnvelopeType envelopeType = (EnvelopeType) marshallingService.unmarshall(answer);
                ExecutorService executorService = new ScheduledThreadPoolExecutor(2);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            testerService.processResponse(envelopeType);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parse input message.
     * @param message
     * @return
     * @throws JMSException
     */
    public static byte[] parseInputMessage(final Message message) throws JMSException {
        byte[] body = new byte[((int) ((JMSBytesMessage) message).getBodyLength())];
        ((JMSBytesMessage) message).readBytes(body);
        return body;
    }

}
