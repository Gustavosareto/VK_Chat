package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /sc (Staff Chat)
 * Envia mensagens no chat da staff
 * 
 * Uso: /sc <mensagem>
 * Permissão: vkchat.staff
 */
public class StaffChatCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public StaffChatCommand(VKChat plugin) {
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
        if (!player.hasPermission("vkchat.staff")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Verificar se está habilitado
        if (!plugin.getConfig().getBoolean("staff-chat.enabled", true)) {
            MessageUtil.sendMessage(player, "command-disabled");
            return true;
        }
        
        // Verificar argumentos
        if (args.length < 1) {
            MessageUtil.sendMessage(player, "staffchat-usage");
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
        
        // Obter formato do staff chat e processar
        String format = plugin.getConfig().getString("staff-chat.format", 
            "&c[STAFF] &7{player}: &f{message}");
        String finalMessage = plugin.getChatManager().formatMessage(player, format.replace("{message}", message));
        
        // Enviar apenas para membros da staff
        String permission = plugin.getConfig().getString("staff-chat.permission", "vkchat.staff");
        int recipients = 0;
        
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission(permission)) {
                online.sendMessage(finalMessage);
                recipients++;
            }
        }
        
        // Enviar para modo spy
        String spyFormat = "&7[SPY-STAFF] &c" + player.getName() + "&7: &f" + message;
        plugin.getChatManager().sendSpyMessage(spyFormat);
        
        // Avisar se nenhum staff online
        if (recipients == 1) { // Apenas o próprio jogador
            MessageUtil.send(player, "&eNenhum outro membro da staff online!");
        }
        
        // Log
        plugin.getLogManager().logStaffMessage(player, message);
        
        return true;
    }
}
