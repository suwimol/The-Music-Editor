package cs3500.music.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;
import cs3500.music.model.RepeatSign;
import cs3500.music.model.SignType;
import cs3500.music.view.CompositeView;

/**
 * Represents the controller of the music editor for Composite View {@link CompositeView}.
 * This controller only controls the behavior of the Composite View, it does not allow to work
 * with the other views.
 * <p></p>
 * The dummyNote and classes are package-private so that if need be other controllers that we might
 * make in the future would have an easier time utilizing the functionality of this class.
 * <p></p>
 * Change (HW9): Added new keys for adding and removing signs (repeat sign)
 */
public class MusicEditorControllerComp implements IMusicEditorController<Note, Piece> {
  Note dummyNote;
  RepeatSign dummySign;
  private CompositeView<Note, Piece> v;
  private IMusicEditorModel<Note, Piece> m;
  private boolean isAddingSign;

  /**
   * Constructs a music editor controller for Composite View.
   *
   * @param m a music editor model {@link IMusicEditorModel}
   * @param v a composite view  {@link CompositeView}
   */
  public MusicEditorControllerComp(IMusicEditorModel<Note, Piece> m,
                                   CompositeView<Note, Piece> v) {
    this.m = m;
    this.v = v;
  }

  @Override
  public void run() throws InvalidMidiDataException, InterruptedException {
    Map<Integer, Runnable> keyType = new HashMap<Integer, Runnable>();
    Map<Integer, Consumer<Point>> mouseClick = new HashMap<Integer, Consumer<Point>>();

    keyType.put(KeyEvent.VK_UP, new UpPitch());
    keyType.put(KeyEvent.VK_DOWN, new DownPitch());
    keyType.put(KeyEvent.VK_RIGHT, new RightBeat());
    keyType.put(KeyEvent.VK_LEFT, new LeftBeat());
    keyType.put(KeyEvent.VK_BACK_SPACE, new DelNote());
    keyType.put(KeyEvent.VK_PLUS, new AddNote());
    keyType.put(KeyEvent.VK_HOME, new GoStart());
    keyType.put(KeyEvent.VK_END, new GoEnd());
    keyType.put(KeyEvent.VK_SPACE, new PlayPause());
    keyType.put(KeyEvent.VK_ALT, new ChangeToSignMode());
    keyType.put(KeyEvent.VK_E, new AddEndSign());
    keyType.put(KeyEvent.VK_B, new AddBeginSign());
    keyType.put(KeyEvent.VK_D, new DelSign());
    mouseClick.put(1, new ClickDummyToggle());

    KeyboardHandler key = new KeyboardHandler(new HashMap<Integer, Runnable>(), keyType,
            new HashMap<Integer, Runnable>());
    MouseHandler mouse = new MouseHandler(new HashMap<Integer, Consumer<Point>>(), mouseClick,
            new HashMap<Integer, Consumer<Point>>(), new HashMap<Integer, Consumer<Point>>(),
            new HashMap<Integer, Consumer<Point>>());

    v.addKeyListener(key);
    v.addMouseListener(mouse);
    v.setPiece(m.toPiece());
    v.render();
  }


  /**
   * The following is a series arrow keys to edit the dummy note,
   * int start, int end, int instrument, int pitch, int volume
   * 'up' moves the dummy note up a pitch.
   */

