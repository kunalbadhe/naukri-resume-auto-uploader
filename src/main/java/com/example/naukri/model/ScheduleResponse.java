package com.example.naukri.model;
import java.util.List;
public record ScheduleResponse(String timezone,List<String> schedules) {}