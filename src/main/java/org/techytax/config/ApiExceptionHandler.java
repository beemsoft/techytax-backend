package org.techytax.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
    Map<String, String> body = new HashMap<>();
    String reason = ex.getReason();
    body.put("message", (reason != null && !reason.isBlank()) ? reason : "Request failed");
    return ResponseEntity.status(ex.getStatusCode()).body(body);
  }
}
