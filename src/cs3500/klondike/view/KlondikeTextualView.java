package cs3500.klondike.view;

import java.io.IOException;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private Appendable ap = System.out;

  /**
   * Constructs a KlondikeTextualView with the specified model.
   *
   * @param model The KlondikeModel to be rendered.
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
  }

  /**
   * Constructs a KlondikeTextualView with the specified model and appendable.
   *
   * @param model The KlondikeModel to be rendered.
   * @param ap    The appendable to be used.
   */
  public KlondikeTextualView(KlondikeModel model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  /**
   * Generates a string representing the draw cards in the Klondike game.
   *
   * @return A string representation of the draw cards.
   */
  public String printDrawCards() {
    String result = "";
    if (model.getDrawCards().isEmpty()) {
      return "Draw: ";
    }
    for (int drawPile = 0; drawPile < model.getNumDraw(); drawPile++) {
      if (drawPile == model.getNumDraw() - 1) {
        result = result + model.getDrawCards().get(drawPile).toString();
      }
      if (drawPile < model.getNumDraw() - 1) {
        result = result + model.getDrawCards().get(drawPile).toString() + ", ";
      }
    }
    return "Draw: " + result;
  }

  /**
   * Generates a string representing the score in the Klondike game.
   *
   * @return A string representation of the score.
   */
  public String printScore() {
    return "Score: " + model.getScore() + "/n";
  }

  /**
   * Generates a string representing the foundation piles in the Klondike game.
   *
   * @return A string representation of the foundation piles.
   */
  public String printFoundationPiles() {
    StringBuilder result = new StringBuilder();

    int numFoundations = model.getNumFoundations();

    for (int found = 0; found < numFoundations; found++) {

      if (model.getCardAt(found) != null) {
        result.append(model.getCardAt(found).toString());
      } else {
        result.append("<none>");
      }

      if (found < numFoundations - 1) {
        result.append(", ");
      }
    }
    return "Foundation: " + result.toString();
  }

  /**
   * Generates a string representing the cascading piles in the Klondike game.
   *
   * @return A string representation of the cascading piles.
   */
  public String printCascades() {
    String result = "";
    StringBuilder sb = new StringBuilder();

    int rowMax = 0;
    for (int row = 0; row < model.getNumRows(); row++) {
      if (rowMax < model.getPileHeight(row)) {
        rowMax = model.getPileHeight(row);
      }
    }
    for (int row = 0; row < rowMax; row++) {
      for (int col = 0; col < model.getNumPiles(); col++) {
        if (row == 0 && 0 == model.getPileHeight(col)) {
          sb.append(" X  ");
        } else if (row >= model.getPileHeight(col)) {
          sb.append("    ");
        } else if (!model.isCardVisible(col, row)) {
          sb.append(" ?  ");
        } else {
          sb.append(String.format("%-4s", model.getCardAt(col, row).toString()));
        }
      }
      sb.append("\n");
    }

    result = sb.toString();

    return result;
  }

  /**
   * Returns a string representation of the entire Klondike game, including draw cards,
   * foundation piles, and cascading piles.
   *
   * @return A string representation of the Klondike game.
   */
  @Override
  public String toString() {
    return this.printDrawCards() + "\n" + this.printFoundationPiles() + "\n" + this.printCascades();
  }

  /**
   * Renders the Klondike game.
   * @throws IOException if the append fails
   */
  @Override
  public void render() throws IOException {
    try {
      ap.append(this.toString());
    } catch (IOException e) {
      throw new IOException("Render failed");
    }
  }

}
