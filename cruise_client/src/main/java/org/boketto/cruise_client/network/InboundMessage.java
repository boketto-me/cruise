package org.boketto.cruise_client.network;

import lombok.Data;

import java.io.*;

/**
 * 自定义二进制通讯协议，参考了如下方案，并在此基础上进行了适当调整：
 * https://www.cnblogs.com/caoweixiong/p/14663492.html
 */
@Data
public class InboundMessage {

    private String messageId;

    private Short magicNumber;

    private Short messageType;

    private Integer contentLength;

    private Short protocolVersion;

    private byte[] messageBody;

    private String messageDigest;

    public InboundMessage decodeMessage(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        this.contentLength = dataInputStream.readInt();
        this.magicNumber = dataInputStream.readShort();
        this.protocolVersion = dataInputStream.readShort();
        byte[] temp1 = new byte[32];
        dataInputStream.read(temp1, 0, 32);
        this.messageId = new String(temp1, "UTF-8");
        this.messageType = dataInputStream.readShort();
        byte[] temp2 = new byte[68];
        dataInputStream.read(temp2, 0, 68);
        this.messageBody = temp2;
        byte[] temp3 = new byte[32];
        dataInputStream.read(temp3, 0, 32);
        this.messageDigest = new String(temp3, "UTF-8");
        return this;
    }

}
