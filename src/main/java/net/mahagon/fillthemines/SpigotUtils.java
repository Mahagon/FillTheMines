package net.mahagon.fillthemines;

/*
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
*/

import org.bukkit.entity.Player;

public class SpigotUtils {
    /**
     * Sends a raw chat message directly to the client. This allows the use of new features like chat
     * JSON
     */

    /* spigot is the better way to do that
    public static void sendTellraw(Player recipient, String rawMessage, String rawCommand,
                                   String rawHowertext) {
        String jsonText = "{\"text\":\"\",\"extra\":[{\"text\":\"" + rawMessage
                + "\",\"color\":\"white\",\"underlined\":\"false\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
                + rawCommand + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + rawHowertext
                + "\"}}]}";
        IChatBaseComponent comp = ChatSerializer.a(jsonText);
        PacketPlayOutChat packet = new PacketPlayOutChat(comp);
        ((CraftPlayer) recipient).getHandle().playerConnection.sendPacket(packet);
    }
    */
    public static void sendTellraw(Player recipient, String rawMessage, String rawCommand, String rawHowertext) {
        String jsonText = "{\"text\":\"\",\"extra\":[{\"text\":\"" + rawMessage
                + "\",\"color\":\"white\",\"underlined\":\"false\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
                + rawCommand + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + rawHowertext
                + "\"}}]}";
        recipient.sendRawMessage(jsonText);
    }
}
