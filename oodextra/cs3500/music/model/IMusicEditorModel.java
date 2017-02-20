package cs3500.music.model;

/**
 * An interface representing a MusicEditorModel.  It is parametrized over how one wants to
 * represent notes (N) and pieces of music (P).  P should be a class holding together the data
 * packaged together for the view and other users of the class.
 * <p></p>
 * Changes (hw9): Added new methods for adding and removing repeat sign.
 */
public interface IMusicEditorModel<N, P> {
  /**
   * Returns a piece of music that is what the music editor currently stores.
   *
   * @return - A piece of music, designated by parameter P
   */
  P toPiece();

  /**
   * Adds a note to the music editor.
   *
   * @param n - A note, designated by parameter N
   */
  void addNote(N n);

  /**
   * Removes a note from the music editor.
   *
   * @param n - A note, designated by parameter N
   */
  void removeNote(N n);

  /**
   * Replaces note nOld with note nNew.
   *
   * @param nOld - A note, designated by parameter N
   * @param nNew - A note, designated by parameter N
   */
  void replaceNote(N nOld, N nNew);

  /**
   * Creates a new Music editor with the same parameters as the current implementation
   * containing the given piece.
   *
   * @param p - A piece of music, designated by parameter P
   * @return - MusicEditor containing the given piece
   */
  IMusicEditorModel<N, P> fromPiece(P p);

  /**
   * Adds the piece of music to the end of the music editor.
   *
   * @param p - A piece of music, designated by parameter P
   */
  void addMusicAtEnd(P p);

  /**
   * Adds the piece of music concurrently to music editor.
   *
   * @param p - A piece of music, designated by parameter P
   */
  void addMusicAtStart(P p);

  /**
   * Sets tempo for the music editor.
   *
   * @param temp - A natural number representing the length of the beat.
   */
  void setTempo(int temp);

  /**
   * Adds the given repeat sign to the music editor.
   * @param sign  a repeat sign object
   */
  void addRepeatSign(RepeatSign sign);

  /**
   * Removes the repeat sign at the given beat.
   * @param beat   a natural number representing beat of music.
   */
  void removeRepeatSignAt(int beat);

}
