package MainApplication.PacketWrapper;

import CS4B.Messages.ChatMessage;
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

    public String getTrimmedMessage(){
        Packet packet = getPacket();
        if(packet.getMessage() instanceof ChatMessage){
            ChatMessage cm = (ChatMessage)packet.getMessage();
            StringBuilder stringBuilder = new StringBuilder("");
            stringBuilder.append(packet.getUser() + ": ");
            stringBuilder.append(cm.getStringMessage());

            if(cm.hasPictureMessage()){
                stringBuilder.append(" + Image");
            }

            return stringBuilder.toString();
        }

        return packet.toString();
    }
}
