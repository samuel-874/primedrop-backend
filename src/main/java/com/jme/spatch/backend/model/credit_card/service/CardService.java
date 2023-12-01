package com.jme.spatch.backend.model.credit_card.service;


import com.jme.spatch.backend.model.credit_card.entity.SaveCardRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    ResponseEntity saveCard( HttpServletRequest servletRequest ,SaveCardRequest cardRequest);
    ResponseEntity getCards(HttpServletRequest servletRequest);
    ResponseEntity getCardDetails(HttpServletRequest servletRequest,long id);
    ResponseEntity deleteCard(HttpServletRequest servletRequest,long id);

}
