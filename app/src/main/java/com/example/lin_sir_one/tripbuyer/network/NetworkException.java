package com.example.lin_sir_one.tripbuyer.network;

/**
 * Created by tc on 6/21/16. 网络操作错误
 */
public class NetworkException extends RuntimeException {

    public static final int REQUEST_OK = 100;
    public static final int REQUEST_FAIL = 101;
    public static final int METHOD_NOT_ALLOWED = 102;
    public static final int PARAMETER_ERROR = 103;
    public static final int UID_OR_PWD_ERROR = 104;
    public static final int SERVER_INTERNAL_ERROR = 105;
    public static final int REQUEST_TIMEOUT = 106;
    public static final int CONNECTION_ERROR = 107;
    public static final int VERIFY_EXPIRED = 108;
    public static final int NO_DATA = 109;


    public NetworkException(int resultCode) {
        this(getNetworkExceptionMessage(resultCode));
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 将结果码转换成对应的文本信息
     */
    private static String getNetworkExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case REQUEST_OK:
                message = "请求成功";
                break;
            case REQUEST_FAIL:
                message = "请求失败";
                break;

            case METHOD_NOT_ALLOWED:
                message = "请求方式不允许";
                break;
            case PARAMETER_ERROR:
                message = "用户不存在";
                break;
            case UID_OR_PWD_ERROR:
                message = "用户名或密码错误";
                break;
            case SERVER_INTERNAL_ERROR:
                message = "服务器内部错误";
                break;
            case REQUEST_TIMEOUT:
                message = "请求超时";
                break;
            case CONNECTION_ERROR:
                message = "连接错误";
                break;
            case VERIFY_EXPIRED:
                message = "验证过期";
                break;
            case NO_DATA:
                message = "没有数据";
                break;
            case 110:
                message = "该用户已存在";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
