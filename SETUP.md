# SETUP

## 1. Local
Install Java 21, Maven and Docker (optional).

## 2. Resume
Copy your own PDF to `resume/resume.pdf`.

## 3. Auth state
Run a small local Playwright setup flow that opens Chromium, manually log in to Naukri, and save storage state to `playwright/.auth/naukri.json`. Keep this file private.

## 4. Dry run
Set `AUTOMATION_DRY_RUN=true`. Start Spring Boot and verify `/api/status` and the browser flow.

## 5. Live mode
Only after confirming normal authenticated access and the current Naukri UI, set `AUTOMATION_DRY_RUN=false`. If CAPTCHA, OTP/MFA, bot verification, or login failure occurs, stop and refresh authentication manually.
