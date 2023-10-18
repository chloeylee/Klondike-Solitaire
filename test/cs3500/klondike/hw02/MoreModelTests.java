package cs3500.klondike.hw02;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `MoreModelTests` class contains test cases and setups for various Klondike.
 * Solitaire game scenarios that were not included in ExamplarModelTests
 */
public class MoreModelTests {

  private BasicKlondike basicKlondike;
  List<Card> basicDeck;
  KlondikeModel acesGame;
  ArrayList<Card> acesDeck;

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
  }

  /**
   * Finds a specific card by its name in a list of cards.
   *
   * @param cardName The name of the card to search for.
   * @return The Card object matching the specified name, or null if not found.
   */
  public Card getCard(String cardName) {
    List<Card> cards = new BasicKlondike().getDeck();
    List<Card> newCards = cards.stream().filter(card ->
            card.toString().equals(cardName)).collect(Collectors.toList());

    return newCards.get(0);
  }

  @Test
  public void testIsGameOver() {
    basicKlondike.startGame(basicKlondike.getDeck(), true, 7, 3);
    Assert.assertFalse(basicKlondike.isGameOver());
  }

  @Test
  public void testInitBasicDeck() {
    Assert.assertEquals(52, this.basicKlondike.getDeck().size());
  }

  @Test
  public void testGetDeckOrder() {
    acesGame.startGame(acesDeck, false, 2, 2);
    Assert.assertEquals(acesDeck, new ArrayList<>(Arrays.asList(getCard("A♣"),
            getCard("A♠"), getCard("A♡"), getCard("A♢"))));
  }

  @Test
  public void testCardFlipsUpAfterMPP() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("2♣"));
    deck.add(getCard("A♣"));
    deck.add(getCard("A♡"));
    deck.add(getCard("2♡"));

    basicKlondike.startGame(deck, false, 2, 1);
    basicKlondike.movePile(1, 1, 0);

    Assert.assertTrue(basicKlondike.isCardVisible(1, 0));
  }

  @Test
  public void testCardFlipsUpAfterMPF() {
    List<Card> deck = new ArrayList<>();
    deck.add(getCard("2♣"));
    deck.add(getCard("A♣"));
    deck.add(getCard("A♡"));
    deck.add(getCard("2♡"));

    basicKlondike.startGame(deck, false, 2, 1);
    basicKlondike.moveToFoundation(1, 0);

    Assert.assertTrue(basicKlondike.isCardVisible(1, 0));
  }
}
