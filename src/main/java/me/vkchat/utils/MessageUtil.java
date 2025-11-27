package me.vkchat.utils;

import me.vkchat.VKChat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Utilitário para processar e enviar mensagens
 */
public class MessageUtil {
    
    private static final VKChat plugin = VKChat.getInstance();
    
    /**
     * Traduz códigos de cor (&) para formato Minecraft
     */
    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Remove todos os códigos de cor de uma string
     */
    public static String stripColor(String message) {
        if (message == null) return "";
        return ChatColor.stripColor(message);
    }
    
    /**
     * Obtém uma mensagem do messages.yml
     */
    public static String getMessage(String path) {
        String message = plugin.getMessagesConfig().getString(path);
        if (message == null) {
            return colorize("&cMensagem não encontrada: " + path);
        }
        
        // Substituir placeholder de prefixo
        String prefix = plugin.getMessagesConfig().getString("prefix", "&8[&bVKChat&8]&r");
        message = message.replace("{prefix}", prefix);
        
        return colorize(message);
    }
    
    /**
     * Obtém uma mensagem com placeholders substituídos
     */
    public static String getMessage(String path, String... placeholders) {
        String message = getMessage(path);
        
        // Substituir placeholders (formato: {key}, valor)
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
        }
        
        return message;
    }
    
    /**
     * Envia uma mensagem para um jogador/sender
     */
    public static void send(CommandSender sender, String message) {
        if (sender != null && message != null && !message.isEmpty()) {
            sender.sendMessage(colorize(message));
        }
    }
    
    /**
     * Envia uma mensagem do messages.yml
     */
    public static void sendMessage(CommandSender sender, String path) {
        send(sender, getMessage(path));
    }
    
    /**
     * Envia uma mensagem do messages.yml com placeholders
     */
    public static void sendMessage(CommandSender sender, String path, String... placeholders) {
        send(sender, getMessage(path, placeholders));
    }
    
    /**
     * Verifica se o jogador tem permissão para usar cores
     */
    public static boolean canUseColors(Player player) {
        if (!plugin.getConfig().getBoolean("general.color-codes.enabled", true)) {
            return false;
        }
        
        String permission = plugin.getConfig().getString("general.color-codes.permission", "vkchat.color");
        return player.hasPermission(permission);
    }
    
    /**
     * Processa uma mensagem, aplicando cores se permitido
     */
    public static String processMessage(Player player, String message) {
        if (canUseColors(player)) {
            return colorize(message);
        }
        return stripColor(message);
    }
    
    /**
     * Conta quantas letras maiúsculas tem em uma string
     */
    public static int countUpperCase(String text) {
        if (text == null || text.isEmpty()) return 0;
        
        int count = 0;
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Calcula a porcentagem de letras maiúsculas
     */
    public static double getUpperCasePercentage(String text) {
        if (text == null || text.isEmpty()) return 0;
        
        int letters = 0;
        int upperCase = 0;
        
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
                if (Character.isUpperCase(c)) {
                    upperCase++;
                }
            }
        }
        
        if (letters == 0) return 0;
        return (double) upperCase / letters * 100;
    }
    
    /**
     * Formata um tempo em segundos para formato legível
     */
    public static String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            int minutes = seconds / 60;
            int secs = seconds % 60;
            return minutes + "m " + secs + "s";
        } else {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            return hours + "h " + minutes + "m";
        }
    }
}
