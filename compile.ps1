# 🔨 Script de Compilação - VKChat
# Execute este arquivo para compilar o plugin automaticamente

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "      VKChat - Sistema de Compilação    " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Maven está instalado
Write-Host "⚙️  Verificando Maven..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven" | Out-String
    if ($mavenVersion) {
        Write-Host "✅ Maven encontrado!" -ForegroundColor Green
        Write-Host $mavenVersion.Trim() -ForegroundColor Gray
    }
} catch {
    Write-Host "❌ Maven não encontrado!" -ForegroundColor Red
    Write-Host "   Instale Maven: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Pressione Enter para sair"
    exit 1
}

Write-Host ""

# Verificar Java
Write-Host "☕ Verificando Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | Out-String
    if ($javaVersion) {
        Write-Host "✅ Java encontrado!" -ForegroundColor Green
        Write-Host $javaVersion.Trim() -ForegroundColor Gray
    }
} catch {
    Write-Host "❌ Java não encontrado!" -ForegroundColor Red
    Write-Host "   Instale Java 8+: https://adoptopenjdk.net/" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Pressione Enter para sair"
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Confirmar compilação
Write-Host "🚀 Pronto para compilar VKChat!" -ForegroundColor Green
Write-Host ""
Write-Host "Isso irá:" -ForegroundColor Yellow
Write-Host "  • Baixar dependências (Vault, PlaceholderAPI, Spigot)" -ForegroundColor Gray
Write-Host "  • Compilar todo o código Java" -ForegroundColor Gray
Write-Host "  • Gerar o arquivo VKChat-1.0.0.jar" -ForegroundColor Gray
Write-Host ""

$confirm = Read-Host "Deseja continuar? (S/N)"
if ($confirm -ne "S" -and $confirm -ne "s") {
    Write-Host "❌ Compilação cancelada." -ForegroundColor Red
    exit 0
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Navegar para pasta do projeto
$projectPath = $PSScriptRoot
Set-Location $projectPath
Write-Host "📁 Pasta do projeto: $projectPath" -ForegroundColor Cyan
Write-Host ""

# Limpar compilações anteriores
Write-Host "🧹 Limpando compilações antigas..." -ForegroundColor Yellow
mvn clean | Out-Null
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Limpeza concluída!" -ForegroundColor Green
} else {
    Write-Host "⚠️  Aviso: Falha na limpeza (ignorando)" -ForegroundColor Yellow
}
Write-Host ""

# Compilar projeto
Write-Host "⚙️  Compilando VKChat..." -ForegroundColor Yellow
Write-Host "   (Isso pode levar alguns minutos...)" -ForegroundColor Gray
Write-Host ""

$startTime = Get-Date
mvn package -DskipTests

if ($LASTEXITCODE -eq 0) {
    $endTime = Get-Date
    $duration = ($endTime - $startTime).TotalSeconds
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "          ✅ COMPILAÇÃO CONCLUÍDA!       " -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "⏱️  Tempo: $([math]::Round($duration, 2)) segundos" -ForegroundColor Cyan
    Write-Host ""
    
    # Verificar se o arquivo foi criado
    $jarPath = Join-Path $projectPath "target\VKChat-1.0.0.jar"
    if (Test-Path $jarPath) {
        $jarSize = (Get-Item $jarPath).Length
        $jarSizeKB = [math]::Round($jarSize / 1KB, 2)
        
        Write-Host "📦 Arquivo gerado:" -ForegroundColor Green
        Write-Host "   Local: target\VKChat-1.0.0.jar" -ForegroundColor Gray
        Write-Host "   Tamanho: $jarSizeKB KB" -ForegroundColor Gray
        Write-Host ""
        
        Write-Host "🎯 Próximos passos:" -ForegroundColor Yellow
        Write-Host "   1. Copie target\VKChat-1.0.0.jar para plugins/ do servidor" -ForegroundColor Gray
        Write-Host "   2. Instale Vault e PlaceholderAPI" -ForegroundColor Gray
        Write-Host "   3. Reinicie o servidor" -ForegroundColor Gray
        Write-Host "   4. Configure plugins/VKChat/config.yml" -ForegroundColor Gray
        Write-Host ""
        
        # Perguntar se quer copiar para algum lugar
        Write-Host "📋 Deseja copiar o arquivo para algum local? (S/N)" -ForegroundColor Cyan
        $copyFile = Read-Host
        
        if ($copyFile -eq "S" -or $copyFile -eq "s") {
            Write-Host ""
            Write-Host "Digite o caminho de destino (ex: C:\Servidor\plugins):" -ForegroundColor Yellow
            $destination = Read-Host
            
            if (Test-Path $destination) {
                try {
                    Copy-Item $jarPath -Destination $destination -Force
                    Write-Host "✅ Arquivo copiado com sucesso!" -ForegroundColor Green
                } catch {
                    Write-Host "❌ Erro ao copiar arquivo: $_" -ForegroundColor Red
                }
            } else {
                Write-Host "❌ Caminho não existe!" -ForegroundColor Red
            }
        }
        
    } else {
        Write-Host "⚠️  Arquivo não encontrado em target\VKChat-1.0.0.jar" -ForegroundColor Yellow
    }
    
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "          ❌ COMPILAÇÃO FALHOU!          " -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "🔍 Possíveis causas:" -ForegroundColor Yellow
    Write-Host "   • Dependências não baixaram (verifique internet)" -ForegroundColor Gray
    Write-Host "   • Erro no código (verifique mensagens acima)" -ForegroundColor Gray
    Write-Host "   • Maven não configurado corretamente" -ForegroundColor Gray
    Write-Host ""
    Write-Host "💡 Tente:" -ForegroundColor Cyan
    Write-Host "   mvn clean install -U" -ForegroundColor Gray
    Write-Host ""
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Aguardar antes de fechar
Read-Host "Pressione Enter para sair"
