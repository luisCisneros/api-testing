package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.example.ScenarioContext;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SearchSteps {

    private static final Logger logger = LogManager.getLogger();

    private final ApiManager apiManager;
    private final ScenarioContext scenarioContext;
    private String type;

    public SearchSteps(ApiManager apiManager, ScenarioContext scenarioContext) {
        this.apiManager = apiManager;
        this.scenarioContext = scenarioContext;
    }

    @Given("I/(the user) want(s) to search for {string} by using the keyword {string}")
    public void iWantToSearchForByUsingTheKeyword(String type, String keyword) {
        RequestSpecification request = given().spec(apiManager.getAuthRequestSpecification()).
                queryParam("q", keyword).
                queryParam("type", type);
        scenarioContext.setRequestSpecification(request);
        this.type = type;
    }

    @Then("at least {int} search result(s) should be returned")
    public void atLeastSearchResultShouldBeReturned(int expectedNumberOfResults) {
        String jsonPath = String.format("%ss.total", type);
        logger.debug("JSON path: {}", jsonPath);
        scenarioContext.getResponse().
                then().log().all().
                assertThat().
                body(jsonPath, greaterThanOrEqualTo(expectedNumberOfResults));
    }
}
