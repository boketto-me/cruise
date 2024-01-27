package org.boketto.cruise_server.network;

public interface NetworkTransport {

    public void startNetwork();

    public void stopNetwork();

    public void rebootNetwork();

}
