package me.vkchat.managers;

import me.vkchat.VKChat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerenciador de mensagens privadas (Tell/Reply)
 */
public class MessageManager {
    
    private final VKChat plugin;
    
    // Armazena a última pessoa com quem cada jogador conversou
    private final Map<UUID, UUID> replyTargets;
    
    public MessageManager(VKChat plugin) {
        this.plugin = plugin;
        this.replyTargets = new HashMap<>();
    }
    
    /**
     * Envia uma mensagem privada entre dois jogadores
     */
    public void sendPrivateMessage(Player sender, Player receiver, String message) {
        // Obter prefixo do sender
        String senderPrefix = "";
        if (plugin.getVaultHook().isAvailable()) {
            senderPrefix = plugin.getVaultHook().getPrefix(sender);
        }
        
        String receiverPrefix = "";
        if (plugin.getVaultHook().isAvailable()) {
            receiverPrefix = plugin.getVaultHook().getPrefix(receiver);
        }

        // Formatar mensagens
        String sentFormat = "&9[✉️]&7Mensagem para " + receiverPrefix + "&7{receiver}" + "&7: &9{message}";
        String receivedFormat = "&9[✉️]&7Mensagem de " + senderPrefix + "&7{sender}" + "&7: &9{message}";
        
        // Substituir placeholders
        sentFormat = sentFormat.replace("{receiver}", receiver.getName())
                               .replace("{sender}", sender.getName())
                               .replace("{message}", message);
        receivedFormat = receivedFormat.replace("{sender}", sender.getName())
                                      .replace("{receiver}", receiver.getName())
                                      .replace("{message}", message);
        
        // Colorizar e enviar mensagens
        sender.sendMessage(me.vkchat.utils.MessageUtil.colorize(sentFormat));
        receiver.sendMessage(me.vkchat.utils.MessageUtil.colorize(receivedFormat));
        
        // Enviar para modo spy
        String spyFormat = "&7[SPY] &d" + sender.getName() + " &7-> &d" + receiver.getName() + "&7: &f" + message;
        plugin.getChatManager().sendSpyMessage(spyFormat);
        
        // Atualizar alvos de reply
        setReplyTarget(sender, receiver);
        setReplyTarget(receiver, sender);
        
        // Log
        if (plugin.getConfig().getBoolean("logging.log-private-messages", true)) {
            plugin.getLogManager().logPrivateMessage(sender, receiver, message);
        }
    }
    
    /**
     * Define o alvo de reply de um jogador
     */
    public void setReplyTarget(Player player, Player target) {
        replyTargets.put(player.getUniqueId(), target.getUniqueId());
    }
    
    /**
     * Obtém o alvo de reply de um jogador
     */
    public Player getReplyTarget(Player player) {
        UUID targetUUID = replyTargets.get(player.getUniqueId());
        if (targetUUID == null) {
            return null;
        }
        
        return plugin.getServer().getPlayer(targetUUID);
    }
    
    /**
     * Verifica se um jogador tem um alvo de reply
     */
    public boolean hasReplyTarget(Player player) {
        return getReplyTarget(player) != null;
    }
    
    /**
     * Remove o alvo de reply de um jogador
     */
    public void removeReplyTarget(Player player) {
        replyTargets.remove(player.getUniqueId());
    }
}
