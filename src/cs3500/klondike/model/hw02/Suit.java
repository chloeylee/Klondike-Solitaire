package cs3500.klondike.model.hw02;

/**
 * Represents the suit of a card.
 */
public enum Suit {
  CLUB("♣"),
  SPADE("♠"),
  HEART("♡"),
  DIAMOND("♢");

  private final String suit;

  Suit(String suit) {
    this.suit = suit;
  }

  /**
   * Gets the suit of this card.
   *
   * @return the suit of the card
   */
  public String getSuit() {
    return this.suit;
  }
}
