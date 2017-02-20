package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * This class is interested in processing a keyboard event. It handles the input signalled by the
 * keyboard.
 */
public class KeyboardHandler implements KeyListener {
  private Map<Integer, Runnable> typeMap;
  private Map<Integer, Runnable> pressMap;
  private Map<Integer, Runnable> releaseMap;

  /**
   * A default constructor taking all of the inputs.
   *
   * @param typeMap - map of runnables for types
   * @param pressMap - map of runnables for presses
   * @param releaseMap - map of runnables for release
   */
  public KeyboardHandler(Map<Integer, Runnable> typeMap, Map<Integer, Runnable> pressMap,
                         Map<Integer, Runnable> releaseMap) {
    this.typeMap = typeMap;
    this.pressMap = pressMap;
    this.releaseMap = releaseMap;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    keyHelper(typeMap, e.getExtendedKeyCode());
  }

  @Override
  public void keyPressed(KeyEvent e) {
    keyHelper(pressMap, e.getExtendedKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keyHelper(releaseMap, e.getExtendedKeyCode());
  }

  void keyHelper(Map<Integer, Runnable> mp, int keyCode) {
    Runnable r = mp.get(keyCode);
    if (r != null) {
      r.run();
    }
  }
}
