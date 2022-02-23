package pages;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class WeatherPage {
    static public Response response;

    public static void requestWeatherWithGet(String latitude, String longitude) {
        response = SerenityRest.get("https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=");
        System.out.println("Response ----> " + response);
        //List<Map<String, String>> responseDataList = response.jsonPath().getList("data");
        //System.out.println(" Response Data list :: " + responseDataList);
        //String state_code = responseDataList.get(0).get("state_code");
        //System.out.println(" state_code :: state_code ---> " + state_code);
    }

    public static int getStatusCode() {
        return response.then().extract().statusCode();
    }

    public static String getContentType() {
        return response.then().extract().contentType();
    }

    public static void requestWeatherWithGet(String city) {
        response = SerenityRest.get("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=");
        System.out.println("Response ----> " + response);
    }

    public static void checkWeatherEndpointHealth() {
        response = SerenityRest.get("https://api.openweathermap.org/data/2.5/weather?q&appid=");
        System.out.println("Response ----> " + response);
    }
}
