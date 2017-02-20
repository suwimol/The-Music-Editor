package cs3500.music.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.Timer;

import cs3500.music.model.Piece;
import cs3500.music.model.RepeatSign;
import cs3500.music.model.SignType;


/**
 * Represents a composite view which is a combination of both GUI {@link GuiView} and Midi Views
 * {@link MidiView}. The composite view concurrently works with both GUI and Midi views.
 */
public class CompositeView<N, P extends Piece> implements SoundView<P>, IGuiView<N, P>,
        ActionListener {
  private final SoundView<P> sound;
  private final IGuiView<N, P> gui;
  private P pe;
  private boolean pauseFlag;
  private int currBeat = 0;
  private int currEnding;
  private N dummyNote;
  private javax.swing.Timer timer;

  /**
   * Constructs a Composite View of the music editor.
   *
   * @param sound an instance of sound view.
   * @param gui   an instance of GUI view.
   */
  public CompositeView(SoundView<P> sound, IGuiView<N, P> gui) {
    this.sound = sound;
    this.gui = gui;
    this.pauseFlag = true;
  }

  public void setDummyNote(N note) {
    this.dummyNote = note;
    gui.setDummyNote(note);
  }

  @Override
  public void setCurrBeat(int currBeat) {
    if (currBeat < 0) {
      throw new IllegalArgumentException("Current beat cannot be negative.");
    }
    this.currBeat = currBeat;
    this.sound.setCurrBeat(currBeat);
    this.gui.setCurrBeat(currBeat);

  }

  @Override
  public void playNoteAtBeat(int beat) throws InvalidMidiDataException, InterruptedException {
    this.sound.playNoteAtBeat(beat);
  }

  @Override
  public void setIsPlaying(boolean isPlaying) {
    this.sound.setIsPlaying(isPlaying);
  }

  @Override
  public void update() {
    this.setCurrBeat(currBeat);
    this.gui.update();
  }

  /**
   * Pauses the piece.
   */
  public void pause() {
    this.pauseFlag = true;
    this.setIsPlaying(false);
    this.timer.stop();
  }

  /**
   * Resumes the piece.
   *
   * @throws InvalidMidiDataException - If the midi fails.
   * @throws InterruptedException     - if it gets interrupted.
   */
  public void resume() throws InvalidMidiDataException, InterruptedException {
    this.pauseFlag = false;
    this.timer.start();
    this.setCurrBeat(currBeat);
  }

  @Override
  public boolean isPlaying() {
    return sound.isPlaying();
  }

  @Override
  public void scroll(boolean right) {
    this.gui.scroll(right);
  }

  @Override
  public void jumpToBeginning() {

    if (isPlaying()) {
      this.pause();
      this.setCurrBeat(0);
      this.gui.jumpToBeginning();
    } else {
      this.setCurrBeat(0);
      this.gui.jumpToBeginning();
    }
  }

  @Override
  public void jumpToEnd() {
    this.gui.jumpToEnd();
  }

  @Override
  public void addMouseListener(MouseListener m) {
    this.gui.addMouseListener(m);
  }

  @Override
  public void addKeyListener(KeyListener k) {
    this.gui.addKeyListener(k);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    this.gui.removeMouseListener(m);
  }

  @Override
  public N noteAtLoc(Point point) {
    return gui.noteAtLoc(point);
  }

  @Override
  public RepeatSign signAtLoc(Point point) {
    return this.gui.signAtLoc(point);
  }

  @Override
  public void setPiece(P piece) {
    this.pe = piece;
    this.gui.setPiece(pe);
    this.sound.setPiece(pe);

    int tempo = pe.getTempo();
    if (timer == null) {
      timer = new Timer((tempo / 1000), this);
    }
  }

  @Override
  public void render() throws InvalidMidiDataException, InterruptedException {
    this.gui.render();
  }

  @Override
  public void showErrorMessage(String error) {
    gui.showErrorMessage(error);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    this.timer.start();

    if (pe == null) {
      return;
    }

    if ((!this.pauseFlag) && this.currBeat != pe.numBeats()) {
      this.currBeat++;
      this.setCurrBeat(this.currBeat);

      // if it's > currEnding
      // is current beat an ending?
      //  if it is an ending that is less than the current ending, jump to beat of current ending
      //  if it is an ending that is greater than current ending, jump to "begin point"
      //  if it is equal to the current ending, do nothing and keep playing

      if (pe.getMap().containsKey(currBeat)) {
        RepeatSign currSign = (RepeatSign) pe.getMap().get(currBeat);
        RepeatSign currEnd = (RepeatSign) pe.getMap().get(currEnding);
        // check if the current sign is the end sign
        if (currSign.getType() == SignType.END_SIGN) {
          // check if this end has start sign right before this and next end right after this.
          // if so, this end is between a section (start and end).
          if (pe.isBeginRightBefore(currBeat) && pe.isEndRightAfter(currBeat) &&
                  currSign.getStatus() != 0) {
            // then do nothing to this end, just setting its status to 0, signifying passed end.
            // and update the current end to next end
            currSign.updateStatus(0);
            pe.isExistingNextEnd(currBeat);
            this.currEnding = pe.getNextEnd();
          } else if (pe.isBeginRightBefore(currBeat) && pe.isEndRightAfter(currBeat) &&
                  currSign.getStatus() == 0) {

            // jumps to the marked current ending.
            this.setCurrBeat(this.currEnding);
            // if it's just a normal end sign,
          } else if (!(pe.isBeginRightBefore(currBeat) && pe.isEndRightAfter(currBeat)) &&
                  currSign.getStatus() != 0) {
            // update current ending
            pe.isExistingNextEnd(currBeat);
            // set curr ending
            this.currEnding = this.currBeat;
            System.out.println(currEnding);

            pe.isExistingBeginningBefore(currBeat);
            // jumps to the last begin sign
            this.setCurrBeat(pe.getLastBegin());
            // and set this end to '0' signifying a passed end.
            currSign.updateStatus(0);
            // if the normal end status == 0,
          } else if (!(pe.isBeginRightBefore(currBeat) && pe.isEndRightAfter(currBeat)) &&
                  currSign.getStatus() == 0 && currBeat < currEnding) {
            this.setCurrBeat(this.currEnding);
          } // else do nothing.
        }
      }

      try {
        this.playNoteAtBeat(this.currBeat);
      } catch (InvalidMidiDataException | InterruptedException e1) {
        e1.printStackTrace();
      }

    } else if (this.currBeat == pe.numBeats()) {
      this.setCurrBeat(0);
      this.pauseFlag = true;
      this.gui.jumpToBeginning();
      this.timer.stop();
      this.sound.setIsPlaying(false);
    }
    this.update();
  }
}
