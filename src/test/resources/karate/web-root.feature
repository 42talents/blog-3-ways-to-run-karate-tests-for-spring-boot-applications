Feature: Example Application

  Scenario: redirect to github
    * configure followRedirects = false
    Given url baseUrl
    When method get
    Then status 302
    And match header location == 'https://github.com/42talents/blog-3-ways-to-run-karate-tests-for-spring-boot-applications'