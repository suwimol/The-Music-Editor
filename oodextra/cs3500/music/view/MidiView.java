package cs3500.music.view;


import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;

/**
 * A view for playing the Piece using Midi.
 * <p></p>
 * Changes:
 * playPiece method has been removed and replaced by the new method 'playNoteAtBeat' which
 * plays all the notes at the given beat. This solved synchronizing problem.
 */
public class MidiView implements SoundView<Piece> {
  private final Synthesizer synth;
  private Receiver receiver;
  private Piece piece;
  private int currBeat;
  private long startTime;
  private boolean isPlaying;

  /**
   * A constructor taking in a Synthesizer.
   *
   * @param synth - A Synthesizer to produce the music
   */
  public MidiView(Synthesizer synth) {
    try {
      this.synth = synth;
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      throw new IllegalStateException("MIDI unavailable");
    }
    currBeat = 0;
  }

  @Override
  public void setIsPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }

  @Override
  public void playNoteAtBeat(int beat) throws InvalidMidiDataException, InterruptedException {
    List<Note> notesAtBeat = this.piece.getNoteAtBeat(beat);
    try {
      synth.open();
      receiver = synth.getReceiver();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    isPlaying = true;
    synth.loadAllInstruments(synth.getDefaultSoundbank());
    long pieceStart = this.synth.getMicrosecondPosition();
    for (Note n : notesAtBeat) {
      int pitch = n.getPitch().asInt();
      this.startTime = pieceStart + piece.getTempo() * (n.getStart() - currBeat);
      long endTime = startTime + piece.getTempo() * (n.getDuration() - currBeat);
      int inst = n.getInstrument();
      int vol = n.getVolume();
      MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, inst - 1, pitch, vol);
      MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, inst - 1, pitch, vol);
      this.receiver.send(start, startTime);
      this.receiver.send(stop, endTime);
    }
  }


  @Override
  public void setPiece(Piece piece) {
    this.piece = piece;
  }

  @Override
  public void render() throws InvalidMidiDataException, InterruptedException {
    if (piece == null) {
      throw new IllegalArgumentException("Music Piece cannot be null.");
    }

    this.playNoteAtBeat(currBeat);

    if (this.currBeat >= piece.numBeats()) {
      this.receiver.close();
    }
  }

  @Override
  public void showErrorMessage(String error) {
    throw new IllegalArgumentException("Errors cannot be displayed\n" + error);
  }

  @Override
  public void setCurrBeat(int beat) {
    currBeat = beat;
  }

  @Override
  public boolean isPlaying() {
    long pieceStart = this.synth.getMicrosecondPosition();
    if ((synth.getMicrosecondPosition() - pieceStart) / piece.getTempo() > piece.numBeats()) {
      isPlaying = false;
      setCurrBeat(0);
    }
    return isPlaying;
  }

}
