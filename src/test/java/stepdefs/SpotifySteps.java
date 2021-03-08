package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SpotifySteps {

    private static final Logger logger = LogManager.getLogger();

    private RequestSpecification request;
    private final RequestSpecification requestSpecification;
    private final ResponseSpecification responseSpecification;
    private Response response;
    private String type;

    public SpotifySteps(ApiManager apiManager) {
        requestSpecification = new RequestSpecBuilder().
                setAuth(oauth2(apiManager.getAccessToken())).
                build();
        responseSpecification = new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                expectStatusCode(200).
                build();
    }

    @Given("an user wants to search for {string} {string}")
    public void aUserSearchForArtist(String type, String query) {
        this.type = type;
        request = given().
                spec(requestSpecification).
                queryParam("q", query).
                queryParam("type", type);
    }

//    @When("the user submits a GET request to {string} endpoint")
//    public void theUserSendsTheRequestToEndpoint(String endpoint) {
//        response = request.
//                when().
//                get(endpoint);
//    }

    @Then("at least {int} search result must be returned")
    public void searchResultsMustBeReturned(int expectedNumberOfResults) {
        String jsonPath = String.format("%ss.total", type);
        logger.debug("JSON path: {}", jsonPath);
        response.
                then().log().all().
                spec(responseSpecification).
                assertThat().
                body(jsonPath, greaterThanOrEqualTo(expectedNumberOfResults));
    }
}
