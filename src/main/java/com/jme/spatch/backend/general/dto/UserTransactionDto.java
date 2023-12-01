package com.jme.spatch.backend.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransactionDto {
    private long id;
    private String name;
    private String phoneNum;
    private String email;
    private long regDate;

    public UserTransactionDto(String name, String phoneNum, String email, long regDate) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.regDate = regDate;
    }
}
