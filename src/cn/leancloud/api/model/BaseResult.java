package cn.leancloud.api.model;

import cn.leancloud.api.http.ResponseWrapper;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/1
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class BaseResult {
    @Expose
    private Date createdAt;
    @Expose
    private Date updatedAt;
    @Expose
    private String objectId;


    protected static Gson _gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerUtil()).excludeFieldsWithoutExposeAnnotation().create();
    private ResponseWrapper responseWrapper;

    public static <T extends BaseResult> T fromResponse(
            ResponseWrapper responseWrapper, Class<T> clazz) {
        T result = null;

        if (responseWrapper.isServerResponse()) {
            result = _gson.fromJson(responseWrapper.responseContent, clazz);
        } else {
            try {
                result = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        result.setResponseWrapper(responseWrapper);
        return result;
    }

    public void setResponseWrapper(ResponseWrapper responseWrapper) {
        this.responseWrapper = responseWrapper;
    }

    public String toString() {
        return _gson.toJson(this);
    }

    private static class DateSerializerUtil implements JsonSerializer<Date>, JsonDeserializer<Date> {

        @Override
        public JsonElement serialize(Date date, Type type,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(date.getTime());
        }

        @Override
        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return df.parse(jsonElement.getAsJsonPrimitive().getAsString());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
