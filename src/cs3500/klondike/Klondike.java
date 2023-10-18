package cs3500.klondike;

import java.io.IOException;
import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * represents all playable variants of klondike.
 */
public class Klondike {

  /**
   * entry point for the klondike program.
   * @param args the inputted arguments
   * @throws IllegalArgumentException if any invalid input is given
   */
  public static void main(String[] args) {
    GameType gameType;
    int numPiles = 0;
    int defaultPiles = 7;
    int numDraws = 0;
    int defaultDraws = 3;
    int maxReuse = 0;
    if (args.length == 0) {
      gameType = GameType.BASIC;
      numPiles = defaultPiles;
      numDraws = defaultDraws;
    } else if (args[0].equals("basic")) {
      gameType = GameType.BASIC;
      if (args.length == 3) {
        numPiles = Integer.parseInt(args[1]);
        numDraws = Integer.parseInt(args[2]);
      } else if (args.length != 1) {
        printGameUsage();
        throw new IllegalArgumentException("Invalid argument for basic type.");
      }
    } else if (args[0].equals("limited") && args.length == 4) {
      gameType = GameType.LIMITED;
      maxReuse = Integer.parseInt(args[1]);
      numPiles = Integer.parseInt(args[2]);
      numDraws = Integer.parseInt(args[3]);
    } else if (args[0].equals("whitehead")) {
      gameType = GameType.WHITEHEAD;
      if (args.length == 2) {
        numPiles = Integer.parseInt(args[2]);
        numDraws = defaultDraws;
      } else if (args.length == 3) {
        numPiles = Integer.parseInt(args[2]);
        numDraws = defaultDraws;
      } else {
        printGameUsage();
        throw new IllegalArgumentException("Invalid argument for whitehead type.");
      }
    } else {
      printGameUsage();
      throw new IllegalArgumentException("Invalid argument.");
    }

    KlondikeModel model = new BasicKlondike();
    Readable r = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeController controller = new KlondikeTextualController(r, ap);

    controller.playGame(model, model.getDeck(), false, 7, 3);
  }

  /**
   * Prints the usage of the game.
   */
  private static void printGameUsage() {
    System.out.print("Invalid command line arguments.\n");
    System.out.print("basic produces a basic game of solitaire with default number of cascade"
            + " piles and visible draw cards\n");
    System.out.print("basic 7 3 achieves the same thing, but explicitly sets the size of the"
            + " game.\n");
    System.out.print("limited 3 6 2 produces a game of limited-draw solitaire, whose draw pile"
            + " can be used 3 times, with six cascade piles and 2 visible draw cards\n");
    System.out.print("whitehead 8 produces a Whitehead Klondike game with 8 cascade piles and"
            + " the default number of visible draw cards\n");
    System.out.print("whitehead 7 8 produces a Whitehead Klondike game with 7 cascade piles and"
            + " 8 visible draw cards\n");
  }
}
