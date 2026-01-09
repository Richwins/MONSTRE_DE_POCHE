# Script PowerShell pour lancer l'interface console
# Usage: .\launch-console.ps1

$outPath = Join-Path $PSScriptRoot "out\production\MONSTRE_DE_POCHE"
$resourcesPath = Join-Path $PSScriptRoot "resources"

Write-Host "Lancement de l'interface console..." -ForegroundColor Green

java -cp "$outPath;$resourcesPath" com.esiea.monstredepoche.Main

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nErreur lors du lancement. Vérifiez que le projet est compilé." -ForegroundColor Red
}
