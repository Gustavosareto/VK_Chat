package me.vkchat.commands;

import me.vkchat.VKChat;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando /g (global)
 * Envia mensagens no chat global
 * 
 * Uso: /g <mensagem>
 * Permissão: vkchat.channel.global
 */
public class GlobalCommand implements CommandExecutor {
    
    private final VKChat plugin;
    
    public GlobalCommand(VKChat plugin) {
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
        if (!player.hasPermission("vkchat.channel.global")) {
            MessageUtil.sendMessage(player, "no-permission");
            return true;
        }
        
        // Verificar se canal está habilitado
        if (!plugin.getChannelManager().isChannelEnabled("global")) {
            MessageUtil.sendMessage(player, "command-disabled");
            return true;
        }
        
        // Verificar argumentos
        if (args.length < 1) {
            MessageUtil.send(player, "&cUso: /g <mensagem>");
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
        
        // Verificar anti-spam
        String spamCheck = plugin.getAntiSpamManager().checkMessage(player, message);
        if (spamCheck != null) {
            MessageUtil.sendMessage(player, spamCheck);
            return true;
        }
        
        // Verificar slow mode
        if (!plugin.getSlowModeManager().canSendMessage(player)) {
            long remaining = plugin.getSlowModeManager().getRemainingCooldown(player);
            MessageUtil.sendMessage(player, "slowmode-wait", "time", String.valueOf(remaining));
            return true;
        }
        
        // Processar mensagem (cores se tiver permissão)
        message = MessageUtil.processMessage(player, message);
        
        // Obter formato do canal
        String format = plugin.getChannelManager().getChannelFormat("global");
        
        // Substituir placeholders básicos do Vault
        format = format.replace("{player}", player.getName());
        format = format.replace("{displayname}", player.getDisplayName());
        format = format.replace("{world}", player.getWorld().getName());
        
        if (plugin.getVaultHook().isAvailable()) {
            format = format.replace("{group}", plugin.getVaultHook().getPrimaryGroup(player));
            format = format.replace("{prefix}", plugin.getVaultHook().getPrefix(player));
            format = format.replace("{suffix}", plugin.getVaultHook().getSuffix(player));
        }
        
        // Processar PlaceholderAPI
        format = plugin.getChatManager().setPlaceholdersPublic(player, format);
        
        // Substituir mensagem
        format = format.replace("{message}", message);
        
        // Colorir
        String finalMessage = MessageUtil.colorize(format);
        
        // Enviar para todos os jogadores
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!plugin.getChatManager().isChatDisabled(online)) {
                online.sendMessage(finalMessage);
            }
        }
        
        // Registrar cooldown
        plugin.getSlowModeManager().recordMessage(player);
        plugin.getChatManager().setLastMessage(player, message);
        
        // Log
        plugin.getLogManager().logPublicMessage(player, message, "GLOBAL");
        
        return true;
    }
}
