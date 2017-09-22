package renue.fts.gateway.admin.autotest.jms;

import com.ibm.jms.JMSBytesMessage;
import org.apache.log4j.Logger;
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

    private static final Logger log = Logger.getLogger(MyMessageListener.class);

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private TesterService testerService;

    @Override
    public void onMessage(final javax.jms.Message message) {
        if (message instanceof BytesMessage) {
            try {
                log.info("Приняли сообщение");
                byte[] answer = parseInputMessage(message);

                log.info("Начинаем размаршивать сообщение");
                EnvelopeType envelopeType = (EnvelopeType) marshallingService.unmarshall(answer);
                log.info("Закончили размаршивать сообщение");
                ExecutorService executorService = new ScheduledThreadPoolExecutor(2);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            testerService.processResponse(envelopeType);
                        } catch (IOException | IllegalAccessException e) {
                            log.error("ошибка при обработке принятого сообщения",e);
                        } catch (InterruptedException e) {
                            log.error("ошибка многопоточности",e);
                        }
                    }
                });
            } catch (Exception e) {
                log.error("ошибка при обработке принятого сообщения",e);
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
