package bdki.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bdki.project.services.UploadDataServices;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")

public class UploadFileController {

    private final UploadDataServices uploadDataServices;

    public UploadFileController(UploadDataServices uploadDataServices) {
        this.uploadDataServices = uploadDataServices;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Memeriksa apakah file tidak kosong
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            
            // Menyimpan file ke sistem atau melakukan operasi lainnya sesuai kebutuhan aplikasi
            // Di sini, Anda dapat melakukan operasi seperti menyimpan file ke sistem file, menyimpan file ke basis data, dll.
            // Contoh sederhana, hanya menampilkan informasi tentang file
            uploadDataServices.readXlsxFile(file);
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            String responseMessage = "File " + fileName + " uploaded successfully. Size: " + fileSize + " bytes. Content type: " + contentType;
            return ResponseEntity.ok().body(responseMessage);
        } catch (Exception e) {
            // Tangani kesalahan jika terjadi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }
}



