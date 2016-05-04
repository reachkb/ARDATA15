package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


public class Encryptor {
	final static Logger logger = Logger.getLogger(Encryptor.class);
	
    private static String key1 = "Bar12345Bar12345"; // 128 bit key
    private static String key2 = "ThisIsASecretKet";
    
	public static String encrypt( String value ) {
		return encrypt( Encryptor.key1, Encryptor.key2, value);
	}
	
	public static String encrypt(String key1, String key2, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes());
			//System.out.println("encrypted string:" + Base64.encodeBase64String(encrypted));
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt( String encrypted ) {
		return decrypt( Encryptor.key1, Encryptor.key2, encrypted);
	}
	
	public static String decrypt(String key1, String key2, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
    public static void main(String[] args) {
        //System.out.println(decrypt(key1, key2, encrypt(key1, key2, "TestPass123")));
        //System.out.println("****" + encrypt("TestPass123") + "****");
        System.out.println(encrypt("TestPass123"));
        System.out.println(decrypt("ziecCwfXs+iETIYiNhxevw=="));
    }
}
