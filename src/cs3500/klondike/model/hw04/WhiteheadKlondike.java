package cs3500.klondike.model.hw04;

import cs3500.klondike.model.AbstractKlondike;
import cs3500.klondike.model.hw02.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a variation of a game of Klondike Solitaire called Whitehead Klondike.
 */
public class WhiteheadKlondike extends AbstractKlondike {

  /**
   * the constructor for the whitehead klondike game class.
   */
  public WhiteheadKlondike() {
    super();
  }

  /**
   * <p>Deal a new game of Klondike.
   * The cards to be used and their order are specified by the the given deck,
   * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
   *
   * <p>This method first verifies that the deck is valid. It deals cards in rows
   * (left-to-right, top-to-bottom) into the characteristic cascade shape
   * with the specified number of rows, followed by (at most) the specified number of
   * draw cards. When {@code shuffle} is {@code false}, the {@code deck} must be used in
   * order and the 0th card in {@code deck} is used as the first card dealt.
   * There will be as many foundation piles as there are Aces in the deck.</p>
   *
   * <p>A valid deck must consist cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single
   * suit).</p>
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, and should work for any valid arguments.</p>
   *
   * @param deck     the deck to be dealt
   * @param shuffle  if {@code false}, use the order as given by {@code deck},
   *                 otherwise use a randomly shuffled order
   * @param numPiles number of piles to be dealt
   * @param numDraw  maximum number of draw cards available at a time
   * @throws IllegalStateException    if the game has already started
   * @throws IllegalArgumentException if the deck is null or invalid,
   *                                  a full cascade cannot be dealt with the given sizes,
   *                                  or another input is invalid
   */
  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    if (gameStarted) {
      throw new IllegalStateException("Game has already started");
    }
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    if (deck.contains(null)) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    if (deck.isEmpty()) {
      throw new IllegalStateException("Deck cannot be empty");
    }
    for (Card card : deck) {
      if (card == null) {
        throw new IllegalArgumentException("Card cannot be null");
      }
      if (card.getRank() == null || card.getSuit() == null) {
        throw new IllegalArgumentException("Card value cannot be null");
      }
      card.turnCardUp();
    }

