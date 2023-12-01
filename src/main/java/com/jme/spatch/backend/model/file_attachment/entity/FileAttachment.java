package com.jme.spatch.backend.model.file_attachment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {
    @Id
    @SequenceGenerator(
            name = "file_seq",
            sequenceName = "file_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "file_seq")
    private long id;
    private String name;
    private String path;
    private String type;
    private String source;
}