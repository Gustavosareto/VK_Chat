package me.vkchat;

import me.vkchat.commands.*;
import me.vkchat.hooks.PlaceholderAPIHook;
import me.vkchat.hooks.VaultHook;
import me.vkchat.listeners.ChatListener;
import me.vkchat.managers.*;
import me.vkchat.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Classe principal do plugin VKChat
 * Sistema completo de chat com suporte a Vault, PlaceholderAPI e LuckPerms
 * 
 * @author VKChat
 * @version 1.0.0
 */
public class VKChat extends JavaPlugin {
    
    // Instância única do plugin (Singleton)
    private static VKChat instance;
    
    // Configurações
    private FileConfiguration messagesConfig;
    private File messagesFile;
    
    // Gerenciadores principais
    private ChatManager chatManager;
    private AntiSpamManager antiSpamManager;
    private SlowModeManager slowModeManager;
    private ChannelManager channelManager;
    private MessageManager messageManager;
    private LogManager logManager;
    
    // Integrações com outros plugins
    private VaultHook vaultHook;
    private PlaceholderAPIHook placeholderAPIHook;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Banner de inicialização
        getLogger().info("========================================");
        getLogger().info("       VKChat v" + getDescription().getVersion());
        getLogger().info("    Sistema Completo de Chat");
        getLogger().info("========================================");
        
        // Carregar configurações
        loadConfigurations();
        
        // Inicializar integrações
        initializeHooks();
        
        // Inicializar gerenciadores
        initializeManagers();
        
        // Registrar eventos
        registerEvents();
        
        // Registrar comandos
        registerCommands();
        
        getLogger().info("Plugin VKChat iniciado com sucesso!");
        getLogger().info("========================================");
    }
    
    @Override
    public void onDisable() {
        // Salvar logs pendentes
        if (logManager != null) {
            logManager.flush();
        }
        
        // Limpar dados em memória
        if (chatManager != null) {
            chatManager.shutdown();
        }
        
        getLogger().info("Plugin VKChat desabilitado!");
    }
    
    /**
     * Carrega todas as configurações do plugin
     */
    private void loadConfigurations() {
        // Salvar config.yml padrão se não existir
        saveDefaultConfig();
        
        // Carregar messages.yml
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        
        getLogger().info("Configurações carregadas!");
    }
    
    /**
     * Inicializa as integrações com outros plugins
     */
    private void initializeHooks() {
        // Integração com Vault
        vaultHook = new VaultHook(this);
        if (vaultHook.setupChat()) {
            getLogger().info("Vault encontrado e conectado!");
        } else {
            getLogger().warning("Vault não encontrado! Algumas funcionalidades estarão limitadas.");
        }
        
        // Integração com PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIHook = new PlaceholderAPIHook(this);
            if (placeholderAPIHook.register()) {
                getLogger().info("PlaceholderAPI encontrado e conectado!");
            } else {
                getLogger().warning("PlaceholderAPI encontrado mas falhou ao registrar expansão.");
            }
        } else {
            getLogger().warning("PlaceholderAPI não encontrado! Placeholders customizados não estarão disponíveis.");
        }
    }
    
    /**
     * Inicializa todos os gerenciadores do plugin
     */
    private void initializeManagers() {
        chatManager = new ChatManager(this);
        antiSpamManager = new AntiSpamManager(this);
        slowModeManager = new SlowModeManager(this);
        channelManager = new ChannelManager(this);
        messageManager = new MessageManager(this);
        logManager = new LogManager(this);
        
        getLogger().info("Gerenciadores inicializados!");
    }
    
    /**
     * Registra todos os listeners de eventos
     */
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getLogger().info("Eventos registrados!");
    }
    
    /**
     * Registra todos os comandos do plugin
     */
    private void registerCommands() {
        // Comando /slowchat
        getCommand("slowchat").setExecutor(new SlowChatCommand(this));
        
        // Comando /chat
        getCommand("chat").setExecutor(new ChatCommand(this));
        
        // Comando /tell
        getCommand("tell").setExecutor(new TellCommand(this));
        
        // Comando /reply
        getCommand("reply").setExecutor(new ReplyCommand(this));
        
        // Comando /g (global)
        getCommand("g").setExecutor(new GlobalCommand(this));
        
        // Comando /l (local)
        getCommand("l").setExecutor(new LocalCommand(this));
        
        // Comando /sc (staff chat)
        getCommand("sc").setExecutor(new StaffChatCommand(this));
        
        getLogger().info("Comandos registrados!");
    }
    
    /**
     * Recarrega todas as configurações do plugin
     */
    public void reloadConfigurations() {
        reloadConfig();
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        
        // Recarregar gerenciadores
        chatManager.reload();
        antiSpamManager.reload();
        slowModeManager.reload();
        channelManager.reload();
        
        getLogger().info("Configurações recarregadas!");
    }
    
    /**
     * Salva a configuração de mensagens
     */
    public void saveMessagesConfig() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Não foi possível salvar messages.yml!", e);
        }
    }
    
    // ==================== GETTERS ====================
    
    public static VKChat getInstance() {
        return instance;
    }
    
    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
    
    public ChatManager getChatManager() {
        return chatManager;
    }
    
    public AntiSpamManager getAntiSpamManager() {
        return antiSpamManager;
    }
    
    public SlowModeManager getSlowModeManager() {
        return slowModeManager;
    }
    
    public ChannelManager getChannelManager() {
        return channelManager;
    }
    
    public MessageManager getMessageManager() {
        return messageManager;
    }
    
    public LogManager getLogManager() {
        return logManager;
    }
    
    public VaultHook getVaultHook() {
        return vaultHook;
    }
    
    public PlaceholderAPIHook getPlaceholderAPIHook() {
        return placeholderAPIHook;
    }
}
