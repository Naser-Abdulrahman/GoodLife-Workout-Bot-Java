import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.support.ui.*;




public class script {
    public static String url = "https://www.goodlifefitness.com/book-workout.html";
    public static ChromeDriver gl = new ChromeDriver();;
    public static WebDriverWait waiting = new WebDriverWait(gl, 10);
    public static String yourEmail = "Change me into your email";
    public static String yourPass = "Change me into your password";


    /*
    This function is responsible for booking the desired workout, I've been told that the previous booking schedule allowed you to have up to 3 workout slots
    this recursive function will find the first available workout that fits the user's criteria (default is any workout from 2pm - closing,
    After find a desired workout, it will call itself again to find the next available slot
     */
    public static void booker() throws InterruptedException {
        waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-class-schedule-weekdays-container\"]/li[4]")));
        boolean blocked = true;
        while (blocked) {
            try {
                WebElement date = gl.findElement(By.xpath("//*[@id=\"js-class-schedule-weekdays-container\"]/li[4]"));
                date.click();
                blocked = false;
            } catch (Exception e) {
                blocked = true;
            }
        }
        List<WebElement> section = gl.findElements(By.xpath(".//*[@id='day-number-4']/li"));
        for (WebElement t : section) {
            if (t.findElement(By.xpath(".//div[2]/div/div[1]/div/div[1]/div[2]/span\n")).getText().equals("Co-ed")) {
                char[] timeSlots = t.findElement(By.xpath(".//div[1]/span[1]")).getText().toCharArray();
                String timeszone = Character.toString(timeSlots[timeSlots.length - 2]);
                if (timeSlots.length == 7) {
                    if (!timeszone.equals("a")) {   //Comment me out if you don't mind working out in the morning
                        if (Character.getNumericValue(timeSlots[0]) >= 2) {      //This lines means it will look for a workout if its after 2pm and before 10pm. Comment the line above if you want to book a workout from opening - 10am
                            waiting.until(ExpectedConditions.elementToBeClickable(t.findElement(By.cssSelector("button[data-class-action-step=\"class-action-confirmation\"]"))));
                            WebElement status = t.findElement(By.cssSelector("button[data-class-action-step=\"class-action-confirmation\"]"));
                            if (status.getText().equals("BOOK")) {
                                boolean wasted = true;
                                while (wasted) {
                                    try {
                                        status.click();
                                        wasted = false;
                                    } catch (ElementClickInterceptedException e) {
                                        System.out.println("Baka");
                                    }
                                }
                                gl.switchTo().activeElement();
                                waiting.until(ExpectedConditions.elementToBeClickable(By.id("js-workout-booking-agreement-label")));
                                gl.findElement(By.id("js-workout-booking-agreement-input")).click();
                                waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"class-modal-container\"]/div[4]/div/button[1]")));
                                gl.findElement(By.xpath("//*[@id=\"class-modal-container\"]/div[4]/div/button[1]")).click();
                                gl.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                                Thread.sleep(200);      //Change the sleep time if workouts are full before the bot can book
                                break;
                            }
                        }
                    }                   //Comment me out if you don't mind working out in the morning
                }
                if (timeSlots.length == 8) {
                    if (!timeszone.equals("a")) {   //Comment me out if you don't mind working out in the morning
                        if (Character.getNumericValue(timeSlots[0] + timeSlots[1]) != 12) {
                            waiting.until(ExpectedConditions.elementToBeClickable(t.findElement(By.cssSelector("button[class='c-btn-outlined class-action']"))));
                            WebElement status = t.findElement(By.cssSelector("button[class='c-btn-outlined class-action']"));
                            if (status.getText().equals("BOOK")) {
                                boolean wasted = true;
                                while (wasted) {
                                    try {
                                        status.click();
                                        wasted = false;
                                    } catch (ElementClickInterceptedException e) {
                                        System.out.println("Baka");
                                    }
                                }
                                gl.switchTo().activeElement();
                                waiting.until(ExpectedConditions.elementToBeClickable(By.id("js-workout-booking-agreement-label")));
                                gl.findElement(By.id("js-workout-booking-agreement-input")).click();
                                waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"class-modal-container\"]/div[4]/div/button[1]")));
                                gl.findElement(By.xpath("//*[@id=\"class-modal-container\"]/div[4]/div/button[1]")).click();
                                gl.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                                Thread.sleep(2000);
                                break;
                            }
                        }
                    }                   //Comment me out if you don't mind working out in the morning
                }
            }
        }
        gl.navigate().refresh();
        booker();
    }

    /*
    This function is responsible for going to the GoodLife url and logging in,
    make sure to change your email and password with the variables above
    after logging, GoodLife has each workout day as a table with each session as an appended child
    the bot goes to the 4th table (GoodLife lets you book a workout 72 hours in advance, if they change the date I will update accordingly
    after it finishes loading then calls the recursive method booker()
     */
    public static void scheduler() throws InterruptedException {
        ((LocationContext)gl).setLocation(new Location(43.198597,-79.842295,1));  //Change this line to find the desired gym
        gl.get(url);
        gl.manage().window().maximize();
        waiting.until(ExpectedConditions.elementToBeClickable(By.id("js-search-location-default-club")));
        WebElement location = gl.findElement(By.id("js-search-location-default-club"));
        System.out.println(location.getText());
        waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"header-path\"]/div[2]/span/span/a[1]")));
        gl.findElement(By.xpath("//*[@id=\"header-path\"]/div[2]/span/span/a[1]")).click();
        waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"page-cbd68da9f6\"]/div[1]/div/div[1]/div/div/div/div/div[1]/div/div[2]/div/div[2]/form/div[1]/label/input")));
        WebElement email = gl.findElement(By.xpath("//*[@id=\"page-cbd68da9f6\"]/div[1]/div/div[1]/div/div/div/div/div[1]/div/div[2]/div/div[2]/form/div[1]/label/input"));
        email.sendKeys(yourEmail);
        WebElement password = gl.findElement(By.xpath("//*[@id=\"page-cbd68da9f6\"]/div[1]/div/div[1]/div/div/div/div/div[1]/div/div[2]/div/div[2]/form/div[2]/label/input"));
        password.sendKeys(yourPass + Keys.RETURN);
        gl.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        waiting.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-class-schedule-weekdays-container\"]/li[4]")));
        booker();
    }

    public static void main (String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        scheduler();
    }
}