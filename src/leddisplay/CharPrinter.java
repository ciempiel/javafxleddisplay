package leddisplay;

import java.util.Arrays;
import java.util.LinkedList;

class CharPrinter {
	private final int lineCount, charCount;
	private char[] buffer;
	private boolean[] changes;
	
	public CharPrinter(int lineCount, int charCount) {
		super();
		this.lineCount = lineCount;
		this.charCount = charCount;
		initArrays();
	}
	
	public void print(int posX, int posY, String text) {
		int index = getBufferIndex(posX, posY);
		int length = Math.min(text.length(), charCount - posX);
		for (int i=0; i<length; i++) {
			updateBuffer(index + i, text.charAt(i));
		}
	}
	
	public void printLine(int posY, String text) {
		int length = Math.min(charCount, text.length());
		for (int i=0; i<length; i++) {
			int index = getBufferIndex(i, posY);
			char c = text.charAt(i);
			updateBuffer(index, c);
		}
		for (int i=length; i<charCount; i++) {
			int index = getBufferIndex(i, posY);
			updateBuffer(index, ' ');
		}
	}
	
	public void clearLine(int posY) {
		for (int i=0; i<charCount; i++) {
			int index = getBufferIndex(i, posY);
			updateBuffer(index, ' ');
		}
	}

	public void setText(String text) {
		if (text != null) {
			String[] splited = text.split(System.lineSeparator());
			int length = Math.min(lineCount, splited.length);
			for (int i=0; i<length; i++) {
				printLine(i, splited[i]);
			}
			for (int i=length; i<lineCount; i++) {
				clearLine(i);
			}
		} else {
			for (int i=0; i<lineCount; i++) {
				clearLine(i);
			}
		}
	}
	
	public String getText() {
		LinkedList<String> lines = new LinkedList<>();
		for (int i=0; i<lineCount; i++) {
			String line = new String(buffer, i * charCount, charCount);
			lines.add(line.replaceAll("\\s+$", ""));
		}
		return String.join(System.lineSeparator(), lines);
	}
	
	public void initChanges() {
		Arrays.fill(changes, false);
	}
	
	public void consumeChanges(CharChangeListener listener) {
		for (int i=0; i<changes.length; i++) {
			if (changes[i]) {
				int posX = i % charCount;
				int posY = i / charCount;
				char c = buffer[i];
				listener.update(posX, posY, c);
			}
		}
	}
	
	public void consumeAll(CharChangeListener listener) {
		for (int i=0; i<buffer.length; i++) {
			int posX = i % charCount;
			int posY = i / charCount;
			char c = buffer[i];
			listener.update(posX, posY, c);
		}
	}
	
	private void updateBuffer(int index, char c) {
		if (buffer[index] != c) {
			buffer[index] = c;
			changes[index] = true;
		}
	}
	
	private void initArrays() {
		buffer = new char[lineCount * charCount];
		Arrays.fill(buffer, ' ');
		changes = new boolean[lineCount * charCount];
	}
	
	private int getBufferIndex(int posX, int posY) {
		return posY * charCount + posX;
	}
	
}