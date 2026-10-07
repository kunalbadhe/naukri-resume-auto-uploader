package com.example.naukri.controller;
import com.example.naukri.config.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
@Component
public class ApiKeyGuard {
 private final AppConfig config;
 public ApiKeyGuard(AppConfig config){this.config=config;}
 public void check(HttpServletRequest request){
  if(config.apiKey==null||config.apiKey.isBlank()) return;
  String supplied=request.getHeader("X-API-KEY");
  if(!config.apiKey.equals(supplied)) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid API key");
 }
}