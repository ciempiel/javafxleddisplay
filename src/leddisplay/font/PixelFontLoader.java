package leddisplay.font;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixelFontLoader implements PixelFont {
	private static final Logger logger = LoggerFactory.getLogger(PixelFontLoader.class);
	
	private final String name;
	private final int size;
	
	private HashMap<Character, PixelChar> buffered = new HashMap<>();
	private Font font;

	public PixelFontLoader(String name, int size) {
		this.name = name;
		this.size = size;
		loadFont();
	}

	@Override
	public PixelChar getChar(char c) {
		if (font == null) {
			return new EmptyPixelChar();
		}
		if (buffered.containsKey(c)) {
			return buffered.get(c);
		}
		PixelChar pixelChar = renderMatrix(c);
		buffered.put(c, pixelChar);
		return pixelChar;
	}

	private PixelChar renderMatrix(char c) {
		String text = Character.toString(c);
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getHeight();
		g2d.dispose();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setColor(Color.BLACK);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		return new IntArrayPixelChar(pixels, new Dimension(width, height), size);
	}

	private void loadFont() {
		try {
			InputStream stream = new FileInputStream(name);
			Font rawFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			font = rawFont.deriveFont((float)size);
		} catch (FontFormatException | IOException e) {
			logger.error("Unable to load font.", e);
		}
	}
}
