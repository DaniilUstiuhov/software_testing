Feature: Student Registration Form Automation

  Scenario: Fill and submit the student registration form
    Given Users launch Chrome browser
    When Users open the registration form webpage
    Then Users enter name "Daniil"
    And Users enter email "daniil@example.com"
    And Users select gender "Male"
    And Users enter mobile number "1234567890"
    And Users enter date of birth "2004-09-28"
    And Users enter subject "Computer Science"
    And Users select hobby "Sports"
    And Users enter current address "123 Main Street, City"
    And Users select state "NCR"
    And Users select city "Agra"
    And Users click Submit button
