package cs3500.music.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class is interested in processing a mouse event. It handles the input signalled by the
 * mouse.
 */
public class MouseHandler implements MouseListener {
  private Map<Integer, Consumer<Point>> clickMap;
  private Map<Integer, Consumer<Point>> pressMap;
  private Map<Integer, Consumer<Point>> releaseMap;
  private Map<Integer, Consumer<Point>> enterMap;
  private Map<Integer, Consumer<Point>> exitMap;

  /**
   * A default constructor taking all of the fields.
   *
   * @param clickMap - map of consumers for clicks
   * @param pressMap - map of consumers for presses
   * @param releaseMap - map of consumers for releases
   * @param enterMap - map of consumers for enters
   * @param exitMap - map of consumers for exits
   */
  public MouseHandler(Map<Integer, Consumer<Point>> clickMap,
                      Map<Integer, Consumer<Point>> pressMap,
                      Map<Integer, Consumer<Point>> releaseMap,
                      Map<Integer, Consumer<Point>> enterMap,
                      Map<Integer, Consumer<Point>> exitMap) {
    this.clickMap = clickMap;
    this.pressMap = pressMap;
    this.releaseMap = releaseMap;
    this.enterMap = enterMap;
    this.exitMap = exitMap;
  }

  void clickHelper(Point point, int button, Map<Integer, Consumer<Point>> mp) {
    Consumer<Point> c = mp.get(button);
    if (c != null) {
      c.accept(point);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    clickHelper(e.getPoint(), e.getButton(), clickMap);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    clickHelper(e.getPoint(), e.getButton(), pressMap);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    clickHelper(e.getPoint(), e.getButton(), releaseMap);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    clickHelper(e.getPoint(), e.getButton(), enterMap);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    clickHelper(e.getPoint(), e.getButton(), exitMap);
  }
}
