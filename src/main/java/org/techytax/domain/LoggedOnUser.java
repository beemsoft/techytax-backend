package org.techytax.domain;

import lombok.Data;

@Data
public class LoggedOnUser {
   private String username;
   private String firstName;
   private String token;
}
