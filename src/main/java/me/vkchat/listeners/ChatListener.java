package me.vkchat.listeners;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Listener principal do chat
 * Processa mensagens, anti-spam, slow mode, mentions e formatação
 */
public class ChatListener implements Listener {
    
    private final VKChat plugin;
    private final Pattern mentionPattern = Pattern.compile("@(\\w+)");
    
    public ChatListener(VKChat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        // ==================== VERIFICAÇÕES ====================
        
        // 1. Verificar se jogador está mutado
        if (plugin.getChatManager().isMuted(player)) {
            event.setCancelled(true);
            MessageUtil.send(player, "&cVocê está mutado!");
            return;
        }
        
        // 2. Verificar slow mode
        if (!plugin.getSlowModeManager().canSendMessage(player)) {
            event.setCancelled(true);
            long remaining = plugin.getSlowModeManager().getRemainingCooldown(player);
            MessageUtil.sendMessage(player, "slowmode-wait", "time", String.valueOf(remaining));
            return;
        }
        
        // 3. Verificar anti-spam
        String spamCheck = plugin.getAntiSpamManager().checkMessage(player, message);
        if (spamCheck != null) {
            event.setCancelled(true);
            MessageUtil.sendMessage(player, spamCheck, "max", 
                String.valueOf(plugin.getConfig().getInt("anti-spam.max-length.characters", 256)));
            return;
        }
        
        // ==================== PROCESSAMENTO ====================
        
        // Processar cores se tiver permissão
        message = MessageUtil.processMessage(player, message);
        
        // Processar mentions
        if (plugin.getConfig().getBoolean("mentions.enabled", true)) {
            message = processMentions(player, message);
        }
        
        // Obter formato do chat (já inclui PlaceholderAPI se disponível)
        String format = plugin.getChatManager().formatMessage(player, message);
        
        // ==================== ENVIO ====================
        
        // Cancelar evento padrão
        event.setCancelled(true);
        
        // Enviar para jogadores que não desabilitaram o chat
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!plugin.getChatManager().isChatDisabled(online)) {
                online.sendMessage(format);
            }
        }
        
        // ==================== PÓS-PROCESSAMENTO ====================
        
        // Registrar cooldown do slow mode
        plugin.getSlowModeManager().recordMessage(player);
        
        // Salvar última mensagem
        plugin.getChatManager().setLastMessage(player, message);
        
        // Log da mensagem
        String channel = plugin.getChannelManager().getCurrentChannel(player);
        plugin.getLogManager().logPublicMessage(player, message, channel.toUpperCase());
    }
    
    /**
     * Processa mentions (@jogador) na mensagem
     */
    private String processMentions(Player sender, String message) {
        // Verificar permissão
        if (!sender.hasPermission("vkchat.mention.use")) {
            return message;
        }
        
        Matcher matcher = mentionPattern.matcher(message);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String mentionedName = matcher.group(1);
            Player mentioned = Bukkit.getPlayer(mentionedName);
            
            // Se o jogador existe e está online
            if (mentioned != null && mentioned.isOnline()) {
                // Substituir pelo formato configurado
                String mentionFormat = plugin.getConfig().getString("mentions.format", "&e&l@{player}");
                String replacement = mentionFormat.replace("{player}", mentioned.getName());
                replacement = MessageUtil.colorize(replacement);
                
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                
                // Notificar o jogador mencionado (de forma assíncrona)
                notifyMentionedPlayer(sender, mentioned);
            } else {
                // Manter o texto original se jogador não existe
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        
        matcher.appendTail(result);
        return result.toString();
    }
    
    /**
     * Notifica um jogador que foi mencionado
     */
    private void notifyMentionedPlayer(Player sender, Player mentioned) {
        // Executar na thread principal para tocar som
        Bukkit.getScheduler().runTask(plugin, () -> {
            // Tocar som se habilitado
            if (plugin.getConfig().getBoolean("mentions.sound.enabled", true)) {
                try {
                    String soundName = plugin.getConfig().getString("mentions.sound.sound-name", "NOTE_PLING");
                    float volume = (float) plugin.getConfig().getDouble("mentions.sound.volume", 1.0);
                    float pitch = (float) plugin.getConfig().getDouble("mentions.sound.pitch", 1.0);
                    
                    // Tentar tocar o som (compatível com 1.8.8)
                    try {
                        Sound sound = Sound.valueOf(soundName);
                        mentioned.playSound(mentioned.getLocation(), sound, volume, pitch);
                    } catch (IllegalArgumentException e) {
                        // Som não existe, usar padrão
                        mentioned.playSound(mentioned.getLocation(), Sound.valueOf("NOTE_PLING"), volume, pitch);
                    }
                } catch (Exception e) {
                    // Ignorar erros de som
                }
            }
            
            // Enviar mensagem de notificação
            MessageUtil.sendMessage(mentioned, "mention-notification", "player", sender.getName());
        });
    }
}
