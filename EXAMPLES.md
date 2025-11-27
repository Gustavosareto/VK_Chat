# 📚 Exemplos de Configuração - VKChat

Este documento contém exemplos práticos de configuração para diferentes cenários.

---

## 📋 Índice

- [Servidor de Survival](#servidor-de-survival)
- [Servidor Hardcore/PvP](#servidor-hardcorepvp)
- [Servidor Roleplay](#servidor-roleplay)
- [Servidor Criativo](#servidor-criativo)
- [Servidor Skyblock](#servidor-skyblock)

---

## 🌳 Servidor de Survival

### Características
- Chat local e global ativos
- Anti-spam moderado
- Formatação por grupo (VIP, MVP, etc)

### config.yml
```yaml
chat-format:
  default: "&7{player}: &f{message}"
  vip: "&6[VIP] &e{player}: &f{message}"
  mvp: "&b[MVP] &3{player}: &f{message}"
  admin: "&c[ADMIN] &4{player}: &f{message}"

channels:
  global:
    enabled: true
    format: "&a[GLOBAL] &7{player}: &f{message}"
    permission: "vkchat.channel.global"
  
  local:
    enabled: true
    format: "&b[LOCAL] &7{player}: &f{message}"
    permission: "vkchat.channel.local"
    range: 100

staff-chat:
  enabled: true
  format: "&c[STAFF] &7{player}: &f{message}"

slow-mode:
  enabled: false
  default-delay: 3

anti-spam:
  enabled: true
  bypass-permission: "vkchat.antispam.bypass"
  
  repeat-messages:
    enabled: true
    max-repeats: 3
    time-window: 30
  
  max-length:
    enabled: true
    characters: 256
  
  caps-block:
    enabled: true
    max-percentage: 70
    min-length: 6
  
  word-filter:
    enabled: true
    action: "block"
    words:
      - "hack"
      - "xray"

mentions:
  enabled: true
  format: "&e&l@{player}"
  sound:
    enabled: true
    sound-name: "NOTE_PLING"
    volume: 1.0
    pitch: 1.0

logging:
  enabled: true
  log-public-chat: true
  log-private-messages: true
  log-staff-chat: true

general:
  color-codes:
    enabled: true
    permission: "vkchat.color"
  default-channel: "global"
```

---

## ⚔️ Servidor Hardcore/PvP

### Características
- Apenas chat local (estratégia)
- Anti-spam rigoroso
- Sem cores para jogadores comuns
- Slow mode ativo

### config.yml
```yaml
chat-format:
  default: "&8[{world}] &7{player}: &f{message}"
  vip: "&8[{world}] &6{player}: &f{message}"
  admin: "&c[ADMIN] &4{player}: &f{message}"

channels:
  global:
    enabled: false  # Desativado em PvP
  
  local:
    enabled: true
    format: "&7[LOCAL] {player}: &f{message}"
    permission: "vkchat.channel.local"
    range: 50  # Alcance menor para estratégia

staff-chat:
  enabled: true
  format: "&c[STAFF] &7{player}: &f{message}"

slow-mode:
  enabled: true
  default-delay: 5  # 5 segundos entre mensagens

anti-spam:
  enabled: true
  bypass-permission: "vkchat.antispam.bypass"
  
  repeat-messages:
    enabled: true
    max-repeats: 2  # Mais rigoroso
    time-window: 60
  
  max-length:
    enabled: true
    characters: 128  # Mensagens curtas
  
  caps-block:
    enabled: true
    max-percentage: 50  # Sem gritos
    min-length: 5
  
  word-filter:
    enabled: true
    action: "block"
    words:
      - "coordenadas"
      - "coords"
      - "base"

mentions:
  enabled: false  # Desativado para não revelar posições

logging:
  enabled: true
  log-public-chat: true
  log-private-messages: true
  log-staff-chat: true

general:
  color-codes:
    enabled: false  # Apenas staff
    permission: "vkchat.color"
  default-channel: "local"
```

---

## 🎭 Servidor Roleplay

### Características
- Chat local obrigatório (imersão)
- Formatação rica com prefixos
- Mentions para interações
- Sem anti-spam rigoroso

### config.yml
```yaml
chat-format:
  default: "&7[Cidadão] {prefix}{player}&7: &f{message}"
  comerciante: "&e[Comerciante] {prefix}{player}&7: &f{message}"
  guarda: "&9[Guarda] {prefix}{player}&7: &f{message}"
  nobre: "&5[Nobre] {prefix}{player}&7: &f{message}"
  rei: "&6[Rei] {prefix}{player}&7: &f{message}"
  admin: "&c[ADM] &4{player}&7: &f{message}"

channels:
  global:
    enabled: true
    format: "&8[&6ANÚNCIO&8] &7{player}: &f{message}"
    permission: "vkchat.channel.global"  # Apenas staff/eventos
  
  local:
    enabled: true
    format: "&7{player} diz: &f{message}"
    permission: "vkchat.channel.local"
    range: 30  # Muito próximo para RP

staff-chat:
  enabled: true
  format: "&c[OOC-STAFF] &7{player}: &f{message}"

slow-mode:
  enabled: false  # Não atrapalhar RP

anti-spam:
  enabled: true
  bypass-permission: "vkchat.antispam.bypass"
  
  repeat-messages:
    enabled: true
    max-repeats: 5  # Mais permissivo
    time-window: 60
  
  max-length:
    enabled: true
    characters: 512  # Mensagens longas para RP
  
  caps-block:
    enabled: false  # Permitir gritos em RP
  
  word-filter:
    enabled: true
    action: "replace"
    words:
      - "afk"
      - "brb"

mentions:
  enabled: true
  format: "&e@{player}"
  sound:
    enabled: true
    sound-name: "NOTE_PLING"
    volume: 0.5
    pitch: 1.5

logging:
  enabled: true
  log-public-chat: true
  log-private-messages: false  # Privacidade em RP
  log-staff-chat: true

general:
  color-codes:
    enabled: true
    permission: "vkchat.color"
  format-codes:
    enabled: true
    permission: "vkchat.color"
  default-channel: "local"
```

---

## 🏗️ Servidor Criativo

### Características
- Apenas chat global
- Sem anti-spam (ambiente criativo)
- Cores liberadas para todos
- Mentions para colaboração

### config.yml
```yaml
chat-format:
  default: "&7{player}: &f{message}"
  construtor: "&b[Construtor] &3{player}: &f{message}"
  arquiteto: "&d[Arquiteto] &5{player}: &f{message}"
  admin: "&c[ADMIN] &4{player}: &f{message}"

channels:
  global:
    enabled: true
    format: "&7{player}: &f{message}"
    permission: "vkchat.channel.global"
  
  local:
    enabled: false  # Não necessário em criativo

staff-chat:
  enabled: true
  format: "&c[STAFF] &7{player}: &f{message}"

slow-mode:
  enabled: false

anti-spam:
  enabled: false  # Desativado para criatividade

mentions:
  enabled: true
  format: "&e&l@{player}"
  sound:
    enabled: true
    sound-name: "NOTE_PLING"

logging:
  enabled: true
  log-public-chat: true
  log-private-messages: true
  log-staff-chat: true

general:
  color-codes:
    enabled: true
    permission: "vkchat.color"  # Todos podem usar
  format-codes:
    enabled: true
    permission: "vkchat.color"
  default-channel: "global"
```

### Permissões (LuckPerms)
```bash
# Dar cores a todos
lp group default permission set vkchat.color true
```

---

## 🏝️ Servidor Skyblock

### Características
- Chat de ilha (local) e global
- Anti-spam moderado
- Formatação por nível/rank
- Mentions para cooperação

### config.yml
```yaml
chat-format:
  default: "&7[Lv.%playerlevels_level%] {player}: &f{message}"
  iron: "&f[FERRO] {prefix}{player}: &f{message}"
  gold: "&6[OURO] {prefix}{player}: &f{message}"
  diamond: "&b[DIAMANTE] {prefix}{player}: &f{message}"
  emerald: "&a[ESMERALDA] {prefix}{player}: &f{message}"
  admin: "&c[ADMIN] &4{player}: &f{message}"

channels:
  global:
    enabled: true
    format: "&a[G] &7{player}: &f{message}"
    permission: "vkchat.channel.global"
  
  local:
    enabled: true
    format: "&b[ILHA] &7{player}: &f{message}"
    permission: "vkchat.channel.local"
    range: 200  # Alcance da ilha

staff-chat:
  enabled: true
  format: "&c[STAFF] &7{player}: &f{message}"

slow-mode:
  enabled: false
  default-delay: 2

anti-spam:
  enabled: true
  bypass-permission: "vkchat.antispam.bypass"
  
  repeat-messages:
    enabled: true
    max-repeats: 3
    time-window: 30
  
  max-length:
    enabled: true
    characters: 256
  
  caps-block:
    enabled: true
    max-percentage: 60
    min-length: 6
  
  word-filter:
    enabled: true
    action: "block"
    words:
      - "free"
      - "gratis"

mentions:
  enabled: true
  format: "&e&l@{player}"
  sound:
    enabled: true
    sound-name: "NOTE_PLING"

logging:
  enabled: true
  log-public-chat: true
  log-private-messages: true
  log-staff-chat: true

general:
  color-codes:
    enabled: true
    permission: "vkchat.color"
  default-channel: "global"
```

---

## 🎨 Formatações Criativas

### Com PlaceholderAPI (Vault + LuckPerms)
```yaml
chat-format:
  default: "&7[%vault_rank%] %player_displayname%: &f{message}"
  vip: "%luckperms_prefix%%player_name%%luckperms_suffix%: &f{message}"
  mvp: "&b[&3MVP&b] %player_name% &8» &f{message}"
```

### Com Estatísticas
```yaml
chat-format:
  default: "&7[Lv.%playerlevels_level%] &f{player} &8[&a%vault_eco_balance_formatted%&8]: &f{message}"
```

### Com Mundo e Ping
```yaml
chat-format:
  default: "&8[&b{world}&8] &7{player} &8(&e%player_ping%ms&8): &f{message}"
```

---

## 🔧 Configurações de Teste/Debug

### Desativar tudo (teste)
```yaml
anti-spam:
  enabled: false
slow-mode:
  enabled: false
word-filter:
  enabled: false
```

### Logs máximos (debug)
```yaml
logging:
  enabled: true
  log-public-chat: true
  log-private-messages: true
  log-staff-chat: true
  log-commands: true
```

---

## 📝 Notas de Configuração

### Dicas Gerais
1. **Teste sempre** antes de aplicar em produção
2. **Backup** das configs antes de mudanças grandes
3. **Reload** com `/reload confirm` (cuidado com memory leaks)
4. **PlaceholderAPI** - Instale expansões necessárias

### Comandos Úteis
```bash
# Recarregar config (não recomendado, reinicie o servidor)
/reload confirm

# Verificar placeholders
/papi parse me %placeholder%

# Verificar permissões
/lp user <player> permission check <permissão>
```

---

**💡 Adapte essas configurações ao seu servidor! Cada comunidade é única.**

---

**Encontrou um cenário não listado? Combine elementos dos exemplos acima!**
