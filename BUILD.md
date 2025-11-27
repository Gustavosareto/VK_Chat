# 🔨 Guia de Compilação - VKChat

Este guia explica como compilar o plugin VKChat do código-fonte.

---

## 📋 Pré-requisitos

### 1. Java Development Kit (JDK) 8+

**Windows:**
```powershell
# Verificar se Java está instalado
java -version

# Deve mostrar algo como:
# java version "1.8.0_xxx"
```

**Baixar JDK 8:**
- Oracle JDK: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
- OpenJDK: https://adoptopenjdk.net/

### 2. Apache Maven

**Windows:**
```powershell
# Verificar se Maven está instalado
mvn -version

# Deve mostrar algo como:
# Apache Maven 3.x.x
```

**Instalar Maven:**
1. Baixe: https://maven.apache.org/download.cgi
2. Extraia para `C:\Program Files\Apache\maven`
3. Adicione ao PATH:
   - Painel de Controle → Sistema → Configurações Avançadas
   - Variáveis de Ambiente
   - Adicionar `C:\Program Files\Apache\maven\bin` ao PATH

---

## 🚀 Compilação Rápida

### Método 1: PowerShell (Windows)

```powershell
# Navegue até a pasta do projeto
cd "C:\Users\I7 Skyline\Documents\VK_Chat"

# Compile o projeto
mvn clean package

# O arquivo .jar estará em:
# target/VKChat-1.0.0.jar
```

### Método 2: Prompt de Comando

```cmd
cd "C:\Users\I7 Skyline\Documents\VK_Chat"
mvn clean package
```

---

## 📦 Comandos Maven Úteis

### Compilar sem testes
```powershell
mvn clean package -DskipTests
```

### Limpar compilações anteriores
```powershell
mvn clean
```

### Compilar e instalar no repositório local
```powershell
mvn clean install
```

### Apenas compilar (sem empacotar)
```powershell
mvn compile
```

### Ver árvore de dependências
```powershell
mvn dependency:tree
```

---

## 🔍 Resolução de Problemas

### Erro: "JAVA_HOME not set"

**Solução:**
```powershell
# Adicionar variável JAVA_HOME
# Painel de Controle → Sistema → Variáveis de Ambiente
# Nova variável do sistema:
# Nome: JAVA_HOME
# Valor: C:\Program Files\Java\jdk1.8.0_xxx
```

### Erro: "mvn não é reconhecido"

**Solução:**
Adicione o Maven ao PATH conforme instruções acima.

### Erro: "Cannot find symbol" ou "package does not exist"

**Solução:**
```powershell
# Forçar download de dependências
mvn dependency:purge-local-repository
mvn clean install
```

### Erro: Dependências não baixam

**Solução:**
```powershell
# Limpar cache do Maven
mvn dependency:purge-local-repository
# ou manualmente deletar:
# C:\Users\<usuario>\.m2\repository
```

---

## 📁 Estrutura do Projeto Compilado

Após compilação bem-sucedida:

```
VK_Chat/
├── src/                          # Código-fonte (não modificado)
├── target/                       # Pasta de compilação
│   ├── classes/                  # Arquivos .class compilados
│   ├── maven-archiver/           # Metadados Maven
│   ├── VKChat-1.0.0.jar         # ✅ ARQUIVO FINAL DO PLUGIN
│   └── original-VKChat-1.0.0.jar # Versão sem dependências
├── pom.xml                       # Configuração Maven
└── README.md                     # Documentação
```

---

## ✅ Verificar Compilação

### 1. Verificar se o .jar foi criado
```powershell
dir target\VKChat-1.0.0.jar
```

### 2. Verificar conteúdo do .jar
```powershell
# Listar arquivos dentro do .jar
jar tf target\VKChat-1.0.0.jar
```

### 3. Verificar tamanho do arquivo
O arquivo deve ter aproximadamente **50-100 KB** (sem dependências empacotadas).

