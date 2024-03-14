package bdki.project.publisher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

 public void sendMessage(String message) {
    try {
        LOGGER.info(String.format("Message sent -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    } catch (AmqpException e) {
        // Tangani eksepsi terkait RabbitMQ di sini
        LOGGER.error("Error sending message to RabbitMQ: " + e.getMessage(), e);
    } catch (Exception e) {
        // Tangani eksepsi umum lainnya di sini
        LOGGER.error("Unexpected error sending message to RabbitMQ: " + e.getMessage(), e);
    }
}

}
