package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.example.ScenarioContext;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.junit.Assert.assertEquals;

public class UserProfileSteps {

    private static final Logger logger = LogManager.getLogger();

    private final ApiManager apiManager;
    private final ScenarioContext scenarioContext;

    public UserProfileSteps(ApiManager apiManager, ScenarioContext scenarioContext) {
        this.apiManager = apiManager;
        this.scenarioContext = scenarioContext;
    }

    @Given("I have a valid access token")
    public void iHaveAValidAccessToken() {
        RequestSpecification request = given().spec(apiManager.getAuthRequestSpecification());
        scenarioContext.setRequestSpecification(request);
    }

    @When("I/(the user) submit(s) a {string} request to {string}( endpoint)")
    public void iSubmitAGETRequestToEndpoint(String method, String endpoint) {
        Response response = apiManager.sendHttpRequest(scenarioContext, method, endpoint);
        scenarioContext.setResponse(response);
    }

    @Then("the following details must be present on the response")
    public void theFollowingDetailsMustBePresentOnTheResponse(Map<String, String> expectedResponseDetails) {
        JsonPath jsonPath = scenarioContext.getResponse().then().extract().jsonPath();
        logger.debug("Values from response");
        for (String key : expectedResponseDetails.keySet()) {
            String actualValue = jsonPath.getString(key);
//            logger.debug("{}: [{}]", key, actualValue);
            logger.debug("{}:   expected [{}]    actual [{}]", key, expectedResponseDetails.get(key), actualValue);
            assertEquals(expectedResponseDetails.get(key), actualValue);
        }
    }

    @And("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        scenarioContext.getResponse().then().assertThat().statusCode(statusCode);
    }
}
