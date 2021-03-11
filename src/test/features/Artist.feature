@all @artist
Feature: Get Spotify catalog information for artists

  Scenario: Get an artist
    Get Spotify catalog information for a single artist identified by their unique Spotify ID.

    Given I want to search for "artist" id "2uFUBdaVGtyMqckSeCl0Qj"
    When I submit a "GET" request to "/artists/{artist_id}" endpoint
    Then the response status code should be 200
    And the following details must be present on the response
    | name | Ludovico Einaudi |
    | type | artist           |

  Scenario: Get an Artist's Albums
  Get Spotify catalog information about an artist’s albums.

    Given I want to search for albums by "artist" id "2uFUBdaVGtyMqckSeCl0Qj"
    When I submit a "GET" request to "/artists/{artist_id}/albums" endpoint
    Then the response status code should be 200
    And the following albums should be included
    | Undiscovered       |
    | 12 Songs From Home |
    | Seven Days Walking |
