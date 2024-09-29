import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardOrderTest {

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup(); // Автоматическая настройка ChromeDriver
    }

    private void fillForm(String name, String phone, boolean isAgreementChecked) {
        open("http://localhost:9999/");
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        if (isAgreementChecked) {
            $("[data-test-id=agreement]").click();
        }
        $("button").click();
    }

    @Test
    void shouldTestForm() {
        fillForm("Иванова Татьяна", "+79001234567", true);
        $("[data-test-id=order-success]").shouldHave(Condition.exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldTestFormDoubleName() {
        fillForm("Иванова-Петрова Татьяна", "+79001234567", true);
        $("[data-test-id=order-success]").shouldHave(Condition.exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldNotTestName() {
        fillForm("Ivanova Татьяна", "+79001234567", true);
        $("[data-test-id=name]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=name] .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotTestPhone() {
        fillForm("Иванова Татьяна", "791234567", true);
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone] .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
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
        $("[data-test-id=name] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotTestEmptyPhone() {
        fillForm("Иванова Татьяна", "", true);
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }
}
