package com.example.naukri.controller;
import com.example.naukri.model.*;
import com.example.naukri.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api")
public class UploadController {
 private final UploadService uploadService; private final HistoryService history; private final ApiKeyGuard guard;
 public UploadController(UploadService u,HistoryService h,ApiKeyGuard g){uploadService=u;history=h;guard=g;}
 @PostMapping("/upload/trigger") public UploadResponse trigger(HttpServletRequest req){guard.check(req); uploadService.run("MANUAL"); return new UploadResponse("STARTED","Resume upload job triggered");}
 @GetMapping("/upload/history") public List<UploadHistory> history(){return history.recent();}
}