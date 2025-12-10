package com.devlab.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AssistantService {
    private static final Map<Pattern, String> RULES = new HashMap<>();

    static {
        RULES.put(Pattern.compile("(?i).*java.*", Pattern.CASE_INSENSITIVE), 
            "Java est un langage de programmation orienté objet. Pour apprendre : variables, classes, héritage, interfaces.");
        RULES.put(Pattern.compile("(?i).*html.*|.*css.*", Pattern.CASE_INSENSITIVE), 
            "HTML structure le contenu, CSS le style. Utilisez des balises sémantiques et des sélecteurs CSS modernes.");
        RULES.put(Pattern.compile("(?i).*formation.*ia.*|.*ia.*formation.*", Pattern.CASE_INSENSITIVE), 
            "Pour accéder à la formation IA gratuite, envoyez votre email.");
    }

    public String processMessage(String text) {
        if (text == null) return "Bonjour ! Comment puis-je vous aider ?";
        
        String lowerText = text.toLowerCase();
        
        if (lowerText.contains("formation")) {
            return "Pour accéder à la formation gratuite, veuillez envoyer votre adresse email.";
        }

        for (Map.Entry<Pattern, String> entry : RULES.entrySet()) {
            if (entry.getKey().matcher(text).matches()) {
                return entry.getValue();
            }
        }

        return "Je peux vous aider avec Java, HTML/CSS ou la formation IA. Posez-moi une question !";
    }
}

