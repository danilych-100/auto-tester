package renue.fts.gateway.admin.autotest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@EnableJms
@EnableTransactionManagement
@Configuration
public class JmsConfig {

    @Value("${jms.channelName}")
    private String channelName;

    @Value("${jms.queueManager}")
    private String queueManager;

    @Value("${jms.queueDestinationName}")
    private String queueDestinationName;

    @Value("${jms.queueRecievedName}")
    private String queueRecieverName;

    @Value("${jms.port}")
    private int port;

    @Value("${jms.hostname}")
    private String hostName;



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
            connectionFactory.setHostName(hostName);
            connectionFactory.setPort(port);
            connectionFactory.setQueueManager(queueManager);
            connectionFactory.setChannel(channelName);
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
        template.setDefaultDestinationName(queueDestinationName);
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
        container.setDestinationName(queueRecieverName);
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
