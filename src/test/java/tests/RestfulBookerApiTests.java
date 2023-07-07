package tests;

import bodies.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;


public class RestfulBookerApiTests {

    private String token;



    @Test(priority = 1)
    public void createTokenTest(){
        CreateTokenTestBody body = new CreateTokenTestBody().builder()
                .username("admin")
                .password("password123")
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("http://restful-booker.herokuapp.com/auth");

        int statusCode = response.getStatusCode();
        response.then().statusCode(200);

        token = response.getBody().jsonPath().getString("token");
        Assert.assertNotNull(token, "Token wasn't received.");

        System.out.println("Token: " + token);
    }


    @Test(priority = 2)
    public void createBookingTest(){
        CreateBookingTestBody body = new CreateBookingTestBody().builder()
                .firstname("Yurii")
                .lastname("Bobak")
                .totalprice(200)
                .depositpaid(true)
                .bookingdates(new CreateBookingDates(LocalDate.parse("2023-06-07"), LocalDate.parse("2023-07-08")))
                .additionalneeds("Coffee")
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("https://restful-booker.herokuapp.com/booking");

        response.then().statusCode(200);

        System.out.println("The booking was created successfully:");
        response.prettyPrint();

        response.as(ResponseCreateBookingBody.class);
    }


    @Test(priority = 3)
    public void getBookingsIdsTest(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("https://restful-booker.herokuapp.com/booking");
        response.then().statusCode(200);

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        List<String> bookingIdStrings = bookingIds.stream()
                .map(Object::toString)
                .toList();

        System.out.println("Here are all booking IDs:");

        for (String bookingId : bookingIdStrings) {
            System.out.println("Booking ID: " + bookingId);
        }

    }


    @Test(priority = 4)
    public void updateBookingPriceTest(){
        Response getBookings = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        String firstBookingId = getBookings.then().extract().jsonPath().getString("bookingid[0]");

        UpdatePriceTestBody body = new UpdatePriceTestBody().builder()
                .totalprice(300)
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie("token", token)
                .body(body)
                .patch("http://restful-booker.herokuapp.com/booking/" + firstBookingId);

        response.then().statusCode(200);
        System.out.println("Please, check the updated price below.");
        response.prettyPrint();

        response.as(CreateBookingTestBody.class);
    }


    @Test(priority = 5)
    public void updateBookingNameAndNeedsTest(){
        Response getBookings = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        String firstBookingId = getBookings.then().extract().jsonPath().getString("bookingid[0]");

        Response initialBookingResponse = RestAssured.get("https://restful-booker.herokuapp.com/booking/"+ firstBookingId);
        initialBookingResponse.then().statusCode(200);

        Booking initialBooking = initialBookingResponse.as(Booking.class);
        Booking updatedBooking = Booking.builder()
                .bookingid(Integer.valueOf(firstBookingId))
                .firstname("New Name")
                .lastname(initialBooking.getLastname())
                .totalprice(initialBooking.getTotalprice())
                .depositpaid(initialBooking.getDepositpaid())
                .bookingdates(initialBooking.getBookingdates())
                .additionalneeds("New Needs")
                .build();


//        CreateBookingTestBody updateBody = CreateBookingTestBody.builder()
//                .firstname("Yurii")
////                .lastname("Bobak")
////                .totalprice(500)
////                .depositpaid(true)
////                .bookingdates(new CreateBookingDates(LocalDate.parse("2023-08-09"), LocalDate.parse("2023-08-10")))
//                .additionalneeds("Nothing")
//                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie("token", token)
                .body(updatedBooking)
                .put("http://restful-booker.herokuapp.com/booking/" + firstBookingId);

        response.then().statusCode(200);

        System.out.println("Please, check the updated booking:");
        response.prettyPrint();

        response.as(ResponseCreateBookingBody.class);
    }


    @Test(priority = 6)
    public void deleteBookingTest(){
        Response getBookings = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        String firstBookingId = getBookings.then().extract().jsonPath().getString("bookingid[0]");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie("token", token)
                .delete("https://restful-booker.herokuapp.com/booking/" + firstBookingId);

        response.then().statusCode(201);

        System.out.println("Booking with ID: " + firstBookingId + " was successfully deleted.");
    }




}
