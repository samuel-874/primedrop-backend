package com.jme.spatch.backend.model.credit_card.service;

import com.jme.spatch.backend.model.credit_card.entity.CreditCard;
import com.jme.spatch.backend.model.credit_card.entity.SaveCardRequest;
import com.jme.spatch.backend.general.dto.ApiResponse;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.jme.spatch.backend.general.mapper.DtoMapper.mapCard;

@Service
public class CardServiceImpl implements CardService {

    private final UserUtils userUtils;
    private final CreditCardRepository cardRepository;

    @Autowired
    public CardServiceImpl(
            UserUtils userUtils,
            CreditCardRepository cardRepository) {
        this.userUtils = userUtils;
        this.cardRepository = cardRepository;
    }

    String flutterwaveSecretKey = System.getenv("FLUTTER_WAVE_SECRET_KEY");

    @Override
    public ResponseEntity saveCard(
            HttpServletRequest servletRequest,
            SaveCardRequest cardRequest) {
        CreditCard creditCard = mapCard(cardRequest);
        UserEntity user = userUtils.extractUser(servletRequest);
        creditCard.setCardHolder(user);
        cardRepository.save(creditCard);

        try {
            String url = "https://api.flutterwave.com/v3/tokenized-charges";
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setBearerAuth(flutterwaveSecretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SaveCardRequest> requestEntity = new HttpEntity<>(cardRequest, headers);

            ResponseEntity<ApiResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ApiResponse.class);

            String cardToken = response.getBody().getData().getCard_token();
            System.out.println(cardToken);
            creditCard.setCardToken(cardToken);
            cardRepository.save(creditCard);

            return ResponseHandler.handle(201, "Credit card saved successfully!", creditCard);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity getCards(HttpServletRequest servletRequest) {

        UserEntity user = userUtils.extractUser(servletRequest);
        List<CreditCard> creditCard = cardRepository.findByCardHolder(user);
        if (creditCard.isEmpty()) {
            return ResponseHandler.handle(200, "No card have been added yet", null);

        }

        return ResponseHandler.handle(200, "Credit cards retrieved successfully!", creditCard);
    }

    @Override
    public ResponseEntity getCardDetails(
            HttpServletRequest servletRequest, long id) {
        UserEntity user = userUtils.extractUser(servletRequest);
        CreditCard creditCard = cardRepository.findById(id).get();
        if (creditCard == null) {
            return ResponseHandler.handle(401, "No card was found with id: " + id, null);

        } else if (creditCard.getCardHolder() != user) {
            return ResponseHandler.handle(401, "Invalid request for Card", null);
        }
        return ResponseHandler.handle(200, "Credit card retrieved successfully!", creditCard);

    }

    @Override
    public ResponseEntity deleteCard(HttpServletRequest servletRequest, long id) {
        UserEntity user = userUtils.extractUser(servletRequest);
        CreditCard creditCard = cardRepository.findById(id).get();

        if (creditCard.getCardHolder() != user) {
            return ResponseHandler.handle(401, "Invalid request for Card", null);
        }

        cardRepository.delete(creditCard);

        return ResponseHandler.handle(200, "Credit card deleted successfully!", null);
    }
}
