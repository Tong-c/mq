package com.tong.two;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class PersistJmsTopicReceiver {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.33:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            //1：需要在连接上设置消费者id，用来识别消费者
            connection.setClientID("tong");
            Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("MyTopic");
            //2：需要创建TopicSubscriber来订阅
            //3：要设置好了过后再start 这个 connection
            //4：一定要先运行一次，等于向消息服务中间件注册这个消费者，然后再运行客户端发送信息，这个时候，
            // 无论消费者是否在线，都会接收到，不在线的话，下次连接的时候，会把没有收过的消息都接收下来
            TopicSubscriber topicSubscriber = session.createDurableSubscriber(destination,"sTong");
            connection.start();
            Message message = topicSubscriber.receive();
            while (message != null){
                TextMessage txMsg = (TextMessage) message;
                session.commit();
                System.out.println("收到消息："+txMsg.getText());
                message = topicSubscriber.receive(1000l);

            }
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
