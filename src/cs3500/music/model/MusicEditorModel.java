package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*
  Changes:
  The main design change here is it no longer delegates to the MutablePiece class
  which is now non-existent and instead stores the notes as an array.
  A tempo field and a setter for it were added as well.
  The setter was put in the interface as well.
 */

/**
 * A class that implements the IMusicEditorModel with Note and Piece as its parameters.
 * It holds a single piece and models adding pieces by merging them into one.
 */
public class MusicEditorModel implements IMusicEditorModel<Note, Piece> {
  private ArrayList<Note> piece;
  private int tempo;
  private TreeMap<Integer, RepeatSign> map;


  /**
   * A default constructor that creates an empty Music Editor.
   */
  public MusicEditorModel() {
    this.piece = new ArrayList<>();
    tempo = 200000;
    this.map = new TreeMap<>();

  }

  /**
   * Another constructor that creates a music editor with the given piece of music.
   *
   * @param p a piece of music {@link Piece}
   */
  private MusicEditorModel(Piece p) {
    this.piece = new ArrayList<>();
    this.piece.addAll(p.getAllNotes());
    tempo = p.getTempo();
  }

  /**
   * Returns a piece of music that is what the music editor currently stores.
   *
   * @return - A piece of music.
   */
  @Override
  public Piece toPiece() {
    return new Piece(this.piece, tempo, map);
  }

  /**
   * Adds a note to the music editor.
   *
   * @param note - A note.
   */
  @Override
  public void addNote(Note note) {
    this.piece.add(note);
  }

  /**
   * Removes a note from the music editor.
   *
   * @param note - A note.
   */
  @Override
  public void removeNote(Note note) {
    this.piece.remove(note);
  }

  /**
   * Replaces note nOld with note nNew.
   *
   * @param nOld - A note.
   * @param nNew - A note.
   */
  @Override
  public void replaceNote(Note nOld, Note nNew) {
    if (this.piece.remove(nOld)) {
      this.piece.add(nNew);

    }
  }

  /**
   * Creates a new Music editor with the same parameters as the current implementation
   * containing the given piece.
   *
   * @param piece - A piece of music.
   * @return - MusicEditor containing the given piece
   */
  @Override
  public IMusicEditorModel<Note, Piece> fromPiece(Piece piece) {
    return new MusicEditorModel(piece);
  }

  /**
   * Adds the piece of music to the end of the music editor.
   *
   * @param other - A piece of music.
   */
  @Override
  public void addMusicAtEnd(Piece other) {
    int numBeats = this.toPiece().numBeats();
    for (Note note : other.getAllNotes()) {
      addNote(new Note(numBeats + note.getStart(), numBeats + note.getStart() + note.getDuration(),
              note.getInstrument(), note.getPitch().asInt(), note.getVolume()));
    }
  }

  /**
   * Adds the piece of music concurrently to music editor.
   *
   * @param other - A piece of music.
   */
  @Override
  public void addMusicAtStart(Piece other) {
    this.piece.addAll(other.getAllNotes());
  }

  /**
   * Sets tempo for the music editor.
   *
   * @param tempo - A natural number representing the length of the beat.
   */
  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  @Override
  public void addRepeatSign(RepeatSign sign) {
    this.map.put(sign.getBeat(), sign);
  }

  @Override
  public void removeRepeatSignAt(int beat) {
    if (this.map.containsKey(beat)) {
      this.map.remove(beat);
    }
  }
}