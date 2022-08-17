package com.dmgmedia.daily_mail_group_coding_example;

import io.cucumber.junit.*;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features={"src/test/resources/features"},glue={"classpath:com.dmgmedia.daily_mail_group_coding_example.step_definitions"},plugin={"json:target/cuke-results.json"})
public class DailyMailGroupCodingExampleTest {
}