package com.jme.spatch.backend.config.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSenderService {

    @Autowired
    private  TwilioConfig twilioConfig;

    public void sendSms(String phoneNumber, String message
    ){
        try{

        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        Message message2 = Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber("+13613093210"),
                message).create();
        }catch (Exception e){
            System.out.println(e);
        }
    }



}
