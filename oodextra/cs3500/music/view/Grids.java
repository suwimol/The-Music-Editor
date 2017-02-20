package cs3500.music.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;
import cs3500.music.model.RepeatSign;
import cs3500.music.model.SignType;

/**
 * Represents the grids and notes panel of the music editor in {@link GuiView}
 * This is located at the center; under the {@link BeatsTab} of the music editor.
 * <p></p>
 * Changes: fields currBeat and dummyNote have been added. The field currBeat is to keep track
 * of the current beat in the music piece. The field dummy note is keep track of the behavior
 * of the dummy note (a currently selected note).
 */
public final class Grids extends JPanel {
  private Piece pe;
  private static final int NOTE_SIZE = 25;
  private static final int START = 1;
  private int currBeat;
  private Note dummyNote = null;
  private RepeatSign dummySign = null;

  /**
   * Constructs the grid panel for the music editor.
   *
   * @param pe a piece of music.
   */
  public Grids(Piece pe) {
    this.pe = pe;
    this.setPreferredSize(new Dimension((this.pe.numBeats() * NOTE_SIZE) + NOTE_SIZE, NOTE_SIZE +
            (pe.highestPitch().asInt() - pe.lowestPitch().asInt() + 1) * NOTE_SIZE));
  }

  void setPiece(Piece p) {
    this.pe = p;
  }

  void updateBeat(int beat) {
    this.currBeat = beat;
    if (this.currBeat >= 0) {
      repaint();
    }
  }

  void updatePiece(Piece p) {
    this.pe = p;
  }

  /**
   * Paints all components of this Grid (notes and grids).
   *
   * @param g graphics canvas to be drawn to.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setOpaque(false);
    this.drawsNotes(g);
    this.drawsDummy(g);
    this.drawsGrids(g);
    if (this.pe.getMap() != null) {
      this.drawSigns(g);
    }
    if (this.currBeat >= 0) {
      this.drawsLine(g, this.currBeat);
    }
    this.repaint();
  }

  private void drawSigns(Graphics g) {
    super.paintComponent(g);

    int beats = this.pe.numBeats();

    for (int i = 0; i <= beats; i++) {
      if (this.pe.getMap().containsKey(i)) {
        RepeatSign sign = (RepeatSign) this.pe.getMap().get(i);
        this.drawSingleSign(sign, g);
      }
    }
  }

  /**
   * Draws single sign based on the type of sign. End sign is drawn in BLUE.
   * Start sign is drawn in PURPLE.
   * @param sign  a Repeat Sign.
   * @param g graphics canvas to be drawn to.
   */
  private void drawSingleSign(RepeatSign sign, Graphics g) {
    ArrayList<Pitch> lop = pe.lowestPitch().range(pe.highestPitch());
    Graphics2D g1 = (Graphics2D) g;

    if (sign.getType() == SignType.END_SIGN) {
      g1.setColor(Color.BLUE);
    } else {
      g.setColor(new Color(186, 111, 242));
    }

    g1.setStroke(new BasicStroke(4));
    g1.drawLine(START + (NOTE_SIZE * sign.getBeat()), START, START + NOTE_SIZE * sign.getBeat(),
            NOTE_SIZE * lop.size());
  }

  /**
   * Draws the grids for the music editor.
   *
   * @param g graphics canvas to be drawn to.
   */
  private void drawsGrids(Graphics g) {
    Graphics2D g1 = (Graphics2D) g;
    g1.setColor(Color.BLACK);
    g1.setStroke(new BasicStroke(2));

    ArrayList<Pitch> lop = pe.lowestPitch().range(pe.highestPitch());

    // draws horizontal lines
    for (int i = 0; i < lop.size() + 1; i++) {
      g1.drawLine(START, START + (NOTE_SIZE * i), START + this.pe.numBeats() *
              NOTE_SIZE, START + (NOTE_SIZE * i));
    }

    // draws vertical lines
    for (int i = 0; i <= this.pe.numBeats(); i += 4) {
      g1.drawLine(START + (NOTE_SIZE * i), START, START + (NOTE_SIZE * i),
              (NOTE_SIZE * lop.size()));
    }

    g1.drawLine(START + (NOTE_SIZE * this.pe.numBeats()), START, START +
                    (NOTE_SIZE * this.pe.numBeats()), NOTE_SIZE * lop.size());

  }

