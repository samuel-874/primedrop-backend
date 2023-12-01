package com.jme.spatch.backend.model.complain.service;

import com.jme.spatch.backend.model.complain.entity.Complain;
import com.jme.spatch.backend.general.dto.ComplainRequest;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.order.service.OrderRepository;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserService;
import com.jme.spatch.backend.model.user.service.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComplainServiceImpl implements ComplainService {
    private final ComplainRepository complainRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final UserUtils userUtils;

    @Override
    public ResponseEntity contactSupport( HttpServletRequest servletRequest, ComplainRequest complainRequest) {
        Complain complain = new Complain();
        UserEntity user = userUtils.extractUser(servletRequest);
        complain.setSubject(complainRequest.getSubject());
        complain.setContent(complainRequest.getContent());
        complain.setUser(user);
        long orderId = complainRequest.getOrderId();
        Optional <Order> orderOptional = orderRepository.findById(orderId);

        if(orderOptional.isPresent()){

            Order order = orderOptional.get();
            complain.setOrder(order);
        }
        complainRepository.save(complain);
        return ResponseHandler.handle(201,"Complain has been sent", complain);
    }

    @Override
    public ResponseEntity viewReport(long id) {
        Optional<Complain> complain = complainRepository.findById(id);
        if(complain.isEmpty()){
            return ResponseHandler.handle(403,"Complain not found", null);
        }
        Complain complain1 = complain.get();
        return ResponseHandler.handle(200,"Complain fetched successfully", complain1);
    }

    @Override
    public ResponseEntity viewUserReports(HttpServletRequest servletRequest) {
        UserEntity user = userUtils.extractUser(servletRequest);
        List <Complain> usersComplain = complainRepository.findByUser(user);
        return ResponseHandler.handle(200,"Complain for " + user.getFullName() +" fetched successfully", usersComplain);
    }


}
