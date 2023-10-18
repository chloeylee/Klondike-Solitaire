package cs3500.klondike.hw02;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `ExamplarModelTests` class contains test cases and setups for various Klondike
 * Solitaire game scenarios using different decks and configurations.
 */
public class ExamplarModelTests {
  KlondikeModel basicKlondike;
  List<Card> basicDeck;
  KlondikeModel acesGame;
  List<Card> acesDeck;
  KlondikeModel acesTwosGame;
  List<Card> acesTwosDeck;
  KlondikeModel easyGame;
  List<Card> easyDeck;
  KlondikeModel weirdGame;
  List<Card> weirdDeck;
  KlondikeModel wrongColorGame;
  List<Card> wrongColorDeck;

  @Before
  public void init() {
    this.basicKlondike = new BasicKlondike();
    this.basicDeck = this.basicKlondike.getDeck();

    this.acesGame = new BasicKlondike();
    this.acesDeck = new ArrayList<>();
    this.acesDeck.add(getCard("A♣"));
    this.acesDeck.add(getCard("A♠"));
    this.acesDeck.add(getCard("A♡"));
    this.acesDeck.add(getCard("A♢"));

    this.acesTwosGame = new BasicKlondike();
    this.acesTwosDeck = new ArrayList<>();
    this.acesTwosDeck.add(getCard("2♣"));
    this.acesTwosDeck.add(getCard("2♠"));
    this.acesTwosDeck.add(getCard("2♡"));
    this.acesTwosDeck.add(getCard("2♢"));
    this.acesTwosDeck.add(getCard("A♣"));
    this.acesTwosDeck.add(getCard("A♠"));
    this.acesTwosDeck.add(getCard("A♡"));
    this.acesTwosDeck.add(getCard("A♢"));

    this.easyGame = new BasicKlondike();
    this.easyDeck = new ArrayList<>();
    this.easyDeck.add(getCard("A♣"));
    this.easyDeck.add(getCard("2♢"));
    this.easyDeck.add(getCard("2♡"));
    this.easyDeck.add(getCard("2♣"));
    this.easyDeck.add(getCard("2♠"));
    this.easyDeck.add(getCard("A♢"));
    this.easyDeck.add(getCard("A♡"));
    this.easyDeck.add(getCard("A♠"));

    this.weirdGame = new BasicKlondike();
    this.weirdDeck = new ArrayList<>();
    this.weirdDeck.add(getCard("A♣"));
    this.weirdDeck.add(getCard("A♠"));
    this.weirdDeck.add(getCard("2♢"));
    this.weirdDeck.add(getCard("2♡"));
    this.weirdDeck.add(getCard("2♣"));
    this.weirdDeck.add(getCard("2♠"));
    this.weirdDeck.add(getCard("A♢"));
    this.weirdDeck.add(getCard("A♡"));

    this.wrongColorGame = new BasicKlondike();
    this.wrongColorDeck = new ArrayList<>();
    this.wrongColorDeck.add(getCard("A♢"));
    this.wrongColorDeck.add(getCard("2♢"));
    this.wrongColorDeck.add(getCard("2♡"));
    this.wrongColorDeck.add(getCard("2♣"));
    this.wrongColorDeck.add(getCard("2♠"));
    this.wrongColorDeck.add(getCard("A♣"));
    this.wrongColorDeck.add(getCard("A♡"));
    this.wrongColorDeck.add(getCard("A♠"));
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

  @Test
  public void testAceToEmptyCascade() {
    acesGame.startGame(acesDeck, false, 2, 1);
    this.acesGame.moveToFoundation(0, 0);

    Assert.assertThrows(IllegalStateException.class, () -> this.acesGame.moveDraw(0));
  }

  @Test
  public void testNonAceToEmptyFoundation() {
    acesTwosGame.startGame(acesTwosDeck, false, 2, 1);

    Assert.assertThrows(IllegalStateException.class, () -> acesTwosGame.moveToFoundation(0,
            0));
  }

  @Test
  public void testUpdatePileSize() {
    // easy deck = ace clubs + ace spade + two hearts
    easyGame.startGame(easyDeck, false, 2, 1);
    Assert.assertEquals(1, easyGame.getPileHeight(0));
    Assert.assertEquals(2, easyGame.getPileHeight(1));

    easyGame.movePile(0, 1, 1);
    Assert.assertEquals(3, easyGame.getPileHeight(1));
    Assert.assertEquals(0, easyGame.getPileHeight(0));
  }

  @Test
  public void testWrongColorToPile() {
    wrongColorGame.startGame(wrongColorDeck, false, 2, 1);

    Assert.assertThrows(IllegalStateException.class, () -> wrongColorGame.movePile(0,
            1, 1));
  }

  @Test
  public void testWrongColorToFoundation() {
    weirdGame.startGame(weirdDeck, false, 1, 1);
    weirdGame.moveToFoundation(0, 0);

    Assert.assertThrows(IllegalStateException.class, () -> weirdGame
            .moveDrawToFoundation(0));
  }

  @Test
  public void testUpdateTopCard() {
    acesGame.startGame(acesDeck, false, 2, 1);
    acesGame.moveToFoundation(0, 1);

    Assert.assertTrue(acesGame.getCardAt(1).toString().contains("A"));
  }

  @Test
  public void testOutOfGameBounds() {
    basicKlondike.startGame(basicDeck, false, 3, 1);

    Assert.assertThrows(IllegalArgumentException.class, () -> basicKlondike
            .moveDrawToFoundation(5));
  }

  @Test
  public void testDrawAllCards() {
    basicKlondike.startGame(basicDeck, true, 7, 4);
    int c = this.basicKlondike.getDrawCards().size();

    for (int i = 0; i < basicDeck.size(); i++) {
      this.basicKlondike.discardDraw();
    }
    Assert.assertEquals(c, basicKlondike.getDrawCards().size());
  }

  @Test
  public void testUnwantedMutation() {
    basicKlondike.startGame(basicDeck, true, 2, 10);

    Assert.assertThrows(RuntimeException.class, () ->
            this.basicKlondike.movePile(1, 1, 0));
  }

}