//CHECKSTYLE:OFF
package renue.fts.gateway.admin.autotest.jms;

import com.ibm.rmm.receiver.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Danil on 06.07.2017.
 */
@Component
public class MyMessageSender {

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private JmsTemplate jmsTemplate;


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
