package com.jme.spatch.backend.model.token.service;

import com.jme.spatch.backend.config.mail.MailSenderService;
import com.jme.spatch.backend.config.twilio.TwilioSenderService;
import com.jme.spatch.backend.model.token.entity.Token;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Random;

@Service
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final MailSenderService mailSenderService;
    private final TwilioSenderService senderService;



    public TokenServiceImpl(
            TokenRepository tokenRepository,
            MailSenderService mailSenderService,
            TwilioSenderService senderService) {
        this.tokenRepository = tokenRepository;
        this.mailSenderService = mailSenderService;
        this.senderService = senderService;
    }

    @Override
    public String generateToken()
    {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }


    @Override
    public void sendOtpToEmail(
            String email
    ){
        Token existingToken = tokenRepository.findByOrigin(email);
            if(existingToken != null){
                tokenRepository.delete(existingToken);
            }

        String otp = generateToken();
        System.out.println("otp for :" + email + " is "+otp);
        Token token = new Token();
        token.setOrigin(email);
        token.setOtp(otp);
        long expiration = System.currentTimeMillis() + (30 * 60 * 1000);
        token.setExpiration(expiration); //30mins in miliseconds
            tokenRepository.save( token );

        String key = System.getenv("SECRET_KEY");
        String baseUrl = System.getenv("frontend.baseurl");
        String token1 = key.concat(otp).concat(email);
        String encodedToken = Base64.getEncoder().encodeToString(token1.getBytes());
        String url =baseUrl + "/signup/spatch/confirmation/email?token="+ encodedToken;
              System.out.println(url);
        try{
//            mailSenderService.sendMail(email,url);
        }catch (Exception e){
            System.out.println("Smtp server responded with bad greeting");
        }

    }


    @Override
    public void sendOtpToMobileNum(String number){

        Token existingToken = tokenRepository.findByOrigin(number);
        if(existingToken != null){
            tokenRepository.deleteById(existingToken.getId());
        }


        String otp = generateToken();
        Token token = new Token();
        token.setExpiration(System.currentTimeMillis() + (30 * 60 * 1000));
        token.setOrigin(number);
        token.setOtp(otp);
        tokenRepository.save(token);
        String message = "Dear user your otp is " + otp + ". It expires in 5 minutes";
        System.out.println(message);
        /*senderService.sendSms(number, message);*/
    }

    @Override
    public Boolean validateToken(String origin, String otp) {
        Token token = tokenRepository.findByOtp(otp);
            if(token == null){
                throw new NullPointerException("Otp was not recognized");
            }
            if(!token.getOrigin().equals(origin)){
                throw new IllegalIdentifierException("Otp was not assign to the User");
            }
            long expiration = token.getExpiration();
        Boolean isValid = System.currentTimeMillis() < expiration && token.getOrigin().equals(origin);
            if(isValid){
               tokenRepository.deleteById(token.getId());
            }
        return isValid;
    }

    @Override
    public void deleteToken(String otp) {
        Token token = tokenRepository.findByOtp(otp);
        if(token == null){
            throw  new NullPointerException("Otp was not recognized");
        }
        tokenRepository.deleteById(token.getId());
    }
}
