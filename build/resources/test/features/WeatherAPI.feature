@WeatherAPIValidation
Feature: Test Scenarios for weather API Validation

  Scenario : Verify weather endpoint is healthy

    Given User checks the health state for weather endpoint
    Then API should return status as 200

  Scenario Outline: Verify Current Weather API for "<latitude>" and "<longitude>"

    Given User gets weather response for "<latitude>" and "<longitude>"
    Then API should return status as 200
    And Response content type should be application json
    And Response should have status code
    Examples:
      | latitude | longitude |
      | 50       |    60     |
      | 51.5085  |   -0.1257 |
      | 35       |    139    |
      | 36.48336 | 38.889809 |
      | 44.98333 | 38.450001 |
      | 8.44972  | 49.96278  |
      | 9.67964  | 53.22628  |
      | 0.2      | 51.900002 |
      | -2.37548 | 50.958069 |
      | 100      |    150    |


  Scenario Outline: Verify Current Weather detail for "<city>"

    Given User gets weather response for "<city>"
    Then API should return status as 200
    And Response content type should be application json
    And Response should have status code
    Examples:
      | city |
      |  London    |
      |  New york  |
      | Alpharetta |
      | Mumbai     |
      | Bengaluru  |
      |  Denver    |
      | California |
      |  Kolkata   |
      |  New Delhi |
      | Invalid city |
      | New Jersy  |


