package org.boketto.cruise_client;

import org.boketto.cruise_client.network.NettyTransport;

public class Application {

    public static void main(String[] args) {
        try {
            new NettyTransport().startNetwork();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("hello world");
    }

}
