// package bdki.project.services;

// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.core.JsonProcessingException;

// import bdki.project.repository.RegisrationRepository;

// @Service
// public class RegisrationServices {

//     @Autowired
//     RegisrationRepository regisrationRepository;

//     public Map<String,Object> regisrationGenerate(Map<String,Object> params) throws JsonProcessingException {

//         try {
//             if(params != null) {

//                 String fullname =  (params.get("fullname") !=null)? params.get("fullname").toString() : null;
//                 String email = (params.get("email") != null) ? params.get("email").toString() : null;
//                 String noTelp = (params.get("noTelp") != null) ? params.get("noTelp").toString() : null;

//             }
//         }catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }
        

//         return null;
        
//     }
    
// }
