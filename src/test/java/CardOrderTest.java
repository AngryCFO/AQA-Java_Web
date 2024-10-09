import io.github.bonigarcia.wdm.WebDriverManager;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardOrderTest {

    private final String successMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

    @BeforeAll
    static void setupAll() {
      //  WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void openForm() {
        open("http://localhost:9999/");
    }

    private void fillForm(String name, String phone, boolean agree) {
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        if (agree) {
            $("[data-test-id=agreement]").click();
        }
        $("button").click();
    }

    @Test
    void shouldTestForm() {
        fillForm("Иванова Татьяна", "+79001234567", true);
        $("[data-test-id=order-success]").shouldHave(exactText(successMessage));
    }

    @Test
    void shouldTestFormDoubleName() {
        fillForm("Иванова-Петрова Татьяна", "+79001234567", true);
        $("[data-test-id=order-success]").shouldHave(exactText(successMessage));
    }

    @Test
    void shouldNotTestName() {
        fillForm("Ivanova Татьяна", "+79001234567", true);
        $("[data-test-id=name]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=name] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotTestPhone() {
        fillForm("Иванова Татьяна", "791234567", true);
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotTestAgreement() {
        fillForm("Иванова Татьяна", "+79001234567", false);
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void shouldNotTestEmptyName() {
        fillForm("", "+79001234567", true);
        $("[data-test-id=name]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotTestEmptyPhone() {
        fillForm("Иванова Татьяна", "", true);
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver(); // Закрыть драйвер после каждого теста
    }
}
