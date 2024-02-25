package org.boketto.cruise_server.network;

import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 自定义二进制通讯协议，参考了如下方案，并在此基础上进行了适当调整：
 * https://www.cnblogs.com/caoweixiong/p/14663492.html
 */
@Data
public class OutboundMessage {

    private String messageId;

    private Short magicNumber;

    private Short messageType;

    private Integer contentLength;

    private Short protocolVersion;

    private byte[] messageBody;

    private String messageDigest;

    public OutboundMessage(Short messageType, byte[] messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public byte[] encodeMessage() throws IOException {
        this.messageId = UUID.randomUUID().toString().replaceAll("-", "");
        this.magicNumber = 9527;
        this.protocolVersion = 1;
        //this.messageBody = new byte[68];
        this.messageDigest = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        //长度字段既可放在前面，也可以放在其它消息头字段之后，但一般都是建议放在最前面，这是行业的常规做法。
        //长度字段如果不放最前，那么长度字段以及它之前的所有消息头字段都会被Netty丢弃（否则无法处理拆包、粘包）
        //具体可以看LengthFieldBasedFrameDecoder文件头部给出的几种协议格式示例。
        dataOutputStream.writeInt(2 + 2 + 32 + 2 + messageBody.length + 32);//这里不再传入长度字段本身占用的长度，以简化服务端处理逻辑
        dataOutputStream.writeShort(magicNumber);
        dataOutputStream.writeShort(protocolVersion);
        dataOutputStream.write(messageId.getBytes(Charset.defaultCharset()));
        dataOutputStream.writeShort(messageType);
        dataOutputStream.write(messageBody);
        dataOutputStream.write(new byte[32]);
        return byteArrayOutputStream.toByteArray();
    }

}