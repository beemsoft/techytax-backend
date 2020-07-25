package org.techytax.domain;

import lombok.Data;

@Data
public class RegisterUser {
   private String username;
   private String firstName;
   private String lastName;
   private String password;
}
