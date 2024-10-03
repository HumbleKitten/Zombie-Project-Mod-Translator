package cn.translation.api;

import cn.translation.entity.IdKey;
import cn.translation.tool.HttpGet;
import cn.translation.tool.MD5;

import java.util.HashMap;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    private static String appid;
    private static String securityKey;

    /**
     * 百度翻译 一秒10次调用
     *
     * @param query 需要翻译的文本
     * @param from  文本源语言
     * @param to    翻译语言
     * @param idKey id key配置
     * @return
     */
    public String getTransResult(String query, String from, String to, IdKey idKey) {
        appid = idKey.getId();
        securityKey = idKey.getKey();
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
