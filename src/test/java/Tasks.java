import POJO.ToDo;
import org.testng.annotations.Test;
import POJO.Location;
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

public class Tasks {
    /**
     * Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void task1()
    {
        ToDo todo =

                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .extract().as(ToDo.class)
                ;
        System.out.println("tody = " + todo);

    }

    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void task2()
    {

                given()
                        .when()
                        .get("https://httpstat.us/203")

                        .then()
                        .log().body()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                ;


    }
    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void task3()
    {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;


    }

    /** Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */

    @Test
    public void task4()
    {
        Response body=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                //.log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                
                ;
        List<String> titles=body.path("title");
        List<Integer> idler=body.path("userId");
        ;
        Assert.assertTrue(titles.get(2).equalsIgnoreCase("fugiat veniam minus"));
        Assert.assertEquals((int) idler.get(2), 1);
    }










}
