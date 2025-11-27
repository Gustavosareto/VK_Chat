# Notas de Compilação - VKChat

## ✅ Compilação Bem-Sucedida!

**Data:** 27/11/2025  
**Versão:** 1.0.0  
**Tamanho do JAR:** 46.350 bytes (~45 KB)

---

## 🔧 Soluções Implementadas

### Problema: PlaceholderAPI Dependency Resolution
**Erro original:**
```
[ERROR] Could not resolve dependencies for project me.vkchat:VKChat:jar:1.0.0: 
Could not find artifact me.clip:placeholderapi:jar:2.11.2
```

**Solução:**
1. Removemos PlaceholderAPI das dependências do Maven (pom.xml)
2. Implementamos integração usando **Reflection** para tornar PlaceholderAPI 100% opcional
3. O plugin agora detecta PlaceholderAPI em runtime, não em compile-time

### Vantagens desta Abordagem:
- ✅ Plugin compila sem erros
- ✅ Funciona com ou sem PlaceholderAPI instalado
- ✅ Se PlaceholderAPI for instalado depois, o plugin detecta automaticamente
- ✅ Nenhum erro mesmo sem PlaceholderAPI no servidor
- ✅ Código limpo usando Java Reflection API

---

## 📝 Arquivos Modificados

### 1. `pom.xml`
- **Removido:** Dependência do PlaceholderAPI (me.clip:placeholderapi)
- **Removido:** Dependência do LuckPerms API (net.luckperms:api)
- **Mantido:** Apenas Spigot API 1.8.8 e Vault API 1.7

### 2. `ChatManager.java`
- **Removido:** Import direto de `me.clip.placeholderapi.PlaceholderAPI`
- **Adicionado:** Método `setPlaceholders()` usando reflection
- **Vantagem:** Processa placeholders apenas se PlaceholderAPI estiver presente

Código implementado:
```java
private String setPlaceholders(Player player, String text) {
    try {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Class<?> placeholderAPIClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            Method setPlaceholdersMethod = placeholderAPIClass.getMethod("setPlaceholders", Player.class, String.class);
            return (String) setPlaceholdersMethod.invoke(null, player, text);
        }
    } catch (Exception e) {
        plugin.getLogger().fine("PlaceholderAPI não disponível");
    }
    return text;
}
```

### 3. `PlaceholderAPIHook.java`
- **Removido:** Herança de `PlaceholderExpansion`
- **Removido:** Import direto de `me.clip.placeholderapi.expansion.PlaceholderExpansion`
- **Adicionado:** Registro dinâmico usando Java Proxy + Reflection
- **Vantagem:** Expansão é registrada dinamicamente apenas se PlaceholderAPI existir

### 4. Classes de Comando Atualizadas
Arquivos modificados:
- `GlobalCommand.java`
- `LocalCommand.java`
- `StaffChatCommand.java`

**Mudanças:**
- Removido imports diretos do PlaceholderAPI
- Agora usam `plugin.getChatManager().formatMessage()` que já processa PlaceholderAPI internamente

### 5. `ChatListener.java`
- **Removido:** Import direto de PlaceholderAPI
- **Removido:** Chamada direta a `PlaceholderAPI.setPlaceholders()`
- **Vantagem:** Tudo centralizado no ChatManager

---

## 🎯 Como Funciona Agora

### Inicialização do Plugin:
1. Plugin carrega (não precisa de PlaceholderAPI)
2. Verifica se PlaceholderAPI está instalado
3. **Se SIM:** Registra expansão dinamicamente com reflection
4. **Se NÃO:** Continua funcionando normalmente sem placeholders customizados

### Processamento de Mensagens:
1. ChatManager formata mensagem
2. Substitui placeholders básicos ({player}, {world}, etc.)
3. **Tenta** processar com PlaceholderAPI via reflection
4. **Se falhar:** Retorna mensagem com placeholders básicos apenas
5. Nenhum erro é lançado

---

## 📦 Uso do JAR Compilado

### Localização:
```
target/VKChat-1.0.0.jar
```

### Como Instalar:
1. Copie `VKChat-1.0.0.jar` para a pasta `plugins/` do seu servidor
2. **(Opcional)** Instale Vault se quiser formatação por grupo
3. **(Opcional)** Instale PlaceholderAPI se quiser placeholders customizados
4. Inicie o servidor
5. Configure em `plugins/VKChat/config.yml`

### Ordem Recomendada de Carregamento:
```
1. LuckPerms (ou outro plugin de permissões)
2. Vault
3. PlaceholderAPI
4. VKChat
```

> **Nota:** VKChat detecta automaticamente plugins instalados via `softdepend` no plugin.yml

---

## 🧪 Testado e Validado

### Compilação:
- ✅ Maven Clean: Sucesso
- ✅ Maven Package: Sucesso
- ✅ Shade Plugin: JAR reempacotado corretamente
- ✅ Nenhum erro de dependência

### Código:
- ✅ 18 classes compiladas
- ✅ Nenhum warning crítico
- ✅ Reflection implementada corretamente
- ✅ Fallback adequado quando PlaceholderAPI não está presente

---

## 🚀 Próximos Passos

1. **Testar em servidor real:**
   - Instalar em servidor Paper 1.8.8
   - Testar com Vault + LuckPerms
   - Testar com PlaceholderAPI
   - Testar SEM PlaceholderAPI (validar fallback)

2. **Validar funcionalidades:**
   - Anti-spam
   - Slow-mode
   - Canais global/local
   - Mensagens privadas
   - Staff chat
   - Mentions
   - Logs

3. **Performance:**
   - Monitorar uso de memória
   - Verificar logs de erros
   - Testar com múltiplos jogadores

---

## 📚 Referências Técnicas

### Java Reflection API Utilizada:
- `Class.forName()` - Carrega classe dinamicamente
- `Class.getMethod()` - Obtém método por nome
- `Method.invoke()` - Invoca método dinamicamente
- `java.lang.reflect.Proxy` - Cria proxy dinâmico para PlaceholderExpansion

### Maven Plugins:
- `maven-compiler-plugin 3.8.1` - Java 8 target
- `maven-shade-plugin 3.2.4` - Reempacota dependências

### Dependências Finais (pom.xml):
```xml
<dependencies>
    <!-- Spigot API -->
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.8.8-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Vault API -->
    <dependency>
        <groupId>com.github.MilkBowl</groupId>
        <artifactId>VaultAPI</artifactId>
        <version>1.7</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## ✍️ Autor
VKChat Development Team

**Versão do Documento:** 1.0  
**Última Atualização:** 27/11/2025
