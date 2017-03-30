package trd.algorithms.security;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import trd.algorithms.utilities.Tuples;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyGen {
	public static String SerializeKey(SecretKey key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public static SecretKey DeSerializeKey(String stringKey) {
		byte[] decodedKey = Base64.getDecoder().decode(stringKey);
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		return originalKey;
	}

	public static SecretKey GenerateSymmetricKey() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom();
			keyGen.init(random);
			SecretKey secretKey = keyGen.generateKey();
			return secretKey;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Tuples.Pair<PrivateKey, PublicKey> GeneratePrivatePublicKeyPair(String algorithm, int size) {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "BC");
			keyGen.initialize(size, random);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();
			return new Tuples.Pair<PrivateKey, PublicKey>(priv, pub);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	public static void main(String[] args) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			// Generate Key
			SecretKey key = GenerateSymmetricKey();
			String data = "I am a good boy";
			
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(), "AES"));	
			byte[] encrypted = cipher.doFinal((data).getBytes());
			System.out.printf("encrypted string: %s\n", Arrays.toString(encrypted));	
			SecretKey key2 = DeSerializeKey(SerializeKey(key));			
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key2.getEncoded(), "AES"));
			byte[] original = cipher.doFinal(encrypted);
			String originalString = new String(original);
			System.out.println("decrypted string: " + originalString);
			
			String[]  	Algos = new String[]  {"RSA", "EC"};
			Integer[] 	Sizes = new Integer[] {1024, 256};
			String[] 	CipherSuites = new String[]  {"RSA/ECB/PKCS1Padding", "ECDH"};
			
			for (int i = 0; i < Algos.length; i++) {
				Tuples.Pair<PrivateKey, PublicKey> asymKey = GeneratePrivatePublicKeyPair(Algos[i], Sizes[i]);
				PrivateKey priv = asymKey.elem1;
				PublicKey pub = asymKey.elem2;
				String cipherMode = CipherSuites[i];
				Cipher encrypt=Cipher.getInstance(cipherMode);
				encrypt.init(Cipher.ENCRYPT_MODE, pub);
				encrypted = encrypt.doFinal(data.getBytes(StandardCharsets.UTF_8));			
				System.out.printf("encrypted string: %s\n", Arrays.toString(encrypted));	
				Cipher decrypt=Cipher.getInstance(cipherMode);
				decrypt.init(Cipher.DECRYPT_MODE, priv);
				originalString = new String(decrypt.doFinal(encrypted), StandardCharsets.UTF_8);			
				System.out.println("decrypted string: " + originalString);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
