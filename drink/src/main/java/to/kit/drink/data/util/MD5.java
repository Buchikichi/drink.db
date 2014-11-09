package to.kit.drink.data.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {
	/** Unique instance. */
	private static final MD5 ME = new MD5();
	private MessageDigest md;

	private MD5() {
		try {
			this.md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// nop
		}
	}

	public static MD5 getInstance() {
		return ME;
	}

	public String digest(String input) {
		StringBuilder result = new StringBuilder();
		this.md.update(input.getBytes());
		byte[] bytes = this.md.digest();
		for (byte b : bytes) {
			result.append(String.format("%02x", Byte.valueOf(b)));
		}
		return result.toString();
	}
}
