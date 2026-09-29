package com.odemehub;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * How a merchant and the gateway vouch for each other's bodies. A body
 * travels as plain JSON and, next to it in the {@code X-Signature} header, an
 * HMAC-SHA256 of that exact text under the merchant's secret. The secret
 * itself never travels; a body whose signature does not match was not
 * written by the holder of the secret, or was changed on the way.
 *
 * <p>This is the same calculation the application makes in its own
 * {@code Services\Gateway\Signer}. Nothing is layered on top, so a body can
 * be signed and checked by hand with any HMAC-SHA256.
 *
 * <p>Test vector: with the secret {@code secret_test} the body
 * {@code {"a":1}} is signed
 * {@code 6d0c951564cdd2b6b70e75b214293a8cd2542815ba54fe91c7f6ce105bc3d592}.
 */
public final class Signature {

    public static final String ALGORITHM = "HmacSHA256";

    /** The header both the request and the answer carry the signature in. */
    public static final String HEADER = "X-Signature";

    private final SecretKeySpec key;

    public Signature(String apiSecret) {
        this.key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    /**
     * The signature that vouches for a body: HMAC-SHA256 over the exact
     * bytes, written as lowercase hex.
     */
    public String sign(byte[] body) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);

            return HexFormat.of().formatHex(mac.doFinal(body));
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new IllegalStateException("HMAC-SHA256 kullanılamıyor.", exception);
        }
    }

    public String sign(String body) {
        return sign(body.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Whether a signature vouches for a body.
     */
    public boolean verify(byte[] body, String signature) {
        return signature != null
            && MessageDigest.isEqual(
                sign(body).getBytes(StandardCharsets.US_ASCII),
                signature.getBytes(StandardCharsets.US_ASCII)
            );
    }

    public boolean verify(String body, String signature) {
        return verify(body.getBytes(StandardCharsets.UTF_8), signature);
    }

    @Override
    public String toString() {
        return "Signature[apiSecret=*****]";
    }
}
