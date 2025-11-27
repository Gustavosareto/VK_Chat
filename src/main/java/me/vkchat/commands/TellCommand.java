package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /tell
 * Envia mensagens privadas para outros jogadores
 * 
 * Uso: /tell <player> <mensagem>
 * Permissão: vkchat.tell
 */
public class TellCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public TellCommand(VKChat plugin) {
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
        if (!player.hasPermission("vkchat.tell")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Verificar argumentos
        if (args.length < 2) {
            MessageUtil.sendMessage(player, "tell-usage");
            return true;
        }
        
        // Encontrar jogador alvo
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null || !target.isOnline()) {
            MessageUtil.sendMessage(player, "player-offline");
            return true;
        }
        
        // Verificar se não está tentando enviar para si mesmo
        if (target.equals(player)) {
            MessageUtil.sendMessage(player, "tell-yourself");
            return true;
        }
        
        // Construir mensagem
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
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