  /**
   * Draws all the notes of a piece of music.
   *
   * @param g graphics canvas to be drawn to.
   */
  private void drawsNotes(Graphics g) {
    super.paintComponent(g);
    List<Note> notes = this.pe.getAllNotes();
    for (Note n : notes) {
      this.drawsSingleNote(n, g);
    }

  }

  /**
   * Draws the red line informing the user the notes at current beat being played.
   *
   * @param g        graphics canvas to be drawn to.
   * @param currBeat a natural number representing the current beat being played.
   */
  private void drawsLine(Graphics g, int currBeat) {
    ArrayList<Pitch> lop = pe.lowestPitch().range(pe.highestPitch());
    Graphics2D g1 = (Graphics2D) g;

    g1.setStroke(new BasicStroke(4));
    g1.setColor(Color.RED);
    g1.drawLine(START + (NOTE_SIZE * currBeat), START, START + NOTE_SIZE * currBeat,
            NOTE_SIZE * lop.size());

  }

  /**
   * Draws a single note at its start time with its duration onto this grid panel.
   *
   * @param n a note of music {@link Note}
   * @param g graphics canvas to be drawn to.
   */
  private void drawsSingleNote(Note n, Graphics g) {

    // draws head
    g.setColor(Color.BLACK);
    g.fillRect(START + (n.getStart() * NOTE_SIZE), (this.pe.highestPitch().asInt()
            - n.getPitch().asInt()) * NOTE_SIZE, NOTE_SIZE, NOTE_SIZE);

    // draws durations
    g.setColor(new Color(67, 242, 158));
    g.fillRect(START + (n.getStart() + 1) * NOTE_SIZE, (this.pe.highestPitch().asInt()
            - n.getPitch().asInt()) * NOTE_SIZE, (n.getDuration() - 1) * NOTE_SIZE, NOTE_SIZE);

  }

  private void drawsDummy(Graphics g) {
    // draws dummy notes
    if (dummyNote != null && !this.pe.getAllNotes().contains(dummyNote)) {

      ArrayList<Pitch> lop = pe.lowestPitch().range(pe.highestPitch());
      Collections.reverse(lop);
      int xd = dummyNote.getStart() * NOTE_SIZE;
      int yd = lop.indexOf(dummyNote.getPitch()) * NOTE_SIZE;

      // draw note's head when clicked.
      g.setColor(new Color(71, 0, 242));
      g.fillRect(START + (dummyNote.getStart() * NOTE_SIZE), (this.pe.highestPitch().asInt()
              - dummyNote.getPitch().asInt()) * NOTE_SIZE, NOTE_SIZE, NOTE_SIZE);

      // draw note's duration when right arrow key is pressed once.
      g.setColor(new Color(145, 234, 242));
      g.fillRect(START + (dummyNote.getStart() + 1) * NOTE_SIZE, (this.pe.highestPitch().asInt()
              - dummyNote.getPitch().asInt()) * NOTE_SIZE,
              (dummyNote.getDuration() - 1) * NOTE_SIZE, NOTE_SIZE);

    }
  }

  /**
   * Returns a Note at the given x and y coordinates relative to the windows frame.
   *
   * @param x x coordinate relative to the windows frame.
   * @param y y coordinate relative to the windows frame.
   * @return a Note at the given x and y coordinates relative to the windows frame.
   */
  Note locInfo(double x, double y) {
    x = (x - START) / NOTE_SIZE;
    y = pe.highestPitch().asInt() - (y / NOTE_SIZE);
    int pitch = (int) Math.ceil(y) + 1;
    int startBeat = (int) (x - 2.5);

    return new Note(new Pitch(pitch), startBeat, 1);
  }

  /**
   * Returns a Repeat Sign at the given x coordinate relative to the windows frame.
   * @param x x coordinate relative to the windows frame.
   * @return a repeat sign at the given x coordinate relative to the windows frame.
   */
  RepeatSign locInfoSign(double x) {
    x = (x - START) / NOTE_SIZE;
    int startBeat = (int) (x - 2);

    return new RepeatSign(SignType.END_SIGN, startBeat);
  }

  void setDummyNote(Note note) {
    this.dummyNote = note;
  }

  void setDummySign(RepeatSign sign) { this.dummySign = sign; }
}
