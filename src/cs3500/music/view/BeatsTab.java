package cs3500.music.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import cs3500.music.model.Piece;

/**
 * Represents the beat panel of the music editor in {@link GuiView}
 * This is the top panel of the music editor representing the beats of a music.
 */
public final class BeatsTab extends JPanel {
  private Piece pe;
  private static final int NOTE_SIZE = 25;
  private static final int START = 60;

  /**
   * Constructs the beat panel for the music editor.
   *
   * @param pe a piece of music.
   */
  public BeatsTab(Piece pe) {
    this.pe = pe;
    this.setPreferredSize(new Dimension(this.pe.numBeats() * NOTE_SIZE, NOTE_SIZE));
  }

  /**
   * Paints this beat panel.
   *
   * @param g graphics canvas to be drawn to.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setOpaque(false);

    this.drawBeats(g);
  }

  void setPiece(Piece p) {
    this.pe = p;
  }

  void updatePiece(Piece p) {
    this.pe = p;
  }

  /**
   * Draws the beats of the piece of music onto this panel.
   *
   * @param g graphics canvas to be drawn to.
   */
  private void drawBeats(Graphics g) {
    int numBeat = this.pe.numBeats();
    g.setFont(new Font("Helvetica", Font.BOLD, 13));
    g.setColor(Color.BLACK);

    for (int i = 0; i <= numBeat; i = i + 16) {
      if (i == 0) {
        g.drawString("" + i, START + (NOTE_SIZE * i) - 1, (NOTE_SIZE / 2) + 2);
      } else if (i % 16 == 0 && (i != numBeat)) {
        g.drawString("" + i, START + (NOTE_SIZE * i) - 5, (NOTE_SIZE / 2) + 2);
      } else if (i == numBeat) {
        int length = (int) Math.log10(i) + 1;
        g.drawString("" + i, START + (NOTE_SIZE * i) - (length + 14), (NOTE_SIZE / 2) + 2);
      }
    }
  }
}
