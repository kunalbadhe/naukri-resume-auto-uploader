# TROUBLESHOOTING

| Problem | Cause | Solution |
|---|---|---|
| Login failed | Session expired | Recreate auth state manually |
| CAPTCHA appeared | Site requested verification | Complete it manually; never bypass |
| MFA/OTP appeared | Extra verification | Complete manually and refresh session |
| Browser failed | Missing browser/dependencies | Install Playwright Chromium |
| Resume not found | Wrong path | Set `RESUME_PATH` |
| Workflow delayed | GitHub scheduler behavior | Check Actions run history |
| Upload control missing | Naukri UI changed | Inspect screenshot and update `NaukriSelectors.java` |
| API unavailable | Hosting sleep/restart | Wait for startup or use GitHub workflow |
