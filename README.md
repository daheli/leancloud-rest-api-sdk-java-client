# Leancloud Rest Api Java Client Library (For Java Server)

## 概述

这是 Leancloud Rest Api 的 Java 版本封装开发包，适用于Java Server 和Rest Api通讯访问。

还在完善更多的 1.1 版本Rest API 功能中。

对应的 REST API 文档：<https://leancloud.cn/docs/rest_api.html>



## 初始化

> 下片断来自项目代码里的文件：test / cn.leancloud.api.LCClientTest
```
    //id, key, apnsProduction
    LCClient client = new LCClient(id, key, false);        
```

## 安装数据

* 上传安装数据 <https://leancloud.cn/docs/rest_api.html#安装数据-1>

```
	Map data = new HashMap();
    data.put("deviceType", "ios");
    data.put("deviceToken", token);
    List<String> channels = new ArrayList<String>();
    data.put("channels", channels);
    LCInstallation installation = client.installationsCreate(data);
```

## Push 通知 
<https://leancloud.cn/docs/push_guide.html#使用-rest-api-推送消息>

* 构建推送对象：真对某一个Installation ObjectId, 通知内容为 ALERT。

```
    client.sendNotificationAlertWithObjectId("hello test world...", objectId);
```

* 构建推送对象：可以更丰富的进行设置，内容是 show me 通知 ALERT，并且附带extra data
```
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
    
    Map data = new HashMap();
    data.put("alert", "show me");
    data.put("sound","default");
    data.put("url","http://leancloud.cn");
    data.put("type","001");
    data.put("badge", "Increment");
    BaseResult result = client.sendNotificationObjectId(data, objectId);
```

## License

Distributed under `GNU Lesser General Public License` .




