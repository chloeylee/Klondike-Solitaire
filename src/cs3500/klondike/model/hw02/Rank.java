package cs3500.klondike.model.hw02;

/**
 * Represents the rank of a card.
 */
public enum Rank {
  ACE("A"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8"),
  NINE("9"),
  TEN("10"),
  JACK("J"),
  QUEEN("Q"),
  KING("K");

  private final String rank;

  Rank(String rank) {
    this.rank = rank;
  }

  /**
   * Gets the rank of this card.
   *
   * @return the rank of the card
   */
  public String getRank() {
    return this.rank;
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
}
