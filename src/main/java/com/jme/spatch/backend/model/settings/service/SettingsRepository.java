package com.jme.spatch.backend.model.settings.service;

import com.jme.spatch.backend.model.settings.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingsRepository extends JpaRepository<Settings,Long> {
}
