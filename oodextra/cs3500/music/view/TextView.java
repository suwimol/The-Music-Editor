package cs3500.music.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.model.Pitch;

/**
 * Represents a textual (console) view of the music editor.
 */
public class TextView implements IMusicEditorView<Piece> {
  Piece piece;
  Appendable out;
  Scanner input;

  /**
   * Constructs a textual view of the music editor.
   *
   * @param ap an appendable object
   * @param rd a readable object
   */
  public TextView(Appendable ap, Readable rd) {
    out = ap;
    input = new Scanner(rd);
  }

  @Override
  public void render() {
    if (this.piece == null) {
      throw new IllegalArgumentException("Music Piece cannot be null.");
    }

    if (this.piece.getAllNotes().isEmpty()) {
      return;
    }

    ArrayList<Pitch> pitchRange = piece.lowestPitch().range(piece.highestPitch());
    NoteType[][] notes = NoteType.plays(piece, pitchRange);

    int beatLen = Integer.toString(piece.numBeats()).length();

    String str = String.format("%" + beatLen + "s", "");
    for (Pitch p : pitchRange) {
      String temp = p.toString();
      if (temp.length() < 4) {
        temp += " ";
      }
      str += String.format("%5s", temp);
    }
    str += "\n";

    for (int i = 0; i < piece.numBeats(); i++) {
      str += String.format("%" + beatLen + "s", i + "");
      for (int j = 0; j < pitchRange.size(); j++) {
        str += notes[i][j].toString();
      }
      str += "\n";
    }

    try {
      out.append(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setPiece(Piece piece) {
    this.piece = piece;
  }

  @Override
  public void showErrorMessage(String error) {
    try {
      out.append(error);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private enum NoteType {
    notPlaying, playing, starting;

    @Override
    public String toString() {
      switch (this) {
        case notPlaying:
          return "     ";
        case playing:
          return "  |  ";
        case starting:
          return "  X  ";
        default:
          throw new IllegalArgumentException("Can't be null");
      }
    }

    private static NoteType[][] plays(Piece piece, ArrayList<Pitch> pitchRange) {
      NoteType[][] notes = new NoteType[piece.numBeats()][pitchRange.size()];
      for (NoteType[] n : notes) {
        Arrays.fill(n, NoteType.notPlaying);
      }

      List<Note> n = piece.getAllNotes();
      Collections.sort(n);

      for (Note note : n) {
        int i = pitchRange.indexOf(note.getPitch());
        int start = note.getStart();
        int end = note.getDuration() + start;
        notes[start][i] = NoteType.starting;
        for (int j = start + 1; j < end; j++) {
          notes[j][i] = NoteType.playing;
        }
      }

      return notes;
    }
  }
}
