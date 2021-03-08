package stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ApiManager;

public class Hooks {

    private static final Logger logger = LogManager.getLogger();

    private ApiManager apiManager;

    public Hooks(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @Before
    public void setUp(Scenario scenario) {
        String titleFormat = String.format(" SCENARIO: %s ", scenario.getName());
        String title = StringUtils.center(titleFormat, 65, '=');
        logger.info(title);
        apiManager.setUp();
    }
}
