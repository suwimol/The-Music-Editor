package cs3500.music.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.RepeatSign;
import cs3500.music.model.SignType;

/**
 * Represents the interface for GUI view.
 * It factors out all the functionality or operation that are suitable for GUI view.
 */
public interface IGuiView<N, P> extends IMusicEditorView<P> {
  /**
   * Scrolls the frame to the right or left based on the key pressed.
   * @param right  a boolean value representing the scroll direction.
   */
  void scroll(boolean right);

  /**
   * Controls the behavior of jumping back to the beginning of the song.
   * This also means that the song will start from the beginning again.
   * Controlled by the home button key.
   */
  void jumpToBeginning();

  /**
   * Controls the behavior of jumping to the end of the song (last beat).
   * Controlled by the end button key.
   */
  void jumpToEnd();

  /**
   * Adds the mouse listener to the GUI view to allow GUI view to behave according to
   * the mouse event (action signalled by the mouse, done by the user).
   * @param m   a mouse listener object.
   */
  void addMouseListener(MouseListener m);

  /**
   * Adds the key listener to the GUI view to allow GUI view to behave according to the
   * key event (action signalled by the keyboard, done by the user).
   * @param k  a key listener object.
   */
  void addKeyListener(KeyListener k);

  /**
   * Removes the mouse listener in the GUI view to prevent GUI view from responding to the mouse
   * event.
   * @param m  a mouse listener object.
   */
  void removeMouseListener(MouseListener m);

  /**
   * Returns the note at specific mouse location.
   * Doesn't care about the piece stored in this view.
   *
   * @return a gibberish note representing the beat and pitch
   *         the mouse is currently hovering over
   */
  N noteAtLoc(Point point);

  RepeatSign signAtLoc(Point point);

  /**
   * Sets dummy note, a temporary note representing the currently selected note, in GUI view
   * to be the given note.
   * @param note  a Note of music
   */
  void setDummyNote(N note);

  /**
   * Sets the current beat of the GUI view.
   * @param beat  a natural number representing the current beat.
   */
  void setCurrBeat(int beat);

  /**
   * Updates the visual view. Repaints all the components in the frame.
   */
  void update();
}
