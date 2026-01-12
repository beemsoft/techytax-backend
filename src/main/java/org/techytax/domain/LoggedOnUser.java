package org.techytax.domain;

import lombok.Data;

@Data
public class LoggedOnUser {
   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   private String username;
   private String firstName;
   private String token;
}
