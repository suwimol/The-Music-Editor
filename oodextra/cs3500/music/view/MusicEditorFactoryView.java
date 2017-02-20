package cs3500.music.view;

import java.io.InputStreamReader;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import cs3500.music.model.Piece;

/**
 * Represents a factory view of the music editor.
 * This class allows the user to switch among the three views.
 * <p></p>
 * Changes:
 * Added a new if case to create a composite view.
 */
public class MusicEditorFactoryView {

  /**
   * Creates the specified view for the user.
   *
   * @param view one of the strings "console", "visual" or "midi".
   * @return an IMusicEditorView; "console" for TextView, "visual" for GuiView, "midi" for MidiView.
   */
  public static IMusicEditorView<Piece> createView(String view) {
    if (view.equals("console")) {
      return new TextView(System.out, new InputStreamReader(System.in));
    } else if (view.equals("visual")) {
      return new GuiView();
    } else if (view.equals("midi")) {
      try {
        return new MidiView(MidiSystem.getSynthesizer());
      } catch (MidiUnavailableException e) {
        throw new IllegalStateException("MIDI unavailable");
      }
    } else if (view.equals("composite")) {
      try {
        return new CompositeView<>(new MidiView(MidiSystem.getSynthesizer()), new GuiView());
      } catch (MidiUnavailableException e) {
        throw new IllegalStateException("MIDI unavailable");
      }
    } else {
      throw new IllegalArgumentException("Invalid view mode.");
    }
  }




}
