package com.example.naukri.service;

import com.example.naukri.automation.BrowserManager;
import com.example.naukri.automation.NaukriSelectors;
import com.example.naukri.config.AppConfig;
import com.example.naukri.model.UploadResult;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitUntilState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;

@Service
public class NaukriAutomationService {

  private static final Logger log = LoggerFactory.getLogger(NaukriAutomationService.class);

  private final AppConfig config;
  private final BrowserManager browserManager;

  public NaukriAutomationService(
      AppConfig config,
      BrowserManager browserManager) {

    this.config = config;
    this.browserManager = browserManager;
  }

  public UploadResult execute(String trigger) {

    String now = OffsetDateTime.now(
        ZoneId.of(config.timezone)).toString();

    log.info("========================================");
    log.info("NAUKRI AUTOMATION STARTED");
    log.info("Trigger: {}", trigger);
    log.info("Dry Run: {}", config.dryRun);
    log.info("========================================");

    Path resume = Paths.get(config.resumePath);

    log.info(
        "Checking resume file: {}",
        resume.toAbsolutePath());

    if (!Files.isRegularFile(resume)
        || !Files.isReadable(resume)) {

      return new UploadResult(
          "FAILED",
          trigger,
          "Resume file not found/readable: "
              + resume.toAbsolutePath(),
          "RESUME_NOT_FOUND",
          now);
    }

    log.info("Resume file found successfully.");

    try (
        Playwright playwright = browserManager.openPlaywright();

        Browser browser = browserManager.openBrowser(playwright)) {

      log.info("Playwright started.");
      log.info("Browser started.");

      Path authState = Paths.get(
          System.getenv().getOrDefault(
              "NAUKRI_AUTH_STATE_PATH",
              "playwright/.auth/naukri.json"));

      BrowserContext context;

      if (Files.isRegularFile(authState)) {

        log.info(
            "Authentication state found: {}",
            authState.toAbsolutePath());

        context = browser.newContext(
            new Browser.NewContextOptions()
                .setStorageStatePath(authState));

      } else {

        log.warn(
            "Authentication state not found.");

        context = browser.newContext();
      }

      try (context) {

        Page page = context.newPage();

        log.info("Browser page created.");

        // -------------------------------------------------
        // Open Naukri
        // -------------------------------------------------

        page.navigate(
            config.baseUrl,
            new Page.NavigateOptions()
                .setWaitUntil(
                    WaitUntilState.LOAD)
                .setTimeout(30000));

        log.info(
            "Naukri opened. URL: {}",
            page.url());

        log.info(
            "Naukri title: {}",
            page.title());

        // -------------------------------------------------
        // Check login
        // -------------------------------------------------

        String currentUrl = page.url().toLowerCase(Locale.ROOT);

        if (currentUrl.contains("login")
            || currentUrl.contains("loginpage")) {

          return failScreenshot(
              page,
              trigger,
              "AUTHENTICATION_REQUIRED",
              "Naukri login session is not active.");
        }

        log.info(
            "Authentication appears to be active.");

        // -------------------------------------------------
        // Profile
        // -------------------------------------------------

        String profileUrl = "https://www.naukri.com/mnjuser/profile";

        log.info(
            "Navigating to profile: {}",
            profileUrl);

        page.navigate(
            profileUrl,
            new Page.NavigateOptions()
                .setWaitUntil(
                    WaitUntilState.LOAD)
                .setTimeout(30000));

        log.info(
            "Profile page loaded.");

        log.info(
            "Profile URL: {}",
            page.url());

        log.info(
            "Profile title: {}",
            page.title());

        // -------------------------------------------------
        // Wait for UI
        // -------------------------------------------------

        log.info(
            "Waiting for profile UI...");

        page.waitForTimeout(5000);

        // -------------------------------------------------
        // Screenshot
        // -------------------------------------------------

        saveScreenshot(
            page,
            "screenshots/naukri-opened.png");

        // -------------------------------------------------
        // Read page
        // -------------------------------------------------

        String body = page.locator("body")
            .innerText(
                new Locator.InnerTextOptions()
                    .setTimeout(10000));

        log.info(
            "Rendered body text length: {}",
            body.length());

        String bodyLower = body.toLowerCase(Locale.ROOT);

        // -------------------------------------------------
        // CAPTCHA / verification check
        // -------------------------------------------------

        if (bodyLower.contains("captcha")
            || bodyLower.contains(
                "verify you are human")) {

          return failScreenshot(
              page,
              trigger,
              "CAPTCHA_REQUIRED",
              "Site requested verification.");
        }

        if (bodyLower.contains("two-factor")
            || bodyLower.contains("otp")
            || bodyLower.contains(
                "one-time password")) {

          return failScreenshot(
              page,
              trigger,
              "MFA_REQUIRED",
              "Additional verification is required.");
        }

        log.info(
            "Security verification check completed.");

        // -------------------------------------------------
        // Find file inputs
        // -------------------------------------------------

        log.info(
            "Searching for resume upload controls...");

        Locator inputs = page.locator(
            NaukriSelectors.RESUME_INPUT);

        int count = inputs.count();

        log.info(
            "File input controls found: {}",
            count);

        for (int i = 0; i < count; i++) {

          try {

            log.info(
                "File input #{} visible: {}",
                i,
                inputs.nth(i).isVisible());

          } catch (Exception e) {

            log.warn(
                "Could not inspect input #{}: {}",
                i,
                e.getMessage());
          }
        }

        // -------------------------------------------------
        // Dry run
        // -------------------------------------------------

        if (config.dryRun) {

          log.info(
              "========================================");

          log.info(
              "DRY RUN MODE");

          log.info(
              "Actual resume upload will NOT happen.");

          log.info(
              "File input count: {}",
              count);

          log.info(
              "========================================");

          return new UploadResult(
              "SUCCESS",
              trigger,
              "Dry run completed successfully.",
              "",
              now);
        }

        // -------------------------------------------------
        // Actual upload
        // -------------------------------------------------

        if (count == 0) {

          return failScreenshot(
              page,
              trigger,
              "TARGET_PAGE_CHANGED",
              "No resume file input was found.");
        }

        Locator input = inputs.first();

        log.info(
            "Resume file input found. Visible: {}",
            input.isVisible());

        log.info(
            "Hidden file inputs are allowed for Playwright setInputFiles().");

        log.info(
            "Selecting resume file: {}",
            resume.toAbsolutePath());

        try {

          input.setInputFiles(resume);

          log.info(
              "Resume file selected successfully.");

          page.waitForTimeout(3000);

          saveScreenshot(
              page,
              "screenshots/after-resume-selection.png");

          log.info(
              "Post-selection screenshot captured.");

          return new UploadResult(
              "SUCCESS",
              trigger,
              "Resume file was selected successfully.",
              "",
              now);

        } catch (Exception uploadError) {

          log.error(
              "Resume file selection failed.",
              uploadError);

          return new UploadResult(
              "FAILED",
              trigger,
              "Resume file selection failed: "
                  + uploadError.getMessage(),
              "UPLOAD_FAILED",
              now);
        }

      }

    } catch (PlaywrightException e) {

      log.error(
          "Playwright automation failed.",
          e);

      return new UploadResult(
          "FAILED",
          trigger,
          "Browser automation failed: "
              + e.getMessage(),
          "BROWSER_START_FAILED",
          now);

    } catch (Exception e) {

      log.error(
          "Unexpected automation error.",
          e);

      return new UploadResult(
          "FAILED",
          trigger,
          "Unexpected error: "
              + e.getMessage(),
          "UNKNOWN_ERROR",
          now);
    }
  }

  // =============================================================
  // Screenshot helper
  // =============================================================

  private void saveScreenshot(
      Page page,
      String filePath) {

    try {

      Path path = Paths.get(filePath);

      if (path.getParent() != null) {
        Files.createDirectories(path.getParent());
      }

      page.screenshot(
          new Page.ScreenshotOptions()
              .setPath(path)
              .setFullPage(true));

      log.info(
          "Screenshot saved: {}",
          path.toAbsolutePath());

    } catch (Exception e) {

      log.warn(
          "Could not save screenshot: {}",
          e.getMessage());
    }
  }

  // =============================================================
  // Failure helper
  // =============================================================

  private UploadResult failScreenshot(
      Page page,
      String trigger,
      String category,
      String message) {

    String fileName = "screenshots/"
        + category.toLowerCase(Locale.ROOT)
        + ".png";

    saveScreenshot(page, fileName);

    log.error(
        "Automation failed. Category: {} | Message: {}",
        category,
        message);

    return new UploadResult(
        "FAILED",
        trigger,
        message,
        category,
        OffsetDateTime.now(
            ZoneId.of(config.timezone)).toString());
  }
}