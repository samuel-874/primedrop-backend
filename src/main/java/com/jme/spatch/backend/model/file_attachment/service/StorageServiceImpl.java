package com.jme.spatch.backend.model.file_attachment.service;

import com.jme.spatch.backend.model.complain.entity.Complain;
import com.jme.spatch.backend.model.complain.service.ComplainRepository;
import com.jme.spatch.backend.model.file_attachment.entity.FileAttachment;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserRepository;
import com.jme.spatch.backend.model.user.service.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final ComplainRepository complainRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    private static String STORAGE_PATH = "C:/Users/test/Downloads/spatch[backend]/spatch-backend/src/main/resources/static/imgs/";

    @Override
    public ResponseEntity uploadImage(
            HttpServletRequest servletRequest,
            MultipartFile multipartFile)
            throws IOException {
        String IMAGE_PATH = STORAGE_PATH+multipartFile.getOriginalFilename();
        FileAttachment fileAttachment = FileAttachment.builder()
                .name(multipartFile.getOriginalFilename())
                .type(multipartFile.getContentType())
                .path(IMAGE_PATH)
                .build();
        multipartFile.transferTo(new File(IMAGE_PATH));

        UserEntity user = userUtils.extractUser(servletRequest);
        fileAttachment.setSource("profile pic");
        storageRepository.save(fileAttachment);

        user.setImagePath("/api/v1/auth/spatch/file/view?name="+ multipartFile.getOriginalFilename());
        userRepository.save(user);

      return ResponseHandler.handle(200,"Image saved", IMAGE_PATH);
    }


    @Override
    public ResponseEntity uploadAttachment(
            HttpServletRequest servletRequest,
            MultipartFile multipartFile,
            long complainId
    ) throws IOException {
        String FILE_PATH = STORAGE_PATH+multipartFile.getOriginalFilename();


        FileAttachment fileAttachment = FileAttachment.builder()
                .name(multipartFile.getOriginalFilename())
                .type(multipartFile.getContentType())
                .path(FILE_PATH)
                .source("complain attachment")
                .build();
        Optional<Complain> complainOptional =  complainRepository.findById(complainId);
                if(complainOptional.isEmpty()){
                    return ResponseHandler.handle(400,"No complain found with id "+complainId, null);
                }
        Complain complain = complainOptional.get();
        List<String> attachments  =  complain.getAttachment();
        if(attachments == null){
            attachments = new ArrayList<>();
        }
        attachments.add(FILE_PATH);
        complain.setAttachment(attachments);
        complainRepository.save(complain);
        storageRepository.save(fileAttachment);
        return ResponseHandler.handle(200,"Attachment saved", FILE_PATH);
    }


    @Override
    public ResponseEntity viewFile(String fileName) throws IOException {
        FileAttachment fileAttachment = storageRepository.findByName(fileName);
        if(fileAttachment == null){
            return ResponseHandler.handle(404,"file with name " + fileName + " not found", null);
        }
            String filepath = fileAttachment.getPath();
            byte[] image = Files.readAllBytes(new File(filepath).toPath());
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).contentType(MediaType.valueOf("image/jpg")).body(image);
    }
}
