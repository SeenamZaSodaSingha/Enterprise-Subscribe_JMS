/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asynconsumer;

import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.Scanner;

/**
 *
 * @author sarun
 */
public class Main {

    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;
    static Scanner sc = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String destType = null;
        Connection connection = null;
        Session session = null;
        Destination dest = null;
        MessageConsumer consumer = null;
        TextListener listener = null;
        TextMessage message = null;
        InputStreamReader inputStreamReader = null;
        String answer = "";
        

        if (args.length != 1) {
            System.err.println("Program takes one argument: <dest_type>");
            System.exit(1);
        }

        destType = args[0];
        System.out.println("Destination type is " + destType);

        if (!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or \"topic\"");
            System.exit(1);
        }

        try {
            if (destType.equals("queue")) {
                dest = (Destination) queue;
            } else {
                dest = (Destination) topic;
            }
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }

        try {
            System.out.println("[CLIENT IS STARTED]");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(dest);
            listener = new TextListener();
            consumer.setMessageListener(listener);
            connection.start();
            inputStreamReader = new InputStreamReader(System.in);
            MessageProducer producer = session.createProducer(queue);

            //take input length
            //sent to server
            do {
                String primeCnt = "";
                System.out.println("Enter two numbers, Use ',' to seperate each number. To end the program press Enter.");
                answer = sc.nextLine();
                if (answer.isEmpty()) {
                    break;
                }
                message.setText(answer);
                producer.send(message);
                //wait for server to repond
                
            

            } while (true);
//                try {
//                    answer = (char) inputStreamReader.read();
//                    //wait for answer form server
//                } catch (IOException e) {
//                    System.err.println("I/O exception: " + e.toString());
//                }

        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }

}
