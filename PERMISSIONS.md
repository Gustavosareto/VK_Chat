# 🔐 Guia Completo de Permissões - VKChat

Este documento detalha todas as permissões disponíveis no plugin VKChat e como configurá-las.

---

## 📋 Índice

- [Permissões Básicas](#permissões-básicas)
- [Permissões de Comandos](#permissões-de-comandos)
- [Permissões de Bypass](#permissões-de-bypass)
- [Permissões de Recursos](#permissões-de-recursos)
- [Configuração por Plugin](#configuração-por-plugin)
- [Exemplos Práticos](#exemplos-práticos)

---

## 🔑 Permissões Básicas

### vkchat.*
- **Descrição:** Concede todas as permissões do plugin
- **Padrão:** Apenas OPs
- **Uso:** Administradores e donos
- **Inclui:** Todas as permissões abaixo

---

## 📜 Permissões de Comandos

### Chat Management

#### vkchat.chat
- **Descrição:** Permite usar comandos de gerenciamento do chat
- **Padrão:** OP
- **Comandos:** `/chat toggle`, `/chat clear`

#### vkchat.chat.toggle
- **Descrição:** Permite ativar/desativar visualização do chat
- **Padrão:** Todos
- **Comando:** `/chat toggle`
- **Uso:** Jogadores que querem desativar o chat temporariamente

#### vkchat.chat.clear
- **Descrição:** Permite limpar o chat de todos os jogadores
- **Padrão:** OP
- **Comando:** `/chat clear`, `/chat clear all`
- **Uso:** Moderadores e administradores

---

### Slow Mode

#### vkchat.slowchat
- **Descrição:** Permite ativar/desativar o slow mode
- **Padrão:** OP
- **Comando:** `/slowchat <segundos|off>`
- **Uso:** Moderadores para controlar flood

#### vkchat.slowchat.bypass
- **Descrição:** Permite ignorar o slow mode
- **Padrão:** OP
- **Uso:** Staff que precisa responder rapidamente

---

### Mensagens Privadas

#### vkchat.tell
- **Descrição:** Permite enviar mensagens privadas
- **Padrão:** Todos
- **Comando:** `/tell <player> <mensagem>`
- **Aliases:** `/msg`, `/w`, `/whisper`

#### vkchat.reply
- **Descrição:** Permite responder mensagens privadas
- **Padrão:** Todos
- **Comando:** `/reply <mensagem>`
- **Alias:** `/r`

---

### Canais de Chat

#### vkchat.channel.global
- **Descrição:** Permite enviar mensagens no chat global
- **Padrão:** Todos
- **Comando:** `/g <mensagem>`
- **Alias:** `/global`

#### vkchat.channel.local
- **Descrição:** Permite enviar mensagens no chat local
- **Padrão:** Todos
- **Comando:** `/l <mensagem>`
- **Alias:** `/local`

---

### Chat da Staff

#### vkchat.staff
- **Descrição:** Permite usar o chat da staff
- **Padrão:** OP
- **Comando:** `/sc <mensagem>`
- **Aliases:** `/staffchat`, `/chatstaff`
- **Uso:** Membros da equipe de moderação

---

## 🛡️ Permissões de Bypass

### vkchat.antispam.bypass
- **Descrição:** Permite ignorar todas as verificações de anti-spam
- **Padrão:** OP
- **Ignora:**
  - Mensagens repetidas
  - Limite de caracteres
  - Bloqueio de CAPS
  - Filtro de palavras
- **Uso:** Administradores e moderadores

---

## 🎨 Permissões de Recursos

### vkchat.color
- **Descrição:** Permite usar códigos de cor no chat
- **Padrão:** OP (configurável)
- **Cores:** `&0` até `&f`
- **Formatação:** `&l`, `&m`, `&n`, `&o`, `&r`
- **Exemplo:** `&6Olá &amundo&r!` → Olá mundo!

### vkchat.mention.use
- **Descrição:** Permite mencionar outros jogadores com @nome
- **Padrão:** Todos
- **Uso:** `@PlayerName` na mensagem
- **Efeito:** Destaque e notificação para o jogador mencionado

---

## ⚙️ Configuração por Plugin

### LuckPerms (Recomendado)

#### Configuração Individual
```bash
# Dar permissão a um jogador específico
lp user PlayerName permission set vkchat.color true

# Remover permissão de um jogador
lp user PlayerName permission unset vkchat.color
```

#### Configuração por Grupo
```bash
# VIP - Cores no chat
lp group vip permission set vkchat.color true

# Moderador - Limpar chat e slow mode
lp group moderador permission set vkchat.chat.clear true
lp group moderador permission set vkchat.slowchat true
lp group moderador permission set vkchat.slowchat.bypass true
lp group moderador permission set vkchat.staff true

# Admin - Todas as permissões
lp group admin permission set vkchat.* true

# Helper - Chat da staff
lp group helper permission set vkchat.staff true
```

#### Verificar Permissões
```bash
# Ver permissões de um jogador
lp user PlayerName permission info

# Ver permissões de um grupo
lp group vip permission info
```

---

### PermissionsEx (PEx)

```bash
# Dar permissão a jogador
pex user PlayerName add vkchat.color

# Dar permissão a grupo
pex group vip add vkchat.color

# Remover permissão
pex user PlayerName remove vkchat.color

# Grupo com todas as permissões
pex group admin add vkchat.*
```

---

### GroupManager

```yaml
# Em groups.yml
groups:
  default:
    permissions:
      - vkchat.tell
      - vkchat.reply
      - vkchat.channel.global
      - vkchat.channel.local
      - vkchat.chat.toggle
      
  vip:
    permissions:
      - vkchat.color
      
  moderador:
    permissions:
      - vkchat.staff
      - vkchat.chat.clear
      - vkchat.slowchat
      - vkchat.slowchat.bypass
      - vkchat.antispam.bypass
      
  admin:
    permissions:
      - vkchat.*
```

---

## 💡 Exemplos Práticos

### Configuração Básica (4 Grupos)

#### 1. Membro (Padrão)
```bash
# LuckPerms
lp group default permission set vkchat.tell true
lp group default permission set vkchat.reply true
lp group default permission set vkchat.channel.global true
lp group default permission set vkchat.channel.local true
lp group default permission set vkchat.chat.toggle true
lp group default permission set vkchat.mention.use true
```

#### 2. VIP
```bash
# LuckPerms - Herda de default + cores
lp group vip parent add default
lp group vip permission set vkchat.color true
```

#### 3. Moderador
```bash
# LuckPerms - Herda de VIP + comandos de moderação
lp group moderador parent add vip
lp group moderador permission set vkchat.staff true
lp group moderador permission set vkchat.chat.clear true
lp group moderador permission set vkchat.slowchat true
lp group moderador permission set vkchat.slowchat.bypass true
lp group moderador permission set vkchat.antispam.bypass true
```

#### 4. Admin
```bash
# LuckPerms - Todas as permissões
lp group admin permission set vkchat.* true
```

---

### Configuração Avançada (Personalizada)

#### Grupo "Construtor" - Apenas chat local
```bash
lp group construtor permission set vkchat.channel.local true
lp group construtor permission set vkchat.tell true
lp group construtor permission set vkchat.reply true
# NÃO dar vkchat.channel.global
```

#### Grupo "Silenciado" - Sem permissões de chat
```bash
# Remover todas as permissões de chat
lp group silenciado permission unset vkchat.tell
lp group silenciado permission unset vkchat.reply
lp group silenciado permission unset vkchat.channel.global
lp group silenciado permission unset vkchat.channel.local
```

#### Jogador específico com bypass temporário
```bash
# Dar bypass de anti-spam por 1 hora
lp user PlayerName permission settemp vkchat.antispam.bypass true 1h
```

---

## 🔍 Hierarquia Recomendada

```
Admin (Tudo)
  ↓ herda de
Moderador (Staff + Clear + Slow + Bypass)
  ↓ herda de
Helper (Staff)
  ↓ herda de
VIP+ (Cores + Formatação)
  ↓ herda de
VIP (Cores)
  ↓ herda de
Membro (Básico: Tell, Reply, Canais)
```

---

## 🎯 Casos de Uso

### Caso 1: Streamer VIP
**Necessidade:** Usar cores mas sem bypass de spam
```bash
lp user StreamerName permission set vkchat.color true
# NÃO dar vkchat.antispam.bypass
```

### Caso 2: Trial Staff
**Necessidade:** Ver chat da staff mas sem poderes de moderação
```bash
lp user TrialStaff permission set vkchat.staff true
# NÃO dar clear, slowchat, bypass
```

### Caso 3: Builder Team
**Necessidade:** Apenas chat local entre construtores
```bash
lp group builders permission set vkchat.channel.local true
lp group builders permission set vkchat.tell true
lp group builders permission unset vkchat.channel.global
```

### Caso 4: Evento Especial
**Necessidade:** Desativar chat global temporariamente
```bash
# Remover permissão global de todos
lp group default permission unset vkchat.channel.global

# Apenas staff pode usar
lp group moderador permission set vkchat.channel.global true
```

---

## 🛠️ Troubleshooting

### Jogador não consegue usar cores
```bash
# Verificar se tem permissão
lp user PlayerName permission check vkchat.color

# Se não tiver, dar permissão
lp user PlayerName permission set vkchat.color true
```

### Jogador não vê mensagens do chat
```bash
# Verificar se desabilitou o chat
# Jogador deve usar: /chat toggle
```

### Staff não consegue limpar chat
```bash
# Verificar permissão
lp user StaffName permission check vkchat.chat.clear

# Dar permissão
lp user StaffName permission set vkchat.chat.clear true
```

---

## 📊 Tabela Resumida

| Permissão | Padrão | Descrição |
|-----------|--------|-----------|
| `vkchat.*` | OP | Todas as permissões |
| `vkchat.tell` | Todos | Mensagens privadas |
| `vkchat.reply` | Todos | Responder mensagens |
| `vkchat.channel.global` | Todos | Chat global |
| `vkchat.channel.local` | Todos | Chat local |
| `vkchat.staff` | OP | Chat da staff |
| `vkchat.chat.toggle` | Todos | Ativar/desativar chat |
| `vkchat.chat.clear` | OP | Limpar chat |
| `vkchat.slowchat` | OP | Controlar slow mode |
| `vkchat.slowchat.bypass` | OP | Ignorar slow mode |
| `vkchat.antispam.bypass` | OP | Ignorar anti-spam |
| `vkchat.color` | OP* | Usar cores |
| `vkchat.mention.use` | Todos | Mencionar jogadores |

*Configurável em `config.yml`

---

## 📝 Notas Importantes

1. **Permissões são case-sensitive** - Use exatamente como mostrado
2. **Wildcards funcionam** - `vkchat.*` concede todas as sub-permissões
3. **Herança de grupos** - Use para evitar repetição
4. **Permissões temporárias** - Use `settemp` no LuckPerms
5. **Verificação em tempo real** - Mudanças aplicam imediatamente

---

**💡 Dica:** Use `/lp verbose on` (LuckPerms) para debugar problemas de permissões em tempo real!

---

**Precisa de mais ajuda? Consulte a documentação do seu plugin de permissões:**
- LuckPerms: https://luckperms.net/wiki
- PermissionsEx: https://github.com/PEXPlugins/PermissionsEx
- GroupManager: https://github.com/ElgarL/GroupManager
