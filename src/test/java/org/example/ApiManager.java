package org.example;

import io.restassured.RestAssured;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class ApiManager {

    private static final Logger logger = LogManager.getLogger();

    public static final String PROPERTIES_PATH = "src/test/resources/config.properties";
    private String accessToken;
    private Properties properties;

    public String getAccessToken() {
        return accessToken;
    }

    public static Properties getProperties(String path) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return properties;
    }

    protected void requestAccessToken() {
        properties = getProperties(PROPERTIES_PATH);
        Properties credentials = getProperties(properties.getProperty("client.credentials.path"));
        String clientId = credentials.getProperty("client.id");
        String clientSecret = credentials.getProperty("client.secret");
        String refreshToken = credentials.getProperty("refresh.token");
        String token = encodeToken(clientId, clientSecret);
        accessToken =
                given().//log().all().
                        header("Authorization", "Basic " + token).
                        contentType("application/x-www-form-urlencoded").
                        formParam("grant_type", "refresh_token").
                        formParam("refresh_token", refreshToken).
                        when().
                        post("https://accounts.spotify.com/api/token").
                        then().//log().all().
                        extract().
                        path("access_token");
        logger.debug("Access token: {}", accessToken);
    }

    public static String encodeToken(String clientId, String clientSecret) {
        String encodedToken;
        String idSecret = String.format("%s:%s", clientId, clientSecret);
        byte[] bytesEncoded = Base64.encodeBase64(idSecret.getBytes());
        encodedToken = new String(bytesEncoded);
        logger.debug("Encoded token: {}", encodedToken);
        return encodedToken;
    }

    public void setUp() {
        requestAccessToken();
        setUpRestAssured(properties.getProperty("base.uri"), properties.getProperty("base.path"));
    }

//    public void setUpRestAssured() {
//        Properties properties = getProperties(PROPERTIES_PATH);
//        RestAssured.baseURI = properties.getProperty("base.uri");
//        RestAssured.basePath = properties.getProperty("base.path");
//    }

    public void setUpRestAssured(String baseURI, String basePath) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;
    }
}
