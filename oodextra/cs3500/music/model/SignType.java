package cs3500.music.model;

/**
 * Represents an enumeration of sign type.
 * The type of repeat sign can only be one of these two types.
 */
public enum SignType {
  /**
   * Represents the start or begin sign in music (where the audio jumps from the end sign).
   */
  START_SIGN,
  /**
   * Represents the end sign in music (where the audio jumps back to begin sign when repeated)
   */
  END_SIGN;
}
