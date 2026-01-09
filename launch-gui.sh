#!/bin/bash
# Script Bash pour lancer l'interface graphique JavaFX
# Usage: ./launch-gui.sh
# 
# IMPORTANT : Modifiez JAVAFX_PATH ci-dessous avec le chemin vers votre SDK JavaFX

# Chemin vers JavaFX SDK (relatif au projet)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVAFX_PATH="$SCRIPT_DIR/javafx-sdk-17.0.17/lib"

# Vérifier que JavaFX est configuré
if [ ! -d "$JAVAFX_PATH" ]; then
    echo "ERREUR: JavaFX SDK non trouvé à $JAVAFX_PATH" >&2
    echo "Vérifiez que le dossier javafx-sdk-17.0.17 est présent dans le projet." >&2
    exit 1
fi

OUT_PATH="out/production/MONSTRE_DE_POCHE"
RESOURCES_PATH="resources"

# Créer le dossier de sortie si nécessaire
mkdir -p "$OUT_PATH"

echo "Compilation du projet avec JavaFX..."
javac --module-path "$JAVAFX_PATH" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -d "$OUT_PATH" \
  -cp "src:$RESOURCES_PATH" \
  -encoding UTF-8 \
  $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "Erreur lors de la compilation." >&2
    exit 1
fi

echo "Lancement de l'interface graphique JavaFX..."
java --module-path "$JAVAFX_PATH" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -cp "$OUT_PATH:$RESOURCES_PATH" \
  com.esiea.monstredepoche.Main gui

if [ $? -ne 0 ]; then
    echo "Erreur lors du lancement." >&2
    echo "Vérifiez que:" >&2
    echo "1. JavaFX est bien installé à $JAVAFX_PATH" >&2
    echo "2. Le projet est compilé ($OUT_PATH existe)" >&2
    echo "3. Vous utilisez Java 11 ou supérieur" >&2
    exit 1
fi
