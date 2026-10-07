package com.example.naukri.automation;
import com.microsoft.playwright.*;
import java.nio.file.*;
public class AuthSetup {
 public static void main(String[] args) {
  Path out=Paths.get(System.getenv().getOrDefault("NAUKRI_AUTH_STATE_PATH","playwright/.auth/naukri.json"));
  try(Playwright pw=Playwright.create(); Browser browser=pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
   BrowserContext ctx=browser.newContext();
   Page page=ctx.newPage();
   page.navigate("https://www.naukri.com");
   System.out.println("Log in manually in the opened browser. Press Enter here after successful login.");
   try { System.in.read(); } catch(Exception ignored) {}
   Files.createDirectories(out.getParent());
   ctx.storageState(new BrowserContext.StorageStateOptions().setPath(out));
   System.out.println("Saved auth state to "+out);
  } catch(Exception e) { throw new RuntimeException(e); }
 }
}