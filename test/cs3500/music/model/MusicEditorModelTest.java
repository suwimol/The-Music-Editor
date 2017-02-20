package cs3500.music.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the methods implemented in the MusicEditorModel.
 */
public class MusicEditorModelTest {
  private boolean listEqualsNoOrder(List<Note> a, List<Note> b) {
    return a.containsAll(b) && b.containsAll(a) && a.size() == b.size();
  }

  @Test
  public void toPieceNotEqualsTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    ArrayList<Note> notes = new ArrayList<>();
    Note n1 = new Note(0, 2, 1, 64, 72);
    Note n2 = new Note(0, 7, 1, 55, 70);
    Note n3 = new Note(2, 4, 1, 62, 72);
    notes.add(n3);
    notes.add(n1);
    notes.add(n2);
    Piece piece = new Piece(notes, 100);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);

    assertNotEquals(model.toPiece(), piece);
  }

  @Test
  public void addMidiNoteTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(0, 2, 1, 64, 72);
    Note n2 = new Note(0, 7, 1, 55, 70);
    Note n3 = new Note(2, 4, 1, 62, 72);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);

    ArrayList<Note> expected = new ArrayList<>();
    expected.add(n3);
    expected.add(n1);
    expected.add(n2);

    assertTrue(model.toPiece().getAllNotes().containsAll(expected));
  }

  @Test
  public void addNoteTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("C_3"), 2, 5);
    Note n2 = new Note(new Pitch("D#10"), 4, 6);
    Note n3 = new Note(new Pitch("A_3"), 8, 20);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);

    ArrayList<Note> expected = new ArrayList<>();
    expected.add(n2);
    expected.add(n1);
    expected.add(n3);

    assertTrue(listEqualsNoOrder(expected, model.toPiece().getAllNotes()));
  }

  @Test
  public void removeNoteTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("C_3"), 2, 5);
    Note n2 = new Note(new Pitch("D#10"), 4, 6);
    Note n3 = new Note(new Pitch("A_3"), 8, 20);
    Note n4 = new Note(new Pitch("F_5"), 8, 100);
    Note n5 = new Note(new Pitch("B_1"), 10, 2);

    List<Note> notes = new ArrayList<>();
    notes.add(n1);
    notes.add(n2);
    notes.add(n3);
    notes.add(n4);
    notes.add(n5);

    model.addNote(n2);
    model.addNote(n4);
    model.addNote(n1);
    model.addNote(n3);
    model.addNote(n5);

    assertTrue(listEqualsNoOrder(model.toPiece().getAllNotes(), notes));

    model.removeNote(n2);
    model.removeNote(n5);
    model.removeNote(n1);

    List<Note> notesAfterRemoved = new ArrayList<>();
    notesAfterRemoved.add(n3);
    notesAfterRemoved.add(n4);

    assertTrue(listEqualsNoOrder(notesAfterRemoved, model.toPiece().getAllNotes()));
  }

  @Test
  public void replaceNoteTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("C_3"), 2, 5);
    Note n2 = new Note(new Pitch("D#10"), 4, 6);
    Note n3 = new Note(new Pitch("A_3"), 8, 20);
    Note n4 = new Note(new Pitch("F_5"), 8, 100);
    Note n5 = new Note(new Pitch("B_1"), 10, 2);

    List<Note> notesAfterReplaced = new ArrayList<>();
    notesAfterReplaced.add(n3);
    notesAfterReplaced.add(n2);
    notesAfterReplaced.add(n4);
    notesAfterReplaced.add(n5);

    model.addNote(n1);
    model.addNote(n5);
    model.addNote(n2);
    model.addNote(n4);

    model.replaceNote(n1, n3);
    assertTrue(listEqualsNoOrder(model.toPiece().getAllNotes(), notesAfterReplaced));

    model.replaceNote(n5, new Note(new Pitch("G#3"), 5, 3));

    notesAfterReplaced.remove(n5);
    notesAfterReplaced.add(new Note(new Pitch("G#3"), 5, 3));
    assertTrue(listEqualsNoOrder(model.toPiece().getAllNotes(), notesAfterReplaced));
  }

  @Test
  public void replaceSameNoteTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("C_3"), 2, 5);
    Note n2 = new Note(new Pitch("D#10"), 4, 6);
    Note n3 = new Note(new Pitch("A_3"), 8, 20);
    Note n4 = new Note(new Pitch("F_5"), 8, 100);
    Note sameNote = new Note(new Pitch("F_5"), 8, 100);

    model.addNote(n4);
    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);

    model.replaceNote(n4, sameNote);

    List<Note> notesAfterReplaced = new ArrayList<>();
    notesAfterReplaced.add(n1);
    notesAfterReplaced.add(n2);
    notesAfterReplaced.add(n3);
    notesAfterReplaced.add(n4);

    assertTrue(listEqualsNoOrder(notesAfterReplaced, model.toPiece().getAllNotes()));
  }

  @Test
  public void fromPieceTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("C_3"), 2, 5);
    Note n2 = new Note(new Pitch("D#10"), 4, 6);
    Note n3 = new Note(new Pitch("A_3"), 8, 20);
    Note n4 = new Note(new Pitch("F_5"), 8, 100);
    Note n5 = new Note(new Pitch("A_5"), 17, 4);
    Note n6 = new Note(new Pitch("E#2"), 10, 4);

    ArrayList<Note> notes = new ArrayList<>();
    notes.add(n1);
    notes.add(n2);
    notes.add(n3);
    notes.add(n4);

    ArrayList<Note> currNotes = new ArrayList<>();
    currNotes.add(n5);
    currNotes.add(n6);
    currNotes.add(n3);
    currNotes.add(n1);

    Piece piece = new Piece(notes, 100);

    model.addNote(n5);
    model.addNote(n6);
    model.addNote(n3);
    model.addNote(n1);

    assertTrue(listEqualsNoOrder(currNotes, model.toPiece().getAllNotes()));

    IMusicEditorModel<Note, Piece> newModel = model.fromPiece(piece);

    assertTrue(listEqualsNoOrder(notes, newModel.toPiece().getAllNotes()));
  }

  @Test
  public void addMusicAtEndTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    ArrayList<Note> notes2 = new ArrayList<>();
    Note n4 = new Note(new Pitch("C_3"), 2, 5);
    Note n5 = new Note(new Pitch("D#10"), 4, 6);
    Note n1 = new Note(new Pitch("D#5"), 12, 3);

    notes2.add(n1);

    model.addNote(n4);
    model.addNote(n5);

    Piece piece = new Piece(notes2, 200);
    model.addMusicAtEnd(piece);

    for (int i = 0; i < model.toPiece().getAllNotes().size(); i++) {
      Note n = model.toPiece().getAllNotes().get(i);
      if (n.getPitch().equals(n1.getPitch())) {
        assertEquals(n.getStart(), 22);
        break;
      }
    }

  }

  @Test
  public void addMusicAtEndTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    ArrayList<Note> notes2 = new ArrayList<>();
    Note n4 = new Note(new Pitch("C_3"), 2, 5);
    Note n5 = new Note(new Pitch("D#10"), 4, 6);
    Note n6 = new Note(new Pitch("A_3"), 8, 20);
    Note n7 = new Note(new Pitch("F_5"), 8, 100);
    Note n8 = new Note(new Pitch("A_5"), 17, 4);
    Note n9 = new Note(new Pitch("E#2"), 10, 4);

    notes2.add(n4);
    notes2.add(n5);
    notes2.add(n6);
    notes2.add(n7);
    notes2.add(n8);
    notes2.add(n9);

    Note n1 = new Note(new Pitch("A#4"), 2, 5);
    Note n2 = new Note(new Pitch("C_1"), 4, 8);
    Note n3 = new Note(new Pitch("A_1"), 4, 4);

    Piece piece = new Piece(notes2, 150);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);

    Note n42 = new Note(new Pitch("C_3"), 14, 5);
    Note n52 = new Note(new Pitch("D#10"), 16, 6);
    Note n62 = new Note(new Pitch("A_3"), 20, 20);
    Note n72 = new Note(new Pitch("F_5"), 20, 100);
    Note n82 = new Note(new Pitch("A_5"), 29, 4);
    Note n92 = new Note(new Pitch("E#2"), 22, 4);

    assertEquals(12 ,model.toPiece().numBeats());
    assertEquals(new Pitch("A#4"), model.toPiece().highestPitch());
    assertEquals(new Pitch("C_1"), model.toPiece().lowestPitch());

    model.addMusicAtEnd(piece);
    assertEquals(120, model.toPiece().numBeats());
    assertEquals(new Pitch("D#10"), model.toPiece().highestPitch());
    assertEquals(new Pitch("C_1"), model.toPiece().lowestPitch());

    assertTrue(model.toPiece().getAllNotes().contains(n1));
    assertTrue(model.toPiece().getAllNotes().contains(n2));
    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n42));
    assertTrue(model.toPiece().getAllNotes().contains(n52));
    assertTrue(model.toPiece().getAllNotes().contains(n62));
    assertTrue(model.toPiece().getAllNotes().contains(n72));
    assertTrue(model.toPiece().getAllNotes().contains(n82));
    assertTrue(model.toPiece().getAllNotes().contains(n92));

  }

  @Test
  public void addMusicAtEndEmptyModelTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("A_3"), 8, 20);
    Note n2 = new Note(new Pitch("F#5"), 10, 10);
    ArrayList<Note> notes = new ArrayList<>();
    notes.add(n1);
    notes.add(n2);
    Piece p = new Piece(notes, 150);

    assertTrue(model.toPiece().getAllNotes().size() == 0);

    model.addMusicAtEnd(p);
    assertEquals(28, model.toPiece().numBeats());
    assertEquals(new Pitch("F#5"), model.toPiece().highestPitch());
    assertEquals(new Pitch("A_3"), model.toPiece().lowestPitch());
    assertTrue(model.toPiece().getAllNotes().contains(n1));
    assertTrue(model.toPiece().getAllNotes().contains(n2));
  }

  @Test
  public void addMusicAtStartEmptyModelTest1() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("A_3"), 8, 20);
    Note n2 = new Note(new Pitch("F#5"), 10, 10);
    Note n3 = new Note(new Pitch("C_4"), 17, 4);

    ArrayList<Note> notes = new ArrayList<>();
    notes.add(n1);
    notes.add(n2);
    notes.add(n3);

    Piece p = new Piece(notes, 200);

    model.addMusicAtStart(p);
    assertEquals(new Pitch("A_3"), model.toPiece().lowestPitch());
    assertEquals(new Pitch("F#5"), model.toPiece().highestPitch());
    assertTrue(model.toPiece().getAllNotes().contains(n2));
    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n1));
  }

  @Test
  public void addMusicAtStartTest2() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("A_3"), 8, 4);
    Note n2 = new Note(new Pitch("F#5"), 1, 3);
    Note n3 = new Note(new Pitch("C_4"), 13, 5);
    Note n4 = new Note(new Pitch("D_4"), 5, 2);
    model.addNote(n1);
    model.addNote(n3);

    ArrayList<Note> notes = new ArrayList<>();
    notes.add(n2);
    notes.add(n4);

    Piece p = new Piece(notes, 150);

    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n1));

    model.addMusicAtStart(p);
    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n1));
    assertTrue(model.toPiece().getAllNotes().contains(n2));
    assertTrue(model.toPiece().getAllNotes().contains(n4));

    for (int i = 0; i < model.toPiece().getAllNotes().size(); i++) {
      Note n = model.toPiece().getAllNotes().get(i);
      if (n.equals(n3)) {
        assertTrue(n.getStart() + n.getDuration() == model.toPiece().numBeats());
        break;
      }
    }
  }

  @Test
  public void addMusicAtStartTest3() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    Note n1 = new Note(new Pitch("A_3"), 0, 4);
    Note n2 = new Note(new Pitch("F#5"), 1, 3);
    Note n3 = new Note(new Pitch("C_4"), 5, 5);
    Note n4 = new Note(new Pitch("D_4"), 5, 2);
    model.addNote(n1);
    model.addNote(n3);

    ArrayList<Note> notes = new ArrayList<>();
    notes.add(n2);
    notes.add(n4);

    Piece p = new Piece(notes, 150);

    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n1));

    model.addMusicAtStart(p);
    assertTrue(model.toPiece().getAllNotes().contains(n3));
    assertTrue(model.toPiece().getAllNotes().contains(n1));
    assertTrue(model.toPiece().getAllNotes().contains(n2));
    assertTrue(model.toPiece().getAllNotes().contains(n4));
  }

  @Test
  public void setTempoTest() {
    IMusicEditorModel<Note, Piece> model = new MusicEditorModel();
    model.setTempo(150);
    Piece p = model.toPiece();
    assertTrue(p.getTempo() == 150);
  }

}