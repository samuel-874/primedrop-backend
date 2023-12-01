package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.model.file_attachment.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/spatch/file")
public class StorageController {

    private final StorageService storageService;
    private final HttpServletRequest servletRequest;

    @PostMapping("/upload_profile")
    public ResponseEntity uploadProfile(
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        return storageService.uploadImage(servletRequest,file);
    }

         @PostMapping("/complain_attachment")
         public ResponseEntity uploadAttachment(
          @RequestParam("file") MultipartFile file,
          @RequestParam("id") long complainId
      )
            throws IOException {
        return storageService.uploadAttachment(servletRequest,file,complainId);
        }
        @GetMapping("/view")
         public ResponseEntity viewFIle(
          @RequestParam("name") String fileName
      )
            throws IOException {
        return storageService.viewFile(fileName);
        }





}
