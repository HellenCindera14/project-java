package bdki.project.services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdki.project.entity.User;
import bdki.project.publisher.RabbitMQProducer;
import bdki.project.repository.UserRepository;

@Service
public class UserServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RabbitMQProducer rabbitMQProducer;

    @Transactional
    public Map<String, Object> processUser(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String responseCode = "";
        boolean status = false;
        String statusCode = "";
        String resultMessage = "";
        User user = new User();

        try {
            if (params != null) {
                String name = (params.get("name") != null) ? params.get("name").toString() : null;
                String description = (params.get("description") != null) ? params.get("description").toString() : null;
                String code = (params.get("code") != null) ? params.get("code").toString() : null;
                JSONObject userInfo;

                if (params.get("additional_info") != null) {
                    String jsonStr = params.get("additional_info").toString();
                    userInfo = new JSONObject(jsonStr);
                } else {
                    userInfo = new JSONObject();
                }
                boolean statusValue = (params.get("status") instanceof Boolean) ? (boolean) params.get("status"): false;

                if (!statusValue) {
                    throw new RuntimeException("Status is false, cannot proceed.");
                }

                user.setName(name);
                user.setDescription(description);
                user.setCode(code);
                // user.setStatus(statusValue);
                // user.setUserInfo(userInfo);

                userRepository.save(user);

                // Mengirim data ke RabbitMQ
                String message = "Berhasil sending to RabbitMQ: " + user.toString();
                System.out.println(user.toString());
                rabbitMQProducer.sendMessage(message);


                responseCode = "00";
                status = true;
                statusCode = "OK";
                resultMessage = " Success";
            } else {
                LOGGER.info(" data is empty");
                result.put("responseCode", "99");
                result.put("status", false);
                result.put("statusCode", "ERROR");
                result.put("resultMessages", "Params cannot be null");
                result.put("result", null);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing : {}", e.getMessage());
            result.put("responseCode", "99");
            result.put("status", false);
            result.put("statusCode", "ERROR");
            result.put("resultMessages", "Failed");
            result.put("result", null);
        
        } finally {
            result.put("responseCode", responseCode);
            result.put("status", status);
            result.put("statusCode", statusCode);
            result.put("resultMessages", resultMessage);
            result.put("result", user);

     
        }
        return result;
    }

}
