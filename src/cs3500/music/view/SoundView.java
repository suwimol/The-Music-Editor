package cs3500.music.view;

import javax.sound.midi.InvalidMidiDataException;

/**
 * Represents the interface for Sound View. It controls the functionality of Midi View.
 * It factors out all the functionality/operation that are suitable for Midi view.
 */
public interface SoundView<P> extends IMusicEditorView<P> {
  /**
   * Sets the current beat in Midi View to be the given one.
   *
   * @param currBeat a natural number representing a specific beat in the music piece.
   */
  void setCurrBeat(int currBeat);

  /**
   * Plays all the notes at the given beat.
   *
   * @param beat a natural number representing the beat.
   * @throws InvalidMidiDataException if the midi data is invalid.
   * @throws InterruptedException     if the exception is interrupted.
   */
  void playNoteAtBeat(int beat) throws InvalidMidiDataException, InterruptedException;

  /**
   * Sets the playing flag to the given one.
   *
   * @param isPlaying a boolean value
   */
  void setIsPlaying(boolean isPlaying);

  /**
   * Checks whether the music is playing or not.
   *
   * @return true if the music is being played, false otherwise.
   */
  boolean isPlaying();
}
