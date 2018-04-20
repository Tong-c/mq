package com.tong.two;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class PersistJmsTopicSend {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.33:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("MyTopic");
            MessageProducer producer = session.createProducer(destination);

            //1：要用持久化订阅，发送消息者要用 DeliveryMode.PERSISTENT 模式发现，在连接之前设定
            //2：一定要设置完成后，再start 这个 connection
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            connection.start();

            for(int i = 0;i < 3;i++){
                TextMessage message = session.createTextMessage("message--"+i);
                Thread.sleep(1000);
                producer.send(message);
            }

            session.commit();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
