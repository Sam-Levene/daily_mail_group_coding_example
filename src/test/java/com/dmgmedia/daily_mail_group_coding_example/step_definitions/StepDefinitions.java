package com.dmgmedia.daily_mail_group_coding_example.step_definitions;

import com.dmgmedia.daily_mail_group_coding_example.pages.DailyMailFootballPage;
import com.dmgmedia.daily_mail_group_coding_example.pages.DailyMailGalleryView;
import com.dmgmedia.daily_mail_group_coding_example.pages.DailyMailMainPage;
import com.dmgmedia.daily_mail_group_coding_example.pages.DailyMailSportPage;
import com.dmgmedia.daily_mail_group_coding_example.reporting.ReportOverview;
import com.dmgmedia.daily_mail_group_coding_example.runner.*;
import com.dmgmedia.daily_mail_group_coding_example.utils.*;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StepDefinitions {
    private final Logger logger = LogManager.getLogger(StepDefinitions.class);
    private BrowserRunTime browserRunTime;
    private BrowserActions browserActions;
    private BrowserMeta browserMeta;
    private static SuiteMeta suiteMeta;
    private static boolean isSetup = false;
    private static boolean isRun = false;
    private static int iterator = 1;

    @Before
    public void setupEnvironment(Scenario scenario) throws IOException, AWTException {
        CustomScreenRecorder customScreenRecorder = new CustomScreenRecorder(new File(System.getProperty("user.dir") + "/report/"));
        List<Map<String, String>> propertyList = FileProperties.readPropertiesAsList(System.getProperties());

        for(Map<String, String> propertyMap : propertyList) {
            for(Map.Entry<String, String> propertyMapEntry : propertyMap.entrySet()) {
                if(propertyMapEntry.getKey().equals("cucumber.options")) {
                    for (String tagName : scenario.getSourceTagNames()) {
                        if (propertyMapEntry.getValue().contains(tagName)) {
                            isRun = true;
                            if (!isSetup) {
                                suiteMeta = new SuiteMeta.Builder().withGroups(Collections.singletonList(System.getProperties().toString())).build();
                                Runtime.getRuntime().addShutdownHook(new Thread(this::startClosure));
                                isSetup = true;

                            }

                            String featureName = "Feature-" + scenario.getUri().toString().split("features/")[1];
                            String scenarioName = "Scenario-" + iterator +"-" + scenario.getName().replace(" ", "-");
                            EnvironmentPropertyReader environmentPropertyReader = new EnvironmentPropertyReader("Daily_Mail_Online");
                            browserMeta = new BrowserMeta.Builder()
                                    .withConfiguration("Daily_Mail_Online")
                                    .withReference("Test Reference")
                                    .withDescription("Daily Mail Online Tech Task")
                                    .withUrl(environmentPropertyReader.getProperties().getProperty("desktopUrl"))
                                    .withGroups("MyPPL")
                                    .withSimulator("Not Specified")
                                    .withProperties(environmentPropertyReader.getProperties())
                                    .withInvokingClass(featureName)
                                    .withMethodName(scenarioName)
                                    .withPropertiesList(null)
                                    .build();

                            String pattern = "dd-MM-yyyy";
                            String dateInString = new SimpleDateFormat(pattern).format(new Date());
                            customScreenRecorder.startRecording(browserMeta.getClassName() + "-"
                                    + environmentPropertyReader.getProperties().getProperty("browserType") + "-"
                                    + browserMeta.getMethodName() + "-"
                                    + browserMeta.getSimulator().replace(" ", "_") + "-"
                                    + dateInString +"-Recording");
                            browserRunTime = new BrowserRunTime.Builder().withBrowserMeta(browserMeta).build();
                            browserActions = new BrowserActions();
                            iterator++;
                        }
                    }
                }
            }
        }
    }

    @Given("^I start up my browser$")
    public void iStartUpMyBrowser() {
        if (isRun) {
            logger.info("Starting Selenium Webdriver");
            browserActions.setCucumberFlag(true);
            browserActions.setDriver(BrowserRunTime.getDriver());
        }
    }

    @When("^I navigate to the Daily Mail UK main web page$")
    public void iNavigateToTheDailyMailUkMainWebPage() {
        if (isRun) {
            browserActions.changeUrl("https://www.dailymail.co.uk/home/index.html")
                    .origin();
        }
    }

    @When("I navigate to the Sport menu")
    public void iNavigateToTheSportMenu() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailMainPage.MAIN_PAGE_COOKIE_BUTTON))
                    .touch()
                    .origin();

            browserActions.focus(Locator.xpath(DailyMailMainPage.MAIN_PAGE_SPORTS_HYPERLINK))
                    .touch()
                    .origin();
        }
    }

    @When("I navigate to the Football Sub-menu")
    public void iNavigateToTheFootballSubMenu() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailSportPage.SPORT_PAGE_FOOTBALL_HYPERLINK))
                    .touch()
                    .origin();
        }
    }

    @When("I click on the first article")
    public void iClickOnTheFirstArticle() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailFootballPage.FOOTBALL_PAGE_FIRST_ARTICLE_LINK))
                    .touch()
                    .origin();
        }
    }

    // Not Working - I can't scroll to the element or click it; it is not present on the DOM!
    @When("I scroll to the premier league table")
    public void iScrollToThePremierLeagueTable() throws BrowserException {
        if (isRun) {
            browserActions.scrollToElement(Locator.xpath(DailyMailFootballPage.FOOTBALL_ARTICLE_PREM_LEAGUE_TABLE));
        }
    }

    @Then("I can see the position that the {string} team are in")
    public void iCanSeeThePositionThatTheTeamAreIn(String arg0) throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailFootballPage.PREM_LEAGUE_TABLE))
                    .collect(HtmlTags.TABLE_ROW)
                    .select(arg0);
        }
    }

    // Not Working - I can't scroll to the element or click it; it is not visible on the DOM!
    @Then("I can see the gallery")
    public void iCanSeeTheGallery() throws BrowserException {
        if (isRun) {
            browserActions.scrollToElement(Locator.xpath(DailyMailFootballPage.FOOTBALL_PAGE_GALLERY_BUTTON));
            browserActions.focus(Locator.xpath(DailyMailFootballPage.FOOTBALL_PAGE_GALLERY_BUTTON))
                    .touch()
                    .origin();
        }
    }

    // Not Working - I can't scroll to the element or click it; it is not visible on the DOM!
    @And("I can see the facebook modal")
    public void iCanSeeTheFacebookModal() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailGalleryView.GALLERY_VIEW_NEXT))
                    .touch()
                    .pause(10);
            browserActions.focus(Locator.xpath(DailyMailGalleryView.GALLERY_VIEW_PREVIOUS))
                    .touch()
                    .pause(10);
            browserActions.focus(Locator.xpath(DailyMailGalleryView.GALLERY_VIEW_CLOSE))
                    .touch();
            browserActions.focus(Locator.xpath(DailyMailFootballPage.FOOTBALL_ARTICLE_SHARE_LIST))
                    .touch()
                    .collect(Locator.xpath(DailyMailFootballPage.FOOTBALL_ARTICLE_SHARE_LIST))
                    .select(Indexes.FIRST)
                    .touch();
        }
    }

    // Not Working - No video on article!
    @Then("I can see an embedded video")
    public void iCanSeeAnEmbeddedVideo() {
        if (isRun) {
            //TODO: Do some action to verify a video; if one exists
        }
    }

    @Then("I confirm the navigation menu colour is correct")
    public void iConfirmTheNavigationMenuColourIsCorrect() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailSportPage.SPORT_PAGE_MAIN_NAVIGATION));
            String sportsPageMainNavigationColour = browserActions.getColor();
            browserActions.focus(Locator.xpath(DailyMailSportPage.SPORT_PAGE_SECONDARY_NAVIGATION));
            String sportsPageSecondaryNavigationColour = browserActions.getColor();
            assertEquals(sportsPageMainNavigationColour, sportsPageSecondaryNavigationColour);
        }
    }

    @Then("^I confirm that the time and date on the main page is correct$")
    public void iConfirmThatTheTimeAndDateOnTheMainPageIsCorrect() throws BrowserException {
        if (isRun) {
            browserActions.focus(Locator.xpath(DailyMailMainPage.MAIN_PAGE_TIME_AND_DATE));
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE, MMM dd yyyy");
            Date date = new Date(System.currentTimeMillis());
            String dateAppender = "";
            switch (date.getDate()){
                case 1:
                case 21:
                case 31:
                    dateAppender = "st";
                    break;
                case 2:
                case 22:
                    dateAppender = "nd";
                    break;
                case 3:
                    dateAppender = "rd";
                    break;
                default:
                    dateAppender = "th";
                    break;
            }
            String currentDate = formatter.format(date);
            String[] dateArray = currentDate.split(" ");
            String newDateArray = dateArray[0] + " " + dateArray[1] + " " + dateArray[2] + dateAppender + " " + dateArray[3];
            assertTrue(browserActions.getText().contains(newDateArray));
        }
    }

    private void startClosure() {
        browserRunTime.quit();
        suiteMeta.close();
        ReportOverview reportOverview = new ReportOverview(suiteMeta);
        reportOverview.run();
        if (System.getProperty("openreport").equals("true")) {
            new BrowserRunTime("file:///" + System.getProperty("user.dir") + "/report/reportOverview.html");
        }
    }
}