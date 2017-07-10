package renue.fts.gateway.admin.autotest.config;

import org.springframework.context.annotation.ComponentScan;
import renue.fts.gateway.admin.autotest.jms.MyMessageListener;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.oxm.jibx.JibxMarshaller;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import javax.jms.JMSException;

/**
 * Created by Danil on 07.07.2017.
 */
@ComponentScan({"renue.fts.gateway.admin.autotest.jms"})
@Configuration
public class JmsConfig {

    public static final String CHANNEL_NAME = "TEST.CHANNEL";
    public static final String QUEUE_MANAGER = "TEST";
    public static final String QUEUE_DESTINATION_NAME = "TESTQ";
    public static final int PORT = 1414;
    public static final String HOST_NAME = "localhost";


    @Autowired
    private MyMessageListener messageListener;


    /**
     * Create connection factory.
     * @return connectionFactory
     */
    @Bean
    public MQQueueConnectionFactory connectionFactory() {

        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        try {
            connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            connectionFactory.setHostName(HOST_NAME);
            connectionFactory.setPort(PORT);
            connectionFactory.setQueueManager(QUEUE_MANAGER);
            connectionFactory.setChannel(CHANNEL_NAME);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return connectionFactory;
    }

    /**
     * Create Template.
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(QUEUE_DESTINATION_NAME);
        return template;
    }

    /**
     * Create MessageListener.
     * @return
     */
    @Bean
    public MessageListenerContainer jmsListenerContainerFactory() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_DESTINATION_NAME);
        container.setMessageListener(messageListener);
        return container;
    }

    /**
     * Create Marshaller.
     * @return
     */
    @Bean
    public JibxMarshaller jibx2Marshaller() {
        JibxMarshaller marshaller = new JibxMarshaller();
        marshaller.setTargetClass(EnvelopeType.class);
        return marshaller;
    }

}
