package ru.netology.test;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;

import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.page.DashboardPage.CardType.FIRST;
import static ru.netology.page.DashboardPage.CardType.SECOND;

public class ServiceTests {


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Успешное пополнение первой карты с баланса второй карты")
    void shouldDepositFirstCardFromSecondCard() {

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var depositPage = dashboardPage.cardDeposit(FIRST); //выбор карты FIRST, SECOND
        int transferAmount = 1000;

        depositPage.validDeposit(String.valueOf(transferAmount),
                DataHelper.getCardsInfo().getSecondCardNum()); // Нужно указать с какой карты переводить

        int firstCardCurrentBalance = dashboardPage.getCardBalance(DataHelper
                .getCardsInfo().getFirstCardId());
        int secondCardCurrentBalance = dashboardPage.getCardBalance(DataHelper
                .getCardsInfo().getSecondCardId());

        assertEquals(dashboardPage.GetFirstCardStartBalance() + transferAmount, firstCardCurrentBalance);
        assertEquals(dashboardPage.GetSecondCardStartBalance() - transferAmount, secondCardCurrentBalance);

        dashboardPage.restoreInitialBalances();
    }

    @Test
    @DisplayName("Успешное пополнение второй карты с баланса первой карты")
    void shouldDepositSecondCardFromFirstCard() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var depositPage = dashboardPage.cardDeposit(SECOND); //выбор карты для пополнения FIRST, SECOND
        int transferAmount = 3000;

        depositPage.validDeposit(String.valueOf(transferAmount),
                DataHelper.getCardsInfo()
                        .getFirstCardNum());  // Нужно указать с какой карты переводить

        int firstCardCurrentBalance = dashboardPage.getCardBalance(DataHelper
                .getCardsInfo().getFirstCardId());
        int secondCardCurrentBalance = dashboardPage.getCardBalance(DataHelper
                .getCardsInfo().getSecondCardId());

        assertEquals(dashboardPage.GetFirstCardStartBalance() - transferAmount, firstCardCurrentBalance);
        assertEquals(dashboardPage.GetSecondCardStartBalance() + transferAmount, secondCardCurrentBalance);

        dashboardPage.restoreInitialBalances();
    }
}
