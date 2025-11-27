package me.vkchat.managers;

import me.vkchat.VKChat;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Gerenciador de logs do chat
 * Registra mensagens públicas, privadas, staff e comandos
 */
public class LogManager {
    
    private final VKChat plugin;
    private final File logsFolder;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;
    
    // Buffer de logs pendentes
    private final List<String> pendingLogs;
    
    public LogManager(VKChat plugin) {
        this.plugin = plugin;
        this.pendingLogs = new ArrayList<>();
        
        // Criar pasta de logs
        String folderName = plugin.getConfig().getString("logging.log-folder", "logs");
        this.logsFolder = new File(plugin.getDataFolder(), folderName);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
        
        // Formatos de data
        String datePattern = plugin.getConfig().getString("logging.date-format", "dd-MM-yyyy");
        this.dateFormat = new SimpleDateFormat(datePattern);
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
    }
    
    /**
     * Registra uma mensagem pública do chat
     */
    public void logPublicMessage(Player player, String message, String channel) {
        if (!plugin.getConfig().getBoolean("logging.log-public-chat", true)) {
            return;
        }
        
        String log = String.format("[%s] [%s] [%s] %s: %s",
            timeFormat.format(new Date()),
            channel.toUpperCase(),
            player.getWorld().getName(),
            player.getName(),
            message
        );
        
        writeLog(log);
    }
    
    /**
     * Registra uma mensagem privada
     */
    public void logPrivateMessage(Player sender, Player receiver, String message) {
        if (!plugin.getConfig().getBoolean("logging.log-private-messages", true)) {
            return;
        }
        
        String log = String.format("[%s] [TELL] %s -> %s: %s",
            timeFormat.format(new Date()),
            sender.getName(),
            receiver.getName(),
            message
        );
        
        writeLog(log);
    }
    
    /**
     * Registra uma mensagem do chat da staff
     */
    public void logStaffMessage(Player player, String message) {
        if (!plugin.getConfig().getBoolean("logging.log-staff-chat", true)) {
            return;
        }
        
        String log = String.format("[%s] [STAFF] %s: %s",
            timeFormat.format(new Date()),
            player.getName(),
            message
        );
        
        writeLog(log);
    }
    
    /**
     * Registra um comando executado
     */
    public void logCommand(Player player, String command) {
        if (!plugin.getConfig().getBoolean("logging.log-commands", true)) {
            return;
        }
        
        String log = String.format("[%s] [COMMAND] %s: /%s",
            timeFormat.format(new Date()),
            player.getName(),
            command
        );
        
        writeLog(log);
    }
    
    /**
     * Registra uma ação genérica
     */
    public void logAction(String action) {
        String log = String.format("[%s] [ACTION] %s",
            timeFormat.format(new Date()),
            action
        );
        
        writeLog(log);
    }
    
    /**
     * Escreve um log no arquivo
     */
    private void writeLog(String message) {
        if (!plugin.getConfig().getBoolean("logging.enabled", true)) {
            return;
        }
        
        // Adicionar ao buffer
        pendingLogs.add(message);
        
        // Gravar se buffer está grande
        if (pendingLogs.size() >= 10) {
            flush();
        }
    }
    
    /**
     * Grava todos os logs pendentes no arquivo
     */
    public void flush() {
        if (pendingLogs.isEmpty()) {
            return;
        }
        
        File logFile = getLogFile();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            for (String log : pendingLogs) {
                writer.write(log);
                writer.newLine();
            }
            pendingLogs.clear();
        } catch (IOException e) {
            plugin.getLogger().warning("Erro ao salvar logs: " + e.getMessage());
        }
    }
    
    /**
     * Obtém o arquivo de log do dia atual
     */
    private File getLogFile() {
        String fileName = "chat-" + dateFormat.format(new Date()) + ".log";
        return new File(logsFolder, fileName);
    }
}
