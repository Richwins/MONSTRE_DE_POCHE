# Script PowerShell pour lancer l'interface graphique JavaFX
# Usage: .\launch-gui.ps1
# 
# IMPORTANT : Si vous obtenez l'erreur "no suitable pipeline found",
# utilisez le SDK JavaFX complet au lieu du dossier lib/
# Décommentez et modifiez la ligne $javafxSdkPath ci-dessous

# Utiliser le SDK JavaFX inclus dans le projet
$javafxSdkPath = Join-Path $PSScriptRoot "javafx-sdk-17.0.17"
$libPath = Join-Path $javafxSdkPath "lib"
$binPath = Join-Path $javafxSdkPath "bin"

# Ajouter les DLL natives au PATH
if (Test-Path $binPath) {
    $dllCount = (Get-ChildItem -Path $binPath -Filter "*.dll" -ErrorAction SilentlyContinue | Measure-Object).Count
    if ($dllCount -gt 0) {
        $env:PATH = "$binPath;$env:PATH"
        Write-Host "DLL natives trouvées dans javafx-sdk-17.0.17/bin/ ($dllCount fichiers)" -ForegroundColor Green
    } else {
        Write-Host "ATTENTION: Le dossier javafx-sdk-17.0.17/bin/ existe mais ne contient pas de DLL natives !" -ForegroundColor Yellow
    }
} else {
    Write-Host "ERREUR: Le dossier javafx-sdk-17.0.17/bin/ n'existe pas !" -ForegroundColor Red
    Write-Host "Vérifiez que le SDK JavaFX est bien inclus dans le projet." -ForegroundColor Yellow
    exit 1
}

$outPath = Join-Path $PSScriptRoot "out\production\MONSTRE_DE_POCHE"
$resourcesPath = Join-Path $PSScriptRoot "resources"

Write-Host "Lancement de l'interface graphique JavaFX..." -ForegroundColor Green
Write-Host "Module path: $libPath" -ForegroundColor Gray
Write-Host "Class path: $outPath;$resourcesPath" -ForegroundColor Gray

java --module-path "$libPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "$outPath;$resourcesPath" com.esiea.monstredepoche.Main gui

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nErreur lors du lancement. Vérifiez que:" -ForegroundColor Red
    Write-Host "1. Le SDK JavaFX est bien dans javafx-sdk-17.0.17/" -ForegroundColor Yellow
    Write-Host "2. Le projet est compilé (out/production/MONSTRE_DE_POCHE existe)" -ForegroundColor Yellow
    Write-Host "3. Vous utilisez Java 11 ou supérieur" -ForegroundColor Yellow
    Write-Host "4. Les DLL natives sont dans javafx-sdk-17.0.17/bin/" -ForegroundColor Yellow
}
