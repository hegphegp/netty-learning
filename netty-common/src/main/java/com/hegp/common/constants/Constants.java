package com.hegp.common.constants;

public class Constants {

    /** netty传输的数据格式 */
    public static final byte JSON = 0x00;
    public static final byte PROTOBUFF = 0x00;
    public static final byte THRIFT = 0x00;

    // 0x0F表示心跳包, 0x1F表示超时包, 0x2F业务信息包
    public static final byte HEARTBEAT_PACKET = 0x00;
    public static final byte TIMEOUT_PACKET = 0x01;
    public static final byte BUSINESS_PACKET = 0x02;

    public static final byte TEST_MODULE = 0x00;

    public static final byte USER_MODULE = 0x01;
    public static final byte USER_MODULE_TEST1 = 0x00;
    public static final byte USER_MODULE_TEST2 = 0x01;

    public static final byte FILE_MODULE = 0x02;

    public static final byte CHAT_MODULE = 0x03;

}
