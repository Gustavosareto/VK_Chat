package me.vkchat.hooks;

import me.vkchat.VKChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

/**
 * Hook para integração com PlaceholderAPI
 * Registra placeholders customizados do VKChat de forma dinâmica usando reflection
 * Isto permite que o plugin compile sem PlaceholderAPI como dependência
 * 
 * Placeholders disponíveis:
 * - %vkchat_last_message% - Última mensagem enviada
 * - %vkchat_cooldown% - Tempo restante de cooldown
 * - %vkchat_muted% - Se está mutado (true/false)
 * - %vkchat_channel% - Canal atual (global/local)
 * - %vkchat_chat_enabled% - Se o chat está habilitado (true/false)
 */
public class PlaceholderAPIHook {
    
    private final VKChat plugin;
    private boolean registered = false;
    
    public PlaceholderAPIHook(VKChat plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Tenta registrar a expansão do PlaceholderAPI
     * @return true se registrado com sucesso
     */
    public boolean register() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }
        
        try {
            // Criar uma expansão dinâmica usando reflection
            Class<?> expansionClass = Class.forName("me.clip.placeholderapi.expansion.PlaceholderExpansion");
            
            Object expansion = java.lang.reflect.Proxy.newProxyInstance(
                expansionClass.getClassLoader(),
                new Class<?>[] { },
                (proxy, method, args) -> {
                    String methodName = method.getName();
                    
                    if (methodName.equals("getIdentifier")) {
                        return "vkchat";
                    }
                    if (methodName.equals("getAuthor")) {
                        return plugin.getDescription().getAuthors().toString();
                    }
                    if (methodName.equals("getVersion")) {
                        return plugin.getDescription().getVersion();
                    }
                    if (methodName.equals("persist")) {
                        return true;
                    }
                    if (methodName.equals("onPlaceholderRequest")) {
                        Player player = (Player) args[0];
                        String identifier = (String) args[1];
                        return handlePlaceholder(player, identifier);
                    }
                    
                    return null;
                }
            );
            
            // Registrar a expansão
            Method registerMethod = expansionClass.getMethod("register");
            registerMethod.invoke(expansion);
            
            registered = true;
            plugin.getLogger().info("PlaceholderAPI expansão registrada com sucesso!");
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().warning("Erro ao registrar PlaceholderAPI expansão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Processa um placeholder request
     */
    private String handlePlaceholder(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        
        // %vkchat_last_message%
        if (identifier.equals("last_message")) {
            return plugin.getChatManager().getLastMessage(player);
        }
        
        // %vkchat_cooldown%
        if (identifier.equals("cooldown")) {
            long remaining = plugin.getSlowModeManager().getRemainingCooldown(player);
            return remaining > 0 ? String.valueOf(remaining) : "0";
        }
        
        // %vkchat_muted%
        if (identifier.equals("muted")) {
            return String.valueOf(plugin.getChatManager().isMuted(player));
        }
        
        // %vkchat_channel%
        if (identifier.equals("channel")) {
            return plugin.getChannelManager().getCurrentChannel(player);
        }
        
        // %vkchat_chat_enabled%
        if (identifier.equals("chat_enabled")) {
            return String.valueOf(!plugin.getChatManager().isChatDisabled(player));
        }
        
        return null; // Placeholder não encontrado
    }
    
    /**
     * Verifica se a expansão foi registrada
     */
    public boolean isRegistered() {
        return registered;
    }
}
