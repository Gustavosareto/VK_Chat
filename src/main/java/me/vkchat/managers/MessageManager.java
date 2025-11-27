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
        // Formatar mensagens
        String sentFormat = plugin.getMessagesConfig().getString("tell-format.sent", 
            "&d[Você -> {receiver}] &f{message}");
        String receivedFormat = plugin.getMessagesConfig().getString("tell-format.received", 
            "&d[{sender} -> Você] &f{message}");
        
        // Substituir placeholders
        sentFormat = sentFormat.replace("{receiver}", receiver.getName())
                               .replace("{message}", message);
        receivedFormat = receivedFormat.replace("{sender}", sender.getName())
                                      .replace("{message}", message);
        
        // Enviar mensagens
        sender.sendMessage(plugin.getChatManager().replacePlaceholders(sender, sentFormat));
        receiver.sendMessage(plugin.getChatManager().replacePlaceholders(receiver, receivedFormat));
        
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
