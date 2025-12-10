# Bot Telegram DevLab

Bot Telegram complet développé en Java Spring Boot avec notifications YouTube, exercices journaliers et assistant basé sur règles.

## Fonctionnalités

1. **Notification automatique YouTube** : Vérifie toutes les 10 minutes les nouvelles vidéos de la chaîne "DKL DevLab"
2. **Exercices journaliers** : Envoie un exercice aléatoire chaque jour à 09h00
3. **Assistant DevLab** : Système de règles pour répondre aux questions Java/HTML/CSS/Formation IA

## Prérequis

- Java 21
- Maven 3.8+
- PostgreSQL (local ou cloud)
- Token Telegram Bot (via @BotFather)
- Clé API YouTube Data API v3 (gratuite)

## Configuration

### Variables d'environnement

Créez un fichier `.env` ou configurez ces variables :

```bash
TELEGRAM_BOT_TOKEN=votre-token-bot
TELEGRAM_BOT_USERNAME=votre-bot-username
TELEGRAM_GROUP_ID=votre-group-id
YOUTUBE_API_KEY=votre-youtube-api-key
YOUTUBE_CHANNEL_ID=votre-channel-id
DATABASE_URL=jdbc:postgresql://localhost:5432/telegrambot
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres
```

### Obtenir les identifiants

1. **Token Telegram** : Créez un bot via @BotFather sur Telegram
2. **Group ID** : Ajoutez @userinfobot dans votre groupe et récupérez l'ID
3. **YouTube API Key** : Créez un projet sur [Google Cloud Console](https://console.cloud.google.com/) et activez YouTube Data API v3
4. **Channel ID** : Utilisez un outil en ligne ou l'API YouTube pour trouver l'ID de la chaîne

## Déploiement local

### Avec Docker Compose

```bash
docker-compose up -d
```

### Sans Docker

1. Démarrer PostgreSQL :
```bash
# Créer la base de données
createdb telegrambot
```

2. Compiler et lancer :
```bash
mvn clean package
java -jar target/telegram-bot-1.0.0.jar
```

## Déploiement sur Render.com

1. Créez un compte sur [Render.com](https://render.com)

2. Créez une base de données PostgreSQL :
   - New → PostgreSQL
   - Notez l'URL de connexion

3. Créez un Web Service :
   - New → Web Service
   - Connectez votre repository GitHub
   - Build Command : `mvn clean package`
   - Start Command : `java -jar target/telegram-bot-1.0.0.jar`
   - Ajoutez toutes les variables d'environnement

4. Configurez le webhook Telegram :
   - Dans votre service Render, récupérez l'URL HTTPS (ex: `https://your-app.onrender.com`)
   - Configurez le webhook :
   ```bash
   curl https://api.telegram.org/bot<TOKEN>/setWebhook?url=https://your-app.onrender.com/webhook
   ```

5. Note : Pour le free tier, Render met l'app en veille après inactivité. Le webhook Telegram réveillera automatiquement l'app.

## Structure du projet

```
src/main/java/com/devlab/
├── TelegramBotApplication.java      # Classe principale
├── config/
│   └── TelegramBotConfig.java       # Configuration Telegram
├── controller/
│   └── TelegramWebhookController.java  # Webhook endpoint
├── entity/
│   ├── User.java                    # Entité utilisateur
│   └── Task.java                    # Entité exercice
├── repository/
│   ├── UserRepository.java
│   └── TaskRepository.java
└── service/
    ├── AssistantService.java        # Système de règles
    ├── NotificationScheduler.java   # Scheduler YouTube
    ├── TaskScheduler.java           # Scheduler exercices
    ├── TelegramService.java         # Service Telegram
    └── YouTubeService.java          # Service YouTube API
```

## Commandes disponibles

- `/start` : Démarrer le bot
- `/correction` : Voir la solution de l'exercice du jour

## Notes importantes

- Le bot utilise un système de webhook (recommandé pour la production)
- Les notifications YouTube sont envoyées dans le groupe Telegram configuré
- Les exercices sont envoyés individuellement à chaque utilisateur
- Le système de formation demande l'email et stocke dans PostgreSQL

## Support

Pour toute question, contactez le support DevLab.

