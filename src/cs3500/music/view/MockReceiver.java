package cs3500.music.view;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * Represents a MockMidiReceiver.
 */
public class MockReceiver implements Receiver {
  private final StringBuilder ap;

  /**
   * Constructs a MockMidiReceiver with a StringBuilder.
   * @param ap   a StringBuilder.
   */
  public MockReceiver(StringBuilder ap) {
    this.ap = ap;
  }

  /**
   * Sends a MIDI message and time-stamp to this receiver. If time-stamping is not supported by this
   * receiver, the time-stamp value should be -1.
   *
   * @param message   the MIDI message to send
   * @param timeStamp the time-stamp for the message, in microseconds.
   */
  @Override
  public void send(MidiMessage message, long timeStamp) {
    ap.append(Arrays.toString(message.getMessage())).append(" ").append(timeStamp).append("\n");
  }

  /** Informs the client that the application has finished using the receiver.
   * The limited resources it requires may be released or made available.
   */
  @Override
  public void close() {
    ap.append("close\n");
  }
}
