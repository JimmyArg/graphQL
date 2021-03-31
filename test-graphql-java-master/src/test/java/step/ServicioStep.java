package step;

import com.vimalselvam.graphql.TestClass;
import com.vimalselvam.graphql.GraphqlTemplate;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.trustStore;

public class ServicioStep {
    private final String graphqlUri = "http://sesamo.dtvdev.net:8080/graphql";
    private Response response;
    private Response response2;



    @Step
    public void conexionDelServico() throws IOException {
        InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon.graphql");

        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, null);

        response= SerenityRest.given()
            .header(new Header("Content-type", "application/json"))
            .body(graphqlPayload)
            .when()
            .post(new URL(graphqlUri));
        response.then().statusCode(HttpStatus.SC_OK);;

    }

    @Step
    public String conexionDelServico2(String strDatoAExtraer ) {


        String dato =response.jsonPath().getString("data.customers.ibsCustomer."+strDatoAExtraer+".");
        System.out.println(dato);
        return dato;
    }

    @Step
    public void conexionDelServicosinevidenci() throws IOException {
        InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon.graphql");

        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, null);

        response2 = RestAssured.given()
            .header(new Header("Content-type", "application/json"))
            .body(graphqlPayload)
            .when()
            .post(new URL(graphqlUri));
        response2.then().statusCode(HttpStatus.SC_OK);

    }

    @Step
    public void conexionDelServico2Sinevidencia() {

        String dato =response.jsonPath().getString("data.customers.ibsCustomer.id");
        System.out.println(dato);
        String dato1 =response.jsonPath().getString("data.customers.ibsCustomer.agreementDetails[0].financialAccount.accstat.description");
        System.out.println(dato1);

    }
}
