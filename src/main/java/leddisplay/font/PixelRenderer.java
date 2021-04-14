package leddisplay.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixelRenderer implements PixelFont {
	private static final Logger logger = LoggerFactory.getLogger(PixelRenderer.class);
	
	private final Font font;
	private FontMetrics metrics;
	private PixelFontMetrics pixelMetrics;

	public PixelRenderer(javafx.scene.text.Font font) {
		int size = (int)font.getSize();
		int style = Font.PLAIN;
		boolean bold = font.getName().contains("Bold");
		if (bold) {
			style |= Font.BOLD;
		}
		boolean italic = font.getName().contains("Italic");
		if (italic) {
			style |= Font.ITALIC;
		}
		Font rawFont = new Font(font.getFamily(), style, size);
		if (!rawFont.getFamily().equals(font.getFamily())) {
			logger.warn(String.format("Could not find %s font. Using default.", font.getFamily()));
		}
		this.font = rawFont.deriveFont((float) size);
		init();
	}
	
	public PixelRenderer(Font font) {
		super();
		this.font = font;
		init();
	}
	
	private void init() {
		initMetrics();
		initPixelMetrics();
	}

	private void initMetrics() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		metrics = g2d.getFontMetrics();
		g2d.dispose();
	}
	
	private void initPixelMetrics() {
		StringBuilder builder = new StringBuilder();
		for (char ch = 0 ; ch < 256 ; ch++) {
			builder.append(ch);
		}
		PixelsMatrix matrix = renderText(builder.toString());
		int emptyRowsCountBottom = matrix.getEmptyRowsCountBottom();
		int calcDescent = matrix.getHeigth() - metrics.getAscent() - emptyRowsCountBottom;
		pixelMetrics = new PixelFontMetrics(metrics.getHeight(), metrics.getAscent(), metrics.getDescent(), calcDescent);
	}
	
	@Override
	public PixelChar getChar(char c) {
		return renderText(Character.toString(c));
	}
	
	private PixelsMatrix renderText(String text) {
		int width = metrics.stringWidth(text);
		int height = metrics.getHeight();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		g2d.drawString(text, 0, metrics.getAscent());
		g2d.dispose();
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
//		printPixels(pixels, width, height);
		return new PixelsMatrix(pixels, height);
	}

	public PixelFontMetrics getMetrics() {
		return pixelMetrics;
	}
	
}