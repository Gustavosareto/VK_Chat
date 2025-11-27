package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /spy
 * Permite espionar mensagens privadas e staff chat de outros jogadores
 * 
 * Uso: /spy [on|off|toggle]
 * Permissão: vkchat.spy
 */
public class SpyCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public SpyCommand(VKChat plugin) {
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
        if (!player.hasPermission("vkchat.spy")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Processar argumentos
        if (args.length == 0) {
            // Sem argumentos = toggle
            toggleSpy(player);
        } else {
            String arg = args[0].toLowerCase();
            
            switch (arg) {
                case "on":
                case "ativar":
                case "enable":
                    enableSpy(player);
                    break;
                    
                case "off":
                case "desativar":
                case "disable":
                    disableSpy(player);
                    break;
                    
                case "toggle":
                    toggleSpy(player);
                    break;
                    
                default:
                    MessageUtil.send(player, "&cUso: /spy [on|off|toggle]");
                    break;
            }
        }
        
        return true;
    }
    
    /**
     * Alterna o modo spy
     */
    private void toggleSpy(Player player) {
        if (plugin.getChatManager().isSpyEnabled(player)) {
            disableSpy(player);
        } else {
            enableSpy(player);
        }
    }
    
    /**
     * Ativa o modo spy
     */
    private void enableSpy(Player player) {
        plugin.getChatManager().enableSpy(player);
        MessageUtil.send(player, "&aModo Spy &aATIVADO&a! Você verá todas as mensagens privadas.");
    }
    
    /**
     * Desativa o modo spy
     */
    private void disableSpy(Player player) {
        plugin.getChatManager().disableSpy(player);
        MessageUtil.send(player, "&cModo Spy &cDESATIVADO&c!");
    }
}
