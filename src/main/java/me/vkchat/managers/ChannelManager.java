package me.vkchat.managers;

import me.vkchat.VKChat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerenciador de canais de chat (Global e Local)
 */
public class ChannelManager {
    
    private final VKChat plugin;
    
    // Canal atual de cada jogador (global ou local)
    private final Map<UUID, String> playerChannels;
    
    public ChannelManager(VKChat plugin) {
        this.plugin = plugin;
        this.playerChannels = new HashMap<>();
    }
    
    /**
     * Obtém o canal atual de um jogador
     */
    public String getCurrentChannel(Player player) {
        return playerChannels.getOrDefault(
            player.getUniqueId(),
            plugin.getConfig().getString("general.default-channel", "global")
        );
    }
    
    /**
     * Define o canal de um jogador
     */
    public void setChannel(Player player, String channel) {
        playerChannels.put(player.getUniqueId(), channel.toLowerCase());
    }
    
    /**
     * Verifica se um canal está habilitado
     */
    public boolean isChannelEnabled(String channel) {
        return plugin.getConfig().getBoolean("channels." + channel + ".enabled", false);
    }
    
    /**
     * Obtém o formato de um canal
     */
    public String getChannelFormat(String channel) {
        String format = plugin.getConfig().getString("channels." + channel + ".format");
        if (format == null) {
            return "&7{player}: &f{message}";
        }
        return format;
    }
    
    /**
     * Obtém o alcance do canal local
     */
    public int getLocalRange() {
        return plugin.getConfig().getInt("channels.local.range", 100);
    }
    
    /**
     * Verifica se um jogador tem permissão para usar um canal
     */
    public boolean hasChannelPermission(Player player, String channel) {
        String permission = plugin.getConfig().getString("channels." + channel + ".permission");
        if (permission == null) {
            return true;
        }
        return player.hasPermission(permission);
    }
    
    /**
     * Recarrega as configurações
     */
    public void reload() {
        // Recarregado automaticamente pela config
    }
}
