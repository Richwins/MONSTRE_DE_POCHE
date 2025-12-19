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

public class MonsterLoader {
    
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
        MonsterType type = MonsterType.valueOf(typeStr.toUpperCase());
        
        int[] hpRange = DataParser.parseRange(data.get("HP"));
        int[] speedRange = DataParser.parseRange(data.get("Speed"));
        int[] attackRange = DataParser.parseRange(data.get("Attack"));
        int[] defenseRange = DataParser.parseRange(data.get("Defense"));
        
        int hp = DataParser.randomInRange(hpRange[0], hpRange[1]);
        int speed = DataParser.randomInRange(speedRange[0], speedRange[1]);
        int attack = DataParser.randomInRange(attackRange[0], attackRange[1]);
        int defense = DataParser.randomInRange(defenseRange[0], defenseRange[1]);
        
        switch (type) {
            case ELECTRIC:
                double paralysis = Double.parseDouble(data.getOrDefault("Paralysis", "0.2"));
                return new ElectricMonster(name, hp, speed, attack, defense, paralysis);
                
            case WATER:
                double floodChance = Double.parseDouble(data.getOrDefault("Flood", "0.3"));
                double fallChance = Double.parseDouble(data.getOrDefault("Fall", "0.2"));
                return new WaterMonster(name, hp, speed, attack, defense, floodChance, fallChance);
                
            case GROUND:
                double digChance = Double.parseDouble(data.getOrDefault("Dig", "0.3"));
                return new GroundMonster(name, hp, speed, attack, defense, digChance);
                
            case FIRE:
                double burnChance = Double.parseDouble(data.getOrDefault("Burn", "0.3"));
                return new FireMonster(name, hp, speed, attack, defense, burnChance);
                
            case PLANT:
                double healChance = Double.parseDouble(data.getOrDefault("Heal", "0.2"));
                return new PlantMonster(name, hp, speed, attack, defense, healChance);
                
            case INSECT:
                return new InsectMonster(name, hp, speed, attack, defense);
                
            default:
                return null;
        }
    }
}

