package com.esiea.monstredepoche.loaders;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.monsters.*;
import com.esiea.monstredepoche.models.enums.MonsterType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chargeur de monstres depuis un fichier texte.
 * Parse le fichier monsters.txt et crée les instances de monstres
 * selon leur type (ElectricMonster, WaterMonster, etc.)
 */
public class MonsterLoader {
    
    /**
     * Parse un fichier de monstres et retourne la liste des monstres créés
     * @param filePath Chemin vers le fichier monsters.txt
     * @return Liste des monstres chargés
     * @throws IOException Si une erreur survient lors de la lecture du fichier
     */
    public static List<Monster> parseMonsterFile(String filePath) throws IOException {
        List<Monster> monsters = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Map<String, String> monsterData = new java.util.HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.equals("Monster")) {
                    monsterData.clear();
                } else if (line.equals("EndMonster")) {
                    Monster monster = createMonster(monsterData);
                    if (monster != null) {
                        monsters.add(monster);
                    }
                    monsterData.clear();
                } else {
                    Map<String, String> field = DataParser.parseField(line);
                    if (field.containsKey("key") && field.containsKey("value")) {
                        monsterData.put(field.get("key"), field.get("value"));
                    }
                }
            }
        }
        
        return monsters;
    }
    
    private static Monster createMonster(Map<String, String> data) {
        String name = data.get("Name");
        String typeStr = data.get("Type");
        MonsterType type = parseMonsterType(typeStr);
        
        int[] hpRange = DataParser.parseRange(data.get("HP"));
        int[] speedRange = DataParser.parseRange(data.get("Speed"));
        int[] attackRange = DataParser.parseRange(data.get("Attack"));
        int[] defenseRange = DataParser.parseRange(data.get("Defense"));
        
        int hp = DataParser.randomInRange(hpRange[0], hpRange[1]);
        int speed = DataParser.randomInRange(speedRange[0], speedRange[1]);
        int attack = DataParser.randomInRange(attackRange[0], attackRange[1]);
        int defense = DataParser.randomInRange(defenseRange[0], defenseRange[1]);
        
        switch (type) {
            case FOUDRE:
                double paralysis = Double.parseDouble(data.getOrDefault("Paralysis", "0.2"));
                return new ElectricMonster(name, hp, speed, attack, defense, paralysis);
                
            case EAU:
                double floodChance = Double.parseDouble(data.getOrDefault("Flood", "0.3"));
                double fallChance = Double.parseDouble(data.getOrDefault("Fall", "0.2"));
                return new WaterMonster(name, hp, speed, attack, defense, floodChance, fallChance);
                
            case TERRE:
                double digChance = Double.parseDouble(data.getOrDefault("Dig", "0.3"));
                return new GroundMonster(name, hp, speed, attack, defense, digChance);
                
            case FEU:
                double burnChance = Double.parseDouble(data.getOrDefault("Burn", "0.3"));
                return new FireMonster(name, hp, speed, attack, defense, burnChance);
                
            case NATURE:
                double healChance = Double.parseDouble(data.getOrDefault("Heal", "0.2"));
                return new NatureMonster(name, hp, speed, attack, defense, healChance);
                
            default:
                return null;
        }
    }
    
    private static MonsterType parseMonsterType(String typeStr) {
        // Convertir les types français en enum
        String normalized = typeStr.trim().toUpperCase();
        switch (normalized) {
            case "FOUDRE":
            case "ELECTRIC":
                return MonsterType.FOUDRE;
            case "EAU":
            case "WATER":
                return MonsterType.EAU;
            case "TERRE":
            case "GROUND":
                return MonsterType.TERRE;
            case "FEU":
            case "FIRE":
                return MonsterType.FEU;
            case "NATURE":
            case "PLANTE":
            case "PLANT":
            case "INSECTE":
            case "INSECT":
                return MonsterType.NATURE;
            default:
                throw new IllegalArgumentException("Type de monstre inconnu: " + typeStr);
        }
    }
}

