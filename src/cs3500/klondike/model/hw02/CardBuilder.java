package cs3500.klondike.model.hw02;

/**
 * The `CardBuilder` class implements the `Card` interface and represents a playing card
 * with a specified suit, rank, and face-up status.
 */
public class CardBuilder implements Card {
  private final Suit suit;
  private final Rank rank;
  boolean faceUp;

  /**
   * Constructs a new card with the specified suit, rank, and face-up status.
   *
   * @param suit   the suit of the card (e.g., "♣", "♢", "♡", "♠")
   * @param rank   the rank of the card (e.g., "A", "2", "10", "K")
   * @param faceUp {@code true} if the card is initially face up, {@code false} if it is face down
   */
  public CardBuilder(Rank rank, Suit suit, boolean faceUp) {
    this.rank = rank;
    this.suit = suit;
    this.faceUp = faceUp;
  }

  /**
   * Checks if this card is the same as another card.
   * @param obj the card to compare to
   * @return {@code true} if the cards are equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true; // Same object reference
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false; // Different classes or null reference
    }
    CardBuilder other = (CardBuilder) obj; // Cast to the correct class
    // Compare fields for equality
    return rank.equals(other.rank) && suit.equals(other.suit);
  }

  /**
   * Generates a hash code for this card.
   * @return the hash code
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + rank.hashCode();
    result = 31 * result + suit.hashCode();

    return result;
  }

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   *
   * @return the formatted card
   */
  @Override
  public String toString() {
    return this.rank.getRank() + this.suit.getSuit();
  }

  /**
   * Gets the suit of this card.
   *
   * @return the suit of the card
   */
  @Override
  public String getSuit() {
    return this.suit.getSuit();
  }

  /**
   * Gets the rank of this card.
   *
   * @return the rank of the card
   */
  public String getRank() {
    return this.rank.getRank();
  }

  /**
   * Converts the rank of this card to a number.
   * @return integer representing card rank, with A being 1 and K being 13
   */
  public Integer rankToNumber() {
    if (this.getRank().equals("A")) {
      return 1;
    }
    if (this.getRank().equals("2")) {
      return 2;
    }
    if (this.getRank().equals("3")) {
      return 3;
    }
    if (this.getRank().equals("4")) {
      return 4;
    }
    if (this.getRank().equals("5")) {
      return 5;
    }
    if (this.getRank().equals("6")) {
      return 6;
    }
    if (this.getRank().equals("7")) {
      return 7;
    }
    if (this.getRank().equals("8")) {
      return 8;
    }
    if (this.getRank().equals("9")) {
      return 9;
    }
    if (this.getRank().equals("10")) {
      return 10;
    }
    if (this.getRank().equals("J")) {
      return 11;
    }
    if (this.getRank().equals("Q")) {
      return 12;
    }
    if (this.getRank().equals("K")) {
      return 13;
    }
    return null;
  }

  /**
   * Checks if this card is the same color as another card.
   *
   * @param c the card to compare color with
   * @return {@code true} if this card and the given c have the same color, {@code false} otherwise
   * @throws IllegalArgumentException if the card has an invalid suit type
   */
  @Override
  public Boolean sameColor(Card c) {
    if (this.getSuit().equals("♣") || this.getSuit().equals("♠")) {
      return c.getSuit().equals("♣") || c.getSuit().equals("♠");
    }
    if (this.getSuit().equals("♡") || this.getSuit().equals("♢")) {
      return c.getSuit().equals("♡") || c.getSuit().equals("♢");
    }
    throw new IllegalArgumentException("Invalid suit type");
  }

  /**
   * Checks if this card is a rank higher of the given card.
   *
   * @param c the card to compare ranks with
   * @return {@code true} if this card is a rank neighbor of the given card, {@code false} otherwise
   * @throws IllegalArgumentException if the ranks of the cards are invalid
   */
  @Override
  public boolean rankHigher(Card c) {
    return this.rankToNumber() == c.rankToNumber() + 1;
  }

  /**
   * sees if this card is face up/visible.
   *
   * @return {@code true} if the card's faceUp value is true, {@code false} otherwise
   */
  public boolean isCardFacedUp() {
    return this.faceUp;
  }

  /**
   * Turns this card face up.
   */
  @Override
  public void turnCardUp() {
    this.faceUp = true;
  }
}
