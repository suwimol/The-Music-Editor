package cs3500.music.view;

/**
 * Changes (hw8): This abstract class factors out the functionality that is common for both
 * SoundView (MidiView) and IGuiView (GuiView). It is parametrized over how one
 * wants to represent notes (N) and pieces of music (P).
 */
public abstract class ACompositeView<N, P> implements SoundView<P>, IGuiView<N, P> {
  /**
   * Resumes the music.
   */
  abstract public void resume();

  /**
   * Pauses the music.
   */
  abstract public void pause();
}
