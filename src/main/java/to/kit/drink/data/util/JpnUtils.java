package to.kit.drink.data.util;

public class JpnUtils {
	public static String toHiragana(String str) {
		StringBuilder buff = new StringBuilder();
		int diff = 'ァ' - 'ぁ';

		for (char ch : str.toCharArray()) {
			boolean isExclude = 'ー' == ch || '・' == ch;

			if (!isExclude
					&& Character.UnicodeBlock.KATAKANA
							.equals(Character.UnicodeBlock.of(ch))) {
				int code = ch - diff;
				buff.append(((char) code));
				continue;
			}
			buff.append(ch);
		}
		return buff.toString();
	}

	public static void main(String[] args) throws Exception {
		for (char c = 'ァ'; c <= 'ー'; c++) {
			System.out.println(c + JpnUtils.toHiragana(String.valueOf(c)));
		}
		System.out.println(JpnUtils.toHiragana("ピルスナー・ウルケル(元祖)"));
	}
}
