package cs3500.klondike;

/**
 * represents all playable variants of klondike as enumerations.
 */
public enum GameType {
  BASIC("basic"),
  LIMITED("limited"),
  WHITEHEAD("whitehead");

  private final String gameType;

  /**
   * Constructs a GameType with the specified game type.
   * @param gameType the type of game
   */
  GameType(String gameType) {
    this.gameType = gameType;
  }

  /**
   * Gets the game type.
   * @return the game type
   */
  public String getGameType() {
    return this.gameType;
  }
}
