import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestContainerizedApp {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Setup WebDriver (this will automatically download the correct version of ChromeDriver)
        WebDriverManager.chromedriver().setup();
        
        // Optionally, configure Chrome for headless mode (ideal for CI/CD)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Uncomment this line for headless execution
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        
        // Open the containerized web app (ensure it's accessible on the given URL)
        driver.get("http://localhost:8080");  // Change this URL if needed
    }

    @Test
    public void testLogin() {
        // Find the username input field by its name attribute and enter the username
        driver.findElement(By.name("username")).sendKeys("test_user");

        // Find the password input field and enter the password
        driver.findElement(By.name("password")).sendKeys("test_password");

        // Find and click the login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Wait for the page to load (use explicit wait for better synchronization)
        try {
            Thread.sleep(2000);  // Add appropriate waiting logic or use WebDriverWait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert that the welcome message is displayed after successful login
        String welcomeMessage = driver.findElement(By.id("welcome-message")).getText();
        assertTrue(welcomeMessage.contains("Welcome"));
    }

    @Test
    public void testInvalidLogin() {
        // Find the username input field and enter incorrect username
        driver.findElement(By.name("username")).sendKeys("invalid_user");

        // Find the password input field and enter incorrect password
        driver.findElement(By.name("password")).sendKeys("invalid_password");

        // Find and click the login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Wait for the error message (you can improve this with WebDriverWait)
        try {
            Thread.sleep(2000);  // Add appropriate waiting logic
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert that an error message is displayed for invalid login
        String errorMessage = driver.findElement(By.className("error-message")).getText();
        assertTrue(errorMessage.contains("Invalid username or password"));
    }

    @AfterEach
    public void tearDown() {
        // Close the browser after each test
        if (driver != null) {
            driver.quit();
        }
    }
}
