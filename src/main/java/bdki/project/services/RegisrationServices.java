package bdki.project.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import bdki.project.entity.Regisration;
import bdki.project.entity.User;
import bdki.project.repository.RegisrationRepository;

@Service
public class RegisrationServices {

    @Autowired
    RegisrationRepository regisrationRepository;

    @Autowired
    UserService userService;

    public ResponseEntity<Object> regisrationGenerate(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String responseCode = "";
        String statusCode = "";
        String responseMessage = "";
        Regisration regisration = new Regisration();

        try {
            if (params != null) {
                String fullname = (String) params.get("fullname");
                String email = (String) params.get("email");
                String noTelp = (String) params.get("noTelp");

                User user = userService.findByPhoneNumber(noTelp);

                if (user != null) {
                    // Redirect to registration link if user already exists
                    responseCode = "99";
                    statusCode = "REDIRECT";
                    responseMessage = "User already registered";
                    result.put("responseCode", responseCode);
                    result.put("statusCode", statusCode);
                    result.put("responseMessage", responseMessage);
                    return ResponseEntity.status(HttpStatus.FOUND).body(result);
                } else {
                    // Save registration if user does not exist
                    regisration.setName(fullname);
                    regisration.setNoTelp(noTelp);
                    regisration.setEmail(email);
                    regisrationRepository.save(regisration);

                    responseCode = "00";
                    statusCode = "OK";
                    responseMessage = "Registration Success";
                    result.put("responseCode", responseCode);
                    result.put("statusCode", statusCode);
                    result.put("responseMessage", responseMessage);
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }
        } catch (Exception e) {
            responseCode = "05";
            statusCode = "NOT OK";
            responseMessage = "Registration Failed: " + e.getMessage();
        }

        result.put("responseCode", responseCode);
        result.put("statusCode", statusCode);
        result.put("responseMessage", responseMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