---

## 🎯 Instalação no Servidor

### 1. Copiar o .jar

**PowerShell:**
```powershell
# Exemplo: Copiar para servidor local
Copy-Item "target\VKChat-1.0.0.jar" "C:\Servidor\plugins\"
```

### 2. Instalar dependências

No servidor, certifique-se de ter:
- ✅ Vault.jar
- ✅ PlaceholderAPI.jar
- ✅ LuckPerms.jar (ou outro plugin de permissões)

### 3. Reiniciar servidor

```bash
# No console do servidor
stop
# Aguarde parar completamente, então inicie novamente
```

---

## 🔧 Configurações Avançadas

### Compilar para Java 7
```xml
<!-- Em pom.xml, altere: -->
<properties>
    <java.version>1.7</java.version>
</properties>
```

### Incluir dependências no .jar
```xml
<!-- Já configurado no pom.xml com maven-shade-plugin -->
<!-- O plugin já empacota tudo necessário -->
```

### Alterar nome do arquivo final
```xml
<!-- Em pom.xml, adicione em <build>: -->
<finalName>VKChat-Custom</finalName>
```

---

## 📊 Logs de Compilação

### Compilação Bem-Sucedida
```
[INFO] BUILD SUCCESS
[INFO] Total time: 5.123 s
[INFO] Finished at: 2024-11-27T14:30:00-03:00
```

### Compilação com Erro
```
[ERROR] BUILD FAILURE
[ERROR] Failed to execute goal...
```

Se houver erro, leia atentamente a mensagem para identificar o problema.

---

## 🚀 Automatização

### Script PowerShell para compilar

Crie um arquivo `build.ps1`:

```powershell
# build.ps1
Write-Host "=== Compilando VKChat ===" -ForegroundColor Cyan

# Navegar para a pasta do projeto
Set-Location "C:\Users\I7 Skyline\Documents\VK_Chat"

# Compilar
Write-Host "Executando Maven..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=== Compilação concluída com sucesso! ===" -ForegroundColor Green
    Write-Host "Arquivo gerado: target\VKChat-1.0.0.jar" -ForegroundColor Green
    
    # Exibir tamanho do arquivo
    $fileSize = (Get-Item "target\VKChat-1.0.0.jar").Length / 1KB
    Write-Host ("Tamanho: {0:N2} KB" -f $fileSize) -ForegroundColor Cyan
} else {
    Write-Host "`n=== Erro na compilação! ===" -ForegroundColor Red
    exit 1
}
```

**Executar:**
```powershell
.\build.ps1
```

---

## 📚 Recursos Adicionais

### Maven
- Documentação oficial: https://maven.apache.org/guides/
- Repositório central: https://mvnrepository.com/

### Spigot API
- JavaDocs: https://hub.spigotmc.org/javadocs/spigot/
- Guia de plugins: https://www.spigotmc.org/wiki/

### Vault API
- GitHub: https://github.com/MilkBowl/VaultAPI

### PlaceholderAPI
- Wiki: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki

---

## 💡 Dicas

1. **Sempre use `mvn clean` antes de compilar** para evitar problemas com arquivos antigos
2. **Mantenha o JDK 8** para máxima compatibilidade com Minecraft 1.8.8
3. **Use `-DskipTests`** para compilações mais rápidas (se não houver testes)
4. **Versione seus builds** alterando a versão em `pom.xml`

---

## ✅ Checklist Pré-Compilação

- [ ] JDK 8+ instalado e configurado
- [ ] Maven instalado e no PATH
- [ ] Variável JAVA_HOME configurada
- [ ] Conexão com internet (para baixar dependências)
- [ ] Espaço em disco (mínimo 100 MB)

---

**Pronto! Agora você pode compilar o VKChat! 🎉**

Se encontrar problemas, consulte a seção de [Resolução de Problemas](#-resolução-de-problemas).
