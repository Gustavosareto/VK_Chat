package me.vkchat.managers;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerenciador principal do sistema de chat
 * Responsável por formatação, estado do chat dos jogadores e processamento de mensagens
 */
public class ChatManager {
    
    private final VKChat plugin;
    
    // Jogadores que desabilitaram o chat
    private final Map<UUID, Boolean> chatDisabled;
    
    // Última mensagem enviada por cada jogador
    private final Map<UUID, String> lastMessages;
    
    // Jogadores mutados (para expansão futura)
    private final Map<UUID, Long> mutedPlayers;
    
    // Jogadores com modo spy ativado
    private final Map<UUID, Boolean> spyEnabled;
    
    public ChatManager(VKChat plugin) {
        this.plugin = plugin;
        this.chatDisabled = new HashMap<>();
        this.lastMessages = new HashMap<>();
        this.mutedPlayers = new HashMap<>();
        this.spyEnabled = new HashMap<>();
    }
    
    /**
     * Formata uma mensagem de chat para um jogador
     */
    public String formatMessage(Player player, String message) {
        // Obter grupo do jogador
        String group = "default";
        if (plugin.getVaultHook().isAvailable()) {
            group = plugin.getVaultHook().getPrimaryGroup(player);
        }
        
        // Obter formato configurado para o grupo
        String format = plugin.getConfig().getString("chat-format." + group);
        if (format == null) {
            format = plugin.getConfig().getString("chat-format.default", "&7{player}: &f{message}");
        }
        
        // Substituir placeholders básicos
        format = replacePlaceholders(player, format);
        
        // Substituir mensagem
        format = format.replace("{message}", message);
        
        // Processar PlaceholderAPI se disponível (usando reflection)
        format = setPlaceholders(player, format);
        
        return MessageUtil.colorize(format);
    }
    
    /**
     * Processa placeholders do PlaceholderAPI usando reflection
     * Isto permite que o plugin compile sem PlaceholderAPI como dependência
     */
    private String setPlaceholders(Player player, String text) {
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Class<?> placeholderAPIClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                Method setPlaceholdersMethod = placeholderAPIClass.getMethod("setPlaceholders", Player.class, String.class);
                return (String) setPlaceholdersMethod.invoke(null, player, text);
            }
        } catch (Exception e) {
            // PlaceholderAPI não disponível ou erro ao processar
            plugin.getLogger().fine("PlaceholderAPI não disponível para processar placeholders");
        }
        return text;
    }
    
    /**
     * Versão pública do setPlaceholders para uso externo
     */
    public String setPlaceholdersPublic(Player player, String text) {
        return setPlaceholders(player, text);
    }
    
    /**
     * Substitui placeholders padrão em uma string
     */
    public String replacePlaceholders(Player player, String text) {
        text = text.replace("{player}", player.getName());
        text = text.replace("{displayname}", player.getDisplayName());
        text = text.replace("{world}", player.getWorld().getName());
        
        // Placeholders do Vault
        if (plugin.getVaultHook().isAvailable()) {
            text = text.replace("{group}", plugin.getVaultHook().getPrimaryGroup(player));
            text = text.replace("{prefix}", plugin.getVaultHook().getPrefix(player));
            text = text.replace("{suffix}", plugin.getVaultHook().getSuffix(player));
        } else {
            text = text.replace("{group}", "default");
            text = text.replace("{prefix}", "");
            text = text.replace("{suffix}", "");
        }
        
        return text;
    }
    
    /**
     * Verifica se o chat está desabilitado para um jogador
     */
    public boolean isChatDisabled(Player player) {
        return chatDisabled.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * Alterna o estado do chat para um jogador
     */
    public void toggleChat(Player player) {
        UUID uuid = player.getUniqueId();
        boolean current = chatDisabled.getOrDefault(uuid, false);
        chatDisabled.put(uuid, !current);
    }
    
    /**
     * Desabilita o chat para um jogador
     */
    public void disableChat(Player player) {
        chatDisabled.put(player.getUniqueId(), true);
    }
    
    /**
     * Habilita o chat para um jogador
     */
    public void enableChat(Player player) {
        chatDisabled.put(player.getUniqueId(), false);
    }
    
    /**
     * Obtém a última mensagem enviada por um jogador
     */
    public String getLastMessage(Player player) {
        return lastMessages.getOrDefault(player.getUniqueId(), "");
    }
    
    /**
     * Define a última mensagem de um jogador
     */
    public void setLastMessage(Player player, String message) {
        lastMessages.put(player.getUniqueId(), message);
    }
    
    /**
     * Verifica se um jogador está mutado
     */
    public boolean isMuted(Player player) {
        Long muteTime = mutedPlayers.get(player.getUniqueId());
        if (muteTime == null) return false;
        
        // Verificar se o mute expirou
        if (System.currentTimeMillis() > muteTime) {
            mutedPlayers.remove(player.getUniqueId());
            return false;
        }
        
        return true;
    }
    
    /**
     * Muta um jogador por um tempo específico
     */
    public void mutePlayer(Player player, long duration) {
        mutedPlayers.put(player.getUniqueId(), System.currentTimeMillis() + duration);
    }
    
    /**
     * Desmuta um jogador
     */
    public void unmutePlayer(Player player) {
        mutedPlayers.remove(player.getUniqueId());
    }
    
    /**
     * Limpa o chat de todos os jogadores
     */
    public void clearChat() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Enviar 100 linhas vazias para "limpar" o chat
            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
        }
    }
    
    /**
     * Limpa o chat de um jogador específico
     */
    public void clearChat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage("");
        }
    }
    
    /**
     * Recarrega as configurações
     */
    public void reload() {
        // Configurações são carregadas diretamente do arquivo
    }
    
    /**
     * Limpa dados ao desabilitar
     */
    public void shutdown() {
        chatDisabled.clear();
        lastMessages.clear();
        mutedPlayers.clear();
        spyEnabled.clear();
    }
    
    // ==================== SISTEMA SPY ====================
    
    /**
     * Verifica se o modo spy está ativado para um jogador
     */
    public boolean isSpyEnabled(Player player) {
        return spyEnabled.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * Ativa o modo spy para um jogador
     */
    public void enableSpy(Player player) {
        spyEnabled.put(player.getUniqueId(), true);
    }
    
    /**
     * Desativa o modo spy para um jogador
     */
    public void disableSpy(Player player) {
        spyEnabled.put(player.getUniqueId(), false);
    }
    
    /**
     * Envia mensagem spy para todos os jogadores com spy ativado
     */
    public void sendSpyMessage(String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (isSpyEnabled(online) && online.hasPermission("vkchat.spy")) {
                online.sendMessage(MessageUtil.colorize(message));
            }
        }
    }
}
