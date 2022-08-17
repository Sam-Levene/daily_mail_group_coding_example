package com.dmgmedia.daily_mail_group_coding_example.runner;

import com.dmgmedia.daily_mail_group_coding_example.utils.TestState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.openqa.selenium.remote.BrowserType.SAFARI;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BrowserRunTime {
    private static final Logger logger = LogManager.getLogger(BrowserRunTime.class);
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static BrowserMeta browserMeta;
    private static final String DESKTOP_URL = "desktopUrl";
    private static final String USER_DIR = "user.dir";

    public BrowserRunTime(String fileName) {
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get(fileName);
    }

    public BrowserRunTime(Builder builder) {
        browserMeta = builder.browserMeta;
        Boolean runnable = builder.runnable;

        if (Boolean.TRUE.equals(runnable)) {
            try {
                switch(browserMeta.getProperties().getProperty("browserType")) {
                    case "Chrome":
                        chromeStuff();
                        break;
                    case "Firefox":
                        firefoxStuff();
                        break;
                    case "Safari":
                        safariStuff();
                        break;
                    case "Opera":
                        operaStuff();
                        break;
                    default:
                        throw new BrowserException("No browser specified or browser not supported. Try again.");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                assert(false);
            }
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static WebDriverWait getWait() {
        return wait;
    }

    public static BrowserMeta getBrowserMeta() {
        return browserMeta;
    }

    public static void fail() {
        browserMeta.setTestState(TestState.FAIL);
    }

    public void setIgnoreFlag() {
        browserMeta.setTestState(TestState.IGNORE);
    }

    public void quit() {
        browserMeta.setEndTime(System.currentTimeMillis());
        if (Objects.nonNull(driver)) {
            driver.quit();
        }
    }

    public static class Builder {
        private Boolean runnable = true;
        private BrowserMeta browserMeta;

        public Builder withRunnable(Boolean runnable) {
            this.runnable = runnable;
            return this;
        }

        public Builder withBrowserMeta(BrowserMeta browserMeta) {
            this.browserMeta = browserMeta;
            return this;
        }

        public BrowserRunTime build() {
            return new BrowserRunTime(this);
        }
    }

    private static void operaStuff() {
        logger.info("*** Opera Simulation(): default viewport");

        String exePath = System.getProperty(USER_DIR) + "\\src\\main\\resources\\operadriver.exe";

        System.setProperty("webdriver.opera.driver", exePath);

        driver = new OperaDriver();
        driver.get(browserMeta.getProperties().getProperty(DESKTOP_URL));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static void safariStuff() {
        if (!browserMeta.getSimulator().equals("Not Specified")) {
            logger.info("*** Safari Simulation(): " + browserMeta.getSimulator());
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setCapability(PLATFORM_NAME, "iOS");
            safariOptions.setCapability("deviceName", browserMeta.getSimulator());
            safariOptions.setCapability("udid", "");
            safariOptions.setCapability(BROWSER_NAME, SAFARI);
            safariOptions.setCapability("nativeWebTap", true);

            driver = new SafariDriver(safariOptions);
        } else {
            logger.info("*** Safari Simulation(): default viewport");
            driver = new SafariDriver();
        }
        driver.get(browserMeta.getProperties().getProperty(DESKTOP_URL));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static void firefoxStuff() {
        logger.info("*** Firefox Simulation(): default viewport");

        String exePath = System.getProperty(USER_DIR) + "\\src\\main\\resources\\geckodriver.exe";

        System.setProperty("webdriver.gecko.driver", exePath);

        driver = new FirefoxDriver();
        driver.get(browserMeta.getProperties().getProperty(DESKTOP_URL));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static void chromeStuff() {
        if (!browserMeta.getSimulator().equals("Not Specified")) {
            logger.info("*** Chrome Simulation(): " + browserMeta.getSimulator());
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", browserMeta.getSimulator());
            ChromeOptions chromeOptions = new ChromeOptions();

            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");

            String exePath = System.getProperty(USER_DIR) + "\\src\\main\\resources\\chromedriver.exe";

            System.setProperty("webdriver.chrome.driver", exePath);

            driver = new ChromeDriver(chromeOptions);
        } else {
            logger.info("*** Chrome Simulation(): default viewport");

            String exePath = System.getProperty(USER_DIR) + "\\src\\main\\resources\\chromedriver.exe";

            System.setProperty("webdriver.chrome.driver", exePath);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--start-maximized");

//            chromeOptions.addArguments("--headless");
//            chromeOptions.addArguments("--disable-gpu");
//            chromeOptions.addArguments("--window-size=1920,1080");

            driver = new ChromeDriver(chromeOptions);
        }
        driver.get(browserMeta.getProperties().getProperty(DESKTOP_URL));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
