package com.jme.spatch.backend.model.route_cost.service;


import com.jme.spatch.backend.model.route_cost.entity.RouteCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteCost,Long> {

}
