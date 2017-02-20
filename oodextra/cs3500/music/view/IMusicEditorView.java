package cs3500.music.view;

import javax.sound.midi.InvalidMidiDataException;

/**
 * An interface which is common for both visual (GUI) {@link GuiView}  and midi views
 * {@link MidiView}.
 * <p></p>
 * Changes: Update method has been renamed as 'render' because 'update' is declared in IGuiView
 * interface.
 *
 */
public interface IMusicEditorView<P> {
  /**
   * Changes the piece of music that should be displayed.
   *
   * @param piece - The piece of music that should be displayed.
   */
  void setPiece(P piece);

  /**
   * Renders the display.
   */
  void render() throws InvalidMidiDataException, InterruptedException;

  /**
   * Transmit an error message to the view, in case
   * the command could not be processed correctly.
   *
   * @param error - a string that shows the error message
   */
  void showErrorMessage(String error);
}
