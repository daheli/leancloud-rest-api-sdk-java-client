package cn.leancloud.api.exception;


public class APIException extends Exception {

    public APIException() {
        super();
    }

    public APIException(String s) {
        super(s);
    }

    public APIException(String s, Throwable throwable) {
        super(s, throwable);

    }

}

