package com.hegp.common.entity;

import com.hegp.common.constants.Constants;

/**
 * 消息协议数据包
 */
public class Message {

    private byte decryptType;    // 对称解密方式，
    private short version;       // 版本
    private Long reqId;          // 请求id
    private Long resId;          // 响应id
    private byte type;           // 消息类型  0x00表示心跳包, 0x01表示超时包, 0x02业务信息包, 订下的规矩是: 心跳包的内容长度为0
    /**
     * 参照t-io的集群消息转发，消息是否是另外一台机器通过topic转过来的，如果是就不要死循环地再一次转发啦
     */
    private byte isFromCluster;
    private int byteCount;        // 内容的长度
    private byte[] body;          // 内容

    public static Message heartbeatPacket = new Message(Constants.HEARTBEAT_PACKET);

    public static Message businessPacket(byte[] body) {
        Message message = new Message(Constants.BUSINESS_PACKET);
        message.setBody(body);
        return message;
    }

    public Message() { }

    public Message(byte type) {
        this.type = type;
    }

    public Message(byte type, byte[] body) {
        this.type = type;
        this.body = body;
    }

    public byte getDecryptType() {
        return decryptType;
    }

    public void setDecryptType(byte decryptType) {
        this.decryptType = decryptType;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getIsFromCluster() {
        return isFromCluster;
    }

    public void setIsFromCluster(byte isFromCluster) {
        this.isFromCluster = isFromCluster;
    }

    public int getByteCount() {
        return byteCount;
    }

    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("type:%s len:%s", type, body.length);
    }
}
