package me.vkchat.hooks;

import me.vkchat.VKChat;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Hook de integração com Vault
 * Fornece acesso a prefixos, sufixos e grupos
 */
public class VaultHook {
    
    private final VKChat plugin;
    private Chat chat = null;
    
    public VaultHook(VKChat plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Configura a integração com Vault
     * @return true se configurado com sucesso
     */
    public boolean setupChat() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        
        chat = rsp.getProvider();
        return chat != null;
    }
    
    /**
     * Verifica se Vault está disponível
     */
    public boolean isAvailable() {
        return chat != null;
    }
    
    /**
     * Obtém o prefixo de um jogador
     */
    public String getPrefix(Player player) {
        if (!isAvailable()) return "";
        
        String prefix = chat.getPlayerPrefix(player);
        return prefix != null ? prefix : "";
    }
    
    /**
     * Obtém o sufixo de um jogador
     */
    public String getSuffix(Player player) {
        if (!isAvailable()) return "";
        
        String suffix = chat.getPlayerSuffix(player);
        return suffix != null ? suffix : "";
    }
    
    /**
     * Obtém o grupo principal de um jogador
     */
    public String getPrimaryGroup(Player player) {
        if (!isAvailable()) return "default";
        
        String group = chat.getPrimaryGroup(player);
        return group != null ? group : "default";
    }
    
    /**
     * Obtém todos os grupos de um jogador
     */
    public String[] getGroups(Player player) {
        if (!isAvailable()) return new String[]{"default"};
        
        String[] groups = chat.getPlayerGroups(player);
        return groups != null && groups.length > 0 ? groups : new String[]{"default"};
    }
}
