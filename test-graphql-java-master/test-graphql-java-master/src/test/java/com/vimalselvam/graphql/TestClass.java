package com.vimalselvam.graphql;

import java.io.*;
import java.net.URL;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import okhttp3.*;

import static org.junit.Assert.fail;

/**
 * Test
 */
public class TestClass {
    private static final OkHttpClient client = new OkHttpClient();
    private final String graphqlUri = "https://www.narutoql.com/graphql";

    private Response prepareResponse(String graphqlPayload) throws IOException {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = new Request.Builder().url(graphqlUri).post(body).build();
        return client.newCall(request).execute();
    }

    @Test
    public void testGraphqlWithInputStream() throws IOException {
        // Read a graphql file as an input stream
        InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", "kakashi");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, variables);
        System.out.println(graphqlPayload);
        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();

        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        System.out.println(jsonNode.get("data").toString());
        System.out.println(jsonNode.get("data").get("characters").toString());
        System.out.println(jsonNode.get("data").get("characters").get("results"));
        System.out.println(jsonNode.get("data").get("characters").get("results").get(0).get("name").asText());

        Assert.assertEquals(jsonNode.get("data").get("characters").get("results").get(0).get("name").asText(), "Hatake Kakashi");
    }
    @Test
    public void testGraphqlWithRestAsure() throws IOException {

       /* InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon-with-no-variable.graphql");
        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, null);
        System.out.println(graphqlPayload);*/
        InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon.graphql");
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", "kakashi");
        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, variables);
    System.out.println(graphqlPayload);

           RestAssured.
            given()
            .header(new Header("Content-type", "application/json"))
            .body(graphqlPayload)
            .post(new URL(graphqlUri)).then().
           statusCode(200).log().all();
        String actual = RestAssured.
            given()
            .header(new Header("Content-type", "application/json"))
            .body(graphqlPayload)
            .post(new URL(graphqlUri))
            .jsonPath().getString("data.characters.results.name");

        System.out.println("la actual"+actual);

    }



    @Test
    public void testGraphqlWithFile() throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemon.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", "Pikachu");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }



    @Test
    public void testGraphqlWithNoVariables() throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemon-with-no-variable.graphql");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }
}
