package cs3500.music.controller;

import org.junit.Test;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the mouse handler.
 */
public class MouseHandlerTest {
  @Test
  public void testClick() {
    StringBuilder ap = new StringBuilder("");
    Map<Integer, Consumer<Point>> mp = new HashMap<Integer, Consumer<Point>>();
    mp.put(KeyEvent.VK_BACK_SPACE, (Point pt) -> ap.append("RAN here: " + pt.toString()));
    Point test = new Point(0, 0);
    new MouseHandler(null, null, null, null, null).clickHelper(test, KeyEvent.VK_ADD, mp);
    assertEquals("", ap.toString());
    new MouseHandler(null, null, null, null, null).clickHelper(test, KeyEvent.VK_BACK_SPACE, mp);
    assertEquals("RAN here: java.awt.Point[x=0,y=0]", ap.toString());
  }
}
