@all
Feature: Spotify search
  Users should be able to submit GET requests in order to retrieve search results

  Scenario: An user searches for an artist
    Given an user wants to search for "artist" "Nobuo Uematsu"
    When the user submits a GET request to "/search" endpoint
    Then at least 1 search result must be returned