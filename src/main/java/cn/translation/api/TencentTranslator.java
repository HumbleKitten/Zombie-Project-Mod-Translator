package cn.translation.api;

import cn.translation.entity.IdKey;
import com.alibaba.fastjson2.JSON;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TencentTranslator {
    // singleton client for connection reuse and better performance

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String secretId = "xxx";
        String secretKey = "xxx";
        String token = "";
        String service = "tmt";
        String version = "2018-03-21";
        String action = "TextTranslate";
        String sourceText = "apple";
        String source = "auto";
        String target = "zh";
        String body = "{\"SourceText\":\"" +
                sourceText +
                "\",\"Source\":\"" +
                source +
                "\",\"Target\":\"" +
                target +
                "\",\"ProjectId\":1322750}";
        String region = "ap-guangzhou";
        String resp = null;
        try {
            resp = doRequest(secretId, secretKey, service, version, action, body, region, token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> parse = (HashMap<String, Object>) JSON.parse(resp);
        if (parse != null && parse.get("Response") == null) {
            System.out.println(parse);
            System.exit(1);
        }
        String string = parse.get("Response").toString();
        HashMap<String, String> parse1 = (HashMap<String, String>) JSON.parse(string);
        String dst = parse1.get("TargetText");
        System.out.println(dst);
    }

    /**
     * 腾讯翻译 一秒5次调用
     *
     * @param sourceText 需要翻译的文本
     * @param source     文本源语言
     * @param target     翻译语言
     * @param idKey
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String tencentTranslator(String sourceText, String source, String target, IdKey idKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String secretId = idKey.getId();
        String secretKey = idKey.getKey();
        String token = "";
        String service = "tmt";
        String version = "2018-03-21";
        String action = "TextTranslate";
        String body = "{\"SourceText\":\"" +
                sourceText +
                "\",\"Source\":\"" +
                source +
                "\",\"Target\":\"" +
                target +
                "\",\"ProjectId\":1322750}";
        String region = "ap-guangzhou";
        return doRequest(secretId, secretKey, service, version, action, body, region, token);
    }

    private static final OkHttpClient client = new OkHttpClient();

    public static String doRequest(
            String secretId, String secretKey,
            String service, String version, String action,
            String body, String region, String token
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Request request = buildRequest(secretId, secretKey, service, version, action, body, region, token);
        Call call = client.newCall(request);
        Response response = call.execute();
        String string = response.body().string();
        response.close();
        return string;
    }

    public static Request buildRequest(
            String secretId, String secretKey,
            String service, String version, String action,
            String body, String region, String token
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String host = "tmt.tencentcloudapi.com";
        String endpoint = "https://" + host;
        String contentType = "application/json; charset=utf-8";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String auth = getAuth(secretId, secretKey, host, contentType, timestamp, body);
        return new Request.Builder()
                .header("Host", host)
                .header("X-TC-Timestamp", timestamp)
                .header("X-TC-Version", version)
                .header("X-TC-Action", action)
                .header("X-TC-Region", region)
                .header("X-TC-Token", token)
                .header("X-TC-RequestClient", "SDK_JAVA_BAREBONE")
                .header("Authorization", auth)
                .url(endpoint)
                .post(RequestBody.create(MediaType.parse(contentType), body))
                .build();
    }

    private static String getAuth(
            String secretId, String secretKey, String host, String contentType,
            String timestamp, String body
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:" + contentType + "\nhost:" + host + "\n";
        String signedHeaders = "content-type;host";

        String hashedRequestPayload = sha256Hex(body.getBytes(StandardCharsets.UTF_8));
        String canonicalRequest = "POST"
                + "\n"
                + canonicalUri
                + "\n"
                + canonicalQueryString
                + "\n"
                + canonicalHeaders
                + "\n"
                + signedHeaders
                + "\n"
                + hashedRequestPayload;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.valueOf(timestamp + "000")));
        String service = host.split("\\.")[0];
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest =
                sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        String stringToSign =
                "TC3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

        byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "tc3_request");
        String signature =
                printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
        return "TC3-HMAC-SHA256 "
                + "Credential="
                + secretId
                + "/"
                + credentialScope
                + ", "
                + "SignedHeaders="
                + signedHeaders
                + ", "
                + "Signature="
                + signature;
    }

    public static String sha256Hex(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(b);
        return printHexBinary(d).toLowerCase();
    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static byte[] hmac256(byte[] key, String msg) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }
}