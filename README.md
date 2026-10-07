# Naukri Resume Auto-Upload

A Java 21 + Spring Boot + Playwright project for attempting a normal authenticated resume upload/update on Naukri twice daily. The laptop does not need to stay on when GitHub Actions performs the scheduled workflow.

## Important compliance

Use only your own account and normal authenticated access. Do not bypass CAPTCHA, OTP/MFA, bot detection, rate limits, or access controls. If verification appears, stop and re-authenticate manually.

## Stack

Java 21, Spring Boot 3.5.x, Maven, REST API, Playwright Java, Chromium, Docker, GitHub Actions.

## Local setup

1. Install Java 21 and Maven.
2. Put your resume at `resume/resume.pdf`.
3. Create `.env` from `.env.example`.
4. Run `mvn clean test`.
5. For a safe browser check, set `AUTOMATION_DRY_RUN=true` and start with `mvn spring-boot:run`.
6. API health: `/actuator/health`; status: `/api/status`; schedule: `/api/schedule`.

## Authentication

Create a Playwright storage state locally by logging in manually. Never commit the state file. The application looks for `NAUKRI_AUTH_STATE_PATH`, defaulting to `playwright/.auth/naukri.json`.

Because storage state contains authentication cookies/tokens, treat it like a password. If the site asks for CAPTCHA/MFA, complete it manually and recreate the state.

## REST API

- `GET /api/status`
- `POST /api/upload/trigger` (protect with `X-API-KEY` when `API_SECRET` is set)
- `GET /api/upload/history`
- `GET /api/config`
- `GET /api/schedule`
- `GET /actuator/health`

## Schedule

Spring local scheduler uses Asia/Kolkata at 08:00 and 13:00. GitHub Actions uses UTC cron:
- `30 2 * * *` = 08:00 IST
- `30 7 * * *` = 13:00 IST

GitHub scheduled workflows can be delayed by GitHub. The laptop can be off.

## GitHub Actions

Use a private repository. Do not commit your resume or auth state. For live operation, securely provision the resume and Playwright auth state as GitHub Secrets (prefer encoded secret material), reconstruct them only during the workflow, and delete them after the run. Keep logs free of secrets.

The included schedule workflow defaults to dry-run until you deliberately configure live secret provisioning.

## Docker

`docker compose up --build` starts the API with dry-run enabled. Do not place credentials directly in compose files.

## Selector maintenance

Naukri UI selectors can change. Site-specific selectors are isolated in `NaukriSelectors.java`. Do not claim selectors are verified if they have not been tested against the current site.

## Free/cloud note

GitHub Actions is used as the primary scheduled execution mechanism. Hosting a separate Spring Boot control API on a free tier is optional; do not assume any cloud provider's free tier is permanent.

## Security checklist

- No credentials in source
- No auth state in Git
- No real resume in Git
- API trigger protected when exposed publicly
- No secret logging
- No CAPTCHA/MFA bypass


## GitHub private inputs

Create two repository secrets:
- `RESUME_B64`: base64 of your private PDF resume.
- `NAUKRI_AUTH_STATE_B64`: base64 of `playwright/.auth/naukri.json`.

Example on Linux/macOS:
`base64 -w 0 resume/resume.pdf`
`base64 -w 0 playwright/.auth/naukri.json`

On Windows PowerShell, use `[Convert]::ToBase64String([IO.File]::ReadAllBytes("resume/resume.pdf"))` and save the output securely as the secret value.

The workflow reconstructs these files only inside the runner and deletes them after the job. Keep the repository private.

## Create auth state locally

Run:
`mvn exec:java -Dexec.mainClass=com.example.naukri.automation.AuthSetup`

A visible Chromium window opens. Log in manually, then press Enter in the terminal. The script saves the storage state. Do not commit it.
