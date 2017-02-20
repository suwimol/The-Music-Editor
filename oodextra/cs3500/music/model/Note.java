package cs3500.music.model;

/*
  Changes:
  A few new fields (instrument and volume as well as getters for those fields were added.
  The .equals and the .hashcode methods were changed accordingly.  A constructor taking in all
  of the fields as integers was added as to make reading in files easier.
 */

import java.util.Objects;

/**
 * An immutable class that represents a note of music.
 */
public class Note implements Comparable<Note> {
  private final Pitch p;
  private final int startBeat;
  private final int beatsHeld;
  private final int instrument;
  private final int volume;

  /**
   * A default constructor for a Note that takes in all of the fields as inputs.
   *
   * @param p         - The pitch of the note.
   * @param startBeat - A natural number, representing when the note starts.
   * @param beatsHeld - A positive integer, representing how many beats the note plays for.
   */
  public Note(Pitch p, int startBeat, int beatsHeld) {
    if (startBeat < 0 && beatsHeld < 1) {
      throw new IllegalArgumentException("Invalid beats");
    }
    this.p = p;
    this.startBeat = startBeat;
    this.beatsHeld = beatsHeld;
    this.instrument = 1;
    this.volume = 64;
  }

  /**
   * Another constructor for a Note that takes in a start beat, an end beat, an instrument,
   * a pitch, and a volume.
   *
   * @param start      A natural number, representing when the note starts.
   * @param end        A natural number that has to be greater than the start. It represents when
   *                   the note ends.
   * @param instrument A number between 1 to 16.
   * @param pitch      A natural number between 1 to 120.
   * @param volume     A number that is greater than 0.
   */
  public Note(int start, int end, int instrument, int pitch, int volume) {
    if (start < 0 || end < start || instrument < 0 || instrument > 16 ||
            pitch < 0 || pitch > 120 || volume < 0) {
      throw new IllegalArgumentException("Invalid note");
    }
    this.startBeat = start;
    this.beatsHeld = end - start;
    this.instrument = instrument;
    this.volume = volume;
    this.p = new Pitch(pitch);
  }

  @Override
  public int compareTo(Note n) {
    return this.startBeat - n.getStart();
  }

  /**
   * An accessor for the beat this note starts on.
   *
   * @return - The starting beat of the note.
   */
  public int getStart() {
    return startBeat;
  }

  /**
   * An accessor for how many beats the note is held.
   *
   * @return - The amount beats of the note is held.
   */
  public int getDuration() {
    return beatsHeld;
  }


  /**
   * An accessor for the pitch of the note.
   *
   * @return - The pitch of the note.
   */
  public Pitch getPitch() {
    return p;
  }

  /**
   * Checks if this Note is equal to the other Note.
   *
   * @param other a Note
   * @return true if this Note is equal to the other Note, false otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Note)) {
      return false;
    }
    Note that = (Note) other;

    return this.p.equals(that.p) && this.startBeat == that.startBeat &&
            this.beatsHeld == that.beatsHeld && this.volume == that.volume &&
            this.instrument == that.instrument;
  }

  @Override
  public int hashCode() {
    return Objects.hash(p, startBeat, beatsHeld, instrument, volume);
  }

  /**
   * An accessor for the instrument of the note.
   *
   * @return - The instrument of the note.
   */
  public int getInstrument() {
    return instrument;
  }

  /**
   * An accessor for the volume of the note.
   *
   * @return - The volume of the note.
   */
  public int getVolume() {
    return volume;
  }
}
