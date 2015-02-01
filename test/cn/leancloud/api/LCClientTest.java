package cn.leancloud.api;

import cn.leancloud.api.model.LCInstallation;
import org.apache.log4j.Logger;
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

    @Test
    public void installation() throws Exception {
        LCClient client = new LCClient(id, key);
        Map data = new HashMap();
        data.put("deviceType", "ios");
        data.put("deviceToken", "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");

        List<String> channels = new ArrayList<String>();
        data.put("channels", channels);

        LCInstallation installation = client.installationsCreate(data);
        LOG.debug(installation);


    }
}
