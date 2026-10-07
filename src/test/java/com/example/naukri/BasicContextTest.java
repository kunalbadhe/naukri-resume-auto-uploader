package com.example.naukri;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties={"AUTOMATION_DRY_RUN=true"})
class BasicContextTest { @Test void contextLoads() {} }
