package cn.leancloud.api;

import cn.leancloud.api.model.BaseResult;
import cn.leancloud.api.model.LCInstallation;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/1
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class LCClientTest {
    private static final Logger LOG = Logger.getLogger(LCClient.class);

    private static String id = "yk1t19sy6qogm4ekebuc9bcq20dk65ph6gdzv2d4xzl955ci";
    private static String key = "5989xh78ysth4e72b4qxb02hvgir8rujw5synam3lwi8ljvu";
    protected LCClient client;
    private String token;
    private String objectId;

    @Before
    public void before() {
        client = new LCClient(id, key, false);

        token = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
        objectId = "54cf156de4b05f545fadba48";
    }

    @Test
    public void installation() throws Exception {
        Map data = new HashMap();
        data.put("deviceType", "ios");
        data.put("deviceToken", token);
        List<String> channels = new ArrayList<String>();
        data.put("channels", channels);
        LCInstallation installation = client.installationsCreate(data);
        LOG.debug(installation);
    }


    @Test
    public void send_notification_alert() throws Exception {
        BaseResult result = client.sendNotificationAlertWithObjectId("hello test world...", objectId);
        LOG.debug(result);
    }

    /**
    {
        aps =     {
            alert = "show me";
            badge = 1
            sound = default;
        };
        type = 001;
        url = "http://leancloud.cn";
    }
    **/
    @Test
    public void send_notification_extras() throws Exception {
        Map data = new HashMap();
        data.put("alert", "show me");
        data.put("sound","default");
        data.put("url","http://leancloud.cn");
        data.put("type","001");
        data.put("badge", "Increment");

        BaseResult result = client.sendNotificationObjectId(data, objectId);
        LOG.debug(result);
    }
}
