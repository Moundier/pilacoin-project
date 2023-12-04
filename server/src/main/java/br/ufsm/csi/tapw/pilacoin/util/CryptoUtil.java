package br.ufsm.csi.tapw.pilacoin.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String ENCRYPTION_ALGORITHM = "RSA";
    private static final SecureRandom secureRandom = new SecureRandom();;

    @SneakyThrows
    public static boolean compareHash(String json, BigInteger hash) {
        BigInteger calculatedHash = CryptoUtil.hash(json);

        return calculatedHash.compareTo(hash) < 0;
    }

    @SneakyThrows
    public static byte[] sign(String json, PrivateKey privateKey) {
        Cipher encryptCipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        byte[] hashByteArr = CryptoUtil.digest(json);

        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return encryptCipher.doFinal(hashByteArr);
    }

    @SneakyThrows
    public static byte[] digest(String json) {
        return CryptoUtil.digest(json.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static byte[] digest(byte[] byteArray) {
        MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);

        return md.digest(byteArray);
    }

    public static BigInteger hash(String json) {
        return new BigInteger(CryptoUtil.digest(json)).abs();
    }

    public static BigInteger hash(byte[] byteArray) {
        return new BigInteger(CryptoUtil.digest(byteArray)).abs();
    }

    public static String getRandomNonce() {
        byte[] byteArray = new byte[256 / 8];

        CryptoUtil.secureRandom.nextBytes(byteArray);

        return CryptoUtil.hash(byteArray).toString();
    }

    public static byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
