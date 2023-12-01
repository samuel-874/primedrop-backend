package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.model.credit_card.entity.SaveCardRequest;
import com.jme.spatch.backend.model.credit_card.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/spatch/card")
public class CardController {

    private final CardService cardService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/add")
    ResponseEntity saveCard(
            @RequestBody SaveCardRequest cardRequest
    ){
        return cardService.saveCard(servletRequest,cardRequest);
    }

        @GetMapping("/all")
    ResponseEntity getCards(){
        return cardService.getCards(servletRequest);
    }

        @GetMapping
    ResponseEntity getCard(@RequestParam long id){
        return cardService.getCardDetails(servletRequest,id);
    }

           @DeleteMapping("remove")
    ResponseEntity remove(@RequestParam long id){
        return cardService.deleteCard(servletRequest,id);
    }




}
