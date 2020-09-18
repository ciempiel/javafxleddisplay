package leddisplay.font;

import java.util.HashMap;
import java.util.Map;

/**
 * Test font 5 x 7 pixels, only A, B & C
 * @author Przemek
 *
 */
public class Test5x7PixelFontABC implements PixelFont {
	@SuppressWarnings("serial")
	private static final Map<Character, int[][]> pixelCharMap = new HashMap<Character, int[][]>() {
		{
			put('A', new int[][] { 
				{ 0, 1, 1, 1, 0 }, 
				{ 1, 0, 0, 0, 1 }, 
				{ 1, 0, 0, 0, 1 }, 
				{ 1, 1, 1, 1, 1 }, 
				{ 1, 0, 0, 0, 1 }, 
				{ 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1 } 
			});
			put('B', new int[][] {
				{ 1, 1, 1, 1, 0 },
				{ 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1 },
				{ 1, 1, 1, 1, 0 },
				{ 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1 },
				{ 1, 1, 1, 1, 0 }
			});
			put('C', new int[][] {
				{ 0, 1, 1, 1, 0 },
				{ 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 1 },
				{ 0, 1, 1, 1, 0 }
			});
		}
	};

	@Override
	public PixelChar getChar(char c) {
		int[][] pixelChar = pixelCharMap.get(c);
		if (pixelChar != null) {
			return new Int2DArrayPixelChar(pixelChar);
		}
		return new EmptyPixelChar();
	}

}