package com.cham.webspheremqjms;

import com.cham.webspheremqjms.domain.Tweet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@EnableJms
public class WebsphereMqJmsApplication {

	@Bean
	public JmsListenerContainerFactory<?> JmsListenerContainerFactory(ConnectionFactory connectionFactory,
																	  DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebsphereMqJmsApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("Sending a tweet ...");

		jmsTemplate.convertAndSend("DEV.QUEUE.1", new Tweet("Tim", "Scala rocks"),new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty("tweet_prop", "exec");
				return message;
			}
		});
		jmsTemplate.convertAndSend("DEV.QUEUE.1", new Tweet("Bar", "I like Cake"));

		dateConversion();
	}

	private static void dateConversion(){
		String originalDate = "2018-10-29 00:55";
		LocalDateTime localTime = LocalDateTime.parse(originalDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		System.out.println("The local time is " + localTime);

		ZonedDateTime utcTime = localTime.atZone(ZoneOffset.UTC);
		ZonedDateTime bstTime = utcTime.withZoneSameInstant(ZoneId.of("Europe/London"));

		System.out.println("The BST time is " + bstTime);

		// the summer time offset is how many milliseconds?
		long dstOffset = ChronoUnit.MILLIS.between(utcTime.toLocalDateTime(), bstTime.toLocalDateTime());
		System.out.println(dstOffset); // prints 0

      // try the same at start of day (midnight)
		utcTime = utcTime.toLocalDate().atStartOfDay(ZoneOffset.UTC);
		bstTime = utcTime.withZoneSameInstant(ZoneId.of("Europe/London"));
		dstOffset = ChronoUnit.MILLIS.between(utcTime.toLocalDateTime(), bstTime.toLocalDateTime());
		System.out.println(dstOffset); // prints 3600000

      // and next midnight
		utcTime = utcTime.plusDays(1);
		bstTime = utcTime.withZoneSameInstant(ZoneId.of("Europe/London"));
		dstOffset = ChronoUnit.MILLIS.between(utcTime.toLocalDateTime(), bstTime.toLocalDateTime());
		System.out.println(dstOffset);
	}
}
