package cs3500.music.model;

/**
 * Represents a repeat sign in the piece of music.
 */
public class RepeatSign {
  private SignType type;
  private int beat;
  private int status = 1;

  /**
   * Constructs a repeat sign with its enumerate type and status initialized to '1' meaning this
   * sign has not yet been repeated.
   * @param type  type of repeat sign
   */
  public RepeatSign(SignType type, int beat) {
    this.type = type;
    this.beat = beat;
  }

  /**
   * Gets the status of this repeat sign.
   * @return an integer representing whether or not this sign has been repeated.
   */
  public int getStatus() {
    return this.status;
  }

  /**
   * Updates the value of status. The value can only be 0 or 1.
   * @param i  a number 0 or 1 representing the status the repeat sign.
   */
  public void updateStatus(int i) {
    if (i == 0 || i == 1) {
      this.status = i;
    } else {
      throw new IllegalArgumentException("Invalid status value.");
    }

  }

  /**
   * Gets the beat of this repeat sign.
   * @return a natural number representing the beat.
   */
  public int getBeat() {
    return this.beat;
  }

  /**
   * Gets the type of this repeat sign.
   * @return  an enumerate type of this sign.
   */
  public SignType getType() {
    return this.type;
  }

  /**
   * Updates the type of this repeat sign.
   * @param type  an enumerate type of this sign.
   */
  public void updateType(SignType type) {
    this.type = type;
  }


}
