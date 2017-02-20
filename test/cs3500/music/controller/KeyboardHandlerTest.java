package cs3500.music.controller;

import org.junit.Test;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the keyboard handler.
 */
public class KeyboardHandlerTest {
  @Test
  public void testType() {
    StringBuilder ap = new StringBuilder("");
    Map<Integer, Runnable> mp = new HashMap<Integer, Runnable>();
    mp.put(KeyEvent.VK_BACK_SPACE, () -> ap.append("RAN"));
    new KeyboardHandler(null, null, null).keyHelper(mp, KeyEvent.VK_ADD);
    assertEquals("", ap.toString());
    new KeyboardHandler(null, null, null).keyHelper(mp, KeyEvent.VK_BACK_SPACE);
    assertEquals("RAN", ap.toString());
  }
}
