/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tempqueueproducer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class Main {

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/TempQueue")
    private static Queue queue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        Connection connection = null;
        TextListener listener = null;
        InputStreamReader inputStreamReader = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE);
            listener = new TextListener();
            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            Queue tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            responseConsumer.setMessageListener(listener);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();

//            message.setText("Hello friend" );
//            message.setJMSReplyTo(tempDest);
            //Set a correlation ID so when you get a response you know which sent message the response is for
            //If there is never more than one outstanding message to the server then the
            //same correlation ID can be used for all the messages...if there is more than one outstanding
            //message to the server you would presumably want to associate the correlation ID with this
            //message somehow...a Map works good
            String correlationId = "12345";
            message.setJMSCorrelationID(correlationId);
            connection.start();
//            System.out.println("Sending message: " + message.getText());
//            producer.send(message);

            String ch = "";
            String primeCnt = "";
            Scanner inp = new Scanner(System.in);
            GetPrime p = new GetPrime();
            inputStreamReader = new InputStreamReader(System.in);
//            
            System.out.println("[SERVER IS STARTED]");
            do {
//                message.setText(headerMSG);
//                producer.send(message);
                message.setJMSReplyTo(tempDest);
//                ch = inputStreamReader.read();
                ch = inp.nextLine();
                if (ch.isEmpty()) {
                    break;
                }
//                message.setText(ch);
                System.out.println("Sending message: " + message.getText());
                //sent back to client
//                producer.send(message);
//                message.setJMSReplyTo(tempDest);
                primeCnt = p.GetPrime(ch);
//                message.setText(primeCnt);
//                producer.send(message);
//                message.setJMSReplyTo(tempDest);
            } while (true);

        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
//        } catch (IOException e) {
//            System.err.println("Exception occurred: " + e.toString());
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
