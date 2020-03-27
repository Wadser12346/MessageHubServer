package MainApplication.PacketWrapper;

import CS4B.Messages.Packet;
import MainApplication.ClientConnection;

import java.util.UUID;

public class ServerPacket {
    private ClientConnection clientConnection;
    private Packet packet;

    public ServerPacket(ClientConnection clientConnection, Packet packet) {
        this.clientConnection = clientConnection;
        this.packet = packet;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public Packet getPacket() {
        return packet;
    }
}
