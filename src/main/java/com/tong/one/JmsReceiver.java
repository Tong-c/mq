package com.tong.one;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsReceiver {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.33:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("my-queue");
            MessageConsumer consumer = session.createConsumer(destination);
            int i = 0;
            while(i < 3){
                i++;
                TextMessage message = (TextMessage) consumer.receive();
                session.commit();
                System.out.println("收到消息：" + message.getText());
            }
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
