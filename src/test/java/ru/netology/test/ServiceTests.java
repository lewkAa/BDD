package ru.netology.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServiceTests {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Успешное пополнение первой карты с баланса второй карты")
    void shouldDepositFirstCardFromSecondCard() {
        var depoCard = DataHelper.getCard(0);
        var withdrCard = DataHelper.getCard(1);
        var transferAmount = DataHelper.genAmount(withdrCard);
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var depositPage = dashboardPage.cardDeposit(depoCard.getId());

        depositPage.validDeposit(String.valueOf(transferAmount),
                withdrCard.getNumber());

        int depoCardCurrentBalance = dashboardPage.getCardBalance(depoCard.getId());
        int withdrawCardCurrentBalance = dashboardPage.getCardBalance(withdrCard.getId());

        assertEquals(depoCard.getInitialBalance() + transferAmount, depoCardCurrentBalance);
        assertEquals(withdrCard.getInitialBalance() - transferAmount, withdrawCardCurrentBalance);

        dashboardPage.revertTransfer(withdrCard.getId(), depoCard.getNumber(), transferAmount);
    }

    @Test
    @DisplayName("Успешное пополнение второй карты с баланса первой карты")
    void shouldDepositSecondCardFromFirstCard() {
        var depoCard = DataHelper.getCard(1);
        var withdrCard = DataHelper.getCard(0);
        var transferAmount = DataHelper.genAmount(withdrCard);
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var depositPage = dashboardPage.cardDeposit(depoCard.getId());

        depositPage.validDeposit(String.valueOf(transferAmount),
                withdrCard.getNumber());

        int depoCardCurrentBalance = dashboardPage.getCardBalance(depoCard.getId());
        int withdrawCardCurrentBalance = dashboardPage.getCardBalance(withdrCard.getId());

        assertEquals(depoCard.getInitialBalance() + transferAmount, depoCardCurrentBalance);
        assertEquals(withdrCard.getInitialBalance() - transferAmount, withdrawCardCurrentBalance);

        dashboardPage.revertTransfer(withdrCard.getId(), depoCard.getNumber(), transferAmount);
    }
}
