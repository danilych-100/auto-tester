package renue.fts.gateway.admin.autotest.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import java.io.IOException;

/**
 * Sender.
 */
@Component
public class MyMessageSender {

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Send message to mq.
     * @param envelopeType
     */
    public void sendMessage(final EnvelopeType envelopeType) {


        try {
            byte[] marsheledEnv = marshallingService.marshall(envelopeType);

            System.out.println("Отправили сообщение!");
            jmsTemplate.convertAndSend(marsheledEnv);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
