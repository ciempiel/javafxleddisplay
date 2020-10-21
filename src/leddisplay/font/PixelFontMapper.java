package leddisplay.font;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixelFontMapper implements PixelFont {
	private static final Logger logger = LoggerFactory.getLogger(PixelFontMapper.class);
	
	private final int size;
	private final Font font;
	
	private HashMap<Character, PixelChar> buffered = new HashMap<>();

	public PixelFontMapper(String family, int size) {
		this.size = size;
		Font rawFont = new Font(family, Font.PLAIN, size);
		if (!rawFont.getFamily().equals(family)) {
			logger.warn(String.format("Could not find %s font. Using default.", family));
		}
		font = rawFont.deriveFont((float)size);
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

}
