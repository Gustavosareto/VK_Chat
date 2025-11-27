package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /slowchat
 * Ativa ou desativa o slow mode do chat
 * 
 * Uso: /slowchat <segundos|off>
 * Permissão: vkchat.slowchat
 */
public class SlowChatCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public SlowChatCommand(VKChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verificar permissão
        if (!sender.hasPermission("vkchat.slowchat")) {
            MessageUtil.sendMessage(sender, "no-permission");
            return true;
        }
        
        // Verificar argumentos
        if (args.length != 1) {
            MessageUtil.sendMessage(sender, "slowmode-invalid");
            return true;
        }
        
        String arg = args[0].toLowerCase();
        
        // Desativar slow mode
        if (arg.equals("off") || arg.equals("disable") || arg.equals("desativar")) {
            plugin.getSlowModeManager().disable();
            MessageUtil.sendMessage(sender, "slowmode-disabled");
            
            // Log
            if (sender instanceof Player) {
                plugin.getLogManager().logCommand((Player) sender, "slowchat off");
            }
            
            return true;
        }
        
        // Ativar slow mode com delay
        try {
            int seconds = Integer.parseInt(arg);
            
            if (seconds < 1) {
                MessageUtil.send(sender, "&cO delay deve ser maior que 0!");
                return true;
            }
            
            if (seconds > 300) {
                MessageUtil.send(sender, "&cO delay máximo é 300 segundos (5 minutos)!");
                return true;
            }
            
            plugin.getSlowModeManager().enable(seconds);
            MessageUtil.sendMessage(sender, "slowmode-enabled", "delay", String.valueOf(seconds));
            
            // Log
            if (sender instanceof Player) {
                plugin.getLogManager().logCommand((Player) sender, "slowchat " + seconds);
            }
            
        } catch (NumberFormatException e) {
            MessageUtil.sendMessage(sender, "slowmode-invalid");
        }
        
        return true;
    }
}
