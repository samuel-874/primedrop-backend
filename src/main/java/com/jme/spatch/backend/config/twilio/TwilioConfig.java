package com.jme.spatch.backend.config.twilio;


import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TwilioConfig {

    private String accountSid = System.getenv("twilio.account.sid");
    private String authToken = System.getenv("twilio.account.auth_token");
    private String twilioNum = System.getenv("twilio.account.trial_number");

}
