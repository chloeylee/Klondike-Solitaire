package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Class for testing different variants of the Klondike Game.
 */
public class ExamplarExtendedModelTests {
  WhiteheadKlondike whiteheadKlondike;
  List<Card> acesDeck;
  LimitedDrawKlondike limitedDrawKlondike;

  @Before
  public void init() {
    this.whiteheadKlondike = new WhiteheadKlondike();
    this.limitedDrawKlondike = new LimitedDrawKlondike(1);

    this.acesDeck = new ArrayList<>();
    this.acesDeck.add(getCard("A♣"));
    this.acesDeck.add(getCard("A♠"));
    this.acesDeck.add(getCard("A♡"));
    this.acesDeck.add(getCard("A♢"));
  }

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

  // these are all whitehead tests
  @Test
  public void testAnyCardToEmptyFoundation() {
    this.whiteheadKlondike.startGame(this.acesDeck, false, 2, 1);
    this.whiteheadKlondike.moveToFoundation(0, 0);
    this.whiteheadKlondike.movePile(1, 1, 0);

    Assert.assertEquals(1, this.whiteheadKlondike.getPileHeight(0));
  }

  @Test
  public void testNormalCascadeMoveFails() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♣"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♡"));
    deck.add(getCard("A♡"));
    this.whiteheadKlondike.startGame(deck, false, 2, 1);

    Assert.assertThrows(IllegalStateException.class, () ->
            this.whiteheadKlondike.movePile(0, 1, 1));
  }

  @Test
  public void testInvalidMoveStack() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♢"));
    deck.add(getCard("2♢"));
    deck.add(getCard("A♡"));
    deck.add(getCard("2♡"));

    this.whiteheadKlondike.startGame(deck, false, 2, 1);
    this.whiteheadKlondike.moveToFoundation(0, 1);

    Assert.assertThrows(IllegalStateException.class, () ->
            this.whiteheadKlondike.movePile(1, 2, 0));
  }

  @Test
  public void testMoveSameColorToPile() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("A♢"));
    deck.add(getCard("2♢"));
    deck.add(getCard("2♡"));
    deck.add(getCard("2♣"));
    deck.add(getCard("2♠"));
    deck.add(getCard("A♣"));
    deck.add(getCard("A♡"));
    deck.add(getCard("A♠"));
    this.whiteheadKlondike.startGame(deck, false, 2, 1);
    this.whiteheadKlondike.movePile(0, 1, 1);

    Assert.assertEquals(3, this.whiteheadKlondike.getPileHeight(1));
  }

  // these are all limited discard tests
  @Test
  public void testTooManyLimitedDiscardDraw() {
    this.limitedDrawKlondike.startGame(this.acesDeck, false, 2, 1);
    this.limitedDrawKlondike.discardDraw();
    this.limitedDrawKlondike.discardDraw();

    Assert.assertThrows(IllegalStateException.class, () ->
            this.limitedDrawKlondike.discardDraw());
  }

  @Test
  public void testInvalidLimitedDiscardDrawValue() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new LimitedDrawKlondike(-10));
  }
}
