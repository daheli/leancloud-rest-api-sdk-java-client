package cn.leancloud.api;


import cn.leancloud.api.exception.APIException;
import cn.leancloud.api.http.NativeHttpClient;
import cn.leancloud.api.http.ResponseWrapper;
import cn.leancloud.api.model.BaseResult;
import cn.leancloud.api.model.LCInstallation;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/1/31
 * Time: 下午8:38
 * To change this template use File | Settings | File Templates.
 */
public class LCClient {
    private static final Logger LOG = Logger.getLogger(LCClient.class);
    public final static String API_URL = "https://leancloud.cn/1.1/";
    public final static String MODULE_INSTALLATIONS_PATH = "installations";
    public final static String MODULE_PUSH_PATH = "push";

    private String id;
    private String key;
    private NativeHttpClient client;
    private boolean apnsProduction = false;

    public LCClient(String id, String key) {
        this(id, key, false);
    }

    public LCClient(String id, String key, boolean apnsProduction) {
        this.id = id;
        this.key = key;
        this.apnsProduction = apnsProduction;
        client = new NativeHttpClient(id, key);
    }


    public ResponseWrapper post(String path, Map data) throws APIException {
        String url = API_URL + path;
        String contont = new Gson().toJson(data);
        LOG.debug("post content:" + contont);
        return client.sendPost(url, contont);
    }

    public ResponseWrapper put(String path, Map data) throws APIException {
        String url = API_URL + path;
        String contont = new Gson().toJson(data);
        LOG.debug("put content:" + contont);
        return client.sendPost(url, contont);
    }

    public ResponseWrapper get(String path) throws APIException {
        String url = API_URL + path;
        return client.sendGet(url);
    }

    public ResponseWrapper delete(String path) throws APIException {
        String url = API_URL + path;
        return client.sendDelete(url);
    }

    public LCInstallation installationsCreate(Map data) throws APIException {
        ResponseWrapper res = post(MODULE_INSTALLATIONS_PATH, data);
        return LCInstallation.fromResponse(res, LCInstallation.class);
    }

    public BaseResult pushIosMessageWithInstallationId(String alert, String objectId) throws APIException {
        Map map = new LinkedHashMap(); //should move to payload
        if (apnsProduction) {
            map.put("prod", "prod");
        } else {
            map.put("prod", "dev");
        }

        Map data = new LinkedHashMap(); //should move to payload
        data.put("alert", alert);
        data.put("sound", "default");
        map.put("data", data);

        Map where = new LinkedHashMap(); //should move to payload
        where.put("objectId", objectId);
        map.put("where", where);

        ResponseWrapper res = post(MODULE_PUSH_PATH, map);
        LOG.debug(res.responseContent);
        return BaseResult.fromResponse(res, BaseResult.class);
    }


}
