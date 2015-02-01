package cn.leancloud.api;


import cn.leancloud.api.exception.APIException;
import cn.leancloud.api.http.NativeHttpClient;
import cn.leancloud.api.http.ResponseWrapper;

/**
 * Created with IntelliJ IDEA.
 * User: lidahe
 * Date: 15/1/31
 * Time: 下午8:38
 * To change this template use File | Settings | File Templates.
 */
public class LCClient {
    public final static String API_URL = "https://leancloud.cn/1.1/installations";
    private static String id = "yk1t19sy6qogm4ekebuc9bcq20dk65ph6gdzv2d4xzl955ci";
    private static String key = "5989xh78ysth4e72b4qxb02hvgir8rujw5synam3lwi8ljvu";


    public static void main(String[] args) throws Exception {
        LCClient client = new LCClient(id, key);
        client.test();
    }

    public LCClient(String id, String key) {
        this.id = id;
        this.key = key;
    }



    public void test() throws APIException {
        NativeHttpClient client = new NativeHttpClient(id, key);
        ResponseWrapper res = client.sendPost(API_URL, "{\n" +
                "   \t\t\"deviceType\": \"ios\",\n" +
                "        \"deviceToken\": \"0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef\",\n" +
                "        \"channels\": [\n" +
                "          \"\"\n" +
                "        ]\n" +
                "      }");
        System.out.println(res);

    }


}