    if ((numPiles * (numPiles - 1) / 2) + numPiles > deck.size()) {
      throw new IllegalArgumentException("Invalid numPiles value");
    }
    if (numPiles < 1 || numPiles > 9) {
      throw new IllegalArgumentException("Invalid numPiles value");
    }
    if (numDraw < 1 || numDraw > deck.size()) {
      throw new IllegalArgumentException("Invalid numDraw value");
    }
    this.deck = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(this.deck);
    }
    int numAces = 0;
    for (Card card : this.deck) {
      if (card == null) {
        throw new IllegalArgumentException("Deck cannot be null");
      }
      if (card.getRank().contains("A")) {
        numAces++;
      }
    }
    if (numAces == 0) {
      throw new IllegalArgumentException("Invalid ace cards");
    }
    if (deck.size() % numAces != 0) {
      throw new IllegalArgumentException("Invalid ace cards");
    }
    this.cascadePiles.clear();

    for (int pile = 0; pile < numPiles; pile++) {
      this.cascadePiles.add(new ArrayList<>());
    }

    for (int pile = 0; pile < numPiles; pile++) {
      for (int card = 0; card < numPiles; card++) {
        if (card >= pile) {
          this.cascadePiles.get(card).add(this.deck.remove(0));
        }
      }
      this.cascadePiles.get(pile).get(this.cascadePiles.get(pile).size() - 1).turnCardUp();
    }

    this.foundationPiles.clear();
    for (int fPile = 0; fPile < numAces; fPile++) {
      this.foundationPiles.add(new ArrayList<>());
    }

    this.drawPile.clear();

    List<Card> tempDeck = new ArrayList<>(this.deck);
    for (Card card : tempDeck.subList(0, Math.min(numDraw, tempDeck.size()))) {
      this.deck.remove(card);
      card.turnCardUp();
      this.drawPile.add(card);
    }
    this.numDraw = numDraw;

    this.gameStarted = true;
  }
  
  /**
   * Moves the requested number of cards from the source pile to the destination pile,
   * if allowable by the rules of the game.
   *
   * @param srcPile  the 0-based index (from the left) of the pile to be moved
   * @param numCards how many cards to be moved from that pile
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 moved cards
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid, if the pile
   *                                  numbers are the same, or there are not enough cards to move
   *                                  from the srcPile to the
   *                                  destPile (i.e. the move is not physically possible)
   * @throws IllegalStateException    if the move is not allowable (i.e. the move is not
   *                                  logically possible)
   */
  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    if (gameStarted) {
      if (srcPile > this.cascadePiles.size() - 1 || destPile > this.cascadePiles.size() - 1
              || srcPile < 0 || destPile < 0 || numCards > this.cascadePiles.get(srcPile).size()
              || numCards < 1 || srcPile == destPile || this.cascadePiles.get(srcPile).isEmpty()) {
        throw new IllegalArgumentException("Invalid pile value");
      }

      List<Card> movingPile = new ArrayList<>(this.cascadePiles.get(srcPile)
              .subList(this.cascadePiles.get(srcPile).size() - numCards,
                      this.cascadePiles.get(srcPile).size()));
      List<Card> endPile = this.cascadePiles.get(destPile);

      if (endPile.isEmpty()) {
        if (movingPile.size() > 1) {
          for (int c = 0; c < movingPile.size() - 1; c++) {
            if (!movingPile.get(c).getSuit().equals(movingPile.get(c + 1).getSuit())) {
              throw new IllegalStateException("Move is not allowed");
            }
          }
          this.cascadePiles.get(srcPile).removeAll(movingPile);
          if (!this.cascadePiles.get(srcPile).isEmpty()) {
            this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1)
                    .turnCardUp();
          }
        } else if (movingPile.size() == 1) {
          endPile.addAll(movingPile);
          this.cascadePiles.get(srcPile).removeAll(movingPile);
          if (!this.cascadePiles.get(srcPile).isEmpty()) {
            this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1)
                    .turnCardUp();
          }
        }
      } else if (this.validCascadeMove(movingPile, endPile.get(endPile.size() - 1))) {
        endPile.addAll(movingPile);
        this.cascadePiles.get(srcPile).removeAll(movingPile);
        if (!this.cascadePiles.get(srcPile).isEmpty()) {
          this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1)
                  .turnCardUp();
        }
      } else {
        throw new IllegalStateException("Move is not allowed");
      }
    } else {
      throw new IllegalStateException("Game has not been started");
    }
  }

  /**
   * Checks if moving a card from one cascade pile to another is a valid move in the Klondike game.
   * For the move to be valid, the following conditions must be met:
   *
   * <p>The color of the starting card must not be the same as the color of the ending card.
   * The rank of the starting card must be one less than the rank of the ending card.
   *
   * @param movingPile The list of cards to be moved.
   * @param end        The card at the top of the destination cascade pile.
   * @return {@code true} if the move is valid, {@code false} otherwise.
   * @throws IllegalArgumentException if either card is not face up/visible
   */
  boolean validCascadeMove(List<Card> movingPile, Card end) {
    if (movingPile.get(0).isCardFacedUp() && end.isCardFacedUp()) {
      if (movingPile.size() == 1) {
        return movingPile.get(0).sameColor(end) && end.rankHigher(movingPile.get(0));
      } else if (movingPile.size() > 1) {
        for (int c = 0; c < movingPile.size() - 1; c++) {
          if (!movingPile.get(c).getSuit().equals(movingPile.get(c + 1).getSuit())) {
            return false; // card has diff suit from the next
          }
        }
        return true; // All cards have the same suit
      }
    }
    throw new IllegalArgumentException("Can't move card onto non-visible card");
  }

  /**
   * Moves the topmost draw-card to the destination pile.  If no draw cards remain,
   * reveal the next available draw cards
   *
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if destination pile number is invalid
   * @throws IllegalStateException    if there are no draw cards, or if the move is not
   *                                  allowable
   */
  @Override
  public void moveDraw(int destPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid pile value");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }

    Card draw = this.getDrawCards().get(0);
    List<Card> endPile = this.cascadePiles.get(destPile);
    if (this.validCascadeMove(this.getDrawCards(), endPile.get(endPile.size() - 1))) {
      endPile.add(draw);
      this.drawPile.remove(draw);
      if (!deck.isEmpty()) {
        deck.get(0).turnCardUp();
        this.drawPile.add(deck.get(0));
        deck.remove(0);
      }
    } else {
      throw new IllegalStateException("This is an invalid move");
    }
  }
}
