@all @playlist
Feature: Get information and manage playlists

  Scenario: Change a playlist's details
    Change a playlist’s name and public/private state. The user must, of course, own the playlist.

    Given I want to update the playlist with id "1ZM4IjM9ZdFyZJ1JGCx1Ft"
    And by changing the "description" to "This is a new description"
    When I submit a "PUT" request to "/playlists/{playlist_id}" endpoint
    Then the response status code should be 200

  Scenario Outline: Test
    Given I want to update the playlist with id "<playlist_id>"
    And by changing the "<field>" to "<value>"
    And I submit a "PUT" request to "<endpoint>"
    And I want to search for "playlist" id "<playlist_id>"
    When I submit a "GET" request to "<endpoint>"
    Then the "<field>" should have been updated to "<value>"

    Examples:
    | playlist_id            | endpoint                 | field       | value                                   |
    | 1ZM4IjM9ZdFyZJ1JGCx1Ft | /playlists/{playlist_id} | description | This is a new description - {timestamp} |