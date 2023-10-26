import org.apache.activemq.ActiveMQConnectionFactory;
import sun.print.UnixPrintService;

import javax.jms.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {

        String brokerURL = "tcp://localhost:61616";
        Connection connection = null;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("myqueue");
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(new Exception("axxx"));
            producer.send(message);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }  finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
