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

## Déploiement sur Railway

1. Créez un compte sur [Railway](https://railway.app)

2. Créez un nouveau projet :
   - Cliquez sur "New Project"
   - Sélectionnez "Deploy from GitHub repo"
   - Connectez votre repository GitHub
   - Sélectionnez le repository du bot

3. Ajoutez une base de données PostgreSQL :
   - Dans votre projet Railway, cliquez sur "+ New"
   - Sélectionnez "Database" → "PostgreSQL"
   - Railway créera automatiquement la base de données

4. Configurez les variables d'environnement :
   - Dans votre service, allez dans l'onglet "Variables"
   - Ajoutez les variables suivantes :
     ```
     DATABASE_URL=<URL fournie par Railway PostgreSQL>
     DATABASE_USER=postgres
     DATABASE_PASSWORD=<mot de passe fourni par Railway>
     TELEGRAM_BOT_TOKEN=votre-token-bot
     TELEGRAM_BOT_USERNAME=votre-bot-username
     TELEGRAM_GROUP_ID=votre-group-id (optionnel - voir OBTENIR_GROUP_ID.md)
     YOUTUBE_API_KEY=votre-youtube-api-key
     YOUTUBE_CHANNEL_ID=votre-channel-id
     FORMATION_LINK=https://youtu.be/x6yepLvHctk
     PORT=8080
     ```
   - **Note** : Railway fournit automatiquement `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` via les variables de service PostgreSQL
   - **Note** : `TELEGRAM_GROUP_ID` est optionnel. Sans lui, les notifications YouTube ne seront pas envoyées, mais le bot fonctionnera normalement pour les autres fonctionnalités

5. Déployez :
   - Railway détectera automatiquement le fichier `railway.json` ou `railway.toml`
   - Le build se lancera automatiquement
   - Une fois déployé, récupérez l'URL HTTPS de votre service

6. Configurez le webhook Telegram :
   - Récupérez l'URL HTTPS de votre service Railway (ex: `https://your-app.up.railway.app`)
   - Configurez le webhook :
   ```bash
   curl https://api.telegram.org/bot<TOKEN>/setWebhook?url=https://your-app.up.railway.app/webhook
   ```

7. **Avantages Railway** :
   - Pas de mise en veille (contrairement à Render free tier)
   - Déploiement automatique depuis GitHub
   - Base de données PostgreSQL incluse
   - HTTPS automatique

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

