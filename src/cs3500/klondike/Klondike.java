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
          } else if (args.length == 2) {
            startGameWithTwoArguments(GameType.BASIC, args[1]);
            initController().playGame(KlondikeCreator.create(GameType.BASIC),
                    KlondikeCreator.create(GameType.BASIC).getDeck(), true,
                    checkValidInputType(args[1]), 3);
          } else if (args.length == 3) {
            startGameWithThreeArguments(GameType.BASIC, args[1], args[2]);
            initController().playGame(KlondikeCreator.create(GameType.BASIC),
                    KlondikeCreator.create(GameType.BASIC).getDeck(), true,
                    checkValidInputType(args[1]), checkValidInputType(args[2]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid input(s) for custom basic game");
          throw new IllegalArgumentException("Invalid input(s) for custom basic game");
        }
        break;
      case "limited":
        try {
          if (args.length == 2) {
            KlondikeCreator.create(GameType.LIMITED, checkValidInputType(args[1]))
                    .startGame(KlondikeCreator.create(GameType.LIMITED,
                                    checkValidInputType(args[1])).getDeck(), true, 7,
                            checkValidInputType(args[1]));
            initController().playGame(KlondikeCreator.create(GameType.LIMITED),
                    KlondikeCreator.create(GameType.LIMITED).getDeck(), true, 7, 3);
          } else if (args.length == 4) {
            KlondikeModel customLimited = new LimitedDrawKlondike(checkValidInputType(args[1]));
            customLimited.startGame(KlondikeCreator.create(GameType.LIMITED).getDeck(), true,
                    checkValidInputType(args[2]), checkValidInputType(args[3]));
            initController().playGame(customLimited, KlondikeCreator.create(GameType.LIMITED)
                            .getDeck(), true, checkValidInputType(args[2]),
                    checkValidInputType(args[3]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid input(s) for custom limited discard game");

          throw new IllegalArgumentException("Invalid input(s) for custom limited game");
        }
        break;
      case "whitehead":
        try {
          if (args.length == 1) {
            startDefaultGameType(GameType.WHITEHEAD);
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true, 7, 3);
          } else if (args.length == 2) {
            startGameWithTwoArguments(GameType.WHITEHEAD, args[1]);
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true,
                    checkValidInputType(args[1]), 3);
          } else if (args.length == 3) {
            startGameWithThreeArguments(GameType.WHITEHEAD, args[1], args[2]);
            initController().playGame(KlondikeCreator.create(GameType.WHITEHEAD),
                    KlondikeCreator.create(GameType.WHITEHEAD).getDeck(), true,
                    checkValidInputType(args[1]), checkValidInputType(args[2]));
          }
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid input(s) for custom whitehead game");
          throw new IllegalArgumentException("Invalid input(s) for custom whitehead game");
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

  /**
   * creates and starts a game when there are two arguments given.
   * @param type the type of game to create
   * @param piles the number of cascade piles to create
   */
  static void startGameWithTwoArguments(GameType type, String piles) {
    KlondikeCreator.create(type).startGame(KlondikeCreator.create(type).getDeck(), true,
            checkValidInputType(piles), 3);
  }

  /**
   * creates and starts a game when there are three arguments given.
   * @param type the type of game to create
   * @param piles the number of cascade piles to create
   * @param draws the number of draw cards in the draw pile
   */
  static void startGameWithThreeArguments(GameType type, String piles, String draws) {
    KlondikeCreator.create(type).startGame(KlondikeCreator.create(type).getDeck(), true,
            checkValidInputType(piles), checkValidInputType(draws));
  }

  /**
   * Checks if the input is a valid integer.
   *
   * @param arg the input to check
   * @return the input as an integer
   */
  static int checkValidInputType(String arg) {
    try {
      Integer.parseInt(arg);
    } catch (Exception e) {
      System.out.println("Invalid input type for create game params");
    }
    return Integer.parseInt(arg);
  }
}
