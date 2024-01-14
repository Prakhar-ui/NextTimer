package com.prakhar.nextTimer.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;


    @RestController
    @RequestMapping("/api")
    public class ImageController {
        @PostMapping("/save-image")
        public ResponseEntity<String> saveImage(@RequestBody Map<String, String> imageInfo) {
            try {
                String imageData = imageInfo.get("imageData");
                // Remove the "blob:" prefix
                String base64Data = imageData.replace("blob:", "");

                // Decode the base64 data
                byte[] data = Base64.getDecoder().decode(base64Data);
                // Save the image to resources/static/images with the current date as the file name
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
                String fileName = currentDate.format(formatter) + ".jpg";
                String imagePath = "src/main/resources/static/images/" + fileName;

                File file = new File(imagePath);

                if (!file.exists()) {
                    try (FileOutputStream fos = new FileOutputStream(file);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        bos.write(data);
                    }

                    return ResponseEntity.ok("Image saved successfully with file name: " + fileName);
                } else {
                    return ResponseEntity.ok("Image file already exists with file name: " + fileName);
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving image: " + e.getMessage());
            }
        }
    }