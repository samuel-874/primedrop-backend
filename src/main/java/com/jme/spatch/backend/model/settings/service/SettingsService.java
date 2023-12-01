package com.jme.spatch.backend.model.settings.service;


import com.jme.spatch.backend.model.settings.model.Settings;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SettingsService {
    ResponseEntity init ();
    ResponseEntity addSettings (Settings  settings);
    ResponseEntity updateSettings(long id,Settings settingsUpdate);
    ResponseEntity updateSettings(List<Settings> settingsUpdate);

    ResponseEntity deleteSettings(long id);

    ResponseEntity getAllSettings();

    ResponseEntity getAppSettings();

    ResponseEntity deleteAll();
}
