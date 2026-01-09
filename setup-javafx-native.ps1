# Script pour copier les DLL natives JavaFX depuis le SDK vers le projet
# Usage: .\setup-javafx-native.ps1
# Le SDK JavaFX est supposé être dans: ..\openjfx-17.0.17_windows-x64_bin-sdk\javafx-sdk-17.0.17

$javafxSdkPath = Join-Path $PSScriptRoot "..\openjfx-17.0.17_windows-x64_bin-sdk\javafx-sdk-17.0.17"
$binSource = Join-Path $javafxSdkPath "bin"
$binDest = Join-Path $PSScriptRoot "bin"

if (-not (Test-Path $binSource)) {
    Write-Host "Erreur: Le SDK JavaFX n'a pas été trouvé !" -ForegroundColor Red
    Write-Host "Chemin attendu: $javafxSdkPath" -ForegroundColor Yellow
    Write-Host "Vérifiez que le SDK JavaFX est bien dans ce dossier" -ForegroundColor Yellow
    exit 1
}

Write-Host "Copie des DLL natives depuis le SDK vers le projet..." -ForegroundColor Green
Write-Host "Source: $binSource" -ForegroundColor Gray
Write-Host "Destination: $binDest" -ForegroundColor Gray

# Créer le dossier bin s'il n'existe pas
if (-not (Test-Path $binDest)) {
    New-Item -ItemType Directory -Path $binDest | Out-Null
    Write-Host "Dossier bin/ créé" -ForegroundColor Green
}

# Copier les DLL
$dllFiles = Get-ChildItem -Path "$binSource\*.dll"
Copy-Item -Path "$binSource\*.dll" -Destination $binDest -Force

$copiedCount = (Get-ChildItem -Path $binDest -Filter "*.dll" | Measure-Object).Count
Write-Host "`n✅ DLL natives copiées avec succès ! ($copiedCount fichiers)" -ForegroundColor Green
Write-Host "Les DLL sont maintenant dans: $binDest" -ForegroundColor Gray
Write-Host "`nVous pouvez maintenant lancer l'interface graphique avec: .\launch-gui.ps1" -ForegroundColor Cyan