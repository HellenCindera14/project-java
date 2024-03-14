package bdki.project.controller;

import java.util.Map;
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

import bdki.project.publisher.RabbitMQProducer;
import bdki.project.services.UserServices;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    RabbitMQProducer producer;

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, Object> params) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(params);
        System.out.println("ini adalah data json" + jsonData);
        producer.sendMessage(jsonData);
        return ResponseEntity.ok("Message sent to RabbitMQ ...");
    }
}
