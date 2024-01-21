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

    private Integer magicNumber;

    private Integer messageType;

    private Integer contentLength;

    private Integer protocolVersion;

    private byte[] messageBody;

    private String messageDigest;

    public byte[] encodeMessage() throws IOException {
        byte[] bytes = new byte[10000];
        this.messageId = UUID.randomUUID().toString().replaceAll("-", "");
        this.magicNumber = 9527;
        this.protocolVersion = 1;
        this.messageBody = new byte[68];
        this.messageDigest = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(magicNumber);
        dataOutputStream.writeInt(protocolVersion);
        dataOutputStream.write(messageId.getBytes(Charset.defaultCharset()));
        dataOutputStream.writeInt(messageType);
        dataOutputStream.writeInt(2 + 2 + 32 + 2 + 2 + 68 + 32);
        dataOutputStream.write(messageBody);
        dataOutputStream.write(new byte[32]);
        return byteArrayOutputStream.toByteArray();
    }

}