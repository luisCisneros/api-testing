package stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.example.ScenarioContext;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class PlaylistSteps {

    private static final Logger logger = LogManager.getLogger();

    private final ApiManager apiManager;
    private final ScenarioContext scenarioContext;

    public PlaylistSteps(ApiManager apiManager, ScenarioContext scenarioContext) {
        this.apiManager = apiManager;
        this.scenarioContext = scenarioContext;
    }

    @Given("I want to update the playlist with id {string}")
    public void iWantToUpdateTheWithId(String id) {
        RequestSpecification request = given().log().all().
                spec(apiManager.getAuthRequestSpecification()).
                pathParam("playlist_id", id).
                contentType(ContentType.JSON);
        scenarioContext.setRequestSpecification(request);
    }

    @And("by changing the {string} to {string}")
    public void byChangingTheTo(String field, String value) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        scenarioContext.setTimeStamp(timeStamp);
        String formattedValue = value.replace("{timestamp}", timeStamp); // String.format("%s : %s", value, timeStamp);
        logger.debug("{}: {}", field, formattedValue);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode playlist = mapper.createObjectNode();
        playlist.put(field, formattedValue);
        scenarioContext.setRequestSpecification(
                scenarioContext.getRequestSpecification().body(playlist)
        );
    }

    @Then("the {string} should have been updated to {string}")
    public void theShouldHaveBeenUpdatedTo(String field, String expectedValue) {
        JsonPath jsonPath = scenarioContext.getResponse().then().log().all().extract().jsonPath();
        String actualValue = jsonPath.getString(field);
        logger.debug("From response - {}: {}", field, actualValue);
        assertThat(actualValue, equalTo(expectedValue.replace("{timestamp}", scenarioContext.getTimeStamp())));
    }
}
