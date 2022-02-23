package stepdefinition;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.WeatherPage;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class stepdefs {
    WebDriver driver;
    PropertyReader reader =  PropertyReader.getInstance();

    @Given("^user navigates to \"([^\"]*)\"$")
    public void user_navigates_to(String url){
        driver = WebDriverFactory.get(reader.getProperty("browserName"));
        driver.get(url);
        WebDriverWaitFactory.waitForpageLoad(driver,120);
    }

    @Then("validate title as {string}")
    public void validateTitleOn(String value) {
        String title = driver.getTitle();
        Assert.assertEquals(title,value);
    }

    @And("validate {string} has text {string} on {string}")
    public void validateHasTextOn(String elementName, String expectedValue, String pageName) {
        String actualText = PageObjectManager.getTextWebElement(driver,pageName,elementName);
        Assert.assertTrue(actualText.equals(expectedValue),"Expected value : "+expectedValue+" but found : "+actualText);
    }

    @And("validate list of {string} has value as {string} on {string}")
    public void validateOn(String elementName,String data, String pageName) {
        List<String> actualdata = PageObjectManager.getTextListWebElement(driver,pageName,elementName);
        List<String> expectedData = Arrays.asList(data.split(","));
        Assert.assertTrue(actualdata.containsAll(expectedData),"Expected value : "+expectedData+" but found : "+actualdata);
    }

    @After
    public void after(Scenario scenario) throws InterruptedException {
        if(scenario.isFailed()) {
            TakesScreenshot screenshot = (TakesScreenshot)driver;
            byte img[]=screenshot.getScreenshotAs(OutputType.BYTES);
            scenario.attach(img,"image/jpeg","Screenshot");
        }
        if(driver!=null){
            PageObjectManager.clearObjectAfterScenario(driver);
            driver.quit();
            driver = null;
        }
        Thread.sleep(1000);
    }

    @Then("user click on {string} on {string}")
    public void userClickOnOn(String elementName, String pageName) {
        ActionClass.clickOnElement(driver,pageName,elementName);
    }

    @Then("wait for {string} on {string}")
    public void waitForOn(String elementName, String pageName) throws InterruptedException {
        if(elementName.equals("pageLoad")) {
            Thread.sleep(1000);
            WebDriverWaitFactory.waitForpageLoad(driver,20);
        } else {
            WebElement ele = PageObjectManager.getWebElement(driver, pageName, elementName);
            WebDriverWaitFactory.waitForElement(driver, ele, 10);
            if (!ele.isDisplayed())
                Assert.fail("Element not displayed on page");
        }
    }

    @Then("user selects {string} value as {string} on {string}")
    public void userSelectsValueAsOn(String elementName, String option, String pageName) {
        ActionClass.selectOption(driver,pageName,elementName,option);
    }

    @Then("scroll in to view {string} on {string}")
    public void scrollInToViewOn(String elementName, String pageName) {
        ActionClass.scrollIntoView(driver,pageName,elementName);
    }

    @Then("user enters data for {string} as {string} on {string}")
    public void userEntersDataForAsOn(String elementName, String value, String pageName) {
        ActionClass.sendKeys(driver,pageName,elementName,value);
    }

    @And("validate {string} are displayed on {string}")
    public void validateAreDisplayedOn(String elementName, String pageName) {
        List<String> data = PageObjectManager.getTextListWebElement(driver,pageName,elementName);
        if(data.isEmpty()) {
            Assert.fail("Search result are not displayed");
        }
    }

    @And("validate {string} contains product name as {string} for atleast {string} products on {string}")
    public void validateContainsProductNameAsOn(String elementName, String productName, String count,String pageName) {
        List<String> actualdata = PageObjectManager.getTextListWebElement(driver,pageName,elementName);
        ArrayList<String> found = new ArrayList<>();
        actualdata.stream().forEach(y->{
            if(y.toLowerCase().contains(productName.toLowerCase())) {
                found.add(y);
            }
        });
        if(found.isEmpty() && found.size()<Integer.parseInt(count)) {
            Assert.fail("Searched result does not contain searched product name :: "+productName + " for atleast :: "+count);
        }
    }

    @And("validate {string} {string} loaded")
    public void validateLoaded(String pageName, String pageloadedorNot) {
        boolean loaded = PageObjectManager.pageLoaded(driver,pageName,pageName);
        if(pageloadedorNot.equalsIgnoreCase("is not") && loaded) {
            Assert.fail(pageName+" should not be loaded");
        } else if(pageloadedorNot.equalsIgnoreCase("is") && !loaded) {
            Assert.fail(pageName+" should be loaded");
        }
    }

    @And("validate {string} displayed has searched char as {string} on {string}")
    public void validateSuggestion_listDisplayedHasSearchedCharAsOn(String elementName,String expectedChar, String pageName) {
        List<String> actualdata = PageObjectManager.getTextListWebElement(driver,pageName,elementName);
        ArrayList<String> incorrectfound = new ArrayList<>();
        actualdata.stream().forEach(y->{
            if(!y.toLowerCase().startsWith(expectedChar.toLowerCase())) {
                incorrectfound.add(y);
            }
        });

        if(!incorrectfound.isEmpty())
            Assert.fail("Incorrect Suggestion provided :: "+incorrectfound.toString());
    }

    @When("User gets weather response for {string} and {string}")
        public void the_user_sents_GET_request_to_get_state_code_value(String latitude, String longitude) throws Exception {
            WeatherPage.requestWeatherWithGet(latitude,longitude);
        }

        @Then("^API should return status as (\\d+)$")
        public void api_should_return_status_as(int statusCode) {
            assertThat("Verify Status code for Weather Api ", WeatherPage.getStatusCode(), equalTo(statusCode));
        }

        @Then("^Response content type should be application json$")
        public void response_content_type_should_be_json() {
            assertThat("Verify Content Type for Weather Api ", WeatherPage.getContentType(),
                    equalTo("application/json; charset=utf-8"));
        }

        @Then("^Response should have status code$")
        public void response_should_have_status_code() {
            assertThat("Verify Result for Weather Api ", WeatherPage.getStatusCode(),
                    greaterThanOrEqualTo(1));
        }

    @Given("User gets weather response for {string}")
    public void userGetsWeatherResponseFor(String city) {
        WeatherPage.requestWeatherWithGet(city);
    }

    @Given("User checks the health state for weather endpoint")
    public void userChecksTheHealthStateForWeatherEndpoint() {
        WeatherPage.checkWeatherEndpointHealth();
    }
}
