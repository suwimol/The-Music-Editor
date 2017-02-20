package cs3500.music.controller;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.view.IMusicEditorView;

/**
 * Represents a controller of the music editor. This controls a specified model and a specified
 * view inputted by the user. It informs the view to present the specified model.
 */
public class MusicEditorController implements IMusicEditorController<Note, Piece> {
  private IMusicEditorModel<Note, Piece> model;
  private IMusicEditorView<Piece> view;

  /**
   * Constructs a Music Editor controller for GUI and Midi views.
   *
   * @param model a music editor model {@link IMusicEditorModel}
   * @param view  a music editor view {@link IMusicEditorView}
   */
  public MusicEditorController(IMusicEditorModel<Note, Piece> model,
                               IMusicEditorView<Piece> view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void run() throws InvalidMidiDataException, InterruptedException {
    view.setPiece(model.toPiece());
    view.render();
  }
}
