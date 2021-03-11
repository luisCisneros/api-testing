package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;
import org.example.ScenarioContext;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ArtistSteps {

    private static final Logger logger = LogManager.getLogger();

    private final ApiManager apiManager;
    private final ScenarioContext scenarioContext;

    public ArtistSteps(ApiManager apiManager, ScenarioContext scenarioContext) {
        this.apiManager = apiManager;
        this.scenarioContext = scenarioContext;
    }

    @Given("I want to search for {string} id {string}")
    @Given("I want to search for albums by {string} id {string}") // TODO verify if this line is really necessary
    public void iWantToSearchForArtistId(String type, String id) {
        RequestSpecification request = given().log().all().
                spec(apiManager.getAuthRequestSpecification()).
                pathParam(type + "_id", id);
        scenarioContext.setRequestSpecification(request);
    }

    @And("the following albums should be included")
    public void theFollowingAlbumsShouldBeIncluded(List<String> albums) {
        JsonPath jsonPath = scenarioContext.getResponse().then().extract().jsonPath();
        int arraySize = jsonPath.getInt("items.size()");
        List<String> itemsFromJson = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            String path = String.format("items[%d].name", i);
            itemsFromJson.add(jsonPath.getString(path));
        }
        albums.forEach(item -> assertThat(itemsFromJson, hasItem(item)));
    }
}
