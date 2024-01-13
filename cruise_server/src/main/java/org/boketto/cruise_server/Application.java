package org.boketto.cruise_server;

public class Application {

    public static void main(String[] args) {
        try {
            new NettyServer().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("hello world");
    }

}
