package com.hegp.constants;

public class Constants {

    /** netty传输的数据格式 */
    public static byte JSON = 0x00;
    public static byte PROTOBUFF = 0x00;
    public static byte THRIFT = 0x00;

    // 0x0F表示心跳包, 0x1F表示超时包, 0x2F业务信息包
    public static byte HEARTBEAT_PACKET = 0x00;
    public static byte TIMEOUT_PACKET = 0x01;
    public static byte BUSINESS_PACKET = 0x02;
}
