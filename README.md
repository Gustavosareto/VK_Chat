# VKChat - Sistema Completo de Chat para Paper 1.8.8

![Java](https://img.shields.io/badge/Java-8-orange.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.8.8-green.svg)
![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)

Sistema completo de chat para Minecraft Paper 1.8.8 com suporte a **Vault**, **PlaceholderAPI** e **LuckPerms**.

---

## 📋 Índice

- [Funcionalidades](#-funcionalidades)
- [Requisitos](#-requisitos)
- [Instalação](#-instalação)
- [Compilação](#-compilação)
- [Configuração](#-configuração)
- [Comandos](#-comandos)
- [Permissões](#-permissões)
- [PlaceholderAPI](#-placeholderapi)
- [Suporte](#-suporte)

---

## ✨ Funcionalidades

### 🎨 Formatação Avançada de Chat
- ✅ Formatação por grupo via **Vault/LuckPerms**
- ✅ Suporte completo a **PlaceholderAPI**
- ✅ Placeholders padrão: `{player}`, `{displayname}`, `{world}`, `{group}`, `{prefix}`, `{suffix}`
- ✅ Configuração individual por grupo

### ⏱️ Slow Mode
- ✅ Comando `/slowchat <segundos|off>`
- ✅ Cooldown configurável entre mensagens
- ✅ Bypass por permissão
- ✅ Mensagens personalizáveis

### 💬 Sistema de Canais
- ✅ **Canal Global** (`/g <mensagem>`) - Todos ouvem
- ✅ **Canal Local** (`/l <mensagem>`) - Apenas jogadores próximos (100 blocos configurável)
- ✅ Formatos individuais por canal
- ✅ Permissões separadas

### 📨 Mensagens Privadas
- ✅ `/tell <player> <mensagem>` - Enviar mensagem privada
- ✅ `/reply <mensagem>` - Responder última mensagem
- ✅ Logs opcionais para staff
- ✅ Formatos personalizáveis

### 🛡️ Anti-Spam Robusto
- ✅ Detecção de mensagens repetidas
- ✅ Limite de caracteres configurável
- ✅ Bloqueio de CAPS excessivo
- ✅ Filtro de palavras personalizável
- ✅ Bypass por permissão
- ✅ Mensagens de erro configuráveis

### 👮 Chat Staff
- ✅ `/sc <mensagem>` - Chat exclusivo da staff
- ✅ Formato configurável
- ✅ Permissão: `vkchat.staff`

### 🔧 Gerenciamento de Chat
- ✅ `/chat toggle` - Desativar/ativar visualização do chat
- ✅ `/chat clear` - Limpar chat de todos
- ✅ `/chat clear local` - Limpar apenas seu chat

### 📝 Sistema de Logs
- ✅ Arquivos rotativos por dia
- ✅ Registro de mensagens públicas, privadas e staff
- ✅ Logs de comandos executados
- ✅ Formato: `logs/chat-DD-MM-YYYY.log`

### 📢 Sistema de Mentions
- ✅ Mencione jogadores com `@nome`
- ✅ Destaque visual configurável
- ✅ Som de notificação (configurável)
- ✅ Mensagem de notificação

### 🔌 PlaceholderAPI Expansion
Placeholders disponíveis:
- `%vkchat_last_message%` - Última mensagem enviada
- `%vkchat_cooldown%` - Tempo restante de cooldown
- `%vkchat_muted%` - Se está mutado
- `%vkchat_channel%` - Canal atual
- `%vkchat_chat_enabled%` - Se o chat está habilitado

---

## 📦 Requisitos

### Obrigatórios
- ✅ **Java 8** ou superior
- ✅ **Paper/Spigot 1.8.8**
- ✅ **Maven** (para compilação)

### Opcionais (mas recomendados)
- 🔷 **Vault** - Para prefixos, sufixos e grupos (funcionalidade limitada sem ele)
- 🔷 **PlaceholderAPI** - Para placeholders customizados (%vkchat_*%)
- 🔷 **LuckPerms** - Sistema de permissões (funciona automaticamente via Vault)

> **Nota Importante:** O plugin compila e funciona SEM PlaceholderAPI ou Vault instalados. PlaceholderAPI é detectado em runtime usando reflection, então você pode instalar depois se quiser usar os placeholders customizados.

---

## 🚀 Instalação

1. **Baixe o plugin** compilado (arquivo `.jar`)
2. **Coloque** na pasta `plugins/` do seu servidor
3. **Instale as dependências** opcionais (Vault, PlaceholderAPI)
4. **Reinicie** o servidor
5. **Configure** os arquivos em `plugins/VKChat/`

---

## 🔨 Compilação

### Passo a passo:

```bash
# 1. Clone ou baixe o projeto
cd VK_Chat

# 2. Compile com Maven
mvn clean package

# 3. O arquivo .jar estará em:
# target/VKChat-1.0.0.jar
```

### Estrutura do projeto:
```
VK_Chat/
├── pom.xml                          # Configuração Maven
├── README.md                        # Documentação
└── src/main/
    ├── java/me/vkchat/
    │   ├── VKChat.java             # Classe principal
    │   ├── commands/               # Todos os comandos
    │   │   ├── ChatCommand.java
    │   │   ├── GlobalCommand.java
    │   │   ├── LocalCommand.java
    │   │   ├── ReplyCommand.java
    │   │   ├── SlowChatCommand.java
    │   │   ├── StaffChatCommand.java
    │   │   └── TellCommand.java
    │   ├── hooks/                  # Integrações
    │   │   ├── PlaceholderAPIHook.java
    │   │   └── VaultHook.java
    │   ├── listeners/              # Event Listeners
    │   │   └── ChatListener.java
    │   ├── managers/               # Gerenciadores
    │   │   ├── AntiSpamManager.java
    │   │   ├── ChannelManager.java
    │   │   ├── ChatManager.java
    │   │   ├── LogManager.java
    │   │   ├── MessageManager.java
    │   │   └── SlowModeManager.java
    │   └── utils/                  # Utilitários
    │       └── MessageUtil.java
    └── resources/
        ├── plugin.yml              # Configuração do plugin
        ├── config.yml              # Configurações principais
        └── messages.yml            # Mensagens personalizáveis
```

---

## ⚙️ Configuração

### 📄 config.yml

```yaml
# Formato de chat por grupo
chat-format:
  default: "&7{player}: &f{message}"
  vip: "&6[VIP] &e{player}: &f{message}"
  admin: "&c[ADMIN] &4{player}: &f{message}"

# Sistema de canais
channels:
  global:
    enabled: true
    format: "&a[GLOBAL] &7{player}: &f{message}"
  local:
    enabled: true
    format: "&b[LOCAL] &7{player}: &f{message}"
    range: 100  # Distância em blocos

# Anti-Spam
anti-spam:
  enabled: true
  repeat-messages:
    enabled: true
    max-repeats: 3
  max-length:
    enabled: true
    characters: 256
  caps-block:
    enabled: true
    max-percentage: 70
  word-filter:
    enabled: true
    words:
      - "palavrao1"
      - "palavrao2"

# Sistema de Mentions
mentions:
  enabled: true
  format: "&e&l@{player}"
  sound:
    enabled: true
    sound-name: "NOTE_PLING"
```

### 📄 messages.yml

```yaml
prefix: "&8[&bVKChat&8]&r"

# Mensagens do sistema
no-permission: "{prefix} &cVocê não tem permissão!"
player-not-found: "{prefix} &cJogador não encontrado!"

# Slow Mode
slowmode-enabled: "{prefix} &aSlow mode ativado! Delay: &f{delay}s"
slowmode-wait: "{prefix} &cAguarde &f{time}s antes de enviar outra mensagem!"

# Anti-Spam
antispam-repeat: "{prefix} &cNão repita a mesma mensagem!"
antispam-caps: "{prefix} &cNão abuse de letras maiúsculas!"
```

---

## 🎮 Comandos

| Comando | Descrição | Permissão | Aliases |
|---------|-----------|-----------|---------|
| `/slowchat <segundos\|off>` | Ativa/desativa slow mode | `vkchat.slowchat` | `/slow` |
| `/chat toggle` | Ativa/desativa visualização do chat | `vkchat.chat.toggle` | - |
| `/chat clear` | Limpa o chat de todos | `vkchat.chat.clear` | - |
| `/tell <player> <msg>` | Envia mensagem privada | `vkchat.tell` | `/msg`, `/w` |
| `/reply <mensagem>` | Responde última mensagem | `vkchat.reply` | `/r` |
| `/g <mensagem>` | Envia no chat global | `vkchat.channel.global` | `/global` |
| `/l <mensagem>` | Envia no chat local | `vkchat.channel.local` | `/local` |
| `/sc <mensagem>` | Chat da staff | `vkchat.staff` | `/staffchat` |

---

## 🔐 Permissões

### Permissões Principais

| Permissão | Descrição | Padrão |
|-----------|-----------|--------|
| `vkchat.*` | Todas as permissões | OP |
| `vkchat.slowchat` | Usar `/slowchat` | OP |
| `vkchat.slowchat.bypass` | Ignorar slow mode | OP |
| `vkchat.chat.toggle` | Desativar chat | Todos |
| `vkchat.chat.clear` | Limpar chat | OP |
| `vkchat.tell` | Mensagens privadas | Todos |
| `vkchat.reply` | Responder mensagens | Todos |
| `vkchat.channel.global` | Chat global | Todos |
| `vkchat.channel.local` | Chat local | Todos |
| `vkchat.staff` | Chat da staff | OP |
| `vkchat.antispam.bypass` | Ignorar anti-spam | OP |
| `vkchat.color` | Usar cores no chat | OP |
| `vkchat.mention.use` | Mencionar jogadores | Todos |

### Exemplo LuckPerms

```bash
# Dar permissão para VIP usar cores
lp group vip permission set vkchat.color true

# Dar permissão para admin ignorar slow mode
lp group admin permission set vkchat.slowchat.bypass true

# Dar todas as permissões para dono
lp group dono permission set vkchat.* true
```

---

## 🔌 PlaceholderAPI

### Expansão Interna

O plugin registra automaticamente uma expansão do PlaceholderAPI com os seguintes placeholders:

| Placeholder | Descrição | Exemplo |
|-------------|-----------|---------|
| `%vkchat_last_message%` | Última mensagem enviada | "Olá mundo!" |
| `%vkchat_cooldown%` | Tempo de cooldown restante (segundos) | "3" |
| `%vkchat_muted%` | Se está mutado | "true" / "false" |
| `%vkchat_channel%` | Canal atual | "global" / "local" |
| `%vkchat_chat_enabled%` | Se o chat está ativo | "true" / "false" |

### Uso em Outros Plugins

```yaml
# Exemplo: DeluxeMenus
- '%vkchat_channel%'  # Mostra canal atual
- '%vkchat_cooldown%' # Mostra cooldown
```

### Placeholders Suportados no Chat

Além dos placeholders do VKChat, você pode usar **qualquer placeholder** do PlaceholderAPI nas mensagens de chat:

```yaml
chat-format:
  vip: "&6[VIP] %player_displayname%: &f{message}"
  admin: "&c[ADMIN] %vault_prefix%%player_name%: &f{message}"
```

---

## 📊 Sistema de Logs

### Formato dos Logs

```
[14:23:45] [GLOBAL] [world] PlayerName: Mensagem do chat
[14:24:01] [TELL] PlayerA -> PlayerB: Mensagem privada
[14:25:12] [STAFF] AdminPlayer: Mensagem da staff
[14:26:00] [COMMAND] PlayerName: /slowchat 5
[14:27:30] [ACTION] Chat cleared by AdminPlayer
```

### Localização

Os logs são salvos em: `plugins/VKChat/logs/chat-DD-MM-YYYY.log`

Exemplo: `chat-27-11-2024.log`

---

## 🎨 Códigos de Cor

### Cores Disponíveis (Minecraft 1.8.8)

```
&0 - Preto         &8 - Cinza Escuro
&1 - Azul Escuro   &9 - Azul
&2 - Verde Escuro  &a - Verde
&3 - Ciano         &b - Ciano Claro
&4 - Vermelho      &c - Vermelho Claro
&5 - Roxo          &d - Rosa
&6 - Dourado       &e - Amarelo
&7 - Cinza         &f - Branco
```

### Formatação

```
&l - Negrito
&m - Riscado
&n - Sublinhado
&o - Itálico
&r - Reset
```

---

## 🐛 Troubleshooting

### Plugin não inicia

1. Verifique se está usando **Java 8** ou superior
2. Verifique se é **Paper/Spigot 1.8.8**
3. Veja os logs em `logs/latest.log`

### Vault não conecta

1. Instale o plugin **Vault**
2. Instale um plugin de permissões (**LuckPerms** recomendado)
3. Reinicie o servidor

### PlaceholderAPI não funciona

1. Instale o plugin **PlaceholderAPI**
2. Reinicie o servidor
3. Verifique: `/papi list` (deve mostrar "vkchat")

### Caracteres especiais não aparecem

1. Certifique-se que os arquivos estão em **UTF-8**
2. Use um editor que suporte UTF-8 (VS Code, Notepad++)

---

## 📝 Notas Importantes

### Compatibilidade

- ✅ **Paper 1.8.8** - Totalmente compatível
- ✅ **Spigot 1.8.8** - Totalmente compatível
- ⚠️ **Versões superiores** - Pode funcionar, mas não testado
- ❌ **Bukkit puro** - Não recomendado

### Performance

- O plugin é **otimizado** para servidores grandes
- Logs são gravados em **buffer** (menos I/O)
- Anti-spam usa **cache em memória**
- Slow mode usa **timestamps eficientes**

### Segurança

- ✅ Filtro de palavras configurável
- ✅ Anti-spam robusto
- ✅ Logs completos para auditoria
- ✅ Permissões granulares

---

## 📞 Suporte

### Reportar Bugs

Encontrou um bug? Abra uma issue com:
- Versão do Minecraft
- Versão do plugin
- Logs de erro
- Passos para reproduzir

### Sugestões

Tem uma sugestão? Compartilhe com:
- Descrição da funcionalidade
- Caso de uso
- Mockup (se aplicável)

---

## 📜 Licença

Este projeto é de código aberto. Sinta-se livre para usar, modificar e distribuir.

---

## 👨‍💻 Créditos

**Desenvolvido por:** VKChat Team  
**Versão:** 1.0.0  
**Data:** Novembro 2024  

**Dependências:**
- Vault API
- PlaceholderAPI
- Spigot API 1.8.8

---

## 🎯 Roadmap

### Futuras Funcionalidades

- [ ] Sistema de mute temporário
- [ ] Chat de clãs/facções
- [ ] Comandos de moderação (/mute, /unmute)
- [ ] Integração com Discord
- [ ] Sistema de badges customizados
- [ ] Chat de anúncios
- [ ] Cooldown individual por grupo
- [ ] Blacklist de IPs para spam

---

**⭐ Se gostou do plugin, considere dar uma estrela no repositório!**

---

## 📸 Screenshots

### Chat Formatado
```
[VIP] VKPlayer: Olá pessoal!
[ADMIN] AdminPlayer: Bem-vindos ao servidor!
Player123: Obrigado!
```

### Chat Local
```
[LOCAL] PlayerA: Alguém tem ferro?
[LOCAL] PlayerB: Tenho! Vem aqui
```

### Mentions
```
Player: @AdminPlayer preciso de ajuda!
AdminPlayer recebe: [VKChat] Player mencionou você no chat!
```

---

**Bom uso! 🎮**
