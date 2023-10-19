package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A textual controller for the Klondike game.
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private final Readable r;
  private final Appendable a;
  private boolean didPlayerQuit;

  /**
   * Constructs a KlondikeTextualController with the specified readable and appendable.
   *
   * @param r The readable to be used.
   * @param a The appendable to be used.
   * @throws IllegalArgumentException if the readable or appendable is null
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Readable and Appendable cannot be null");
    }
    this.r = r;
    this.a = a;
  }

  /**
   * The primary method for beginning and playing a game.
   *
   * @param model    The game of solitaire to be played
   * @param deck     The deck of cards to be used
   * @param shuffle  Whether to shuffle the deck or not
   * @param numPiles How many piles should be in the initial deal
   * @param numDraw  How many draw cards should be visible
   * @throws IllegalArgumentException if any of the parameters is null or any appending fails
   * @throws IllegalStateException    if the game cannot be started or if the controller cannot
   *                                  interact with the player or the move is invalid
   * @throws IllegalStateException    if rendering or message appending fails
   */
  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw) {
    // check for all exceptions
    if (model == null || deck == null) {
      throw new IllegalArgumentException("Model or deck cannot be null");
    }
    if (deck.isEmpty()) {
      throw new IllegalStateException("Deck cannot be empty");
    }
    if (numPiles < 1) {
      throw new IllegalArgumentException("Invalid numPiles value");
    }
    if (numDraw < 1) {
      throw new IllegalArgumentException("Invalid numDraw value");
    }

    // need to start the game before playing it
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Failed to start game");
    }

    // render the view / start the board
    KlondikeTextualView view = new KlondikeTextualView(model, this.a);
    try {
      view.render();
    } catch (IOException s) {
      throw new IllegalStateException("Could not render game");
    }

    // check the readable inputs
    Scanner scanner = new Scanner(this.r);
    while (!model.isGameOver() && !this.didPlayerQuit && scanner.hasNext()) {
      String input = scanner.next();
      switch (input) {
        case "mpp":

          // Extract move parameters
          if (scanner.hasNextInt()) {
            int src = scanner.nextInt() - 1;
            if (scanner.hasNextInt()) {
              int cards = scanner.nextInt();
              if (scanner.hasNextInt()) {
                int dest = scanner.nextInt() - 1;
                // Try to perform the move
                try {
                  model.movePile(src, cards, dest);
                  try {
                    view.render();
                  } catch (IOException io) {
                    throw new RuntimeException("Failed to render game");
                  }
                } catch (IllegalStateException e) {
                  // Handle invalid move
                  invalidInputTryCatch();
                  try {
                    view.render();
                  } catch (IOException io) {
                    throw new RuntimeException("Failed to render game");
                  }
                } catch (IllegalArgumentException e) {
                  // Handle invalid arguments
                  invalidInputTryCatch();
                  try {
                    view.render();
                  } catch (IOException io) {
                    throw new RuntimeException("Failed to render game");
                  }
                }
              }
            }
          }
          break;

        case "md":
          // Extract draw destination
          if (scanner.hasNextInt()) {
            int dest = scanner.nextInt() - 1;
            // Try to move a card from draw to cascade
            try {
              model.moveDraw(dest);
              try {
                view.render();
              } catch (IOException io) {
                throw new RuntimeException("Failed to render game");
              }
            } catch (IllegalStateException e) {
              invalidInputTryCatch();
              try {
                view.render();
              } catch (IOException io) {
                throw new RuntimeException("Failed to render game");
              }
            } catch (IllegalArgumentException e) {
              // Handle invalid arguments
              invalidInputTryCatch();
              try {
                view.render();
              } catch (IOException io) {
                throw new RuntimeException("Failed to render game");
              }
            }
          }
          break;

        case "mpf":
          if (scanner.hasNextInt()) {
            int src = scanner.nextInt() - 1;
            if (scanner.hasNextInt()) {
              int dest = scanner.nextInt() - 1;
              // Try to move a card to the foundation
              try {
                model.moveToFoundation(src, dest);
                try {
                  view.render();
                } catch (IOException io) {
                  throw new RuntimeException("Failed to render game");
                }
              } catch (IllegalStateException e) {
                invalidInputTryCatch();
                try {
                  view.render();
                } catch (IOException io) {
                  throw new RuntimeException("Failed to render game");
                }
              } catch (IllegalArgumentException e) {
                // Handle invalid arguments
                invalidInputTryCatch();
                try {
                  view.render();
                } catch (IOException io) {
                  throw new RuntimeException("Failed to render game");
                }
              }
            }
          }
          break;

        case "mdf":
          try {
            if (scanner.hasNextInt()) {
              int foundation = scanner.nextInt() - 1;
              // Try to move a draw card to foundation
              try {
                model.moveDrawToFoundation(foundation);
                try {
                  view.render();
                } catch (IOException io) {
                  throw new RuntimeException("Failed to render game");
                }
              } catch (IllegalStateException e) {
                invalidInputTryCatch();
                try {
                  view.render();
                } catch (IOException io) {
                  throw new RuntimeException("Failed to render game");
                }
              }
            }
          } catch (IllegalStateException e) {
            invalidInputTryCatch();
          }
          break;

        case "dd":
          try {
            model.discardDraw();
            try {
              view.render();
            } catch (IOException io) {
              throw new RuntimeException("Failed to render game");
            }
          } catch (IllegalStateException e) {
            invalidInputTryCatch();
            try {
              view.render();
            } catch (IOException io) {
              throw new RuntimeException("Failed to render game");
            }
          }
          break;

        case "q":
        case "Q":
          try {
            this.didPlayerQuit = true;
            quitGameMessageTryCatch();
            try {
              view.render();
            } catch (IOException io) {
              throw new RuntimeException("Failed to render game");
            }
            this.a.append("\nScore: ").append(String.valueOf(model.getScore()));
          } catch (IOException io) {
            throw new IllegalArgumentException("Failed to quit game");
          }
          break;

        default:
          invalidInputTryCatch();
          try {
            view.render();
          } catch (IOException io) {
            throw new RuntimeException("Failed to render game");
          }
      }
    }
    if (model.isGameOver()) {
      if (didPlayerWin(model)) {
        try {
          this.a.append("You win!");
          newLine();
          this.a.append("Score: ").append(String.valueOf(model.getScore()));
        } catch (IOException io) {
          throw new IllegalStateException("Failed to append win message");
        }
      } else {
        try {
          this.a.append("Game over. Score: ").append(String.valueOf(model.getScore()));
        } catch (IOException i) {
          throw new IllegalStateException("Error in score");
        }
      }
    }
  }

  /**
   * determines if the player has won or lost the game.
   *
   * @param model represents the current game model
   * @return true if the player has won (whole deck has been used or all piles are empty),
   *         false otherwise
   */
  private boolean didPlayerWin(KlondikeModel model) {
    if (!model.getDrawCards().isEmpty() && !model.getDeck().isEmpty()) {
      return false;
    }
    for (int pile = 0; pile < model.getNumPiles(); pile++) {
      if (model.getPileHeight(pile) != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Appends the invalid input message to the appendable.
   *
   * @throws IllegalStateException if the append fails
   */
  private void invalidInputTryCatch() {
    try {
      a.append("Invalid move. Play again.");
      newLine();
    } catch (IOException s) {
      throw new IllegalStateException("Could not append error message");
    }
  }

  /**
   * appends a new line to the appendable.
   *
   * @throws IllegalStateException if the append fails
   */
  private void newLine() {
    try {
      a.append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Could not append new line");
    }
  }

  /**
   * Appends the quit game message to the appendable.
   *
   * @throws IllegalStateException if the append fails
   */
  private void quitGameMessageTryCatch() {
    try {
      a.append("Game quit!");
      newLine();
      a.append("State of game when quit:");
      newLine();
    } catch (IOException s) {
      throw new IllegalStateException("Could not append quit message");
    }
  }

}
