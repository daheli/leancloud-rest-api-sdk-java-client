package cn.leancloud.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/1
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class LCInstallation extends BaseResult {
    @Expose
    private String deviceType;
    @Expose
    private String deviceToken;
    @Expose
    private List<String> channels = new ArrayList<String>();


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

}
