package cs3500.music.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.RepeatSign;

/**
 * Represents a Graphic User Interface view (GUI) of the music editor.
 * This view consists of three panels: {@link BeatsTab}, {@link PitchesTab} and {@link Grids}.
 */
public class GuiView<P extends Piece> extends JFrame implements IGuiView<Note, P> {
  private P pe;
  private static final int NOTE_SIZE = 25;
  private final JFrame frame = new JFrame("The Music Editor");
  private final JPanel main = new JPanel(new BorderLayout());
  private JScrollPane scroll = new JScrollPane(main);
  private int currBeat;
  private Grids g;
  private BeatsTab bt;
  private PitchesTab pt;
  private Note dummyNote = null;

  /**
   * Constructs a Graphic User Interface view (GUI) of the music editor.
   */
  public GuiView() {
    // empty constructor.
  }

  @Override
  public void setPiece(P pe) {
    this.pe = pe;
    this.requestFocus();
  }

  @Override
  public void addMouseListener(MouseListener m) {
    super.addMouseListener(m);
    main.addMouseListener(m);

  }

  @Override
  public void addKeyListener(KeyListener m) {
    super.addKeyListener(m);
    this.requestFocus();
    main.addKeyListener(m);
    frame.addKeyListener(m);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    super.removeMouseListener(m);
    this.bt.removeMouseListener(m);
    this.pt.removeMouseListener(m);
  }

  @Override
  public void render() {
    if (this.pe == null) {
      throw new IllegalArgumentException("Music Piece cannot be null.");
    }

    if (this.pe.getAllNotes().isEmpty()) {
      this.showErrorMessage("Your music piece is empty. Try again.");
    } else {
      this.setInit();
    }
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(null, error);
    this.requestFocus();
  }

  /**
   * Intialized the Piece in the components.
   */
  void setPe() {
    this.g = new Grids(pe);
    this.bt = new BeatsTab(pe);
    this.pt = new PitchesTab(pe);
  }

  /**
   * Initializes this GUI view.
   */

  private void setInit() {

    this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 500);

    if (this.g != null) {
      assert (this.bt != null);
      assert (this.pt != null);
      this.main.remove(this.g);
      this.main.remove(this.bt);
      this.main.remove(this.pt);
    }

    setPe();

    this.main.add(g, BorderLayout.CENTER);
    this.main.add(bt, BorderLayout.NORTH);
    this.main.add(pt, BorderLayout.WEST);

    this.scroll.createHorizontalScrollBar();
    this.scroll.createVerticalScrollBar();
    this.scroll.getVerticalScrollBar().setUnitIncrement(10);
    this.scroll.getHorizontalScrollBar().setUnitIncrement(10);

    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.add(this.scroll);
    this.frame.pack();
    this.frame.setVisible(true);
  }

  @Override
  public void scroll(boolean right) {
    this.scroll.getHorizontalScrollBar().setValue(scroll.getHorizontalScrollBar().getValue() +
            ((right ? 1 : -1) * NOTE_SIZE));
    this.requestFocus();
  }

  @Override
  public void jumpToBeginning() {
    this.scroll.getHorizontalScrollBar().setValue(0);
    this.currBeat = 0;
    this.g.updateBeat(0);
  }

  @Override
  public void jumpToEnd() {
    this.scroll.getHorizontalScrollBar().setValue(this.pe.numBeats() * NOTE_SIZE);
    this.requestFocus();
  }

  @Override
  public Note noteAtLoc(Point point) {
    return g.locInfo(point.getX(), point.getY());
  }

  @Override
  public RepeatSign signAtLoc(Point point) {
    return g.locInfoSign(point.x);
  }

  @Override
  public void setDummyNote(Note note) {
    g.setDummyNote(note);
    dummyNote = note;
  }

  @Override
  public void setCurrBeat(int beat) {
    this.currBeat = beat;
    this.g.updateBeat(beat);
  }

  @Override
  public void update() {
    this.g.updatePiece(pe);
    this.bt.updatePiece(pe);
    this.pt.updatePiece(pe);
    this.frame.repaint();

    if (this.currBeat * NOTE_SIZE >=
            this.frame.getBounds().getSize().getWidth() - (NOTE_SIZE * 4)) {
      this.scroll.getHorizontalScrollBar().setValue(this.currBeat * NOTE_SIZE);
    }
  }
}
