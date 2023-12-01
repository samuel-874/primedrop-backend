package com.jme.spatch.backend.config.mail;

public class EmailTemplate {

    public static String getRegMail(String url){
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Confirm email</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"mail\">\n" +
                    "        <div class=\"section-1\">\n" +
                    "            <a href=\""+url+"\">verify email</a>\n" +
                    "        </div>\n" +
                    "        \n" +
                    "        <div class=\"section-2\"></div>\n" +
                    "        <div class=\"footer\"></div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
    }
}
