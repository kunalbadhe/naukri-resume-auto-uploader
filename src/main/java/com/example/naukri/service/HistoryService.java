package com.example.naukri.service;
import com.example.naukri.model.UploadHistory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
@Service
public class HistoryService {
 private final List<UploadHistory> history=new CopyOnWriteArrayList<>();
 public void add(UploadHistory h){history.add(0,h); while(history.size()>50) history.remove(history.size()-1);}
 public List<UploadHistory> recent(){return List.copyOf(history);}
}