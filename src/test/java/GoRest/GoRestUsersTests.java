package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTests {

    int usersID;

    @Test
    public void createUser() {
        usersID =
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri

                        .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"" + getRandomName() + "\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")
                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");


        ;
        System.out.println("usersID = " + usersID);

    }

    public String getRandomEmail() {
        return RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    @Test
    public void createUserMap() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", getRandomName());
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");

        usersID =
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
        ;
        System.out.println("usersID = " + usersID);
    }

    @Test
    public void createUserObject() {
        User newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        usersID =
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id");
                        .extract().jsonPath().getInt("id")
        /**        ^^^^^^^^^^
         *  path: class veya tip dönüşümüne imkan vermeyen direk veriyi verir , List<String> gib
         *  jsonpath : class dönüşümüne ce tip dönüşümüne izin verecek , veriyi istediğimiz formatta verir
         * */
        ;
        System.out.println("usersID = " + usersID);
    }

    @Test(dependsOnMethods = "createUserObject", priority = 1)
    public void updateUserObject() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "bltcglr");


        given()
                // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().body()
                .pathParam("userID", usersID)

                .when()
                .put("https://gorest.co.in/public/v2/users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("bltcglr"))

        ;
    }

    @Test(dependsOnMethods = "createUserObject", priority = 2)
    public void getUserByID() {

        given()
                // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", usersID)

                .when()
                .put("https://gorest.co.in/public/v2/users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(usersID))

        ;
    }

    @Test(dependsOnMethods = "createUserObject", priority = 3)
    public void deleteUserByID() {

        given()
                // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", usersID)

                .when()
                .delete("https://gorest.co.in/public/v2/users/{userID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteUserByID")
    public void deleteUserByIDNegative() {

        given()
                // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", usersID)

                .when()
                .delete("https://gorest.co.in/public/v2/users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;
    }


    @Test()
    public void getUsers() {
        Response response =
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")

                        .when()
                        .get("https://gorest.co.in/public/v1/users/")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        /** : 3. usersın idsini alınız (path ve jsonPath ile ayrı ayrı yapınız) */
        List<Integer> idler = response.path("id");
        List<Integer> idler2 = response.jsonPath().getList("id");
        System.out.println("idler.get(2) = " + idler.get(2));
        System.out.println("idler2.get(2) = " + idler2.get(2));
    }



    // TODO : GetUserByID testinde dönen useri bir neseneye atınız
    @Test()
    public void getUsersV1() {
        Response response =
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization", "Bearer 6c8b11c11b7f4d6677867e0da7e36aac04e4a06d223910c432c37f8c41826c1b")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();

        List<User> dataUser=response.jsonPath().getList("data", User.class);
        System.out.println("dataUser = " + dataUser);
        
    }
    // TODO : Tüm gelen veriyi bir nesneye atınız (google araştırması)
}


class User {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String gender;
    private String email;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}