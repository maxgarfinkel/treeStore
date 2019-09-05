package com.maxgarfinkel.treeStore.controller;

import com.maxgarfinkel.treeStore.model.Tree;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TreeControllerIntegrationTest {

    @LocalServerPort
    private int portNumber;

    private String latinName = "Acer";
    private String commonName = "Maple";
    private double[] location = new double[]{1.1,1.1};
    private RestAssuredConfig config = RestAssured.config()
            .jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE));

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost/realTree/V1/";
        RestAssured.port = portNumber;
    }

    @Test
    public void getNonExistentTreeReturns404() throws Exception {
        given()
        .when()
                .get("tree/ff1a0637-01b5-4712-b30c-18b9c2ae04d0")
        .then()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void creatValidTreeAndGetTreeByIdSucceeds() throws Exception {
        String uuidAsString = UUID.randomUUID().toString();

        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getDefaultValidTreeJsonWithUuid(uuidAsString))
        .when()
                .post("tree")
        .then()
                .assertThat().statusCode(HttpStatus.OK.value());

        given()
                .config(config)
        .when()
                .get("tree/" + uuidAsString +"/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("uuid", equalTo(uuidAsString))
                .body("latinName", is(latinName))
                .body("commonName", is(commonName))
                .body("location.size()", is(2))
                .body("location[0]", is(location[0]))
                .body("location[1]", is(location[1]));
    }

    @Test
    public void createTreeWithoutIdReturnsTreeWithId() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getDefaultValidTreeJsonWithUuid(""))
        .when()
                .post("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("uuid.size()", is(36));
    }

    @Test
    public void reCreatingExistingTreeCauses409ConflictError() throws Exception {
        String uuidAsString = UUID.randomUUID().toString();

        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getDefaultValidTreeJsonWithUuid(uuidAsString))
        .when()
                .post("tree")
        .then()
                .assertThat().statusCode(HttpStatus.OK.value());

        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getDefaultValidTreeJsonWithUuid(uuidAsString))
        .when()
                .post("tree")
        .then()
                .assertThat().statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updatingExistingTreePersistsChanges() throws Exception {
        String uuidAsString = UUID.randomUUID().toString();

        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getDefaultValidTreeJsonWithUuid(uuidAsString))
        .when()
                .post("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        String newLatinName = "Platanus x hispanica";
        String newCommonName = "plane";
        double[] newLocation = new double[]{51.54394545324343, 0.14510606804638523};
        String updatedTreeJson = createCustomTree(uuidAsString,newLatinName,newCommonName,newLocation);

        given()
                .config(config)
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(updatedTreeJson)
        .when()
                .put("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("latinName", is(newLatinName))
                .body("commonName", is(newCommonName))
                .body("location.size()", is(2))
                .body("location", hasItems(newLocation[0], newLocation[1]));
    }

    @Test
    public void updatingNonExistentTreeCauses404Error() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getRandomValidTree())
        .when()
                .put("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteExistingTreeSucceedsAndSubsequentGetReturn404() throws Exception {
        String uuidAsString = UUID.randomUUID().toString();
        String treeAsJson = getDefaultValidTreeJsonWithUuid(uuidAsString);

        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(treeAsJson)
        .when()
                .post("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(treeAsJson)
        .when()
                .delete("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
        .when()
                .get("tree/"+uuidAsString+"/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void deleteNonExistentTreeCauses404Response() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON.toString())
                .body(getRandomValidTree())
        .when()
                .delete("tree")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private String createCustomTree(String uuid, String latinName, String commonName, double[] location){
        return "{\"uuid\":\""+uuid+"\"," +
                " \"latinName\":\""+latinName+"\"," +
                " \"commonName\":\""+commonName+"\"," +
                " \"location\":["+Double.toString(location[0])+","+Double.toString(location[1])+"]}";
    }

    private Tree getDefaultTree(String uuidAsString){
        UUID uuid = UUID.fromString(uuidAsString);
        return new Tree(uuid, latinName, commonName, location);
    }

    private String getDefaultValidTreeJsonWithUuid(String uuid){
        return createCustomTree(uuid,latinName,commonName,location);
    }

    private String getRandomValidTree(){
        return getDefaultValidTreeJsonWithUuid(UUID.randomUUID().toString());
    }

}
