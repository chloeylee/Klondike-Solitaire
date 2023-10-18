package cs3500.klondike;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests for the KlondikeTextualController class.
 */
public class ExamplarControllerTests {
  BasicKlondike basicModel;
  List<Card> emptyDeck;
  String invalidMoveMsg;
  String gameQuitMsg;

  @Before
  public void init() {
    this.basicModel = new BasicKlondike();
    this.emptyDeck = new ArrayList<>();
    this.invalidMoveMsg = "Invalid move. Play again.";
    this.gameQuitMsg = "State of game when quit:";
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
  public void testWholeQuitMessage() throws IOException {
    Readable r = new StringReader("q");
    StringBuilder a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);

    controller.playGame(basicModel, basicModel.getDeck(), false, 4, 5);
    TextualView view = new KlondikeTextualView(basicModel);

    Assert.assertTrue(a.toString().contains("Game quit!\n" + gameQuitMsg + "\n"
            + view.toString()));
  }

  @Test
  public void testMPFWithDoubles() {
    Readable r = new StringReader("mpf 1.0 4.0");
    StringBuilder a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);

    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(basicModel, basicModel.getDeck(), false, 4, 5));
  }

  @Test
  public void testInvalidMovePileToPile() throws IOException {
    Readable r = new StringReader("mpp 7 1 3 \nq");
    StringBuilder a = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(r, a);

    controller.playGame(basicModel, basicModel.getDeck(), false, 4, 3);

    Assert.assertTrue(a.toString().contains(invalidMoveMsg));
  }

  @Test
  public void testInvalidInputThenQuit() throws IOException {
    Readable r = new StringReader("meow 1 q");
    StringBuilder a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);

    controller.playGame(basicModel, basicModel.getDeck(), false, 4, 5);

    Assert.assertTrue(a.toString().contains(gameQuitMsg));
  }

  @Test
  public void testInvalidMoveDraw() throws IOException {
    Readable r = new StringReader("md 7 \nq");
    StringBuilder a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(basicModel, basicModel.getDeck(), false, 7, 1);

    Assert.assertTrue(a.toString().contains(invalidMoveMsg));
  }

  @Test
  public void testMPPOnCustomDeck() throws IOException {
    List<Card> customDeck = new ArrayList<>();
    customDeck.add(getCard("A♠"));
    customDeck.add(getCard("A♡"));
    customDeck.add(getCard("2♡"));
    customDeck.add(getCard("2♠"));

    Readable r = new StringReader("mpp 1 1 2");
    StringBuilder a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(basicModel, customDeck, false, 2, 1);

    Assert.assertEquals(3, basicModel.getPileHeight(1));
  }
}
