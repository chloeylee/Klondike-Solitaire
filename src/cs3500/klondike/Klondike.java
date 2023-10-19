package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * represents all playable variants of klondike.
 */
public class Klondike {

  /**
   * entry point for the klondike program.
   *
   * @param args the inputted arguments
   * @throws IllegalArgumentException if any invalid input is given
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No game type given");
    }
    switch (args[0]) {
      case "basic":
        try {
          if (args.length == 1) {
            startDefaultGameType(GameType.BASIC);
            initController().playGame(KlondikeCreator.create(GameType.BASIC),
                    KlondikeCreator.create(GameType.BASIC).getDeck(), true, 7, 3);
          } else if (args.length == 3) {
            KlondikeCreator.create(GameType.BASIC).startGame(KlondikeCreator.create(GameType.BASIC)
                            .getDeck(), true, Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));
            initController().playGame(KlondikeCreator.create(GameType.BASIC),
                    KlondikeCreator.create(GameType.BASIC).getDeck(), true,
                    Integer.parseInt(args[1]), Integer.parseInt(args[2]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("invalid number of inputs for custom basic game");
          throw new IllegalArgumentException("invalid number of inputs for custom basic game");
        }
        break;
      case "limited":
        try {
          if (args.length == 2) {
            KlondikeCreator.create(GameType.LIMITED, Integer.parseInt(args[1]))
                    .startGame(KlondikeCreator.create(GameType.LIMITED, Integer.parseInt(args[1]))
                                    .getDeck(), true, 7, Integer.parseInt(args[1]));
            initController().playGame(KlondikeCreator.create(GameType.LIMITED),
                    KlondikeCreator.create(GameType.LIMITED).getDeck(), true, 7, 3);
          } else if (args.length == 4) {
            KlondikeModel customLimited = new LimitedDrawKlondike(Integer.parseInt(args[1]));
            customLimited.startGame(KlondikeCreator.create(GameType.LIMITED).getDeck(), true,
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            initController().playGame(customLimited, KlondikeCreator.create(GameType.LIMITED)
                            .getDeck(), true, Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("invalid number of inputs for custom limited discard game");

          throw new IllegalArgumentException("invalid number of inputs for custom limited game");
        }
        break;
      case "whitehead":
        try {
          if (args.length == 1) {
            startDefaultGameType(GameType.WHITEHEAD);
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true, 7, 3);
          } else if (args.length == 2) {
            KlondikeCreator.create(GameType.WHITEHEAD).startGame(KlondikeCreator.create(GameType
                    .WHITEHEAD).getDeck(), true, Integer.parseInt(args[1]), 3);
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true,
                    Integer.parseInt(args[1]), 3);
          } else if (args.length == 3) {
            KlondikeCreator.create(GameType.WHITEHEAD).startGame(KlondikeCreator.create(GameType
                            .WHITEHEAD).getDeck(), true, Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true,
                    Integer.parseInt(args[1]), Integer.parseInt(args[2]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("invalid number of inputs for custom whitehead game");
          throw new IllegalArgumentException("invalid number of inputs for custom whitehead game");
        }

        break;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }

  /**
   * Initializes the controller for the game.
   *
   * @return the controller for the game
   */
  static KlondikeController initController() {
    Readable r = new InputStreamReader(System.in);
    Appendable a = System.out;
    KlondikeController controller = new KlondikeTextualController(r, a);

    return controller;
  }

  /**
   * Creates and starts a default game of the given type.
   *
   * @param type the type of game to create
   */
  static void startDefaultGameType(GameType type) {
    KlondikeCreator.create(type).startGame(KlondikeCreator.create(type).getDeck(), true,
            7, 3);
  }
}
