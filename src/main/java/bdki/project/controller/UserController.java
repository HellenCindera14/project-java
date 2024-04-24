package bdki.project.controller;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bdki.project.consumer.RabbitMQConsumer;
import bdki.project.publisher.RabbitMQProducer;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    RabbitMQProducer producer;

    @Autowired
    RabbitMQConsumer rabbitMQConsumer;

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, Object> params) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(params);

            // Mengirim pesan ke RabbitMQProducer
            producer.sendMessage(jsonData);

            // Konsumsi pesan yang diterima dari RabbitMQ
            Message message = new Message(objectMapper.writeValueAsBytes(params));
            rabbitMQConsumer.consume(message);
        
            return ResponseEntity.ok("callback succes yaa...");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process JSON data");
        }
    }
}
