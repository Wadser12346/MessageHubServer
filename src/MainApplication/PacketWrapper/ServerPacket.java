package MainApplication.PacketWrapper;

import CS4B.Messages.Packet;

import java.util.UUID;

public class ServerPacket {
    private UUID id;
    private Packet packet;

    public ServerPacket(UUID id, Packet packet) {
        this.id = id;
        this.packet = packet;
    }

    public UUID getId() {
        return id;
    }

    public Packet getPacket() {
        return packet;
    }
}
