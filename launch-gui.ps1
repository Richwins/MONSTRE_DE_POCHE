# Script PowerShell pour lancer l'interface graphique JavaFX
# Usage: .\launch-gui.ps1
# 
# IMPORTANT : Si vous obtenez l'erreur "no suitable pipeline found",
# utilisez le SDK JavaFX complet au lieu du dossier lib/
# Décommentez et modifiez la ligne $javafxSdkPath ci-dessous

# Utiliser les dossiers lib/ et bin/ du projet (tout est dans le projet)
$libPath = Join-Path $PSScriptRoot "lib"
$binPath = Join-Path $PSScriptRoot "bin"

# Ajouter les DLL natives au PATH
if (Test-Path $binPath) {
    $dllCount = (Get-ChildItem -Path $binPath -Filter "*.dll" -ErrorAction SilentlyContinue | Measure-Object).Count
    if ($dllCount -gt 0) {
        $env:PATH = "$binPath;$env:PATH"
        Write-Host "DLL natives trouvées dans bin/ ($dllCount fichiers)" -ForegroundColor Green
    } else {
        Write-Host "ATTENTION: Le dossier bin/ existe mais ne contient pas de DLL natives !" -ForegroundColor Yellow
        Write-Host "Exécutez: .\setup-javafx-native.ps1 pour copier les DLL depuis le SDK" -ForegroundColor Yellow
    }
} else {
    Write-Host "ATTENTION: Le dossier bin/ n'existe pas !" -ForegroundColor Red
    Write-Host "Exécutez: .\setup-javafx-native.ps1 pour créer le dossier et copier les DLL" -ForegroundColor Yellow
}

$outPath = Join-Path $PSScriptRoot "out\production\MONSTRE_DE_POCHE"
$resourcesPath = Join-Path $PSScriptRoot "resources"

Write-Host "Lancement de l'interface graphique JavaFX..." -ForegroundColor Green
Write-Host "Module path: $libPath" -ForegroundColor Gray
Write-Host "Class path: $outPath;$resourcesPath" -ForegroundColor Gray

java --module-path "$libPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "$outPath;$resourcesPath" com.esiea.monstredepoche.Main gui

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nErreur lors du lancement. Vérifiez que:" -ForegroundColor Red
    Write-Host "1. JavaFX est bien dans le dossier lib/ OU utilisez le SDK complet" -ForegroundColor Yellow
    Write-Host "2. Le projet est compilé (out/production/MONSTRE_DE_POCHE existe)" -ForegroundColor Yellow
    Write-Host "3. Vous utilisez Java 11 ou supérieur" -ForegroundColor Yellow
    Write-Host "`nSi vous obtenez 'no suitable pipeline found':" -ForegroundColor Cyan
    Write-Host "- Utilisez le SDK JavaFX complet (pas seulement les JARs)" -ForegroundColor Yellow
    Write-Host "- Les DLL natives sont nécessaires et se trouvent dans le SDK" -ForegroundColor Yellow
    Write-Host "- Modifiez ce script pour utiliser le chemin vers le SDK complet" -ForegroundColor Yellow
}
