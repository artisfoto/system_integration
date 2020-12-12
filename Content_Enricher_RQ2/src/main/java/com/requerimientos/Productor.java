package com.requerimientos;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class Productor {
	private static final String URL = "tcp://localhost:61616";
	private static final String USER = ActiveMQConnection.DEFAULT_USER;
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private static final String DESTINATION_QUEUE = "REQ2.QUEUE";
	private static final boolean TRANSACTED_SESSION = true;
	private static final int MESSAGES_TO_SEND = 7;


	public void sendMessages() throws JMSException {
		final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		final Session session = connection.createSession(TRANSACTED_SESSION, Session.AUTO_ACKNOWLEDGE);
		final Destination destination = session.createQueue(DESTINATION_QUEUE);
		final MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		sendMessages(session, producer);
		session.commit();
		session.close();
		connection.close();

		    System.out.println(" ");
			System.out.println("------------------------------- ");
			System.out.println("Mensajes enviados correctamente");
			System.out.println("------------------------------- ");
	}

	private void sendMessages(Session session, MessageProducer producer) throws JMSException {
		final Productor productor = new Productor();
		for (int i = 1; i <= MESSAGES_TO_SEND; i++) {
			productor.sendMessage("SEGUNDO REQUERIMIENTO PARA LA EVALUACIÓN", session, producer);
		}
	}

	private void sendMessage(String message, Session session, MessageProducer producer) throws JMSException {
		final TextMessage textMessage = session.createTextMessage(message);
		producer.send(textMessage);
	}

	public static void main(String[] args) throws JMSException {
		final Productor productor = new Productor();
		productor.sendMessages();
	}
}