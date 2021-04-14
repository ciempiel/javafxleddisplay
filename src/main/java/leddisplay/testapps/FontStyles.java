package leddisplay.testapps;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.scene.text.Font;

// From SceneBuilder
class FontStyles {
	private Map<String, Map<String, Font>> fonts;

	public Set<String> getStyles(String family) {
		Map<String, Font> styles = getFontMap().get(family);
		if (styles == null) {
			styles = Collections.emptyMap();
		}
		return styles.keySet();
	}

	public static String getPreviewString(Font font) {
		String size = valAsStr(font.getSize());
		return font.getFamily() + " " + size + "px" // NOI18N
				+ (!font.getName().equals(font.getFamily()) && !"Regular".equals(font.getStyle()) ? // NOI18N
				" (" + font.getStyle() + ")" : ""); // NOI18N
	}

	static String valAsStr(Object val) {
		if (val == null) {
			return null;
		}
		String str = val.toString();
		if ((val instanceof Double) && str.endsWith(".0")) { // NOI18N
			str = str.substring(0, str.length() - 2);
		}
		return str;
	}

	private Map<String, Map<String, Font>> getFontMap() {
		if (fonts == null) {
			fonts = makeFontMap();
		}
		return fonts;
	}

	private static Map<String, Map<String, Font>> makeFontMap() {
		final Set<Font> fonts = getAllFonts();
		final Map<String, Map<String, Set<Font>>> fontTree = new TreeMap<>();

		for (Font f : fonts) {
			Map<String, Set<Font>> familyStyleMap = fontTree.get(f.getFamily());
			if (familyStyleMap == null) {
				familyStyleMap = new TreeMap<>();
				fontTree.put(f.getFamily(), familyStyleMap);
			}
			Set<Font> styleFonts = familyStyleMap.get(f.getStyle());
			if (styleFonts == null) {
				styleFonts = new HashSet<>();
				familyStyleMap.put(f.getStyle(), styleFonts);
			}
			styleFonts.add(f);
		}

		final Map<String, Map<String, Font>> res = new TreeMap<>();
		for (Map.Entry<String, Map<String, Set<Font>>> e1 : fontTree.entrySet()) {
			final String family = e1.getKey();
			final Map<String, Set<Font>> styleMap = e1.getValue();
			final Map<String, Font> resMap = new TreeMap<>();
			for (Map.Entry<String, Set<Font>> e2 : styleMap.entrySet()) {
				final String style = e2.getKey();
				final Set<Font> fontSet = e2.getValue();
				int size = fontSet.size();
				assert 1 <= size;
				resMap.put(style, styleMap.get(style).iterator().next());
			}
			res.put(family, Collections.<String, Font> unmodifiableMap(resMap));
		}
		return Collections.<String, Map<String, Font>> unmodifiableMap(res);
	}

	private static Set<Font> getAllFonts() {
		Font f = Font.getDefault();
		double defSize = f.getSize();
		Set<Font> allFonts = new TreeSet<>(fontComparator);
		for (String familly : Font.getFamilies()) {
			// System.out.println("*** FAMILY: " + familly); //NOI18N
			for (String name : Font.getFontNames(familly)) {
				Font font = new Font(name, defSize);
				allFonts.add(font);
				// //NOI18N
			}
		}
		// some font will not appear with the code above: we also need to use getAllNames!
		for (String name : Font.getFontNames()) {
			Font font = new Font(name, defSize);
			allFonts.add(font);
		}
		return allFonts;
	}

	private static final Comparator<Font> fontComparator = (t, t1) -> {
		int cmp = t.getName().compareTo(t1.getName());
		if (cmp != 0) {
			return cmp;
		}
		return t.toString().compareTo(t1.toString());
	};
}