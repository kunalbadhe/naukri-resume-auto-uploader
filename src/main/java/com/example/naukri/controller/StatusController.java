package com.example.naukri.controller;
import com.example.naukri.config.AppConfig;
import com.example.naukri.model.*;
import com.example.naukri.service.HistoryService;
import org.springframework.web.bind.annotation.*;
import java.time.*; import java.util.*;
@RestController @RequestMapping("/api")
public class StatusController {
 private final AppConfig config; private final HistoryService history;
 public StatusController(AppConfig c,HistoryService h){config=c;history=h;}
 @GetMapping("/status") public StatusResponse status(){
  var h=history.recent().stream().findFirst();
  String next=LocalDate.now(ZoneId.of(config.timezone)).atTime(LocalTime.of(8,0)).atZone(ZoneId.of(config.timezone)).toOffsetDateTime().toString();
  if(ZonedDateTime.now(ZoneId.of(config.timezone)).toLocalTime().isAfter(LocalTime.of(8,0))) next=LocalDate.now(ZoneId.of(config.timezone)).plusDays(1).atTime(8,0).atZone(ZoneId.of(config.timezone)).toOffsetDateTime().toString();
  return new StatusResponse("naukri-resume-auto-uploader","UP",h.map(UploadHistory::timestamp).orElse(null),h.map(UploadHistory::status).orElse("NEVER_RUN"),next);
 }
 @GetMapping("/config") public Map<String,Object> config(){return Map.of("timezone",config.timezone,"resumePath",config.resumePath,"dryRun",config.dryRun);}
 @GetMapping("/schedule") public ScheduleResponse schedule(){return new ScheduleResponse(config.timezone,List.of("08:00","13:00"));}
}