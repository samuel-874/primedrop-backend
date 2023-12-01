package com.jme.spatch.backend.controller.admin;

import com.jme.spatch.backend.general.dto.OrderUpdateRequest;
import com.jme.spatch.backend.model.Transaction.service.TransactionService;
import com.jme.spatch.backend.model.admins.service.AdminService;
import com.jme.spatch.backend.model.order.service.OrderService;
import com.jme.spatch.backend.model.rider.entity.RiderRegRequest;
import com.jme.spatch.backend.model.rider.service.RiderService;
import com.jme.spatch.backend.model.route_cost.entity.RouteCost;
import com.jme.spatch.backend.model.route_cost.service.RouteCostService;
import com.jme.spatch.backend.model.settings.model.Settings;
import com.jme.spatch.backend.model.settings.service.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/spatch")
public class AdminController {
    private final AdminService adminService;
    private final OrderService orderService;
    private final RiderService riderService;
    private final SettingsService settingsService;
    private final HttpServletRequest servletRequest;
    private final TransactionService transactionService;
    private final RouteCostService costService;

//    @PostMapping("/save")
//    public ResponseEntity registerAdmin(@Valid @RequestBody AdminRegistration adminRegistration){
//        return adminService.registerAdmin(adminRegistration,servletRequest);
//    }

    @GetMapping("/req/get")
    public ResponseEntity getDetails(){
        return adminService.getDetails(servletRequest);
    }

    @GetMapping("/req/app-data")
    public ResponseEntity getAppData(){
        return adminService.getAppData(servletRequest);
    }

    @GetMapping("/req/get-orders")
    public ResponseEntity getOrders(){
        return orderService.getAllOrdersAdmin(servletRequest);
    }

    @GetMapping("/req/get-order")
    public ResponseEntity getOrder(@RequestParam String orderId){
        return orderService.getOrderAdmin(orderId);
    }

    @PutMapping("/req/update-order")
    public ResponseEntity updateOrder(
            @RequestBody OrderUpdateRequest updateRequest){
        return orderService.updateOrder(updateRequest);
    }

    @GetMapping("/req/get-users")
    public ResponseEntity getUsers(){
        return adminService.getAllUsers();
    }

    @GetMapping("/req/get-user")
    public ResponseEntity getUser(@RequestParam long userId){
        return adminService.getUserDetails(userId);
    }

    @GetMapping("/req/get-riders")
    public  ResponseEntity getAllRider(){
        return riderService.getAllRiders();
    }

    @GetMapping("/req/get-rider")
    public ResponseEntity getRiderDetails(@RequestParam long riderId){
        return riderService.getRiderDetails(riderId);
    }

    @GetMapping("/req/get-admins")
    public ResponseEntity getAdmins(){
        return adminService.getAllAdmins();
    }

    @PostMapping("/req/add-rider")
    public ResponseEntity saveRider(@RequestBody RiderRegRequest regRequest){
        return riderService.addRider(regRequest);
    }

    @PostMapping("/req/init-settings")
    public ResponseEntity initSettings(){
        return settingsService.init();
    }

    @PostMapping("/req/add-settings")
    public ResponseEntity addSettings(@RequestBody Settings settings){
        return settingsService.addSettings(settings);
    }

       @PutMapping("/req/update-setting")
    public ResponseEntity updateSettings(@RequestParam long id, @RequestBody Settings settings){
        return settingsService.updateSettings(id,settings);
    }

           @PutMapping("/req/update-settings")
    public ResponseEntity updateSettings(@RequestBody List<Settings> settings){
        return settingsService.updateSettings(settings);
    }



    @DeleteMapping("/req/delete-setting")
    public ResponseEntity deleteSettings(@RequestParam long id){
        return settingsService.deleteSettings(id);
    }

     @DeleteMapping("/req/delete-settings")
    public ResponseEntity deleteSettings() {
         return settingsService.deleteAll();
     }

    @GetMapping("/req/get-settings")
    public ResponseEntity getAll(){
        return settingsService.getAllSettings();
    }

        @GetMapping("/req/get-transactions")
    public ResponseEntity getTransactions(){
        return transactionService.getAllTransactions();
    }

        @PostMapping("/req/create-cost")
    public ResponseEntity createCost(@RequestBody RouteCost routeCost){
        return costService.createRoute(routeCost);
    }
        @PutMapping("/req/update-cost")
    public ResponseEntity updateCost(@RequestBody RouteCost routeCost){
        return costService.updateRouteCost(routeCost);
    }

    @GetMapping("/req/get-cost")
    public ResponseEntity getCost(@RequestParam long id){
        return costService.getRouteCost(id);
    }

    @GetMapping("/req/get-costs")
    public ResponseEntity getCost(){
        return costService.getRoutesCost();
    }
}
