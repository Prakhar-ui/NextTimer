package com.prakhar.nextTimer.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/save-image")
    public ResponseEntity<String> saveImage(@RequestBody Map<String, String> imageInfo) {
        try {
            String imageData = imageInfo.get("imageData");
            String base64Data = imageData.replace("blob:", "");

            byte[] data = Base64.getDecoder().decode(base64Data);
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            String fileName = currentDate.format(formatter) + ".jpg";
            String imagePath = "src/main/resources/static/images/" + fileName;

            File file = new File(imagePath);

            if (!file.exists()) {
                try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    bos.write(data);
                }
                logger.info("Image saved successfully with file name: {}", fileName);
                return ResponseEntity.ok("Image saved successfully with file name: " + fileName);
            } else {
                logger.info("Image file already exists with file name: {}", fileName);
                return ResponseEntity.ok("Image file already exists with file name: " + fileName);
            }
        } catch (Exception e) {
            logger.error("Error saving image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving image: " + e.getMessage());
        }
    }
}
