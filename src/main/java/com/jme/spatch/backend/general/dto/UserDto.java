package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.user.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
   private long id;
   private String fullName;
   private String email;
   private String phoneNum;
   private Status status;

}
