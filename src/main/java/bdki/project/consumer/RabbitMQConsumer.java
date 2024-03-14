package bdki.project.consumer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bdki.project.constant.LogInfoFormat;

@Service
public class RabbitMQConsumer {

    private Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private Marker MARKER;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RestTemplate restTemplate;

    @RabbitListener(queues = { "${rabbitmq.queue.name}" })
    public void consume(Message message) throws JsonProcessingException {
        try {
            // Menampung pesan dari RabbitMQ
            String jsonMessage = new String(message.getBody());

            //Menampilkan di LOG
            LOGGER.info(String.format("Received message " + jsonMessage));
            System.out.println("ini adalah pesan dari rabbitMQ : " + message.getBody());

        } catch (Exception e) {
            LOGGER.error(MARKER, "Error processing message: " + e.getMessage(), e);
        
        }

    }

}
