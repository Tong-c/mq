package com.tong.one;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsSend {

    public static void main(String[] args)  {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.33:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("my-queue");
            MessageProducer producer = session.createProducer(destination);
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
