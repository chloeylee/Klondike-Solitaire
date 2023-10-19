package cs3500.klondike;

import java.util.Objects;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Represents a factory for creating Klondike games.
 */
public class KlondikeCreator {

  /**
   * Creates a Klondike game based on the given 1 parameter.
   * @param type the type of game to create
   * @return the created Klondike game
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        throw new IllegalArgumentException("Invalid game type for this constructor");
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }

  /**
   * Creates a Klondike game based on the 2 parameters.
   * @param type the type of game to create
   * @param numTimesRedrawAllowed the number of times the card can be redrawn
   * @return the created Klondike game
   */
  public static KlondikeModel create(GameType type, int numTimesRedrawAllowed) {
    if (Objects.requireNonNull(type) == GameType.LIMITED) {
      return new LimitedDrawKlondike(numTimesRedrawAllowed);
    }
    throw new IllegalArgumentException("Invalid game type for this constructor");
  }
}
