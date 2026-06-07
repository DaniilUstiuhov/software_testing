package steps;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class RegistrationSteps {

    private WebDriver driver;

    @Given("Users launch Chrome browser")
    public void launchBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @When("Users open the registration form webpage")
    public void openWebpage() {
        driver.get("https://www.tutorialspoint.com/selenium/practice/selenium_automation_practice.php");
    }

    @Then("Users enter name {string}")
    public void enterName(String name) {
        driver.findElement(By.cssSelector("input[placeholder='First Name']")).sendKeys(name);
    }

    @And("Users enter email {string}")
    public void enterEmail(String email) {
        driver.findElement(By.cssSelector("input[placeholder='name@example.com']")).sendKeys(email);
    }

    @And("Users select gender {string}")
    public void selectGender(String gender) {
        driver.findElement(By.xpath("//label[normalize-space()='" + gender + "']/preceding-sibling::input[@type='radio']")).click();
    }

    @And("Users enter mobile number {string}")
    public void enterMobile(String mobile) {
        driver.findElement(By.cssSelector("input[placeholder='Enter Mobile Number']")).sendKeys(mobile);
    }

    @And("Users enter date of birth {string}")
    public void enterDob(String dob) {
        // using JS to set date value reliably (format: yyyy-MM-dd)
        WebElement dateInput = driver.findElement(By.cssSelector("input[type='date']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + dob + "'", dateInput);
    }

    @And("Users enter subject {string}")
    public void enterSubject(String subject) {
        driver.findElement(By.cssSelector("input[placeholder='Enter Subject']")).sendKeys(subject);
    }

    @And("Users select hobby {string}")
    public void selectHobby(String hobby) {
        driver.findElement(By.xpath("//label[normalize-space()='" + hobby + "']/preceding-sibling::input[@type='checkbox']")).click();
    }

    @And("Users enter current address {string}")
    public void enterAddress(String address) {
        driver.findElement(By.cssSelector("textarea")).sendKeys(address);
    }

    @And("Users select state {string}")
    public void selectState(String state) {
        new Select(driver.findElement(By.id("state"))).selectByVisibleText(state);
        // wait for city dropdown to load
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @And("Users select city {string}")
    public void selectCity(String city) {
        new Select(driver.findElement(By.id("city"))).selectByVisibleText(city);
    }

    @And("Users click Submit button")
    public void clickSubmit() {
        driver.findElement(By.cssSelector("button.btn-primary")).click();
    }

    @After
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
