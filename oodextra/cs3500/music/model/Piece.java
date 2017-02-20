package cs3500.music.model;

/*
  Changes:
  The methods highestPitch, lowestPitch, and numBeats were changed to be accessors to improve
  efficiency of the view.  Tempo was added as a field along with an accessor.  The previous
  getPitch(Pitch p) method was replaced with a getAllNotes() method, leaving the processing of
  that data up to view.

  Changes (in hw7):
  The method 'getNoteAtBeat' has been added. This is to be called by method playNoteAtBeat in the
  midi view.

  Changes: (in hw9):
  Added more methods to handle the repeat signs.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A class for representing a Piece, it has no methods inside it that can change it and is
 * meant to be immutable.  It is meant for internal use with the view and model, to pass along
 * the data without worry of it being mutated.
 */
public final class Piece {
  private ArrayList<Note> piece;
  private final int tempo;
  private final int maxBeat;
  private final Pitch maxPitch;
  private final Pitch minPitch;
  private TreeMap<Integer, RepeatSign> map;
  private int lastBegin;
  private int nextEnd;

  /**
   * A constructor default that creates an empty Piece of music. It takes in a piece of music,
   * and a tempo.
   *
   * @param piece a piece of music.
   * @param tempo a natural number representing the length of the beat.
   */
  Piece(ArrayList<Note> piece, int tempo, TreeMap<Integer, RepeatSign> map) {
    this.piece = new ArrayList<Note>();
    this.piece.addAll(piece);
    this.tempo = tempo;
    this.map = map;

    if (piece.isEmpty()) {
      maxBeat = 0;
      maxPitch = null;
      minPitch = null;
      return;
    }
    int maxB = piece.get(0).getStart() + piece.get(0).getDuration();
    Pitch maxP = piece.get(0).getPitch();
    Pitch min = piece.get(0).getPitch();
    for (Note n : piece) {
      if (maxB < (n.getStart() + n.getDuration())) {
        maxB = n.getStart() + n.getDuration();
      }
      if (maxP.compareTo(n.getPitch()) < 0) {
        maxP = n.getPitch();
      }
      if (min.compareTo(n.getPitch()) > 0) {
        min = n.getPitch();
      }
    }
    maxBeat = maxB;
    maxPitch = maxP;
    minPitch = min;
  }

  /**
   * An accessor for the number of beats in the Piece.
   *
   * @return - The number of beats in a piece
   */
  public int numBeats() {
    return maxBeat;
  }

  /**
   * An accessor for the highest pitch of the Piece.
   *
   * @return - The highest pitch of the piece
   */
  public Pitch highestPitch() {
    return maxPitch;
  }

  /**
   * An accessor for the lowest pitch of the Piece.
   *
   * @return - The lowest pitch of the piece
   */
  public Pitch lowestPitch() {
    return minPitch;
  }

  /**
   * Gets all the notes in this Piece of music.
   *
   * @return a list of all the notes in this Piece of music.
   */
  public List<Note> getAllNotes() {
    ArrayList<Note> notes = new ArrayList<Note>();
    for (Note note : piece) {
      notes.add(note);
    }

    return notes;
  }

  /**
   * Gets all the notes at the given beat.
   *
   * @param beat a natural number representing a beat.
   * @return a list of all the notes at the given beat in this Piece of music.
   */
  public List<Note> getNoteAtBeat(int beat) {
    ArrayList<Note> notes = new ArrayList<Note>();
    for (Note n : piece) {
      if (n.getStart() == beat) {
        notes.add(n);
      }
    }
    return notes;
  }

  /**
   * Gets the tempo of this Piece of music.
   *
   * @return a natural number representing the length of the beat.
   */
  public int getTempo() {
    return this.tempo;
  }

  /**
   * Gets this map.
   * @return  this map.
   */
  public TreeMap getMap() {
    return this.map;
  }

  /**
   * SIDE EFFECT:
   * Checks if there is a begin sign right before the given beat.
   * <p></p>
   * It has a side effect of updating this lastBegin value to the beat of that begin sign.
   * @param end   a natural number representing the beat of an end sign.
   * @return  true if there is a begin sign right before the given beat, false otherwise.
   */
  public boolean isExistingBeginningBefore(int end) {

    for (int i = end; i >= 0; i--) {
      if (i == 0) {
        this.lastBegin = 0;
        return false;
      }
      if (this.map.containsKey(i)) {
        if (this.map.get(i).getType() == SignType.START_SIGN) {
          this.lastBegin = i;
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if there is a begin sign right before the given beat.
   * @param beat   a natural number representing the beat of music.
   * @return  true if there is a begin sign right before the given beat, false otherwise.
   */
  public boolean isBeginRightBefore(int beat) {
    for (int i = beat - 1; i >= 0; i--) {
      if (this.map.containsKey(i)) {
        if (this.map.get(i).getType() == SignType.END_SIGN) {
          return false;
        } else if (this.map.get(i).getType() == SignType.START_SIGN) {
          System.out.println("has start right before");
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks whether there is the next end right after the given beat.
   * @param beat a natural number representing the beat
   * @return true if there is the next end right after the given beat, false otherwise.
   */
  public boolean isEndRightAfter(int beat) {

    for (int i = beat + 1; i <= map.lastEntry().getKey(); i++) {
      if (this.map.containsKey(i)) {
        if (this.map.get(i).getType() == SignType.END_SIGN) {
          return true;
        } else if (this.map.get(i).getType() == SignType.START_SIGN) {
          return false;
        }
      }

    }
    return false;
  }

  /**
   * SIDE EFFECT:
   * Checks whether there is the next end right after the given beat.
   * It has a side effect of setting the beat of the next end to be the given one.
   * @param curr  a natural number representing the beat
   * @return  true if there is the next end right after the given beat, false otherwise.
   */
  public boolean isExistingNextEnd(int curr) {

    for (int i = curr + 1; i <= map.lastEntry().getKey(); i++) {
      if (this.map.containsKey(i)) {
        if (this.map.get(i).getType() == SignType.END_SIGN) {
          this.nextEnd = i;
          return true;
        } else if (this.map.get(i).getType() == SignType.START_SIGN) {
          this.nextEnd = i;
          return false;
        }
      }
    }

    return false;
  }

  /**
   * Checks whether or not the given repeat sign can be added.
   * @param dummySign a repeat sign object representing a dummy sign.
   * @return  true if the given repeat sign can be added, false otherwise.
   */
  public boolean isAbleToAddSign(RepeatSign dummySign) {

    if (!map.isEmpty()) {

      for (int i = map.lastKey(); i >= 0; i--) {
        if (map.containsKey(i)) {
          RepeatSign sign = map.get(i);
          if (sign.getType() == SignType.END_SIGN) {
            return (dummySign.getBeat() > i);
          }
        }
      }
    }

    return true;
  }

  /**
   * Gets the beat of the last beginning sign (start sign) in the map. (this lastBegin)
   * @return an integer representing the beat of the last start sign in the map.
   */
  public int getLastBegin() {
    return this.lastBegin;
  }

  /**
   * Gets the beat of the next end sign in the map.
   * @return an integer representing the beat of the next end.
   */
  public int getNextEnd() {
    return this.nextEnd;
  }

}
