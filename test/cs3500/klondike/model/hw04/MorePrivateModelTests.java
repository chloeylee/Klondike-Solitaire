package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.GameType;
import cs3500.klondike.KlondikeCreator;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * tests for the more private methods of the variants of klondike solitaire.
 */
public class MorePrivateModelTests {
  // test if new valid whitehead mpp work

  // test invalid whitehead and limited constructor

  // test if new valid whitehead mpp work

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
  public void testIsValidMPPWhitehead() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));
    deck.add(getCard("A♠"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("3♣"));
    deck.add(getCard("3♠"));

    KlondikeModel game = KlondikeCreator.create(GameType.WHITEHEAD);

    game.startGame(deck, false, 2, 1);
    game.movePile(0, 1, 1);

    Assert.assertEquals(3, game.getPileHeight(1));
  }

  @Test
  public void testAllCardsAreFaceUpWhitehead() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));
    deck.add(getCard("A♠"));
    deck.add(getCard("A♡"));
    deck.add(getCard("A♢"));
    KlondikeModel game = KlondikeCreator.create(GameType.WHITEHEAD);

    game.startGame(deck, false, 2, 1);

    Assert.assertTrue(game.isCardVisible(1, 0));
  }

  @Test
  public void testValidCascadeMoveMultipleCardsWhitehead() {
    List<Card> movingPile = new ArrayList<>();
    movingPile.add(getCard("2♡"));
    movingPile.add(getCard("A♡"));
    movingPile.get(0).turnCardUp();
    movingPile.get(1).turnCardUp();

    Card endCard = getCard("3♢");
    endCard.turnCardUp();

    WhiteheadKlondike game = new WhiteheadKlondike();

    Assert.assertTrue(game.validCascadeMove(movingPile, endCard));
  }

  @Test
  public void testValidMoveDrawWhitehead() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));
    deck.add(getCard("A♠"));
    deck.add(getCard("2♡"));
    deck.add(getCard("A♡"));
    deck.add(getCard("A♢"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("2♢"));
    WhiteheadKlondike game = new WhiteheadKlondike();

    game.startGame(deck, false, 2, 1);
    game.moveDraw(1);

    Assert.assertEquals(3, game.getPileHeight(1));
  }

  @Test
  public void testValidMoveDrawToFoundationWhitehead() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));    // pile 1
    deck.add(getCard("A♠"));    // pile 2
    deck.add(getCard("2♡"));
    deck.add(getCard("A♡"));    // draws start here
    deck.add(getCard("A♢"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("2♢"));
    WhiteheadKlondike game = new WhiteheadKlondike();

    game.startGame(deck, false, 2, 3);

    game.moveDrawToFoundation(0);

    List<Card> expected = new ArrayList<>();
    expected.add(getCard("A♢"));
    expected.add(getCard("2♣"));
    expected.add(getCard("2♠"));


    Assert.assertEquals(expected, game.getDrawCards());
  }

  @Test
  public void testDiscardAllDrawsLimited() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));    // pile 1
    deck.add(getCard("A♠"));    // pile 2
    deck.add(getCard("2♡"));
    deck.add(getCard("A♡"));    // draws start here
    deck.add(getCard("A♢"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("2♢"));

    LimitedDrawKlondike game = new LimitedDrawKlondike(1);
    game.startGame(deck, false, 2, 3);

    game.discardDraw(); // full cycle 1
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    game.discardDraw(); // full cycle 2
    game.discardDraw();
    game.discardDraw();

    Assert.assertEquals(2, game.getNumDraw());
  }

  @Test
  public void testInvalidSuitLengthsDeck() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♡"));
    deck.add(getCard("2♡"));
    deck.add(getCard("A♢"));

    Assert.assertThrows(IllegalArgumentException.class, () ->
            new WhiteheadKlondike().startGame(deck, false, 2, 1));
  }

  @Test
  public void testNumDrawCardChangesForLimited() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));    // pile 1
    deck.add(getCard("A♠"));    // pile 2
    deck.add(getCard("2♡"));
    deck.add(getCard("A♡"));    // draws start here
    deck.add(getCard("A♢"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("2♢"));
    LimitedDrawKlondike game = new LimitedDrawKlondike(0);

    game.startGame(deck, false, 2, 3);

    game.discardDraw();
    game.discardDraw();
    game.discardDraw();

    Assert.assertEquals(2, game.getNumDraw());
  }

}
