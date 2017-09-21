package renue.fts.gateway.admin.autotest.jms;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import renue.fts.gateway.admin.autotest.service.TesterService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import java.io.IOException;

/**
 * Sender.
 */
@Component
public class MyMessageSender {

    private static final Logger log = Logger.getLogger(MyMessageSender.class);

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
            log.info("Пытаемся отправить сообщение");
            byte[] marsheledEnv = marshallingService.marshall(envelopeType);

            jmsTemplate.convertAndSend(marsheledEnv);
            log.info("Отправили сообщение");
        } catch (IOException e) {
            log.error("Ошибка при маршаллинге сообщеня",e);
        }

    }
}
