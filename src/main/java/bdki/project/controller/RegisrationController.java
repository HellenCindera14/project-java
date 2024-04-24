package bdki.project.controller;

import bdki.project.services.RegisrationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class RegisrationController {

    @Autowired
    RegisrationServices registrationServices;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = registrationServices.registration(params);
            // Jika Anda ingin menambahkan logic lebih lanjut di sini, seperti
            // manipulasi hasil atau validasi, Anda bisa melakukannya sebelum
            // memberikan respons.
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Tangani error jika terjadi
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
