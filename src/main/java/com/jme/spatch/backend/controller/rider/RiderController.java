package com.jme.spatch.backend.controller.rider;


import com.jme.spatch.backend.general.dto.PasswordUpdateRequest;
import com.jme.spatch.backend.general.dto.ProfileUpdateRequest;
import com.jme.spatch.backend.model.order.service.OrderService;
import com.jme.spatch.backend.model.rider.service.RiderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rider/spatch/auth/")
@AllArgsConstructor
public class RiderController {

    private final RiderService riderService;
    private final OrderService orderService;
    private final HttpServletRequest servletRequest;


        @PostMapping("password-update")
         public ResponseEntity updatePassword(
               @Valid   @RequestBody PasswordUpdateRequest updateRequest
         ){
        return riderService.updatePassword(updateRequest,servletRequest);
    }

         @PostMapping("profile-update")
         public ResponseEntity updateProfile(
               @Valid @RequestBody ProfileUpdateRequest updateRequest
         ){
        return riderService.updateProfile(updateRequest,servletRequest);
    }


            @GetMapping("get")
          public ResponseEntity getRidersDetails(){
        return riderService.getDetails(servletRequest);
    }

           @GetMapping("orders")
         public ResponseEntity getRidersOrder(){
       return orderService.getAllRidersOrder(servletRequest);
    }

        @GetMapping("order")
         public ResponseEntity getOrder(
            @RequestParam("orderId") String orderId
         ){
      return orderService.getOrderByIdForRider(orderId,servletRequest);
    }

            @PutMapping("start")
        public ResponseEntity startOrder(
                @RequestParam("orderId") String orderId
        ){
            return orderService.startOrder(orderId);
        }

          @PutMapping("complete")
          public ResponseEntity completeOrder(
                @RequestParam("orderId") String orderId
       ){
        return orderService.completeOrder(orderId);
    }

             @DeleteMapping("cancel")
            public ResponseEntity cancelOrder(
             @RequestParam("orderId") String orderId
           ){
        return orderService.cancelOrder(orderId);
    }




}
