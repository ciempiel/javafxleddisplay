package leddisplay;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class CharPrinterTest {
	private CharPrinter printer;

	@Before
	public void setUp() throws Exception {
		printer = new CharPrinter(3, 16);
	}

	@Test
	public void testEmptyText() {
		assertEquals("\r\n\r\n", printer.getText());
	}

	@Test
	public void testLinePrint() {
		printer.printLine(0, "Line 1");
		printer.printLine(1, "Line 2");
		printer.printLine(2, "Line 3");
		assertEquals("Line 1\r\nLine 2\r\nLine 3", printer.getText());
	}

	@Test
	public void testLine() {
		printer.print(8, 0, "123");
		printer.print(8, 1, "456");
		printer.print(8, 2, "789");
		assertEquals("        123\r\n        456\r\n        789", printer.getText());
	}

	@Test
	public void testLineTrimEnd() {
		printer.setText("Line 1   \r\nLine 2    \r\nLine 3     ");
		assertEquals("Line 1\r\nLine 2\r\nLine 3", printer.getText());
	}

	@Test
	public void testCharPrint() {
		printer.initChanges();
		printer.setText("abc\r\ndef\r\n   ghj");

		@SuppressWarnings("serial")
		Queue<ChangeEntry> changes = new LinkedList<ChangeEntry>() {
			{
				add(new ChangeEntry(0, 0, 'a'));
				add(new ChangeEntry(1, 0, 'b'));
				add(new ChangeEntry(2, 0, 'c'));
				add(new ChangeEntry(0, 1, 'd'));
				add(new ChangeEntry(1, 1, 'e'));
				add(new ChangeEntry(2, 1, 'f'));
				add(new ChangeEntry(3, 2, 'g'));
				add(new ChangeEntry(4, 2, 'h'));
				add(new ChangeEntry(5, 2, 'j'));
			}
		};
		printer.consumeChanges((posX, posY, c) -> {
			ChangeEntry change = changes.poll();
			boolean result = change.compare(posX, posY, c);
			assertTrue(result);
		});
		assertTrue(changes.isEmpty());
	}
	
	@Test
	public void testRefreshDisplay() {
		printer.print(8, 1, "A");
		
		Queue<ChangeEntry> changes = new LinkedList<ChangeEntry>();
		for (int y=0; y<3; y++) {
			for (int x=0; x<16; x++) {
				if (x == 8 && y == 1) {
					changes.add(new ChangeEntry(8, 1, 'A'));
				} else {
					changes.add(new ChangeEntry(x, y, ' '));
				}
			}
		}
		printer.consumeAll((posX, posY, c) -> {
			ChangeEntry change = changes.poll();
			boolean result = change.compare(posX, posY, c);
			assertTrue(result);
		});
		assertTrue(changes.isEmpty());
	}

}
