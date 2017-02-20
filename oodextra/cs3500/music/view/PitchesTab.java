package cs3500.music.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;

/**
 * Represents the pitch panel of the music editor in {@link GuiView}.
 * This is the most left panel of the music editor representing pitches in a music.
 */
public final class PitchesTab extends JPanel {
  private Piece pe;
  private static final int NOTE_SIZE = 25;

  /**
   * Constructs the pitch panel for the music editor.
   *
   * @param pe a piece of music.
   */
  public PitchesTab(Piece pe) {
    this.pe = pe;
    this.setPreferredSize(new Dimension(60, (pe.highestPitch().asInt() - pe.lowestPitch().asInt()
            + 1) * NOTE_SIZE));
  }

  /**
   * Paints all the components of this PitchesTab.
   *
   * @param g graphics canvas to be drawn to.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setOpaque(false);
    this.drawsPitches(g);
  }

  /**
   * Package-private method to update the piece of music.
   *
   * @param p piece of music {@link Piece}
   */
  void updatePiece(Piece p) {
    this.pe = p;
  }

  /**
   * Draws the pitches of the piece of music onto this panel.
   *
   * @param g graphics canvas to be drawn to.
   */
  private void drawsPitches(Graphics g) {
    ArrayList<Pitch> lop = pe.lowestPitch().range(pe.highestPitch());
    Collections.reverse(lop);
    g.setFont(new Font("Helvetica", Font.BOLD, 13));
    for (int i = 0; i < lop.size(); i++) {
      g.drawString(lop.get(i).toString(), 10, 20 + (i * NOTE_SIZE));
    }

  }

  public void setPiece(Piece piece) {
    this.pe = piece;
  }
}
