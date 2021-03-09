package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.example.TestContext;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SearchSteps {

    private static final Logger logger = LogManager.getLogger();

    private final RequestSpecification requestSpecification;
    private final TestContext testContext;
    private String type;

    public SearchSteps(ApiManager apiManager, TestContext testContext) {
        requestSpecification = new RequestSpecBuilder().
                setAuth(oauth2(apiManager.getAccessToken())).
                build();
        this.testContext = testContext;
    }

    @Given("I/(the user) want(s) to search for {string} by using the keyword {string}")
    public void iWantToSearchForByUsingTheKeyword(String type, String keyword) {
        RequestSpecification request = given().spec(requestSpecification).
                queryParam("q", keyword).
                queryParam("type", type);
        testContext.setRequestSpecification(request);
        this.type = type;
    }

    @Then("at least {int} search result(s) should be returned")
    public void atLeastSearchResultShouldBeReturned(int expectedNumberOfResults) {
        String jsonPath = String.format("%ss.total", type);
        logger.debug("JSON path: {}", jsonPath);
        testContext.getResponse().
                then().log().all().
                assertThat().
                body(jsonPath, greaterThanOrEqualTo(expectedNumberOfResults));
    }
}
