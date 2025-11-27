package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /reply
 * Responde à última mensagem privada recebida
 * 
 * Uso: /reply <mensagem>
 * Permissão: vkchat.reply
 */
public class ReplyCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public ReplyCommand(VKChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Apenas jogadores podem usar
        if (!(sender instanceof Player)) {
            MessageUtil.send(sender, "&cApenas jogadores podem usar este comando!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Verificar permissão
        if (!player.hasPermission("vkchat.reply")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Verificar argumentos
        if (args.length < 1) {
            MessageUtil.sendMessage(player, "reply-usage");
            return true;
        }
        
        // Verificar se tem alguém para responder
        if (!plugin.getMessageManager().hasReplyTarget(player)) {
            MessageUtil.sendMessage(player, "reply-no-target");
            return true;
        }
        
        Player target = plugin.getMessageManager().getReplyTarget(player);
        
        // Verificar se o alvo ainda está online
        if (target == null || !target.isOnline()) {
            MessageUtil.sendMessage(player, "player-offline");
            plugin.getMessageManager().removeReplyTarget(player);
            return true;
        }
        
        // Construir mensagem
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();
        
        // Processar mensagem (cores se tiver permissão)
        message = MessageUtil.processMessage(player, message);
        
        // Enviar mensagem
        plugin.getMessageManager().sendPrivateMessage(player, target, message);
        
        return true;
    }
}
