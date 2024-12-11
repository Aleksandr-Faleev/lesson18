package test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.PageObject;

import java.time.Duration;

@Epic("Тестирование платежной формы")
@Feature("Проверка корректности работы платежной формы")
public class PaymentFormTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private PageObject pageObject;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.mts.by/");
        pageObject = new PageObject(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {

        }
    }

    @Test
    @Description("Проверка правильности меток в платежной форме")
    @Story("Проверка корректности отображения меток")
    void testPaymentFormLabels() {
        selectPayOption("Услуги связи");
        fillPaymentForm("297777777", "500,23", "laik@laik.com");

        String[] paymentOptions = {"Домашний интернет", "Рассрочка", "Задолженность"};
        for (String option : paymentOptions) {
            long startTime = System.currentTimeMillis();
            selectPayOption(option);
            long elapsedTime = System.currentTimeMillis() - startTime;

            System.out.println("Время переключения на " + option + ": " + elapsedTime + " мс");
            fillPaymentForm("297777777", "500,23", "laik@laik.com");
            try {
                submitForm();
            } catch (Exception e) {
                System.err.println("Не удалось нажать на кнопку отправки формы: " + e.getMessage());
            }
        }
    }

    @Test
    @Description("Проверка валидации ввода номера банковской карты")
    @Story("Валидация вводимых данных")
    void testBankCardInputValidation() {
        selectPayOption("Услуги связи");
        fillPaymentForm("297777777", "500,23", "laik@laik.com");
    }

    @Step("Выбор способа платежа: {option}")
    private void selectPayOption(String option) {
        pageObject.selectPayOption(option);
    }

    @Step("Заполнение платежной формы с номером: {cardNumber}")
    private void fillPaymentForm(String cardNumber, String amount, String email) {
        pageObject.fillPaymentForm(cardNumber, amount, email);
    }

    @Step("Отправка формы")
    private void submitForm() {
        pageObject.submitForm();
    }
}