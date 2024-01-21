package org.boketto.cruise_server.network;

import lombok.Data;

import java.io.*;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 自定义二进制通讯协议，参考了如下方案，并在此基础上进行了适当调整：
 * https://www.cnblogs.com/caoweixiong/p/14663492.html
 */
@Data
public class InboundMessage {

    private String messageId;

    private Integer magicNumber;

    private Integer messageType;

    private Integer contentLength;

    private Integer protocolVersion;

    private byte[] messageBody;

    private String messageDigest;

    public InboundMessage decodeMessage() throws IOException {
        byte[] bytes = new byte[10000];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        this.magicNumber = dataInputStream.readInt();
        this.protocolVersion = dataInputStream.readInt();
        byte[] temp = new byte[32];
        dataInputStream.readNBytes(temp, 4, 32);
        this.messageId = new String(temp, Charset.defaultCharset());
        this.messageType = dataInputStream.readInt();
        this.contentLength = dataInputStream.readInt();
        dataInputStream.readNBytes(messageBody, 2+2+32+2+2, 68);
        dataInputStream.readNBytes(new byte[32], 2+2+32+2+2+68, 32);
        return this;
    }

}
