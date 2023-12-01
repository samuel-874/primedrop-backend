package com.jme.spatch.backend.model.route_cost.service;

import com.jme.spatch.backend.model.route_cost.entity.RouteCost;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface RouteCostService {
    ResponseEntity createRoute(RouteCost routeCost);
    ResponseEntity updateRouteCost(RouteCost routeCost);
    ResponseEntity getRouteCost(long id);
    ResponseEntity getRoutesCost();
    List<RouteCost> allRoutesCost();
}
