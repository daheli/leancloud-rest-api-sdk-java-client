package cn.leancloud.api.model;

import cn.leancloud.api.http.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/1
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class BaseModel {
    protected static Gson _gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private ResponseWrapper responseWrapper;

    public static <T extends BaseModel> T fromResponse(
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

}
