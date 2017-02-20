package cs3500.music.model;

/*
  Changes:
  Pitch was changed to just store a tone and an octave, the sharp being stored as part of the
  enum Tone.  A constructor was added that takes in a int as to make reading in files easier.
  As int was made public and modified slightly as to be consistent with how midi represents notes.
 */

import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that represents the pitch of a note.  It has a Tone which is a private enum representing
 * twelve tones of the western scale and an octave which is an int from 1 to 10.
 */
public class Pitch implements Comparable<Pitch> {
  private final Tone tone;
  private final int octave;

  /**
   * A constructor taking in a String structured like TAO, where T is one of the 7 tones,
   * A is either _ or # and O is an int from 1 to 10 inclusive (e.g. D_1 or C#10).
   *
   * @param str - A string representing a Pitch.
   */
  public Pitch(String str) {
    String t = str.substring(0, 1);
    String a = str.substring(1, 2);
    int octave = Integer.parseInt(str.substring(2));
    boolean accidental;
    Tone tone;
    switch (t) {
      case "C":
        tone = Tone.C;
        break;
      case "D":
        tone = Tone.D;
        break;
      case "E":
        tone = Tone.E;
        break;
      case "F":
        tone = Tone.F;
        break;
      case "G":
        tone = Tone.G;
        break;
      case "A":
        tone = Tone.A;
        break;
      case "B":
        tone = Tone.B;
        break;
      default:
        throw new IllegalArgumentException("Bad Tone");
    }
    switch (a) {
      case "_":
        accidental = false;
        break;
      case "#":
        accidental = true;
        break;
      default:
        throw new IllegalArgumentException("Bad Tone");
    }
    if (octave > 10 || octave < 1) {
      throw new IllegalArgumentException("Attempted pitch out of range");
    }

    if (accidental) {
      this.tone = tone.next();
    } else {
      this.tone = tone;
    }
    this.octave = octave;
  }

  /**
   * A constructor for Pitch that takes in an integer in the way midi represents pitches.
   *
   * @param pitch an integer ranging from 0 to 108.
   */
  public Pitch(int pitch) {
    octave = pitch / 12 + 1;
    tone = Tone.values()[pitch % 12];
  }

  /**
   * Creates an array of the pitches from this Pitch to end inclusive.
   *
   * @param end - The pitch to stop at, included in the final array.
   * @return An array of Pitches
   */
  public ArrayList<Pitch> range(Pitch end) {
    ArrayList<Pitch> p = new ArrayList<Pitch>();
    Pitch temp = new Pitch(this.tone, this.octave);
    while (!temp.equals(end)) {
      p.add(temp);
      temp = temp.next();
    }
    p.add(end);

    return p;
  }

  @Override
  public int compareTo(Pitch o) {
    return this.asInt() - o.asInt();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Pitch)) {
      return false;
    }
    Pitch that = (Pitch) other;

    return this.tone.equals(that.tone) && this.octave == that.octave;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tone, octave);
  }

  /**
   * Converts the tone of this Pitch to String.
   *
   * @return the string of this Pitch.
   */
  @Override
  public String toString() {
    String str = tone.toString();
    str += octave;

    return str;
  }

  /**
   * Returns the pitch as an integer that represents it.
   *
   * @return an integer representing this Pitch.
   */
  public int asInt() {
    return tone.asInt() + (octave - 1) * 12;
  }

  /**
   * A finite set of tones of a Note.
   */
  private enum Tone {
    C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B;

    private int asInt() {
      switch (this) {
        case C:
          return 0;
        case C_SHARP:
          return 1;
        case D:
          return 2;
        case D_SHARP:
          return 3;
        case E:
          return 4;
        case F:
          return 5;
        case F_SHARP:
          return 6;
        case G:
          return 7;
        case G_SHARP:
          return 8;
        case A:
          return 9;
        case A_SHARP:
          return 10;
        case B:
          return 11;
        default:
          throw new IllegalArgumentException("Tone can't be null");
      }
    }

    /**
     * Returns the tone after this tone.
     */
    private Tone next() {
      switch (this) {
        case C:
          return C_SHARP;
        case C_SHARP:
          return D;
        case D:
          return D_SHARP;
        case D_SHARP:
          return E;
        case E:
          return F;
        case F:
          return F_SHARP;
        case F_SHARP:
          return G;
        case G:
          return G_SHARP;
        case G_SHARP:
          return A;
        case A:
          return A_SHARP;
        case A_SHARP:
          return B;
        case B:
          return C;
        default:
          throw new IllegalArgumentException("Tone can't be null");
      }
    }

    @Override
    public String toString() {
      return this.name().replace("_SHARP", "#");
    }
  }

  /**
   * A private constructor for a Pitch.
   *
   * @param tone   a Tone of this Pitch.
   * @param octave a positive integer greater than 1 representing the octave of this Pitch.
   */
  private Pitch(Tone tone, int octave) {
    if (octave > 10 || octave < 1) {
      throw new IllegalArgumentException("Attempted pitch out of range");
    }
    this.tone = tone;
    this.octave = octave;
  }

  /**
   * Returns the Pitch after this Pitch.
   *
   * @return the Pitch after this Pitch.
   */
  private Pitch next() {
    Tone tone = this.tone.next();
    int octave = this.octave;
    if (tone == Tone.C) {
      if (octave == 10) {
        throw new IllegalArgumentException();
      }
      octave++;
    }

    return new Pitch(tone, octave);
  }
}
