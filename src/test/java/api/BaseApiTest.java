package api;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.TimeUnit;


public class BaseApiTest {
    @AfterEach
    void tearDownAfterEachTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
    }
}
