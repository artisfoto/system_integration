package com.requerimientos;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.HashMap;
import java.util.Map;


	public class Consumidores {
		private static final String URL = "tcp://localhost:61616";
		private static final String USER = ActiveMQConnection.DEFAULT_USER;
		private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
		private static final String DESTINATION_QUEUE = "REQ2.QUEUE";
		private static final boolean TRANSACTED_SESSION = true;
		private static final int TIMEOUT = 1000;
		private static String message = "Segundo Requerimiento para la Evaluación";
		private final Map<String, Integer> consumedMessageTypes;
		private int totalConsumedMessages = 0;
				
		
		public Consumidores() {
			this.consumedMessageTypes = new HashMap<String, Integer>();
		}

		public void processMessages() throws JMSException {
			final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
			final Connection connection = connectionFactory.createConnection();
			connection.start();

			final Session session = connection.createSession(TRANSACTED_SESSION, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(DESTINATION_QUEUE);
			final MessageConsumer consumer = session.createConsumer(destination);
			
			processAllMessagesInQueue(consumer);
			consumer.close();
			session.close();
			connection.close();
			showProcessedResults();
			}

		private void processAllMessagesInQueue(MessageConsumer consumer) throws JMSException {
			Message message;
			while ((message = consumer.receive(TIMEOUT)) != null) {
				proccessMessage(message);
			}
		}

		private void proccessMessage(Message message) throws JMSException {
			if (message instanceof TextMessage) {
				final TextMessage textMessage = (TextMessage) message;
				final String text = textMessage.getText();
				incrementMessageType(text);
				totalConsumedMessages++;
				}
			}

			private void incrementMessageType(String message) {
				if (consumedMessageTypes.get(message) == null) {
					consumedMessageTypes.put(message, 1);
				} else {
					final int numberOfTypeMessages = consumedMessageTypes.get(message);
					consumedMessageTypes.put(message, numberOfTypeMessages + 1);
				}
			}
			
			private void showProcessedResults() {
				
				System.out.println(" ");
				System.out.println("PROCESADOS UN TOTAL DE " + totalConsumedMessages + " MENSAJES");
				System.out.println(" ");
				System.out.println("Mensaje Enriquecido: " + message + " de Integración de Sistemas");
			}

			public static void main(String[] args) throws JMSException {
				final Consumidores consumer = new Consumidores();
				consumer.processMessages();
			}

			public static String getMessage() {
				return message;
			}

			public static void setMessage(String message) {
				Consumidores.message = message;
			}
	}