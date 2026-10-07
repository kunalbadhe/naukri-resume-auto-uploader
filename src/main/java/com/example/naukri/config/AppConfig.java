package com.example.naukri.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
 @Value("${app.timezone}") public String timezone;
 @Value("${app.resume.path}") public String resumePath;
 @Value("${app.naukri.base-url}") public String baseUrl;
 @Value("${app.automation.dry-run}") public boolean dryRun;
 @Value("${app.automation.headless}") public boolean headless;
 @Value("${app.automation.max-retries}") public int maxRetries;
 @Value("${app.security.api-key:}") public String apiKey;
}