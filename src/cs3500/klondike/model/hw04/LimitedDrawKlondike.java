package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.CardBuilder;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a custom game of Klondike Solitaire with a new rules where there is a limited number
 * of times the player can draw cards from the deck.
 */
public class LimitedDrawKlondike implements KlondikeModel {

  private List<Card> deck = new ArrayList<>();
  private final int numCards;
  private final List<Card> drawPile;
  private final ArrayList<ArrayList<Card>> cascadePiles;
  private final ArrayList<ArrayList<Card>> foundationPiles;
  private int numDraw;
  private boolean gameStarted;
  private Map<Card, Integer> cardDiscardMap;
  private int numTimesRedrawAllowed;

  /**
   * Constructs a {@code LimitedDrawKlondike} object.
   *
   * @param numTimesRedrawAllowed the number of times the player can draw each card from the deck
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Invalid number of times to draw");
    }
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    this.deck = this.initDeck();
    this.numCards = this.deck.size();
    this.drawPile = new ArrayList<>();
    this.cascadePiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<>(); // Assuming 4 foundation piles
    this.numDraw = 3; // Default value for the number of draw cards
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
      this.cardDiscardMap.put(card, 0);
    }
  }

  /**
   * Initializes the deck of cards for the Klondike game. This method populates the deck
   * with a standard set of playing cards, including Aces, numbered cards (2 to 10), Jacks,
   * Queens, and Kings for all four suits (Clubs, Diamonds, Hearts, and Spades).
   * The cards are created using the CardBuilder class and added to the deck.
   * This method should be called to set up the initial state of the game's deck.
   */
  List<Card> initDeck() {
    List<Card> deck = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        deck.add(new CardBuilder(rank, suit, false));
      }
    }
    return deck;
  }

  /**
   * Return a valid and complete deck of cards for a game of Klondike.
   * There is no restriction imposed on the ordering of these cards in the deck.
   * The validity of the deck is determined by the rules of the specific game in
   * the classes implementing this interface.  This method may be called as often
   * as desired.
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Card> getDeck() {
    return this.deck;
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
          throws IllegalArgumentException {
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
    this.deck = new ArrayList<Card>(deck);
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
    initCardDiscardedMaps();
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
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
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
        if (movingPile.get(0).getRank().contains("K")) {
          endPile.addAll(movingPile);
          this.cascadePiles.get(srcPile).removeAll(movingPile);
          if (!this.cascadePiles.get(srcPile).isEmpty()) {
            this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1)
                    .turnCardUp();
          }
        } else {
          throw new IllegalStateException("Move is not allowed");
        }
      } else if (this.validCascadeMove(movingPile.get(0), endPile.get(endPile.size() - 1))) {
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
   * @param start The card to be moved from a cascade pile.
   * @param end   The card at the top of the destination cascade pile.
   * @return {@code true} if the move is valid, {@code false} otherwise.
   * @throws IllegalArgumentException if either card is not face up/visible
   */
  boolean validCascadeMove(Card start, Card end) {
    if (start.isCardFacedUp() && end.isCardFacedUp()) {
      return !start.sameColor(end) && end.rankHigher(start);
    }
    throw new IllegalArgumentException("Can't move card on non-visible card");
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
    if (this.drawPile.isEmpty() || this.deck.isEmpty()) {
      throw new IllegalStateException("Deck or draw pile is empty");
    }

    Card draw = drawPile.get(0);
    List<Card> endPile = this.cascadePiles.get(destPile);
    if (this.validDrawMove(draw, endPile.get(endPile.size() - 1))) {
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

  /**
   * Checks if moving a card from the draw pile to a cascade pile is a valid move in the game.
   * For the move to be valid, the following conditions must be met:
   * - The color of the drawn card must not be the same as the color of the card at the top of the
   * destination cascade pile.
   * - The rank of the drawn card must be one less than the rank of the card at the top of the
   * destination cascade pile.
   *
   * @param draw The card to be moved from the draw pile.
   * @param end  The card at the top of the destination cascade pile.
   * @return {@code true} if the move is valid, {@code false} otherwise.
   * @throws IllegalArgumentException if cards are face down
   */
  boolean validDrawMove(Card draw, Card end) {
    if (drawPile.contains(draw) && end.isCardFacedUp()) {
      return !draw.sameColor(end) && end.rankHigher(draw);
    }
    throw new IllegalArgumentException("Cards have to be face up for valid move");
  }

  /**
   * Moves the top card of the given pile to the requested foundation pile.
   *
   * @param srcPile        the 0-based index (from the left) of the pile to move a card
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid
   * @throws IllegalStateException    if the source pile is empty or if the move is not
   *                                  allowable
   */
  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    if (gameStarted) {
      if (srcPile > this.cascadePiles.size() - 1 || srcPile < 0
              || foundationPile > this.foundationPiles.size() - 1 || foundationPile < 0) {
        throw new IllegalArgumentException("Invalid pile value");
      }

      if (this.cascadePiles.get(srcPile).isEmpty()) {
        throw new IllegalStateException("Source pile is empty");
      }

      Card moving = this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1);
      List<Card> foundation = this.foundationPiles.get(foundationPile);

      if (this.validMoveToFoundation(moving, foundation)) {
        foundation.add(moving);
        this.cascadePiles.get(srcPile).remove(moving);

        if (!this.cascadePiles.get(srcPile).isEmpty()) {
          this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1)
                  .turnCardUp();
        }
      } else {
        throw new IllegalStateException("This is an invalid move");
      }
    } else {
      throw new IllegalStateException("Game not started");
    }
  }

  /**
   * Moves the topmost draw-card directly to a foundation pile.
   *
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   * @throws IllegalStateException    if there are no draw cards or if the move is not
   *                                  allowable
   */
  @Override
  public void moveDrawToFoundation(int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (foundationPile < 0 || foundationPile > this.foundationPiles.size() - 1) {
      throw new IllegalArgumentException("Invalid foundation pile index");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }

    Card moving = this.drawPile.get(0);
    List<Card> foundation = this.foundationPiles.get(foundationPile);

    if (this.validMoveToFoundation(moving, foundation) || (foundation.isEmpty()
            && moving.getRank().contains("A"))) {
      foundation.add(moving);
      this.drawPile.remove(moving);
    } else {
      throw new IllegalStateException("This is an invalid move");
    }
  }

  /**
   * Checks if moving a card to a foundation pile is a valid move in the Klondike game.
   * For the move to be valid, the following conditions must be met:
   *
   * <p>The rank of the card to be moved must be one greater than the rank of the card at the top
   * of the foundation pile. The suit of the card to be moved must be the same as the suit of the
   * card at the top of the foundation pile.
   *
   * @param card       The card to be moved to the foundation pile.
   * @param foundation The foundation pile where the card is to be moved.
   * @return {@code true} if the move is valid, {@code false} otherwise.
   */
  boolean validMoveToFoundation(Card card, List<Card> foundation) {
    if (!foundation.isEmpty()) {
      return card.rankHigher(foundation.get(foundation.size() - 1))
              && card.getSuit().equals(foundation.get(foundation.size() - 1).getSuit());
    }
    return card.toString().contains("A");
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
    cardDiscardValue++;
    this.cardDiscardMap.put(card, cardDiscardValue);
    if (cardDiscardValue >= this.numTimesRedrawAllowed) {
      this.drawPile.remove(card);
    } else if (cardDiscardValue < this.numTimesRedrawAllowed) {
      this.drawPile.remove(card);
      this.deck.add(card);
      this.drawPile.add(this.deck.get(0));
      this.drawPile.get(drawPile.size() - 1).turnCardUp();
      this.deck.remove(0);
    }
  }

  @Override
  public int getNumRows() {
    if (gameStarted) {
      int maxRow = 0;
      for (int i = 0; i < cascadePiles.size(); i++) {
        if (cascadePiles.get(i).size() > maxRow) {
          maxRow = cascadePiles.get(i).size();
        }
      }
      return maxRow;
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns the number of piles for this game.
   *
   * @return the number of piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumPiles() {
    if (gameStarted) {
      return cascadePiles.size();
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns the maximum number of visible cards in the draw pile.
   *
   * @return the number of visible cards in the draw pile
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumDraw() {
    if (gameStarted) {
      return this.drawPile.size();
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Signal if the game is over or not.  A game is over if there are no more
   * possible moves to be made, or draw cards to be used (or discarded).
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public boolean isGameOver() {
    if (gameStarted) {
      if (this.deck.isEmpty() && this.drawPile.isEmpty()) {
        return true;
      }
      int score = 0;
      for (int i = 0; i < foundationPiles.size(); i++) {
        score = foundationPiles.get(i).size() + score;
      }
      return score == this.numCards;
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Return the current score, which is the sum of the values of the topmost cards
   * in the foundation piles.
   *
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getScore() throws IllegalStateException {
    if (gameStarted) {
      if (!foundationPiles.contains(null)) {
        int count = 0;
        for (List<Card> list : foundationPiles) {
          count = list.size() + count;
        }
        return count;
      }
      throw new IllegalStateException("foundation piles cant be null");
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns the number of cards in the specified pile.
   *
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the number of cards in the specified pile
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if pile number is invalid
   */
  @Override
  public int getPileHeight(int pileNum) {
    if (gameStarted) {
      if (pileNum > this.cascadePiles.size() - 1 || pileNum < 0) {
        throw new IllegalArgumentException("Invalid pile number");
      }
      List<Card> pile = cascadePiles.get(pileNum);
      return pile.size();
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns whether the card at the specified coordinates is face-up or not.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return whether the card at the given position is face-up or not
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public boolean isCardVisible(int pileNum, int card) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0 || pileNum > this.cascadePiles.size() - 1
            || card < 0 || card > this.cascadePiles.get(pileNum).size() - 1) {
      throw new IllegalArgumentException("Invalid pile number or card number");
    }
    return this.cascadePiles.get(pileNum).get(card).isCardFacedUp();
  }

  /**
   * Returns the card at the specified coordinates, if it is visible.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public Card getCardAt(int pileNum, int card) {
    if (gameStarted) {
      if (pileNum < 0 || pileNum > this.cascadePiles.size() - 1
              || card < 0 || card > this.cascadePiles.get(pileNum).size() - 1) {
        throw new IllegalArgumentException("Invalid pile number or card number");
      }
      if (this.cascadePiles.get(pileNum).isEmpty()) {
        return null;
      }
      if (!this.cascadePiles.get(pileNum).get(card).isCardFacedUp()) {
        throw new IllegalArgumentException("Card is not visible");
      }
      List<Card> pile = cascadePiles.get(pileNum);
      return pile.get(card);
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns the card at the top of the specified foundation pile.
   *
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   */
  @Override
  public Card getCardAt(int foundationPile) {
    if (gameStarted) {
      if (foundationPile < 0 || foundationPile > this.foundationPiles.size() - 1) {
        throw new IllegalArgumentException("Invalid foundation pile parameter");
      }
      List<Card> foundation = this.foundationPiles.get(foundationPile);
      if (foundation.isEmpty()) {
        return null;
      }
      return foundation.get(foundation.size() - 1);
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Returns the currently available draw cards.
   * There should be at most {@link KlondikeModel#getNumDraw} cards (the number
   * specified when the game started) -- there may be fewer, if cards have been removed.
   * NOTE: Users of this method should not modify the resulting list.
   *
   * @return the ordered list of available draw cards (i.e. first element of this list
   * is the first one to be drawn)
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public List<Card> getDrawCards() {
    if (gameStarted) {
      return this.drawPile.subList(0, this.numDraw);
    }
    throw new IllegalStateException("Game not started");
  }

  /**
   * Return the number of foundation piles in this game.
   *
   * @return the number of foundation piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumFoundations() {
    if (gameStarted) {
      return this.foundationPiles.size();
    }
    throw new IllegalStateException("Game not started");
  }
}
