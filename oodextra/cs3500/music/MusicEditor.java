package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.IMusicEditorController;
import cs3500.music.controller.MusicEditorController;
import cs3500.music.controller.MusicEditorControllerComp;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.util.MusicEditorBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.CompositeView;
import cs3500.music.view.IMusicEditorView;
import cs3500.music.view.MusicEditorFactoryView;

/**
 * Represents the Music Editor application.
 * This plays the given piece of music in the specified view.
 * The client should input two arguments one after another in this form:
 * 'the name of the view' (space) 'file name' (ignore the single quotes)
 * The name of the view is one of "console", "visual" or "midi".
 * The file has to be in .txt format.
 */
public final class MusicEditor {
  /**
   * Main method to run the program.
   * @param args   command given by the users.
   */
  public static void main(String[] args) throws InvalidMidiDataException, InterruptedException {
    if (args.length < 2) {
      args = new String[2];
      Scanner s = new Scanner(System.in);
      System.out.println("Please give a view type:");
      args[0] = s.next();
      System.out.println("Please give a file:");
      args[1] = s.next();
    }
    IMusicEditorView<Piece> v = MusicEditorFactoryView.createView(args[0]);
    IMusicEditorModel<Note, Piece> m = null;
    try {
      m = MusicReader.parseFile(new FileReader(args[1]), new MusicEditorBuilder());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    if (v instanceof CompositeView) {
      IMusicEditorController<Note, Piece> c2 = new MusicEditorControllerComp(m,
              (CompositeView<Note, Piece>) v);
      c2.run();
    } else {
      IMusicEditorController<Note, Piece> c1 = new MusicEditorController(m, v);
      c1.run();
    }

  }
}