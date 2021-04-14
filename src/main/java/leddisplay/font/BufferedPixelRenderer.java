package leddisplay.font;

import java.awt.Font;
import java.util.HashMap;

public class BufferedPixelRenderer implements PixelFont {
	private final PixelRenderer renderer;

	private HashMap<Character, PixelChar> buffered = new HashMap<>();

	public BufferedPixelRenderer(javafx.scene.text.Font font) {
		renderer = new PixelRenderer(font);
	}
	
	public BufferedPixelRenderer(Font font) {
		renderer = new PixelRenderer(font);
	}

	@Override
	public PixelChar getChar(char c) {
		if (buffered.containsKey(c)) {
			return buffered.get(c);
		}
		PixelChar pixelChar = renderMatrix(c);
		buffered.put(c, pixelChar);
		return pixelChar;
	}

	private PixelChar renderMatrix(char c) {
		return renderer.getChar(c);
	}

	@Override
	public PixelFontMetrics getMetrics() {
		return renderer.getMetrics();
	}

}
