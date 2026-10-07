package com.example.naukri.service;
import com.example.naukri.model.*;
import org.springframework.stereotype.Service;
import java.time.*;
@Service
public class UploadService {
 private final NaukriAutomationService automation; private final HistoryService history;
 public UploadService(NaukriAutomationService automation,HistoryService history){this.automation=automation;this.history=history;}
 public UploadResult run(String trigger){
  UploadResult r=automation.execute(trigger);
  history.add(new UploadHistory(r.timestamp(),r.trigger(),r.status(),r.message()));
  return r;
 }
}