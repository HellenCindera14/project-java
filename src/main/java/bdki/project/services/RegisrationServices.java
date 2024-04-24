package bdki.project.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import bdki.project.entity.User;
import bdki.project.repository.UserRepository;

@Service
public class RegisrationServices {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  public Map<String, Object> registration(Map<String, Object> params) throws JsonProcessingException {
    Map<String, Object> result = new HashMap<>();

    String responseCode = "";
    boolean status = false;
    String statusCode = "";
    String resultMessage = "";
    String url = "";

    User registration = new User();

    try {
      if (params != null) {
        // untuk menampung input user dari UI
        String phoneNumber = params.get("phoneNumber") != null ? params.get("phoneNumber").toString() : null;
        String fullname = params.get("fullname") != null ? params.get("fullname").toString() : null;
        String birthday = params.get("birthday") != null ? params.get("birthday").toString() : null;
        String placeOfBirth = params.get("placeOfBirth") != null ? params.get("placeOfBirth").toString() : null;
        String email = params.get("email") != null ? params.get("email").toString() : null;
        String channelRequest = params.get("channelRequest") != null ? params.get("channelRequest").toString() : null;
        String stan = params.get("stan") != null ? params.get("stan").toString() : null;
  
        registration.setPhoneNumber(phoneNumber);
        registration.setFullName(fullname);
        registration.setBirthdate(birthday);
        registration.setPlaceOfBirth(placeOfBirth);
        registration.setEmail(email);
        registration.setChannelRequest(channelRequest);
        registration.setStan(stan);

        userRepository.save(registration);

        responseCode = "00";
        status = true;
        statusCode = "OK";
        resultMessage = "Success";

      } else {
        
       result.put("responseCode", "05");
       result.put("status", "false");
       result.put("statusCode", "not oke");
       result.put("resultMessage", "Success");

      }
    } catch (Exception e) {
      responseCode = "400";
      status = false;
      statusCode = "NOT OK";
      resultMessage = "NOT SUCCESS";
      url = "registration";

    } finally {
      result.put("responseCode", responseCode);
      result.put("status", status);
      result.put("statusCode", statusCode);
      result.put("resultMessage", resultMessage);
      return result;
    }
}
}
