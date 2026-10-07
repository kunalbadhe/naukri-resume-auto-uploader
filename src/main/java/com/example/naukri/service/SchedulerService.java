package com.example.naukri.service;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private final NaukriAutomationService naukriAutomationService;

    public SchedulerService(NaukriAutomationService naukriAutomationService) {
        this.naukriAutomationService = naukriAutomationService;
    }

    // Every day at 8:00 AM
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Kolkata")
    public void morningUpload() {
        System.out.println("========================================");
        System.out.println("Scheduled upload started - 8:00 AM");
        System.out.println("========================================");

        naukriAutomationService.execute("SCHEDULED_8AM");
    }

    // Every day at 1:00 PM
    @Scheduled(cron = "0 0 13 * * *", zone = "Asia/Kolkata")
    public void afternoonUpload() {
        System.out.println("========================================");
        System.out.println("Scheduled upload started - 1:00 PM");
        System.out.println("========================================");

        naukriAutomationService.execute("SCHEDULED_1PM");
    }
}