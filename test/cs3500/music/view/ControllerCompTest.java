package cs3500.music.view;

import org.junit.Test;

import java.awt.Point;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;

import static junit.framework.TestCase.assertEquals;

/**
 * A class for testing the controller.
 */
public class ControllerCompTest {
  /**
   * Since all the functionality of the model has been thoroughly tested the only new functionality
   * that the controller depends on is some added functionality in the view, in particular,
   * setDummyNote (which cannot be easily tested), pause, resume (which are tested in the MidiTest),
   * and noteAtLoc.  Since noteAtLoc is the only functionality the controller uses that hasn't been
   * tested, it will be tested here.
   */
  @Test
  public void testNoteAtLoc() {
    GuiView g = (GuiView) MusicEditorFactoryView.createView("visual");
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(new Pitch("D#2"), 0, 1));
    m.addNote(new Note(new Pitch("C_1"), 63, 1));
    g.setPiece(m.toPiece());
    g.setPe();
    Note n = g.noteAtLoc(new Point(1000, 120));
    assertEquals(n, new Note(new Pitch("C_2"), 37, 1));
  }
}
