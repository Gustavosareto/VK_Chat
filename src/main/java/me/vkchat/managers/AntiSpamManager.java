package me.vkchat.managers;

import me.vkchat.VKChat;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Gerenciador de Anti-Spam
 * Controla mensagens repetidas, CAPS, comprimento e filtro de palavras
 */
public class AntiSpamManager {
    
    private final VKChat plugin;
    
    // Histórico de mensagens recentes de cada jogador
    private final Map<UUID, List<MessageRecord>> messageHistory;
    
    // Lista de palavras bloqueadas (cache)
    private List<String> blockedWords;
    
    public AntiSpamManager(VKChat plugin) {
        this.plugin = plugin;
        this.messageHistory = new HashMap<>();
        loadBlockedWords();
    }
    
    /**
     * Carrega as palavras bloqueadas da config
     */
    private void loadBlockedWords() {
        blockedWords = plugin.getConfig().getStringList("anti-spam.word-filter.words");
        if (blockedWords == null) {
            blockedWords = new ArrayList<>();
        }
    }
    
    /**
     * Verifica se uma mensagem é spam
     * @return null se a mensagem é válida, ou a razão se for spam
     */
    public String checkMessage(Player player, String message) {
        // Verificar bypass
        if (player.hasPermission("vkchat.antispam.bypass")) {
            return null;
        }
        
        // Verificar se anti-spam está ativado
        if (!plugin.getConfig().getBoolean("anti-spam.enabled", true)) {
            return null;
        }
        
        // 1. Verificar comprimento máximo
        if (plugin.getConfig().getBoolean("anti-spam.max-length.enabled", true)) {
            int maxLength = plugin.getConfig().getInt("anti-spam.max-length.characters", 256);
            if (message.length() > maxLength) {
                return "antispam-too-long";
            }
        }
        
        // 2. Verificar CAPS excessivo
        if (plugin.getConfig().getBoolean("anti-spam.caps-block.enabled", true)) {
            int minLength = plugin.getConfig().getInt("anti-spam.caps-block.min-length", 6);
            if (message.length() >= minLength) {
                double maxPercentage = plugin.getConfig().getDouble("anti-spam.caps-block.max-percentage", 70);
                double capsPercentage = calculateCapsPercentage(message);
                
                if (capsPercentage > maxPercentage) {
                    return "antispam-caps";
                }
            }
        }
        
        // 3. Verificar palavras bloqueadas
        if (plugin.getConfig().getBoolean("anti-spam.word-filter.enabled", true)) {
            if (containsBlockedWord(message)) {
                return "antispam-blocked-word";
            }
        }
        
        // 4. Verificar mensagens repetidas
        if (plugin.getConfig().getBoolean("anti-spam.repeat-messages.enabled", true)) {
            if (isRepeatedMessage(player, message)) {
                return "antispam-repeat";
            }
        }
        
        // Mensagem válida
        recordMessage(player, message);
        return null;
    }
    
    /**
     * Calcula a porcentagem de letras maiúsculas em uma mensagem
     */
    private double calculateCapsPercentage(String message) {
        int letters = 0;
        int upperCase = 0;
        
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
                if (Character.isUpperCase(c)) {
                    upperCase++;
                }
            }
        }
        
        if (letters == 0) return 0;
        return ((double) upperCase / letters) * 100;
    }
    
    /**
     * Verifica se a mensagem contém palavras bloqueadas
     */
    private boolean containsBlockedWord(String message) {
        String lowerMessage = message.toLowerCase();
        
        for (String word : blockedWords) {
            if (lowerMessage.contains(word.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Processa uma mensagem substituindo palavras bloqueadas
     */
    public String filterMessage(String message) {
        String action = plugin.getConfig().getString("anti-spam.word-filter.action", "block");
        
        if (!action.equalsIgnoreCase("replace")) {
            return message;
        }
        
        String result = message;
        for (String word : blockedWords) {
            String replacement = "*".repeat(word.length());
            result = result.replaceAll("(?i)" + word, replacement);
        }
        
        return result;
    }
    
    /**
     * Verifica se a mensagem é repetida
     */
    private boolean isRepeatedMessage(Player player, String message) {
        UUID uuid = player.getUniqueId();
        List<MessageRecord> history = messageHistory.getOrDefault(uuid, new ArrayList<>());
        
        int maxRepeats = plugin.getConfig().getInt("anti-spam.repeat-messages.max-repeats", 3);
        int timeWindow = plugin.getConfig().getInt("anti-spam.repeat-messages.time-window", 30);
        long currentTime = System.currentTimeMillis();
        
        // Limpar mensagens antigas
        history.removeIf(record -> (currentTime - record.time) / 1000 > timeWindow);
        
        // Contar quantas vezes a mensagem foi repetida
        int count = 0;
        for (MessageRecord record : history) {
            if (record.message.equalsIgnoreCase(message)) {
                count++;
            }
        }
        
        return count >= maxRepeats;
    }
    
    /**
     * Registra uma mensagem no histórico
     */
    private void recordMessage(Player player, String message) {
        UUID uuid = player.getUniqueId();
        List<MessageRecord> history = messageHistory.computeIfAbsent(uuid, k -> new ArrayList<>());
        
        history.add(new MessageRecord(message, System.currentTimeMillis()));
        
        // Limitar tamanho do histórico
        if (history.size() > 10) {
            history.remove(0);
        }
    }
    
    /**
     * Limpa o histórico de um jogador
     */
    public void clearHistory(Player player) {
        messageHistory.remove(player.getUniqueId());
    }
    
    /**
     * Recarrega as configurações
     */
    public void reload() {
        loadBlockedWords();
    }
    
    /**
     * Classe auxiliar para armazenar registros de mensagens
     */
    private static class MessageRecord {
        final String message;
        final long time;
        
        MessageRecord(String message, long time) {
            this.message = message;
            this.time = time;
        }
    }
}
