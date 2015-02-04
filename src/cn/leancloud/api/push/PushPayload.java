package cn.leancloud.api.push;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/3
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class PushPayload implements PushModel {
    private static Gson _gson = new Gson();
    private String prod;
    private Map data;
    private Map where;
    private Map channels;

    public PushPayload(String prod, Map data, Map where, Map channels) {
        this.prod = prod;
        this.data = data;
        this.where = where;
        this.channels = channels;
    }

    public String toString() {
        return _gson.toJson(toJSON());
    }

    public Map toMap() {
        return _gson.fromJson(toJSON(), Map.class);
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if (prod != null) {
            json.addProperty("prod", prod);
        }
        if (data != null) {
            json.add("data", _gson.toJsonTree(data));
        }
        if (where != null) {
            json.add("where", _gson.toJsonTree(where));
        }
        if (channels != null) {
            json.add("channels", _gson.toJsonTree(channels));
        }
        return json;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Platform platform = null;
        private String prod;
        private Map data;
        private Map where;
        private Map channels;

        public Builder setPlatform(Platform platform) {
            this.platform = platform;
            return this;
        }

        public Builder setProd(String prod) {
            this.prod = prod;
            return this;
        }

        public Builder setProd(Boolean prod) {
            if (prod) {
                this.prod = "prod";
            } else {
                this.prod = "dev";
            }
            return this;
        }

        public Builder setData(Map data) {
            if (this.data != null) {
                this.data.putAll(data);
            } else {
                this.data = data;
            }
            return this;
        }

        public Builder setWhere(Map where) {
            this.where = where;
            return this;
        }

        public Builder setChannels(Map channels) {
            this.channels = channels;
            return this;
        }

        public Builder setAlert(String alert) {
            if (data == null) {
                data = new LinkedHashMap();
            }
            data.put("alert", alert);
            data.put("sound", "default");
            return this;
        }

        public Builder setObjectId(String objectId) {
            if (where == null) {
                where = new LinkedHashMap();
            }
            where.put("objectId", objectId);
            return this;
        }

        public PushPayload build() {
            return new PushPayload(prod, data, where, channels);
        }
    }

}
