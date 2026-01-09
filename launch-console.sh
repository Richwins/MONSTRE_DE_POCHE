#!/bin/bash
# Script Bash pour lancer l'interface console
# Usage: ./launch-console.sh

OUT_PATH="out/production/MONSTRE_DE_POCHE"
RESOURCES_PATH="resources"

# Créer le dossier de sortie si nécessaire
mkdir -p "$OUT_PATH"

echo "Compilation du projet (mode console)..."
javac -d "$OUT_PATH" \
  -cp "src:$RESOURCES_PATH" \
  -encoding UTF-8 \
  $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "Erreur lors de la compilation." >&2
    exit 1
fi

echo "Lancement de l'interface console..."
java -cp "$OUT_PATH:$RESOURCES_PATH" \
  com.esiea.monstredepoche.Main

if [ $? -ne 0 ]; then
    echo "Erreur lors du lancement." >&2
    exit 1
fi
