package com.esiea.monstredepoche.loaders;

import com.esiea.monstredepoche.models.Attack;
import com.esiea.monstredepoche.models.enums.AttackType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttackLoader {
    
    public static List<Attack> parseAttackFile(String filePath) throws IOException {
        List<Attack> attacks = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Map<String, String> attackData = new java.util.HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.equals("Attack")) {
                    attackData.clear();
                } else if (line.equals("EndAttack")) {
                    Attack attack = createAttack(attackData);
                    if (attack != null) {
                        attacks.add(attack);
                    }
                    attackData.clear();
                } else {
                    Map<String, String> field = DataParser.parseField(line);
                    if (field.containsKey("key") && field.containsKey("value")) {
                        attackData.put(field.get("key"), field.get("value"));
                    }
                }
            }
        }
        
        return attacks;
    }
    
    private static Attack createAttack(Map<String, String> data) {
        String name = data.get("Name");
        String typeStr = data.get("Type");
        AttackType type = AttackType.valueOf(typeStr.toUpperCase());
        int power = Integer.parseInt(data.get("Power"));
        int nbUse = Integer.parseInt(data.get("NbUse"));
        double fail = Double.parseDouble(data.getOrDefault("Fail", "0.0"));
        
        return new Attack(name, type, power, nbUse, fail);
    }
}

