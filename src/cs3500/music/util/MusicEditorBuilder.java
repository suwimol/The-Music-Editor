package cs3500.music.util;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.RepeatSign;

/**
 * Represents a music editor builder for the music editor.
 */
public class MusicEditorBuilder implements CompositionBuilder<IMusicEditorModel<Note, Piece>> {
  private final MusicEditorModel m;

  /**
   * Constructs a music editor builder for the music editor.
   */
  public MusicEditorBuilder() {
    this.m = new MusicEditorModel();
  }

  @Override
  public IMusicEditorModel<Note, Piece> build() {
    return m;
  }

  @Override
  public CompositionBuilder<IMusicEditorModel<Note, Piece>> setTempo(int tempo) {
    m.setTempo(tempo);
    return this;
  }

  @Override
  public CompositionBuilder<IMusicEditorModel<Note, Piece>> addNote(int start, int end,
                                                            int instrument, int pitch, int volume) {
    m.addNote(new Note(start, end, instrument, pitch, volume));
    return null;
  }

  @Override
  public CompositionBuilder<IMusicEditorModel<Note, Piece>> addSign(RepeatSign sign) {
    m.addRepeatSign(sign);
    return null;
  }
}
