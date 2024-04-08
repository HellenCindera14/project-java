package bdki.project.controller;

import bdki.project.services.RegisrationServices;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RegisrationServices regisrationServices;

    @Autowired
    public RegisrationController(RegisrationServices regisrationServices) {
        this.regisrationServices = regisrationServices;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Map<String, Object> request) {
        return regisrationServices.regisrationGenerate(request);
    }
}
