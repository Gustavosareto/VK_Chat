package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /chat
 * Gerencia funcionalidades do chat como toggle e clear
 * 
 * Uso: /chat <toggle|clear>
 * Permissões: vkchat.chat.toggle, vkchat.chat.clear
 */
public class ChatCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public ChatCommand(VKChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verificar argumentos
        if (args.length < 1) {
            MessageUtil.send(sender, "&cUso: /chat <toggle|clear>");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "toggle":
                return handleToggle(sender);
            
            case "clear":
            case "limpar":
                return handleClear(sender, args);
            
            default:
                MessageUtil.send(sender, "&cSubcomando inválido! Use: /chat <toggle|clear>");
                return true;
        }
    }
    
    /**
     * Processa o subcomando /chat toggle
     */
    private boolean handleToggle(CommandSender sender) {
        // Apenas jogadores podem usar
        if (!(sender instanceof Player)) {
            MessageUtil.send(sender, "&cApenas jogadores podem usar este comando!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Verificar permissão
        if (!player.hasPermission("vkchat.chat.toggle")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Alternar estado do chat
        plugin.getChatManager().toggleChat(player);
        
        // Enviar mensagem apropriada
        if (plugin.getChatManager().isChatDisabled(player)) {
            MessageUtil.sendMessage(player, "chat-disabled");
        } else {
            MessageUtil.sendMessage(player, "chat-enabled");
        }
        
        // Log
        plugin.getLogManager().logCommand(player, "chat toggle");
        
        return true;
    }
    
    /**
     * Processa o subcomando /chat clear
     */
    private boolean handleClear(CommandSender sender, String[] args) {
        // Verificar permissão
        if (!sender.hasPermission("vkchat.chat.clear")) {
            MessageUtil.sendMessage(sender, "no-permission");
            return true;
        }
        
        // Verificar modo (all ou local)
        boolean clearAll = true;
        if (args.length > 1) {
            String mode = args[1].toLowerCase();
            if (mode.equals("local")) {
                clearAll = false;
            }
        }
        
        if (clearAll) {
            // Limpar chat de todos
            plugin.getChatManager().clearChat();
            
            // Anunciar quem limpou
            String message = MessageUtil.getMessage("chat-cleared-by", 
                "player", sender.getName());
            
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendMessage(message);
            }
        } else {
            // Limpar apenas do próprio jogador
            if (!(sender instanceof Player)) {
                MessageUtil.send(sender, "&cApenas jogadores podem limpar o chat local!");
                return true;
            }
            
            Player player = (Player) sender;
            plugin.getChatManager().clearChat(player);
            MessageUtil.sendMessage(player, "chat-cleared");
        }
        
        // Log
        if (sender instanceof Player) {
            plugin.getLogManager().logCommand((Player) sender, "chat clear");
        }
        plugin.getLogManager().logAction("Chat cleared by " + sender.getName());
        
        return true;
    }
}
