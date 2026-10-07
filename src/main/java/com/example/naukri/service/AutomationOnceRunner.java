package com.example.naukri.service;
import com.example.naukri.model.UploadResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
@Component
public class AutomationOnceRunner implements CommandLineRunner {
 private final UploadService uploadService;
 @Value("${automation.once:false}") private boolean once;
 public AutomationOnceRunner(UploadService uploadService){this.uploadService=uploadService;}
 @Override public void run(String... args){
  if(once){
   UploadResult r=uploadService.run("GITHUB_ACTION");
   if(!"SUCCESS".equals(r.status())) throw new IllegalStateException(r.errorCategory()+": "+r.message());
  }
 }
}