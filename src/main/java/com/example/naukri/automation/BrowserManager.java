package com.example.naukri.automation;
import com.example.naukri.config.AppConfig;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;
@Component
public class BrowserManager {
 private final AppConfig config;
 public BrowserManager(AppConfig config){this.config=config;}
 public Playwright openPlaywright(){return Playwright.create();}
 public Browser openBrowser(Playwright pw){
  return pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(config.headless));
 }
}