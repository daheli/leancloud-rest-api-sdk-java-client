package cn.leancloud.api.http;

import cn.leancloud.api.exception.APIException;
import org.apache.log4j.Logger;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class NativeHttpClient implements IHttpClient {
    private static final Logger LOG = Logger.getLogger(NativeHttpClient.class);
    private static final String KEYWORDS_CONNECT_TIMED_OUT = "connect timed out";
    private static final String KEYWORDS_READ_TIMED_OUT = "Read timed out";

    private int _maxRetryTimes = 0;
    private String id;
    private String key;

    public NativeHttpClient(String id, String key) {
        this(id, key, DEFAULT_MAX_RETRY_TIMES);
    }

    public NativeHttpClient(String id, String key, int maxRetryTimes) {
        this._maxRetryTimes = maxRetryTimes;
        LOG.info("Created instance with _maxRetryTimes = " + _maxRetryTimes);
        this.id = id;
        this.key = key;
        initSSL();
    }

    public ResponseWrapper sendGet(String url)
            throws APIException {
        return doRequest(url, null, RequestMethod.GET);
    }

    public ResponseWrapper sendDelete(String url)
            throws APIException {
        return doRequest(url, null, RequestMethod.DELETE);
    }

    public ResponseWrapper sendPost(String url, String content)
            throws APIException {
        return doRequest(url, content, RequestMethod.POST);
    }

    public ResponseWrapper sendPut(String url) throws APIException {
        return doRequest(url, null, RequestMethod.PUT);
    }

    public ResponseWrapper doRequest(String url, String content,
                                     RequestMethod method) throws APIException {
        ResponseWrapper response = null;
        for (int retryTimes = 0; ; retryTimes++) {
            try {
                response = _doRequest(url, content, method);
                break;
            } catch (SocketTimeoutException e) {
                LOG.debug("connect timed out");
                throw new APIException(e.getMessage());
            }
        }
        return response;
    }

    private ResponseWrapper _doRequest(String url, String content,
                                       RequestMethod method) throws APIException,
            SocketTimeoutException {

        LOG.debug("Send request - " + method.toString() + " " + url);
        if (null != content) {
            LOG.debug("Request Content - " + content);
        }

        HttpURLConnection conn = null;
        OutputStream out = null;
        StringBuffer sb = new StringBuffer();
        ResponseWrapper wrapper = new ResponseWrapper();

        try {
            URL aUrl = new URL(url);

            conn = (HttpURLConnection) aUrl.openConnection();
            conn.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
            conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
            conn.setUseCaches(false);
            conn.setRequestMethod(method.name());
            conn.setRequestProperty("User-Agent", "Java Client");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", CHARSET);
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("X-AVOSCloud-Application-Id", id);
            conn.setRequestProperty("X-AVOSCloud-Application-Key", key);

            if (RequestMethod.GET == method) {
                conn.setDoOutput(false);
            } else if (RequestMethod.DELETE == method) {
                conn.setDoOutput(false);
            } else if (RequestMethod.POST == method) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
                byte[] data = content.getBytes(CHARSET);
                conn.setRequestProperty("Content-Length", String.valueOf(data.length));
                out = conn.getOutputStream();
                out.write(data);
                out.flush();
            }

            int status = conn.getResponseCode();
            InputStream in = null;
            if (status == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }

            if (null != in) {
                InputStreamReader reader = new InputStreamReader(in, CHARSET);
                char[] buff = new char[1024];
                int len;
                while ((len = reader.read(buff)) > 0) {
                    sb.append(buff, 0, len);
                }
            }

            String responseContent = sb.toString();
            wrapper.responseCode = status;
            wrapper.responseContent = responseContent;

//            String quota = conn.getHeaderField(RATE_LIMIT_QUOTA);
//            String remaining = conn.getHeaderField(RATE_LIMIT_Remaining);
//            String reset = conn.getHeaderField(RATE_LIMIT_Reset);
//            wrapper.setRateLimit(quota, remaining, reset);

            if (status == 200) {
                LOG.debug("Succeed to get response - 200 OK");
                LOG.debug("Response Content - " + responseContent);
            } else if (status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                wrapper.responseContent = sb.toString();
                wrapper.responseCode = 200;
            } else if (status > 200 && status < 400) {
                LOG.warn("Normal response but unexpected - responseCode:" + status + ", responseContent:" + responseContent);

            } else {
                LOG.warn("Got error response - responseCode:" + status + ", responseContent:" + responseContent);

                switch (status) {
                    case 400:
                        LOG.error("Your request params is invalid. Please check them according to error message.");
                        wrapper.setErrorObject();
                        break;
                    case 401:
                        LOG.error("Authentication failed! Please check authentication params according to docs.");
                        wrapper.setErrorObject();
                        break;
                    case 403:
                        LOG.error("Request is forbidden! Maybe your appkey is listed in blacklist?");
                        wrapper.setErrorObject();
                        break;
                    case 410:
                        LOG.error("Request resource is no longer in service. Please according to notice on official website.");
                        wrapper.setErrorObject();
                    case 429:
                        LOG.error("Too many requests! Please review your appkey's request quota.");
                        wrapper.setErrorObject();
                        break;
                    case 500:
                    case 502:
                    case 503:
                    case 504:
                        LOG.error("Seems encountered server error. Maybe Server is in maintenance? Please retry later.");
                        break;
                    default:
                        LOG.error("Unexpected response.");
                }

                throw new APIException();
            }

        } catch (SocketTimeoutException e) {
            if (e.getMessage().contains(KEYWORDS_CONNECT_TIMED_OUT)) {
                throw e;
            } else if (e.getMessage().contains(KEYWORDS_READ_TIMED_OUT)) {
                throw new SocketTimeoutException(KEYWORDS_READ_TIMED_OUT);
            }
            LOG.debug(IO_ERROR_MESSAGE, e);
            throw new APIException(IO_ERROR_MESSAGE, e);

        } catch (IOException e) {
            LOG.debug(IO_ERROR_MESSAGE, e);
            throw new APIException(IO_ERROR_MESSAGE, e);

        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("Failed to close stream.", e);
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }

        return wrapper;
    }

    protected void initSSL() {
        TrustManager[] tmCerts = new TrustManager[1];
        tmCerts[0] = new SimpleTrustManager();
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tmCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier hostnameVerifier = new SimpleHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        } catch (Exception e) {
            LOG.error("Init SSL error", e);
        }
    }


    private static class SimpleHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    private static class SimpleTrustManager implements TrustManager, X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            return;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static class SimpleProxyAuthenticator extends Authenticator {
        private String username;
        private String password;

        public SimpleProxyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    this.username,
                    this.password.toCharArray());
        }
    }
}
