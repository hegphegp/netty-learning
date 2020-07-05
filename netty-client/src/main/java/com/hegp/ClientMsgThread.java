package com.hegp;

import com.hegp.entity.Message;
import io.netty.channel.Channel;

public class ClientMsgThread extends Thread {
    private Channel clientChannel;
    private ClientMsgThread() { }
    public ClientMsgThread(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void setClientChannel(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        sendMsg();
    }

    public void sendMsg() {
        int currentCount = 10;
        while (currentCount > 0) {
            String message = String.format("client %s", System.currentTimeMillis());
            byte[] body = message.getBytes();
            //发送数据
            clientChannel.writeAndFlush(Message.businessPacket(body));
            currentCount -= 1;
        }
    }
}
