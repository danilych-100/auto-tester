//CHECKSTYLE:OFF
package renue.fts.gateway.admin.autotest.jms;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.rmm.receiver.Event;
import com.ibm.rmm.receiver.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.service.MarshallingService;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil on 06.07.2017.
 */
@Component
public class MyMessageListener implements javax.jms.MessageListener {

    @Autowired
    private MarshallingService marshallingService;

    @Autowired
    private MyMessageSender producer;


    @Override
    public void onMessage(javax.jms.Message message) {
        if(message instanceof BytesMessage){
            System.out.println("Message has been consumed");
            try {
                byte[]  a = parseInputMessage(message);
                EnvelopeType envelopeType = (EnvelopeType) marshallingService.unmarshall(a);
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] parseInputMessage(Message message) throws JMSException {
        byte[] body = new byte[((int) ((JMSBytesMessage) message).getBodyLength())];
        ((JMSBytesMessage) message).readBytes(body);
        return body;
    }

}
