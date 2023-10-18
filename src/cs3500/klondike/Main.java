package cs3500.klondike;

import java.io.IOException;
import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * The main class for the Klondike game.
 */
public class Main {

  /**
   * Constructs a KlondikeTextualView with the specified model.
   * @param args the inputted arguments
   * @throws IOException if the appendable fails
   */
  public static void main(String[] args) throws IOException {
    // KlondikeModel model = new BasicKlondike();
    WhiteheadKlondike model = new WhiteheadKlondike();
    Readable r = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeController controller = new KlondikeTextualController(r, ap);

    controller.playGame(model, model.getDeck(), false, 7, 3);
  }
}
