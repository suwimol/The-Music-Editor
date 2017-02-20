package cs3500.music.view;

import org.junit.Test;

import java.io.StringReader;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;

import static org.junit.Assert.assertEquals;

/**
 * A class for testing that the text view prints things correctly.
 */
public class TextTest {
  private IMusicEditorView<Piece> makeTestView(StringBuilder s) {
    return new TextView(s, new StringReader(""));
  }

  @Test
  public void testNoNotes() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "");
  }

  @Test
  public void testSingleNote() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(new Pitch("C_1"), 0, 1));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "   C1 \n0  X  \n");
  }

  @Test
  public void testRange() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(new Pitch("C_1"), 0, 1));
    m.addNote(new Note(new Pitch("G_1"), 0, 1));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "   C1  C#1   D1  D#1   E1   F1  F#1   G1 \n"
            + "0  X                                  X  \n");
  }

  @Test
  public void testBeats() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(new Pitch("C_1"), 0, 12));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "    C1 \n 0  X  \n 1  |  \n 2  |  \n 3  |  \n 4  |  \n 5  |  \n"
            + " 6  |  \n 7  |  \n 8  |  \n 9  |  \n10  |  \n11  |  \n");
  }

  @Test
  public void testOverWrite() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(new Pitch("C_1"), 0, 12));
    m.addNote(new Note(new Pitch("C_1"), 5, 1));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "    C1 \n 0  X  \n 1  |  \n 2  |  \n 3  |  \n 4  |  \n 5  X  \n"
            + " 6  |  \n 7  |  \n 8  |  \n 9  |  \n10  |  \n11  |  \n");
  }
}
