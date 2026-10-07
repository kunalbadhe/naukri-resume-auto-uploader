# ARCHITECTURE

```text
GitHub Actions / REST client
        |
        v
Spring Boot services
        |
        v
NaukriAutomationService
        |
        v
BrowserManager -> Playwright -> Chromium
        |
        v
Naukri normal authenticated session
```

Scheduled execution is externalized to GitHub Actions so the laptop is unnecessary. The local Spring scheduler exists for local/server execution at 08:00 and 13:00 Asia/Kolkata.

Failure flow: authentication/CAPTCHA/MFA/UI-change/network errors produce a bounded failure result and optional screenshot; no security bypass is attempted.
