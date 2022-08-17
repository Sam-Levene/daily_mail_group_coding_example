@DailyMailAllScenarios
Feature: Daily Mail Coding Example

  Background:
    Given I start up my browser
    When I navigate to the Daily Mail UK main web page

  @Scenario1
  Scenario: Verify that the Time and Date is correct
    Then I confirm that the time and date on the main page is correct

  @Scenario2
  Scenario: Navigate to the Sport menu and confirm colour is correct for primary and secondary navigation
    And I navigate to the Sport menu
    Then I confirm the navigation menu colour is correct

  @Scenario3 @NotWorking
  Scenario: Navigate to the Football Sub-menu, click on the first article and navigate the gallery images
    And I navigate to the Sport menu
    And I navigate to the Football Sub-menu
    And I click on the first article
    Then I can see the gallery

  @Scenario4 @NotWorking
  Scenario: Navigate through gallery and make sure the Facebook modal opens
    And I navigate to the Sport menu
    And I navigate to the Football Sub-menu
    And I click on the first article
    Then I can see the gallery
    And I can see the facebook modal

  @Scenario5 @NotWorking
    Scenario: Navigate to the first article and click on an embedded video
    And I navigate to the Sport menu
    And I navigate to the Football Sub-menu
    And I click on the first article
    Then I can see an embedded video

  @Scenario6 @NotWorking
  Scenario: While within the first article; scroll to the premier league table and display a team's standings
    And I navigate to the Sport menu
    And I navigate to the Football Sub-menu
    And I click on the first article
    When I scroll to the premier league table
    Then I can see the position that the "Liverpool" team are in