package stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;

public class UserProfileSteps {

    private static final Logger logger = LogManager.getLogger();

    private final RequestSpecification requestSpecification;
    private RequestSpecification request;
    private Response response;

    public UserProfileSteps(ApiManager apiManager) {
        requestSpecification = new RequestSpecBuilder().
                setAuth(oauth2(apiManager.getAccessToken())).
                build();
    }

    @Given("I have a valid access token")
    public void iHaveAValidAccessToken() {
        request = given().spec(requestSpecification);
    }

    @When("I/(the user) submit(s) a GET request to {string} endpoint")
    public void iSubmitAGETRequestToEndpoint(String endpoint) {
        response = request.
                when().get(endpoint);
    }

    @Then("the following details must be present on the response")
    public void theFollowingDetailsMustBePresentOnTheResponse(Map<String, String> expectedResponseDetails) {
        JsonPath jsonPath = response.then().extract().jsonPath();
        logger.debug("Values from response");
        for (String key : expectedResponseDetails.keySet()) {
            String actualValue = jsonPath.getString(key);
//            logger.debug("{}: [{}]", key, actualValue);
            logger.debug("{}:   expected[{}]    actual[{}]", key, expectedResponseDetails.get(key), actualValue);
            assertEquals(expectedResponseDetails.get(key), actualValue);
        }
    }

    @Given("I/(the user) want(s) to search for user id {string}")
    public void iWantToSearchForUserId(String userId) {
        request = given().log().all().spec(requestSpecification).pathParam("user_id", userId);
    }
}
