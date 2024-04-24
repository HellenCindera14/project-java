package bdki.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bdki.project.entity.EmailRequest;
import bdki.project.services.EmailServices;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class EmailController {

    private final EmailServices emailServices;

    @Autowired
    public EmailController(EmailServices emailServices) {
        this.emailServices = emailServices;
    }
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailServices.sendEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getBody());
            return "Email sent successfully.";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
    
}
