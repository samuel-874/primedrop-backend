package com.jme.spatch.backend.model.settings.service;


import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.route_cost.entity.RouteCost;
import com.jme.spatch.backend.model.route_cost.service.RouteCostService;
import com.jme.spatch.backend.model.settings.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private RouteCostService costService;


    @Override
    public ResponseEntity init() {

        Settings allService = new Settings("All Services","We are maxed out!!! Please book at 4:00pm against tomorrow fulfilment We are maxed out!!! Please book at 4:00pm against tomorrow fulfilment We are maxed out!!! Please book at 4:00pm against tomorrow fulfilment",true);
        Settings sameDayBike = new Settings("Same Day Bike Delivery","We are maxed out!!! Please book from 4:00 pm against tomorrow fulfillment",true);
        Settings sameDayVan = new Settings("Same Day Van Delivery","We are carrying out our annual review for the year and won’t be operational tomorrow ( 29th of January, 2022). You can start to book by 4:00 pm on Sunday for fulfilment on Monday.",false);
        Settings expressBike = new Settings("Express Bike Delivery","We are fully booked",false);
        Settings expressVan = new Settings("Express Van Delivery","We are carrying out our annual review for the year and won’t be operational tomorrow ( 29th of January, 2022). You can start to book by 4:00 pm on Sunday for fulfilment on Monday.",false);
        Settings interstate = new Settings("Inter State Delivery","We are fully booked",false);
        Settings international = new Settings("International Delivery","We are fully booked",false);

        List<Settings> allSettings = new ArrayList<>();
            allSettings.add(allService);
            allSettings.add(sameDayBike);
            allSettings.add(sameDayVan);
            allSettings.add(expressBike);
            allSettings.add(expressVan);
            allSettings.add(interstate);
            allSettings.add(international);

        settingsRepository.saveAll(allSettings);

        return ResponseHandler.handle(201,"Settings initialize successfully", allSettings);
    }




    @Override
    public ResponseEntity addSettings(Settings settings) {
        if(settings == null){
            return ResponseHandler.handle(403,"Request didn't contain a valid body", null);
        }

        settingsRepository.save(settings);
        return ResponseHandler.handle(201,"Setting Added successfully", settings);
    }

    @Override
    public ResponseEntity updateSettings(long id, Settings settingsUpdate) {
        Settings settings = settingsRepository.findById(id).get();

           if(settingsUpdate.getStatus() != null){
               settings.setStatus(settingsUpdate.getStatus());
           }



           if(settingsUpdate.getSystemMessage() != null){
               settings.setSystemMessage(settingsUpdate.getSystemMessage());
           }

        if(settingsUpdate.getService().equals("All Services")){
            if(settingsUpdate.getStatus() == false){
                List<Settings> allSettings = settingsRepository.findAll();
                for(Settings eachSetting : allSettings){
                    eachSetting.setStatus(false);
                }
                settingsRepository.saveAll(allSettings);
            }
        }

           settingsRepository.save(settings);

        return ResponseHandler.handle(200,"Settings updated successfully", settings);
    }

    @Override
    public ResponseEntity updateSettings(List<Settings> settingsUpdate) {
        settingsRepository.saveAll(settingsUpdate);
        return ResponseHandler.handle(200,"Settings updated successfully", settingsUpdate);
    }

    @Override
    public ResponseEntity deleteSettings(long id) {
        Optional<Settings> settingsOptional = settingsRepository.findById(id);
        if(settingsOptional.isEmpty())
            return ResponseHandler.handle(404,"Settings not found", null);
        else
            settingsRepository.delete(settingsOptional.get());

        return ResponseHandler.handle(200,"Settings deleted successfully", null);
    }

    @Override
    public ResponseEntity getAllSettings() {
        List<Settings> settings = settingsRepository.findAll();
        return ResponseHandler.handle(200,"Settings fetched successfully", settings);
    }
    @Override
    public ResponseEntity getAppSettings() {
        List<Settings> settings = settingsRepository.findAll();
        List<RouteCost> routeCosts = costService.allRoutesCost();
        List appSettings = new ArrayList<>();
            appSettings.add(settings);
            appSettings.add(routeCosts);
        return ResponseHandler.handle(200,"Settings fetched successfully", appSettings);
    }



       @Override
    public ResponseEntity deleteAll() {
        settingsRepository.deleteAll();
        return ResponseHandler.handle(200,"Settings truncated successfully", null);
    }


}
