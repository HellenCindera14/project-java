package bdki.project.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import bdki.project.services.QRCodeGenerator;

@Controller
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class QRController {

    @Autowired
    QRCodeGenerator qrCodeGenerator;

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/img/QRCode.png";

    @GetMapping("/qrcode")
    public String generateQRCode(Model model) {
        String github = "https://github.com/HellenCindera14";

        byte[] image = new byte[0];
        try {
            // Generate QR Code image data
            image = qrCodeGenerator.generateQRCodeImageData(github, 250, 250);

            // Save QR Code image to file
            qrCodeGenerator.generateQRCodeImage(github, 250, 250, QR_CODE_IMAGE_PATH);
            System.out.println("berhasil create QR");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
   
        // Convert Byte Array into Base64 Encoded String
        String qrcode = Base64.getEncoder().encodeToString(image);

        model.addAttribute("github", github);
        model.addAttribute("qrcode", qrcode);

        return "qrcode";
    }
}