  class UpPitch implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        dummyNote = new Note(new Pitch(dummyNote.getPitch().asInt() + 1), dummyNote.getStart(),
                dummyNote.getDuration());
        v.setDummyNote(dummyNote);
      }
    }
  }

  // 'down' moves the dummy note down a pitch
  class DownPitch implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        // dummy note location
        dummyNote = new Note(new Pitch(dummyNote.getPitch().asInt() - 1), dummyNote.getStart(),
                dummyNote.getDuration());
        v.setDummyNote(dummyNote);
      }
    }
  }

  // 'right' extends the dummy note to the right or scrolls to right
  class RightBeat implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        dummyNote = new Note(dummyNote.getPitch(), dummyNote.getStart(), dummyNote.getDuration() + 1);
        v.setDummyNote(dummyNote);
      } else {
        v.scroll(true);
      }
    }
  }

  // 'left' moves the dummy note to the left or scrolls to left
  class LeftBeat implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        try {
          dummyNote = new Note(dummyNote.getPitch(), dummyNote.getStart() - 1,
                  dummyNote.getDuration() + 1);
          v.setDummyNote(dummyNote);
        } catch (IllegalArgumentException e) {
          v.showErrorMessage("Note at start already");
        }
      } else {
        v.scroll(false);
      }
    }
  }

  // '\b' deletes all notes starting where the dummy note starts
  class DelNote implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        int startBeat = dummyNote.getStart();
        Pitch p = dummyNote.getPitch();
        List<Note> piece = m.toPiece().getAllNotes();
        for (Note n : piece) {
          if (p.equals(n.getPitch()) && startBeat == n.getStart()) {
            m.removeNote(n);
          }
        }
        dummyNote = null;
        v.setDummyNote(null);
        v.setPiece(m.toPiece());
        v.update();
      }
    }
  }

  // '+' adds the dummy note to the model
  class AddNote implements Runnable {
    @Override
    public void run() {
      if (dummyNote != null) {
        m.addNote(dummyNote);
        dummyNote = null;
        v.setDummyNote(null);
        v.setPiece(m.toPiece());
        v.update();
      }
    }

  }


  // Press Shift + E to add the end repeat sign.
  class AddEndSign implements Runnable {
    @Override
    public void run() {
      if (isAddingSign == true) {
        System.out.println("Inside addEndSign...");
        if (dummySign != null) {
          if (m.toPiece().isAbleToAddSign(dummySign)) {
            dummySign.updateType(SignType.END_SIGN);
            m.addRepeatSign(dummySign);
            dummySign = null;
            v.setPiece(m.toPiece());
            v.update();
          }
        }
      }
    }
  }

  // Press Shift + B to add the begin repeat sign.
  class AddBeginSign implements Runnable {

    @Override
    public void run() {
      if (isAddingSign == true) {
        if (dummySign != null) {
          if (m.toPiece().isAbleToAddSign(dummySign)) {
            System.out.println("Inside addBeginSign...");
            dummySign.updateType(SignType.START_SIGN);
            m.addRepeatSign(dummySign);
            dummySign = null;
            v.setPiece(m.toPiece());
            v.update();
          }
        }
      }
    }
  }

  // Press Shift + D deletes the repeat sign where the dummy sign is at.
  class DelSign implements Runnable {
    @Override
    public void run() {
      if (isAddingSign == true) {
        if (dummySign != null) {
          if (m.toPiece().getMap().containsKey(dummySign.getBeat())) {
            System.out.println("Contains key!!!");
            m.removeRepeatSignAt(dummySign.getBeat());
          }
          dummySign = null;
          v.setPiece(m.toPiece());
          v.update();
        }
      }
    }
  }

  // In order to add repeat sign, one needs to change the initial note mode to sign mode
  // by pressing ALT key. Changing it back to note mode simply press ALT again.
  class ChangeToSignMode implements Runnable {
    @Override
    public void run() {
      if (!isAddingSign) {
        System.out.println("Inside Change to sign mode...");
        isAddingSign = true;
      } else {
        isAddingSign = false;
      }

    }
  }

  // '+' adds the sign
  class AddSign implements Runnable {

    @Override
    public void run() {
      isAddingSign = true;
      m.addRepeatSign(new RepeatSign(SignType.END_SIGN, 1));
    }
  }

  // 'home' brings to the start
  class GoStart implements Runnable {
    @Override
    public void run() {
      v.jumpToBeginning();
    }
  }

  // 'end' brings to the end
  class GoEnd implements Runnable {
    @Override
    public void run() {
      v.jumpToEnd();
    }
  }

  // click sets the dummy note/sign if there isn't a dummy note/sign and clicking again adds it
  // if the music is playing, do not allow the user to click.
  // For dummySign, the user is not allowed to add sign anywhere before the last end sign.
  class ClickDummyToggle implements Consumer<Point> {
    @Override
    public void accept(Point point) {
      if (!v.isPlaying()) {
        if (!isAddingSign) {
          if (dummyNote == null) {
            dummyNote = v.noteAtLoc(point);
            v.setDummyNote(dummyNote);
          } else {
            // if the dummynote is not null, clicking it again changes it to null.
            dummyNote = null;
            v.setDummyNote(null);
          }
        } else {
          System.out.println("Inside accept");
          if (dummySign == null) {
            dummySign = v.signAtLoc(point);
          } else {
            dummySign = null;
          }
        }
      }
    }
  }

  // 'space' plays or pauses the piece
  class PlayPause implements Runnable {
    @Override
    public void run() {
      if (v.isPlaying()) {
        v.pause();
      } else {
        try {
          v.resume();
        } catch (InvalidMidiDataException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
