package com.jme.spatch.backend.controller.user;


import com.jme.spatch.backend.general.dto.OrderRequest;
import com.jme.spatch.backend.model.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/spatch/order")
public class OrderController {

    private final HttpServletRequest servletRequest;
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity saveOrder(
            @Valid  @RequestBody OrderRequest orderRequest){
       return orderService.createOrder(orderRequest,servletRequest);
    }

      @GetMapping
    public ResponseEntity getOrder(
            @RequestParam String orderId
      ){
       return orderService.getOrder(orderId,servletRequest );
      }

    @GetMapping("/all")
    public ResponseEntity allOrders(){
        return orderService.getAllOrder(servletRequest);
    }

}
