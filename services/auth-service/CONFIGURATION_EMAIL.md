# Configuration de l'Envoi d'Emails

## 📧 Fonctionnalité

Après l'inscription, un mot de passe sécurisé est généré automatiquement et envoyé par email à l'utilisateur.

## ⚙️ Configuration

### 1. Gmail (Recommandé pour le développement)

Dans `application.yml`, configurez :

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: votre-email@gmail.com
    password: votre-mot-de-passe-app  # Mot de passe d'application Gmail
```

**Important pour Gmail** :
1. Activez la validation en 2 étapes sur votre compte Google
2. Générez un "Mot de passe d'application" :
   - Allez sur https://myaccount.google.com/apppasswords
   - Sélectionnez "Mail" et votre appareil
   - Copiez le mot de passe généré (16 caractères)
   - Utilisez ce mot de passe dans `application.yml`

### 2. Outlook/Hotmail

```yaml
spring:
  mail:
    host: smtp-mail.outlook.com
    port: 587
    username: votre-email@outlook.com
    password: votre-mot-de-passe
```

### 3. Yahoo Mail

```yaml
spring:
  mail:
    host: smtp.mail.yahoo.com
    port: 587
    username: votre-email@yahoo.com
    password: votre-mot-de-passe-app
```

### 4. Serveur SMTP personnalisé

```yaml
spring:
  mail:
    host: smtp.votre-serveur.com
    port: 587  # ou 465 pour SSL
    username: votre-email@domaine.com
    password: votre-mot-de-passe
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

## 🔒 Sécurité

**⚠️ Ne commitez JAMAIS vos identifiants email dans Git !**

Utilisez des variables d'environnement ou un fichier de configuration externe :

```yaml
spring:
  mail:
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
```

Puis définissez les variables d'environnement :
```bash
export EMAIL_USERNAME=votre-email@gmail.com
export EMAIL_PASSWORD=votre-mot-de-passe
```

## 📝 Format de l'Email

L'email envoyé contient :
- Salutation personnalisée
- Nom d'utilisateur
- Mot de passe généré (12 caractères sécurisés)
- Recommandation de changer le mot de passe après la première connexion

## 🧪 Test

Pour tester sans envoyer de vrais emails, vous pouvez utiliser un serveur SMTP de test comme **Mailtrap** ou **MailHog**.

### Mailtrap (Recommandé pour le développement)

1. Créez un compte sur https://mailtrap.io
2. Configurez dans `application.yml` :

```yaml
spring:
  mail:
    host: smtp.mailtrap.io
    port: 2525
    username: votre-username-mailtrap
    password: votre-password-mailtrap
```

## 🔄 Flux d'Inscription

1. Utilisateur s'inscrit (sans mot de passe)
2. Système génère un mot de passe sécurisé (12 caractères)
3. Mot de passe est hashé et stocké dans MongoDB
4. Email avec le mot de passe est envoyé à l'utilisateur
5. Utilisateur peut se connecter avec le mot de passe reçu

## 📋 Exemple de Requête d'Inscription

**Avant** (avec mot de passe) :
```json
{
  "username": "john",
  "email": "john@univ.edu",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT"
}
```

**Maintenant** (sans mot de passe) :
```json
{
  "username": "john",
  "email": "john@univ.edu",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT"
}
```

Le mot de passe sera généré automatiquement et envoyé par email.

