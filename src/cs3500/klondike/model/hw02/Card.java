package cs3500.klondike.model.hw02;

/**
 * Represents a card in a standard 52-card deck.
 */
public interface Card {

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   *
   * @return the formatted card
   */
  String toString();

  /**
   * Gets the suit of this card.
   *
   * @return the suit of the card
   */
  String getSuit();

  /**
   * Checks if this card is the same color as another card.
   *
   * @param c the card to compare color with
   * @return {@code true} if this card and the c have the same color, {@code false} otherwise
   * @throws IllegalArgumentException if the card has an invalid suit type
   */
  Boolean sameColor(Card c);

  /**
   * Gets the rank of this card.
   *
   * @return the rank of the card
   */
  String getRank();

  /**
   * converts the card's rank to a numerical value.
   * @return the numerical value of the card's rank, with Ace being 1 and King being 13
   */
  Integer rankToNumber();

  /**
   * Checks if this card is a rank higher of the given card.
   *
   * @param c the card to compare ranks with
   * @return {@code true} if this card is a rank neighbor of the given card, {@code false} otherwise
   * @throws IllegalArgumentException if the ranks of the cards are invalid
   */
  boolean rankHigher(Card c);

  /**
   * sees if this card is face up/visible.
   * @return {@code true} if the card's faceUp value is true, {@code false} otherwise
   */
  boolean isCardFacedUp();

  /**
   * Turns this card face up.
   */
  void turnCardUp();

}
