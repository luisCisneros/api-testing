@all @up
Feature: Get detailed profile information about the users

  Scenario: Get Current User's Profile
    Get detailed profile information about the current user (including the current user’s username).

    Given I have a valid access token
    When I submit a GET request to "/me" endpoint
    Then the following details must be present on the response
    | country               | MX                      |
    | display_name          | John Wick               |
    | email                 | gamene2403@itiomail.com |
    | external_urls.spotify | https://open.spotify.com/user/nrkhcqf6t68ht10nq6odalbcb |

  @wip
  Scenario: Get a User's Profile
    Get public profile information about a Spotify user.

    Given I want to search for user id "nrkhcqf6t68ht10nq6odalbcb"
    When I submit a GET request to "/users/{user_id}" endpoint
    Then the following details must be present on the response
      | display_name          | John Wick               |
      | external_urls.spotify | https://open.spotify.com/user/nrkhcqf6t68ht10nq6odalbcb |