Feature: Actuator api

  Scenario: actuator resource is available

    Given url baseUrl + '/actuator/'
    When method get
    Then status 200
