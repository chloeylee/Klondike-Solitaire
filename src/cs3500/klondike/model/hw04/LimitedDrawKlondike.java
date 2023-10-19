package cs3500.klondike.model.hw04;

import cs3500.klondike.model.AbstractKlondike;
import cs3500.klondike.model.hw02.Card;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a custom game of Klondike Solitaire with a new rules where there is a limited number
 * of times the player can draw cards from the deck.
 */
public class LimitedDrawKlondike extends AbstractKlondike {
  private Map<Card, Integer> cardDiscardMap;
  private int numTimesRedrawAllowed;

  /**
   * Constructs a {@code LimitedDrawKlondike} object.
   *
   * @param numTimesRedrawAllowed the number of times the player can draw each card from the deck
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();

    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Invalid number of times to draw");
    }
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    this.cardDiscardMap = new HashMap<Card, Integer>();
    initCardDiscardedMaps();
  }

  /**
   * Initializes the cardDiscardMap for the Klondike game. This method populates the map
   * with a standard set of playing cards.
   * The cardDiscardMap is used to keep track of the number of times a card has been discarded.
   * The key is the card and the value is the number of times the card has been discarded.
   *
   */
  void initCardDiscardedMaps() {
    for (Card card : this.deck) {
      this.cardDiscardMap.put(card, new Integer(0));
    }
  }

  /**
   * Discards the top-most draw card.
   *
   * @throws IllegalStateException if the game hasn't been started yet or if the draw pile is empty
   * @throws IllegalStateException if the number of discards exceeds the number of times the player
   *                               can draw each card from the deck
   */
  @Override
  public void discardDraw() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty: no cards to discard");
    }
    Card card = this.drawPile.get(0);
    int cardDiscardValue = this.cardDiscardMap.get(card);

    if (cardDiscardValue >= this.numTimesRedrawAllowed) {
      this.drawPile.remove(card);

      if (!this.deck.isEmpty()) {
        this.deck.get(0).turnCardUp();
        this.drawPile.add(this.deck.get(0));
        this.deck.remove(0);
      }
    } else {
      this.drawPile.remove(card);
      this.deck.add(card);
      this.drawPile.add(this.deck.get(0));
      this.drawPile.get(drawPile.size() - 1).turnCardUp();
      this.deck.remove(0);
      cardDiscardValue++;
    }
    this.cardDiscardMap.put(card, cardDiscardValue);
  }
}
