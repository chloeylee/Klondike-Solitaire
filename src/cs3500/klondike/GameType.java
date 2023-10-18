package cs3500.klondike;

/**
 * represents all playable variants of klondike as enumerations.
 */
public enum GameType {
  BASIC("basic"),
  LIMITED("limited"),
  WHITEHEAD("whitehead");

  private final String gameType;

  GameType(String gameType) {
    this.gameType = gameType;
  }
  public String getGameType() {
    return this.gameType;
  }
}
