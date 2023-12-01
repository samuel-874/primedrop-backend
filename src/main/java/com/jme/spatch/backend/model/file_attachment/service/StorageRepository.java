package com.jme.spatch.backend.model.file_attachment.service;

import com.jme.spatch.backend.model.file_attachment.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<FileAttachment,Long> {
    FileAttachment findByName(String fileName);
}
