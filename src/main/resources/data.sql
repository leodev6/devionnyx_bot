INSERT INTO tasks (category, message, solution, created_at) VALUES
('Java', 'Écrivez une méthode qui inverse une chaîne de caractères sans utiliser StringBuilder.reverse()', 'public String reverse(String str) { char[] chars = str.toCharArray(); for (int i = 0; i < chars.length / 2; i++) { char temp = chars[i]; chars[i] = chars[chars.length - 1 - i]; chars[chars.length - 1 - i] = temp; } return new String(chars); }', NOW()),
('HTML/CSS', 'Créez un bouton avec un effet hover qui change de couleur et s''agrandit légèrement', '.btn { padding: 10px 20px; background: blue; transition: all 0.3s; } .btn:hover { background: darkblue; transform: scale(1.1); }', NOW()),
('Java', 'Quelle est la différence entre == et equals() en Java ?', '== compare les références d''objets, equals() compare le contenu. Pour les primitives, == compare les valeurs.', NOW());

