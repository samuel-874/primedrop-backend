package com.jme.spatch.backend.model.file_attachment.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {

    ResponseEntity uploadImage(HttpServletRequest servletRequest , MultipartFile multipartFile) throws IOException;
    ResponseEntity uploadAttachment(HttpServletRequest servletRequest , MultipartFile multipartFile, long complainId) throws IOException;

    ResponseEntity viewFile(String fileName) throws IOException;
}
