package cn.leancloud.api;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/2/1
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class LCClientTest {
    private static String id = "yk1t19sy6qogm4ekebuc9bcq20dk65ph6gdzv2d4xzl955ci";
    private static String key = "5989xh78ysth4e72b4qxb02hvgir8rujw5synam3lwi8ljvu";

    @Test
    public void test() throws Exception{
        LCClient client = new LCClient(id, key);
        client.test();
    }
}
