# DEPLOYMENT

## Recommended split

GitHub Actions is the scheduler/browser runner. A Render/free REST API is optional and is not required for scheduled execution.

### GitHub
1. Create a private repository.
2. Push this project.
3. Add secrets for the auth state and resume using a secure encoding strategy.
4. Reconstruct files during the workflow and remove them after execution.
5. Run the workflow manually first.
6. Confirm logs/artifacts.
7. Scheduled runs are 08:00 and 13:00 IST via UTC cron.

### API hosting
If you deploy Spring Boot separately, use a suitable service available at implementation time. Set `API_SECRET` and environment variables. Verify `/actuator/health`. Do not assume a free web service stays permanently free.

### Laptop
After cloud setup and verification, the laptop can be off because GitHub runs the scheduled job remotely.
