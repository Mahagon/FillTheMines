package net.mahagon.fillthemines;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        TextComponent message = new TextComponent(rawMessage);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, rawCommand));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(rawHowertext).create()));
        recipient.spigot().sendMessage(message);
    }

}
