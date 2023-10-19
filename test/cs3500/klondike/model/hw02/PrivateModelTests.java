package cs3500.klondike.model.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.view.KlondikeTextualView;

/**
 * The `PrivateModelTests` class contains test cases and setups for various Klondike
 * Solitaire game scenarios and private methods using different decks and configurations.
 */
public class PrivateModelTests {
  private BasicKlondike basicGame;
  private KlondikeTextualView view;
  private BasicKlondike specialGame;
  private KlondikeModel mockModel;
  private List<Card> emptyDeck;


  @Before
  public void init() {
    this.emptyDeck = new ArrayList<>();
    this.basicGame = new BasicKlondike();
    this.view = new KlondikeTextualView(mockModel);
  }

  @Test
  public void testEmptyStartDeck() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            this.specialGame.startGame(emptyDeck, false, 1, 1));
  }

  @Test
  public void testValidCascadeMove() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoHearts = new CardBuilder(Rank.TWO, Suit.HEART, true);

    Assert.assertTrue(this.basicGame.validCascadeMove(twoHearts, aceSpade));
  }

  @Test
  public void testLowerRankCascadeMove() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoHearts = new CardBuilder(Rank.TEN, Suit.HEART, true);

    Assert.assertFalse(this.basicGame.validCascadeMove(aceSpade, twoHearts));
  }

  @Test
  public void testCascadeMoveOnFaceDownCard() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoHearts = new CardBuilder(Rank.TWO, Suit.HEART, false);

    Assert.assertThrows(IllegalArgumentException.class, () ->
            this.basicGame.validCascadeMove(twoHearts, aceSpade));
  }

  @Test
  public void testValidDrawMove() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card aceHeart = new CardBuilder(Rank.ACE, Suit.HEART, true);
    Card twoHearts = new CardBuilder(Rank.TWO, Suit.HEART, true);

    this.specialGame.getDeck().add(twoHearts);
    this.specialGame.getDeck().add(aceSpade);

    this.specialGame.startGame(this.specialGame.getDeck(), false, 1, 1);

    Assert.assertTrue(this.specialGame.validCascadeMove(aceSpade, twoHearts));
  }

  @Test
  public void testValidDrawMoveOnFaceDownCard() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoHearts = new CardBuilder(Rank.TWO, Suit.HEART, false);

    Assert.assertThrows(IllegalArgumentException.class, () ->
            this.basicGame.validCascadeMove(twoHearts, aceSpade));
  }

  @Test
  public void testValidMoveToEmptyFoundation() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoSpade = new CardBuilder(Rank.TWO, Suit.SPADE, true);
    List<Card> foundationSpade = new ArrayList<>();

    Assert.assertTrue(this.basicGame.validMoveToFoundation(aceSpade, foundationSpade));
  }

  @Test
  public void testValidMoveToNonEmptyFoundation() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoSpade = new CardBuilder(Rank.TWO, Suit.SPADE, true);
    List<Card> foundationSpade = new ArrayList<>();
    foundationSpade.add(aceSpade);

    Assert.assertTrue(this.basicGame.validMoveToFoundation(twoSpade, foundationSpade));
  }

  @Test
  public void testInValidMoveToFoundation() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card threeSpade = new CardBuilder(Rank.THREE, Suit.SPADE, true);
    List<Card> foundationSpade = new ArrayList<>();
    foundationSpade.add(aceSpade);

    Assert.assertFalse(this.basicGame.validMoveToFoundation(threeSpade, foundationSpade));
  }

  @Test
  public void testValidMoveToFoundationFromDraw() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card twoSpade = new CardBuilder(Rank.TWO, Suit.SPADE, true);

    List<Card> foundationSpade = new ArrayList<>();
    foundationSpade.add(aceSpade);
    List<Card> drawSpade = new ArrayList<>();
    drawSpade.add(twoSpade);

    Assert.assertTrue(this.basicGame.validMoveToFoundation(drawSpade.get(0), foundationSpade));
  }

  @Test
  public void testInValidMoveToFoundationFromDraw() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card threeSpade = new CardBuilder(Rank.THREE, Suit.SPADE, true);

    List<Card> foundationSpade = new ArrayList<>();
    foundationSpade.add(aceSpade);
    List<Card> drawSpade = new ArrayList<>();
    drawSpade.add(threeSpade);

    Assert.assertFalse(this.basicGame.validMoveToFoundation(drawSpade.get(0), foundationSpade));
  }

  @Test
  public void testPrintDrawCards() {
    Card fourSpade = new CardBuilder(Rank.FOUR, Suit.SPADE, true);
    List<Card> drawCards = new ArrayList<>(Arrays.asList(new CardBuilder(Rank.ACE, Suit.SPADE,
            true), new CardBuilder(Rank.TWO, Suit.SPADE, true), new CardBuilder(Rank.THREE,
            Suit.SPADE, true)));
    String expected = new String("Draw: A♠, 2♠, 3♠");

    this.specialGame.getDeck().add(fourSpade);
    this.specialGame.getDeck().addAll(drawCards);
    this.specialGame.startGame(this.specialGame.getDeck(), false, 1, 3);

    KlondikeTextualView view = new KlondikeTextualView(this.specialGame);

    Assert.assertEquals(expected, view.printDrawCards());
  }


  @Test
  public void testPrintFoundationPiles() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card threeSpade = new CardBuilder(Rank.THREE, Suit.SPADE, true);
    this.specialGame.getDeck().add(threeSpade);
    this.specialGame.getDeck().add(aceSpade);
    this.specialGame.startGame(this.specialGame.getDeck(), false, 1, 1);
    this.specialGame.moveDrawToFoundation(0);
    String expected = new String("Foundation: A♠");

    view = new KlondikeTextualView(this.specialGame);

    Assert.assertEquals(expected, view.printFoundationPiles());
  }

  @Test
  public void testPrintMultipleFoundationPiles() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);
    Card aceHeart = new CardBuilder(Rank.ACE, Suit.HEART, true);
    Card threeSpade = new CardBuilder(Rank.THREE, Suit.SPADE, true);
    Card threeHeart = new CardBuilder(Rank.THREE, Suit.HEART, true);
    this.specialGame.getDeck().add(threeSpade);
    this.specialGame.getDeck().add(aceSpade);
    this.specialGame.getDeck().add(aceHeart);
    this.specialGame.getDeck().add(threeHeart);
    this.specialGame.startGame(this.specialGame.getDeck(), false, 1, 1);
    this.specialGame.moveDrawToFoundation(0);

    String expected = new String("Foundation: A♠, <none>");

    view = new KlondikeTextualView(this.specialGame);

    Assert.assertEquals(expected, view.printFoundationPiles());
  }

  @Test
  public void testToStringOnCard() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);

    Assert.assertEquals("A♠", aceSpade.toString());
  }

  @Test
  public void testGetRank() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);

    Assert.assertEquals("A", aceSpade.getRank());
  }

  @Test
  public void testGetSuit() {
    Card aceSpade = new CardBuilder(Rank.ACE, Suit.SPADE, true);

    Assert.assertEquals("♠", aceSpade.getSuit());
  }



}
