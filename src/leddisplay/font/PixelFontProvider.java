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

public class PixelFontProvider implements PixelFont {
	private static final Logger logger = LoggerFactory.getLogger(PixelFontProvider.class);

	private final int size;
	private final Font font;
	private final Dimension targetDimension;

	private HashMap<Character, PixelChar> buffered = new HashMap<>();

	public PixelFontProvider(javafx.scene.text.Font font, int targetWidth, int targetHeight) {
		this.size = (int)font.getSize();
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
		targetDimension = new Dimension(targetWidth, targetHeight);
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
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getHeight();
		g2d.dispose();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		System.out.println(fm.toString());
		g2d.setColor(Color.BLACK);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		System.out.println("Pixels: " + pixels.length);
		System.out.println("-----------");
		printFontMetrics(fm);
		System.out.println("-----------");
		
		printPixels(pixels, width, height);
		PixelChar result = new IndexOutOfBoundsGuardPixelChar(new PixelCharDeployer(pixels, height, fm, targetDimension));
		printPixelChar(result);
		return result;
	}
	
	private static void printFontMetrics(FontMetrics fontMetrics) {
		System.out.println("FontMetrics:");
		System.out.println("Ascent: " + fontMetrics.getAscent());
		System.out.println("Descent: " + fontMetrics.getDescent());
		System.out.println("Height: " + fontMetrics.getHeight());
		System.out.println("Leading: " + fontMetrics.getLeading());
		System.out.println("MaxAdvance: " + fontMetrics.getMaxAdvance());
		System.out.println("MaxAscent: " + fontMetrics.getMaxAscent());
		System.out.println("MaxDescent: " + fontMetrics.getMaxDescent());
	}
	
	private static void printPixelChar(PixelChar pixelChar) {
		for (int y = 0; y < pixelChar.getHeigth(); y++) {
			for (int x = 0; x < pixelChar.getWidth(); x++) {
				if (pixelChar.isPixelSet(x, y)) {
					System.out.print('+');
				} else {
					System.out.print('.');
				}
			}
			System.out.println();
		}
	}

	private static void printPixels(int[] pixels, int width, int height) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int index = y * width + x;
				if (pixels[index] != 0) {
					System.out.print('+');
				} else {
					System.out.print('.');
				}
			}
			System.out.println();
		}
	}

}
