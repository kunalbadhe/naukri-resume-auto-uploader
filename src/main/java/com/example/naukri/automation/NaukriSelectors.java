package com.example.naukri.automation;

public final class NaukriSelectors {

    private NaukriSelectors() {
    }

    public static final String LOGIN_INDICATOR = "body";

    public static final String PROFILE_LINK =
            "a[href*='profile'], a[href*='mnjuser/profile']";

    public static final String RESUME_INPUT =
            "input[type='file']";

    public static final String CAPTCHA_HINT =
            "text=/captcha/i";
}