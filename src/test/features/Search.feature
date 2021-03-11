@all @search
Feature: Search for an item

  Scenario Outline: Search for different items
    Get Spotify Catalog information about albums, artists, playlists, tracks, shows or episodes that match a keyword string.

    Given I want to search for "<type>" by using the keyword "<keyword>"
    When I submit a "GET" request to "/search" endpoint
    Then the response status code should be 200
    And at least 1 search result should be returned

    Examples:
    | keyword           | type      |
    | Una mattina       | album     |
    | Ludovico Einaudi  | artist    |
    | Instrumental      | playlist  |
    | Nuvole Bianche    | track     |