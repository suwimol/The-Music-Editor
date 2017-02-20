package cs3500.music.controller;

import javax.sound.midi.InvalidMidiDataException;

/**
 * An interface representing the controller of the music editor. It is parametrized over how one
 * wants to represent notes (N) and pieces of music (P). P should be a package storing accessors to
 * the data from the view. It controls when the program is to be run.
 */
public interface IMusicEditorController<N, P> {
  /**
   * Runs the Music Editor.
   */
  void run() throws InvalidMidiDataException, InterruptedException;
}
