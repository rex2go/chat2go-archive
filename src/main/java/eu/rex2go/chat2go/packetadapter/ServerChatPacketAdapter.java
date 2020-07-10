package eu.rex2go.chat2go.packetadapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import eu.rex2go.chat2go.Chat2Go;

import java.util.List;

public class ServerChatPacketAdapter extends PacketAdapter {

    private Chat2Go plugin;

    public ServerChatPacketAdapter(Chat2Go plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT);

        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.CHAT) {
            return;
        }

        PacketContainer packet = event.getPacket();
        List<WrappedChatComponent> components = packet.getChatComponents().getValues();

        /* TODO
        for (WrappedChatComponent component : components) {
            component.setJson(component.getJson().replaceAll("\"text\":\".*?\"", "\"text\":\"Test\""));
            packet.getChatComponents().write(components.indexOf(component), component);
        }
         */
    }
}
