package leddisplay.font;

/**
 * Enumeration representing a vertical deployment of character.
 *
 */
public enum VerticalDeployment {
	/**
	 * A vertical deployment to the bottom with font metric descent
	 */
	BOTTOM_FONT_DESCENT,
	
	/**
	 * A vertical deployment to the bottom with calculated descent
	 */
	BOTTOM_CALC_DESCENT,
	
	/**
	 * A vertical deployment to the center
	 */
	CENTER,
	
	/**
	 * A vertical deployment to the top
	 */
	TOP, 
	
	/**
	 * A vertical deployment to the bottom (without descent)
	 */
	BOTTOM

}
