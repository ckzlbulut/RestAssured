import POJO.ToDo;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                .when()
                // link i ve metodu veriyoruz

                .then()
                //  assertion ve verileri ele alma extract
        ;

    }


    @Test
    public void statusCodeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()       // log.all() bütün respons'u gösterir
                .statusCode(200) // status kontrolü
        ;

    }


    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() bütün respons u gösterir
                .body("country", equalTo("United States"))

        ;

    }

    @Test
    public void bodyJsonPathTest2() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() bütün respons u gösterir
                .body("places[0].state", equalTo("California"))
                .statusCode(200)

        ;

    }


    @Test
    public void turkeyCity() {
        given()
                .when()
                .get("https://api.zippopotam.us/TR/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Dervişler Köyü"))
                .statusCode(200)


        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() bütün respons u gösterir
                .body("places", hasSize(1))
                .statusCode(200)

        ;
    }


    @Test
    public void comibingTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() bütün respons u gösterir
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)

        ;
    }


    @Test
    public void pathParamTest() {
        given()

                .pathParam("Country", "us")
                .pathParam("ZipKod", 90210)
                .log().uri()

                .when()
                .get("https://api.zippopotam.us/{Country}/{ZipKod}")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @Test
    public void pathParamTest2() {
        // 90210 dan 90250 ye kadar test sonuçlarında places size nın hepsinde 1 geldiğini test ediniz.
        for (int i = 90210; i <= 90213; i++) {
            given()

                    .pathParam("Country", "us")
                    .pathParam("ZipKod", i)
                    .log().uri()

                    .when()
                    .get("https://api.zippopotam.us/{Country}/{ZipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200)

            ;
        }
    }


    @Test
    public void queryParamTest() {
        // https://gorest.co.in/public/v1/users?page=1
        for (int pageNo = 1; pageNo <= 10; pageNo++) {
            given()
                    .param("page", pageNo)
                    .log().uri()//request linki

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(pageNo))
                    .statusCode(200)

            ;
        }
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeTest
    void Setup() {
        baseURI = "https://gorest.co.in/public/v1";
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void requestResponceSpecification() {

        given()
                .param("page", 1)
                .spec(requestSpecification)

                .when()
                .get("/users") // url nin başında http yoksa baseUri deki değer oromatik geliyor.

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpecification)

        ;
    }


    @Test
    public void extractingJsonPath() {

        String placeName =
                given()
                        .when()
                        .get("https://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("places[0].'place name'")
                // extract metodu ile given ile başlayan satır,bir değer döndrür hale geldi , en sonda extract olmalı
                ;
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void exstractingJsonPathStringList(){

        List<String> isimler=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name") // datadaki bütün idleri bir list şeklinde verir
        ;
        System.out.println("isinler = "+isimler);
        Assert.assertTrue(isimler.contains("Datta Achari"));

    }

    @Test
    public void exstractingJsonPathStringList2(){

        Response body=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response()
                ;
        List<Integer> idler=body.path("data.id");
        List<Integer> isimler=body.path("data.name");
        int limit=body.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("isimeler = " + isimler);
        System.out.println("idler = " + idler);
                
    }
    

    

}
