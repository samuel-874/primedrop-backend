package com.jme.spatch.backend.model.settings.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @SequenceGenerator(name = "settings_id",sequenceName = "settings_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settings_id")
    private long id;
    @Column(unique = true)
    private String service;
    private String systemMessage;
    private Boolean status;




    public Settings(String service, String systemMessage, Boolean status){
        this.service = service;
        this.systemMessage = systemMessage;
        this.status = status;
    }
}
