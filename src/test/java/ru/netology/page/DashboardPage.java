package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.data.DataHelper.getCardsInfo;

public class DashboardPage {

    private SelenideElement heading = $("[data-test-id='dashboard']");
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");
    private SelenideElement firstCard = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private SelenideElement secondCard = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private SelenideElement firstCardDepoButton = firstCard.$("[data-test-id='action-deposit']");
    private SelenideElement secondCardDepoButton = secondCard.$("[data-test-id='action-deposit']");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final int firstCardStartBalance = getCardBalance(getCardsInfo().getFirstCardId());
    private final int secondCardStartBalance = getCardBalance(getCardsInfo().getSecondCardId());
    private String cardToDepositId;

    public DashboardPage() {
        SelenideElement[] elementsToVerify = {
                heading,
                reloadButton,
                firstCard,
                secondCard,
                firstCardDepoButton,
                secondCardDepoButton};
        for (SelenideElement element : elementsToVerify) {
            element.shouldBe(visible);
        }
    }

    public int getCardBalance(String cardId) {
        SelenideElement card = cards.findBy(attribute("data-test-id", cardId));
        String text = card.getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public enum CardType {
        FIRST, SECOND
    }


    public DepositPage cardDeposit(CardType cardType) {

        DataHelper.Cards cards = getCardsInfo();
//        cardToDepositId = cardType == CardType.FIRST
//                ? cards.getFirstCardId()
//                : cards.getSecondCardId();
        SelenideElement button = cardType == CardType.FIRST
                ? firstCardDepoButton
                : secondCardDepoButton;
        button.click();

        return new DepositPage();
    }

    public DashboardPage restoreInitialBalances() {
        int firstCardCurrent = getCardBalance(getCardsInfo().getFirstCardId());
        int firstCardInitial = GetFirstCardStartBalance();
        int difference = firstCardInitial - firstCardCurrent;

        if (difference != 0) {
            cardDeposit(difference > 0 ? CardType.FIRST : CardType.SECOND)
                    .validDeposit(
                            String.valueOf(Math.abs(difference)),
                            difference > 0 ? getCardsInfo().getSecondCardNum()
                                    : getCardsInfo().getFirstCardNum()
                    );
        }
        return this;
    }


    public int GetFirstCardStartBalance() {
        return firstCardStartBalance;
    }

    public int GetSecondCardStartBalance() {
        return secondCardStartBalance;
    }

    public String GetactiveCardId() {
        return cardToDepositId;
    }

}
