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
 * Configuration of JMS connection.
 */
@ComponentScan({"renue.fts.gateway.admin.autotest.jms"})
@Configuration
public class JmsConfig {

    public static final String CHANNEL_NAME = "GATEWAY.TEST.SVRCONN";
    public static final String QUEUE_MANAGER = "RU.FTS.GATEWAY.ADMIN";
    public static final String QUEUE_DESTINATION_NAME = "GW.IB.FROM";
    public static final String QUEUE_RECIEVER_NAME = "BROKER.TO";
    public static final int PORT = 1416;
    public static final String HOST_NAME = "31.186.98.2";


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

    /*@Autowired
    private TestStepConfig testStepConfig;*/

    /**
     * Create MessageListener.
     * @return
     */
    @Bean
    public MessageListenerContainer jmsListenerContainerFactory() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_RECIEVER_NAME);
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
