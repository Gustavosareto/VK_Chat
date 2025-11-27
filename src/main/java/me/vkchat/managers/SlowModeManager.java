package me.vkchat.managers;

import me.vkchat.VKChat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerenciador de Slow Mode
 * Controla o intervalo entre mensagens dos jogadores
 */
public class SlowModeManager {
    
    private final VKChat plugin;
    
    // Estado do slow mode
    private boolean enabled;
    private int delay; // Delay em segundos
    
    // Último horário que cada jogador enviou mensagem
    private final Map<UUID, Long> lastMessageTime;
    
    public SlowModeManager(VKChat plugin) {
        this.plugin = plugin;
        this.lastMessageTime = new HashMap<>();
        this.enabled = plugin.getConfig().getBoolean("slow-mode.enabled", false);
        this.delay = plugin.getConfig().getInt("slow-mode.default-delay", 3);
    }
    
    /**
     * Verifica se slow mode está ativado
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Ativa o slow mode
     */
    public void enable(int seconds) {
        this.enabled = true;
        this.delay = seconds;
    }
    
    /**
     * Desativa o slow mode
     */
    public void disable() {
        this.enabled = false;
        lastMessageTime.clear();
    }
    
    /**
     * Obtém o delay atual em segundos
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * Verifica se um jogador pode enviar mensagem (não está em cooldown)
     */
    public boolean canSendMessage(Player player) {
        // Verificar se tem permissão de bypass
        if (player.hasPermission("vkchat.slowchat.bypass")) {
            return true;
        }
        
        // Se slow mode não está ativo, pode enviar
        if (!enabled) {
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        Long lastTime = lastMessageTime.get(uuid);
        
        // Se nunca enviou mensagem, pode enviar
        if (lastTime == null) {
            return true;
        }
        
        // Calcular tempo decorrido
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - lastTime) / 1000;
        
        return elapsedSeconds >= delay;
    }
    
    /**
     * Obtém o tempo restante de cooldown em segundos
     */
    public long getRemainingCooldown(Player player) {
        if (!enabled || player.hasPermission("vkchat.slowchat.bypass")) {
            return 0;
        }
        
        UUID uuid = player.getUniqueId();
        Long lastTime = lastMessageTime.get(uuid);
        
        if (lastTime == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - lastTime) / 1000;
        long remaining = delay - elapsedSeconds;
        
        return Math.max(0, remaining);
    }
    
    /**
     * Registra que um jogador enviou uma mensagem
     */
    public void recordMessage(Player player) {
        lastMessageTime.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Remove o cooldown de um jogador
     */
    public void removeCooldown(Player player) {
        lastMessageTime.remove(player.getUniqueId());
    }
    
    /**
     * Recarrega as configurações
     */
    public void reload() {
        this.enabled = plugin.getConfig().getBoolean("slow-mode.enabled", false);
        this.delay = plugin.getConfig().getInt("slow-mode.default-delay", 3);
    }
}
