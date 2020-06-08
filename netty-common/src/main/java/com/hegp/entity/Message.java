package com.hegp.entity;

/**
 * 消息协议数据包
 */
public class Message {

    private String dataType;     // 数据类型，json，protobuff，thrift，还是其他类型
    private byte decryptType;    // 对称解密方式，
    private int version;         // 版本
    private int requestId;       // 请求id
    private byte type;           // 消息类型  0xAF表示心跳包, 0xBF表示超时包, 0xCF业务信息包, 订下的规矩是: 心跳包的内容长度为0

    // 信息标志  0xAB 表示心跳包    0xBC 表示超时包  0xCD 业务信息包
    private byte flag;

    // 主题信息的长度
    private int length;
    private Object originData;
    // 内容
    private byte[] body;

    public Message() { }

    public Message(byte type, byte flag, byte[] body) {
        this.type = type;
        this.flag = flag;
        this.body = body;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("type:%s flag:%s len:%s", type, flag, body.length);
    }
}
