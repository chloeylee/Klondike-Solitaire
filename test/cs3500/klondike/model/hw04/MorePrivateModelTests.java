package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * tests for the more private methods of the variants of klondike solitaire.
 */
public class MorePrivateModelTests {
  // test if new valid whitehead mpp work

  // test invalid whitehead constructor

  // test if new valid whitehead mpf work

  /**
   * Finds a specific card by its name in a list of cards.
   *
   * @param cardName The name of the card to search for.
   * @return The Card object matching the specified name, or null if not found.
   */
  public Card getCard(String cardName) {
    KlondikeModel curGame = new BasicKlondike();
    List<Card> cards = new BasicKlondike().getDeck();
    List<Card> newCards = cards.stream().filter(card ->
            card.toString().equals(cardName)).collect(Collectors.toList());

    return newCards.get(0);
  }

  @Test
  public void testAllCardsAreFaceUpWhitehead() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));
    deck.add(getCard("A♠"));
    deck.add(getCard("A♡"));
    deck.add(getCard("A♢"));
    WhiteheadKlondike game = new WhiteheadKlondike();

    game.startGame(deck, false, 2, 1);

    Assert.assertTrue(game.isCardVisible(1, 0));
  }
}
