package com.odemehub;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * How a merchant and the gateway vouch for each other's messages. A body
 * travels as plain JSON and, next to it, two headers: the moment it was
 * signed, in Unix seconds ({@code X-Timestamp}), and an HMAC-SHA256 under the
 * merchant's secret ({@code X-Signature}) over that moment, the HTTP method,
 * the path and the exact text of the body, joined with newlines. The secret
 * itself never travels; a message whose signature does not match was not
 * written by the holder of the secret, or was changed on the way, and one
 * signed more than a few minutes ago is not taken either.
 *
 * <pre>
 * HMAC-SHA256(apiSecret, "{timestamp}\n{METHOD}\n{path}\n{body}")
 * </pre>
 *
 * <p>The path is the one in the address, with its leading slash and without
 * the query string; the body is the raw text as sent, the empty string for a
 * GET. This is the same calculation the application makes in its own
 * {@code ApiCredential::sign()}, so a message can be signed and checked by
 * hand with any HMAC-SHA256.
 *
 * <p>Test vector: with the secret {@code secret_test}, at {@code 1700000000},
 * {@code POST} to {@code /api/1000000001/gateway/regular-payment} with the
 * body {@code {"a":1}} signs as
 * {@code 4d6225c9dd46837418b40dd8140d76a24cd7520d81ff3b280bf98da8da6a8771}.
 */
public final class Signature {

    public static final String ALGORITHM = "HmacSHA256";

    /** The header both the request and the answer carry the signature in. */
    public static final String HEADER = "X-Signature";

    /** The header both the request and the answer carry the moment of signing in. */
    public static final String TIMESTAMP_HEADER = "X-Timestamp";

    /**
     * How far from now, either way, a signature's moment may lie and still be
     * taken, in seconds. The gateway allows the same.
     */
    public static final long TIMESTAMP_TOLERANCE = 300;

    private final SecretKeySpec key;

    public Signature(String apiSecret) {
        this.key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    /**
     * What a signature is taken over: the moment, the method, the path and
     * the body, each on its own line.
     */
    public static byte[] signedText(String method, String path, byte[] body, long timestamp) {
        byte[] head = (timestamp + "\n" + method.toUpperCase(Locale.ROOT) + "\n" + path + "\n").getBytes(StandardCharsets.UTF_8);
        byte[] text = new byte[head.length + body.length];
        System.arraycopy(head, 0, text, 0, head.length);
        System.arraycopy(body, 0, text, head.length, body.length);

        return text;
    }

    /**
     * The signature that vouches for a message: HMAC-SHA256 over the moment,
     * the method, the path and the exact bytes of the body, written as
     * lowercase hex.
     */
    public String sign(String method, String path, byte[] body, long timestamp) {
        return hmac(signedText(method, path, body, timestamp));
    }

    public String sign(String method, String path, String body, long timestamp) {
        return sign(method, path, body.getBytes(StandardCharsets.UTF_8), timestamp);
    }

    /**
     * Whether a signature vouches for a message, made recently enough to be
     * taken.
     *
     * @param timestamp The {@code X-Timestamp} header, as it arrived.
     * @param signature The {@code X-Signature} header, as it arrived.
     */
    public boolean verify(String method, String path, byte[] body, String timestamp, String signature) {
        if (timestamp == null || !timestamp.matches("[0-9]{1,18}") || signature == null) {
            return false;
        }

        long moment = Long.parseLong(timestamp);

        if (Math.abs(System.currentTimeMillis() / 1000 - moment) > TIMESTAMP_TOLERANCE) {
            return false;
        }

        return MessageDigest.isEqual(
            sign(method, path, body, moment).getBytes(StandardCharsets.US_ASCII),
            signature.getBytes(StandardCharsets.US_ASCII)
        );
    }

    public boolean verify(String method, String path, String body, String timestamp, String signature) {
        return verify(method, path, body.getBytes(StandardCharsets.UTF_8), timestamp, signature);
    }

    /**
     * The two headers that vouch for a message going out, made for now.
     */
    public Map<String, String> headers(String method, String path, byte[] body) {
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(TIMESTAMP_HEADER, Long.toString(timestamp));
        headers.put(HEADER, sign(method, path, body, timestamp));

        return headers;
    }

    private String hmac(byte[] text) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);

            return HexFormat.of().formatHex(mac.doFinal(text));
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new IllegalStateException("HMAC-SHA256 kullanılamıyor.", exception);
        }
    }

    @Override
    public String toString() {
        return "Signature[apiSecret=*****]";
    }
}
