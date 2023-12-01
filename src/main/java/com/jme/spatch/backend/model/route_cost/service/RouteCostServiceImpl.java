package com.jme.spatch.backend.model.route_cost.service;

import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.route_cost.entity.RouteCost;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteCostServiceImpl implements RouteCostService {
    @Autowired
    private  RouteRepository routeRepository;



    @Override
    public ResponseEntity createRoute(RouteCost routeCost) {
        if(routeCost == null){
            return ResponseHandler.handle(403,"Request Cannot be Empty",null);
        }

        routeRepository.save(routeCost);

        return ResponseHandler.handle(201,"Route Cost created successfully",routeCost);
    }

    @Override
    public ResponseEntity updateRouteCost(RouteCost routeCost) {
        if(routeCost.getId() == 0){
            return ResponseHandler.handle(403,"Request Id cannot be null",null);
        }

        RouteCost cost = routeRepository.findById(routeCost.getId()).get();
        if(cost == null){
            return ResponseHandler.handle(403,"Request Cannot be Empty",null);
        }

        if(routeCost.getDistanceFrom() != 0){
            cost.setDistanceFrom(routeCost.getDistanceFrom());
        }
        if(routeCost.getDistanceTo() != 0){
            cost.setDistanceTo(routeCost.getDistanceTo());
        }
        if(routeCost.getAmount() != 0){
            cost.setAmount(routeCost.getAmount());
        }
        if(routeCost.getPercentageIncrease() != 0){
            cost.setPercentageIncrease(routeCost.getPercentageIncrease());
        }
        routeRepository.save(cost);

        return ResponseHandler.handle(200,"Route Cost updated successfully",cost);
    }

    @Override
    public ResponseEntity getRouteCost(long id) {
        Optional<RouteCost> routeCostOptional = routeRepository.findById(id);
        if(routeCostOptional.isEmpty()){
            return ResponseHandler.handle(404,"Route Cost not found",null);
        }

        RouteCost cost = routeCostOptional.get();
        return ResponseHandler.handle(200,"Route Cost fetched successfully",cost);
    }

    @Override
    public ResponseEntity getRoutesCost() {
        List<RouteCost> routeCosts = routeRepository.findAll();
        return ResponseHandler.handle(200,"Route Cost\'s fetched successfully",routeCosts);

    }

    @Override
    public List<RouteCost> allRoutesCost() {
        List<RouteCost> routeCosts = routeRepository.findAll();
        return routeCosts;
    }
}
