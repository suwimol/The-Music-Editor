package cs3500.music.view;

import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing that the midi view makes the correct function calls.
 */
public class MidiTest {
  private IMusicEditorView<Piece> makeTestView(StringBuilder s) {
    return new MidiView(new MockSynthesizer(s));
  }

  @Test
  public void testPlayNoNotes() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "opened\nopened\nclose\n");
  }

  @Test
  public void testPlaySingleNote() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(0, 1, 13, 12, 1));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "opened\nopened\n[-100, 12, 1] 0\n[-116, 12, 1] 200000\n");
  }

  @Test
    public void testPlayManyNotes() throws InvalidMidiDataException, InterruptedException {
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(0, 1, 13, 12, 1));
    m.addNote(new Note(0, 0, 13, 18, 1));
    m.addNote(new Note(0, 1, 13, 20, 64));
    v.setPiece(m.toPiece());
    v.render();
    assertEquals(s.toString(), "opened\nopened\n[-100, 12, 1] 0\n[-116, 12, 1] 200000\n"
            + "[-100, 18, 1] 0\n[-116, 18, 1] 0\n"
            + "[-100, 20, 64] 0\n[-116, 20, 64] 200000\n");
  }

  @Test
  public void testPause() throws InvalidMidiDataException, InterruptedException {
    long start = System.nanoTime();
    StringBuilder s = new StringBuilder();
    IMusicEditorView<Piece> v = makeTestView(s);
    IMusicEditorModel<Note, Piece> m = new MusicEditorModel();
    m.addNote(new Note(0, 12, 13, 12, 1));
    v.setPiece(m.toPiece());
    v.render();
    assertTrue(System.nanoTime() - start > 11 * 200000);
  }
}
