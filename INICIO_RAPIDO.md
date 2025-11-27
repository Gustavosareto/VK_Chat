# 🚀 INÍCIO RÁPIDO - VKChat

**Plugin completo de Chat para Minecraft Paper/Spigot 1.8.8**

---

## ⚡ 5 Passos para Começar

### 1️⃣ Compilar o Plugin
```powershell
cd "C:\Users\I7 Skyline\Documents\VK_Chat"
mvn clean package
```

### 2️⃣ Copiar para o Servidor
```powershell
# O arquivo estará em: target/VKChat-1.0.0.jar
# Copie para: servidor/plugins/
```

### 3️⃣ Instalar Dependências
- ✅ **Vault** - https://www.spigotmc.org/resources/vault.34315/
- ✅ **PlaceholderAPI** - https://www.spigotmc.org/resources/placeholderapi.6245/
- ✅ **LuckPerms** - https://luckperms.net/download

### 4️⃣ Iniciar Servidor
```bash
# Inicie seu servidor normalmente
# O plugin criará os arquivos em plugins/VKChat/
```

### 5️⃣ Configurar Permissões
```bash
# Exemplo com LuckPerms
lp group default permission set vkchat.tell true
lp group default permission set vkchat.channel.global true
lp group vip permission set vkchat.color true
lp group admin permission set vkchat.* true
```

---

## 🎮 Teste os Comandos

```
/slowchat 5         # Ativa slow mode (5 segundos)
/chat toggle        # Desativa/ativa seu chat
/tell Player Oi!    # Mensagem privada
/g Olá!            # Chat global
/l Ei!             # Chat local
/sc Teste          # Chat da staff (precisa permissão)
```

---

## ⚙️ Arquivos Importantes

### 📁 plugins/VKChat/

#### config.yml
```yaml
# Edite formatos de chat aqui
chat-format:
  default: "&7{player}: &f{message}"
  vip: "&6[VIP] &e{player}: &f{message}"
```

#### messages.yml
```yaml
# Edite mensagens do plugin aqui
prefix: "&8[&bVKChat&8]&r"
slowmode-enabled: "{prefix} &aSlow mode ativado!"
```

---

## 🔧 Configuração Básica

### Formato de Chat por Grupo

**Arquivo:** `config.yml`

```yaml
chat-format:
  default: "&7{player}: &f{message}"           # Jogadores normais
  vip: "&6[VIP] &e{player}: &f{message}"       # VIPs
  mvp: "&b[MVP] &3{player}: &f{message}"       # MVPs
  admin: "&c[ADMIN] &4{player}: &f{message}"   # Admins
```

### Permissões Básicas

**LuckPerms:**
```bash
# Todos podem usar chat
lp group default permission set vkchat.tell true
lp group default permission set vkchat.reply true
lp group default permission set vkchat.channel.global true
lp group default permission set vkchat.channel.local true

# VIP usa cores
lp group vip permission set vkchat.color true

# Admin tem tudo
lp group admin permission set vkchat.* true
```

---

## 📚 Documentação Completa

- 📄 **README.md** - Documentação principal
- 🔨 **BUILD.md** - Como compilar
- 🔐 **PERMISSIONS.md** - Lista de permissões
- 📝 **EXAMPLES.md** - Exemplos de config
- 🏗️ **ESTRUTURA.md** - Arquitetura do código
- ⚠️ **ERROS_VSCODE.md** - Sobre erros do VS Code

---

## 💡 Dicas Rápidas

### Cores no Chat
```yaml
# Dar cores para VIP
lp group vip permission set vkchat.color true
```

### Desativar Anti-Spam
```yaml
# Em config.yml
anti-spam:
  enabled: false
```

### Aumentar Alcance Local
```yaml
# Em config.yml
channels:
  local:
    range: 200  # Padrão é 100
```

### Desativar Mentions
```yaml
# Em config.yml
mentions:
  enabled: false
```

---

## 🐛 Problemas Comuns

### Plugin não carrega
```
✅ Verifique: Java 8+ instalado
✅ Verifique: Paper/Spigot 1.8.8
✅ Veja logs: logs/latest.log
```

### Comandos não funcionam
```
✅ Plugin carregou? /plugins (verde = sim)
✅ Tem permissão? /lp user <nome> permission check vkchat.tell
```

### Vault não conecta
```
✅ Vault.jar instalado?
✅ Plugin de permissões instalado? (LuckPerms)
✅ Reiniciou o servidor?
```

---

## 🎯 Funcionalidades Principais

✅ Formatação por grupo (Vault)  
✅ PlaceholderAPI integrado  
✅ Slow mode configurável  
✅ Chat toggle (desativar)  
✅ Chat clear (limpar)  
✅ Tell/Reply (mensagens privadas)  
✅ Chat global e local  
✅ Anti-spam robusto  
✅ Chat da staff  
✅ Sistema de logs  
✅ Mentions (@jogador)  
✅ PlaceholderAPI expansion  

---

## 📊 Comandos Resumidos

| Comando | O que faz | Permissão |
|---------|-----------|-----------|
| `/slowchat 5` | Slow mode 5s | `vkchat.slowchat` |
| `/chat toggle` | Liga/desliga chat | `vkchat.chat.toggle` |
| `/chat clear` | Limpa chat | `vkchat.chat.clear` |
| `/tell Player Oi` | Msg privada | `vkchat.tell` |
| `/reply Oi` | Responde msg | `vkchat.reply` |
| `/g Olá` | Chat global | `vkchat.channel.global` |
| `/l Ei` | Chat local | `vkchat.channel.local` |
| `/sc Teste` | Chat staff | `vkchat.staff` |

---

## 🎨 Códigos de Cor

```
&0 = Preto          &8 = Cinza Escuro
&1 = Azul Escuro    &9 = Azul
&2 = Verde Escuro   &a = Verde
&3 = Ciano          &b = Ciano Claro
&4 = Vermelho       &c = Vermelho Claro
&5 = Roxo           &d = Rosa
&6 = Dourado        &e = Amarelo
&7 = Cinza          &f = Branco

&l = Negrito        &o = Itálico
&m = Riscado        &n = Sublinhado
&r = Reset
```

**Uso:** `&6VIP &eJogador&r: Olá!`

---

## 🔌 Placeholders

### VKChat (%vkchat_xxx%)
```
%vkchat_last_message%    - Última mensagem
%vkchat_cooldown%        - Cooldown restante
%vkchat_muted%           - Se está mutado
%vkchat_channel%         - Canal atual
%vkchat_chat_enabled%    - Chat habilitado
```

### Vault (%vault_xxx%)
```
%vault_prefix%           - Prefixo
%vault_suffix%           - Sufixo
%vault_rank%             - Rank/Grupo
```

---

## ✅ Tudo Pronto!

Seu servidor agora tem:

- ✅ Sistema de chat completo
- ✅ Anti-spam funcional
- ✅ Formatação por grupo
- ✅ Mensagens privadas
- ✅ Canais (global/local)
- ✅ Sistema de mentions
- ✅ Logs automáticos

---

## 📞 Precisa de Ajuda?

1. **Leia a documentação:** Todos os arquivos .md
2. **Verifique logs:** `logs/latest.log`
3. **Teste permissões:** `/lp user <nome> permission check <permissão>`
4. **Verifique config:** `plugins/VKChat/config.yml`

---

## 🎉 Aproveite!

**VKChat está pronto para uso!**

Configure ao seu gosto e divirta-se! 🎮

---

**Desenvolvido para a comunidade Minecraft brasileira! 🇧🇷**
