package org.boketto.cruise_server.network;

import lombok.Data;

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

    public byte[] encodeMessage() {
        byte[] bytes = new byte[10000];
        return bytes;
    }

}