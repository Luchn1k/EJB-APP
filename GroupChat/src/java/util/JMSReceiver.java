package util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSReceiver {

    public static String readJMSMessageFromMyTopic(String clientID) throws JMSException {

        System.out.println(clientID);

        Connection connection = null;
        Session session = null;
        String s = "";
        try {
            Context c = new InitialContext();
            ConnectionFactory topicConFactory = (ConnectionFactory) c.lookup("jms/TopicConFactory");
            Topic myTopic = (Topic) c.lookup("MyTopic");

            connection = topicConFactory.createConnection();
            //connection.setClientID("webclient");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageConsumer messageConsumer = session.createSharedDurableConsumer(myTopic, "SEConsumer");

            connection.start();

            Message m = messageConsumer.receive();
           
            if (m != null) {
                if (m instanceof TextMessage) {
                    TextMessage message = (TextMessage) m;
                    s = "" + message.getText() + "<br>";
                }
            }

            /*while (true) {
                Message m = messageConsumer.receive();
                if (m != null) {
                    if (m instanceof TextMessage) {
                        TextMessage message = (TextMessage) m;
                        return clientID + " - " + message.getText();
                    } else {
                        break;
                    }
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            return "err";
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.close();
            }
            return s;
        }

    }

}
