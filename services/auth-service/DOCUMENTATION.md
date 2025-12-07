# Documentation du Service d'Authentification

## 📋 Table des Matières
1. [Architecture Générale](#architecture-générale)
2. [Fichiers par Catégorie](#fichiers-par-catégorie)
3. [Flux d'Authentification](#flux-dauthentification)

---

## 🏗️ Architecture Générale

Le service d'authentification suit une architecture en couches (layered architecture) :

```
Controller (REST API)
    ↓
Service (Logique Métier)
    ↓
Repository (Accès aux Données)
    ↓
Database (MongoDB)
```

---

## 📁 Fichiers par Catégorie

### 🚀 1. POINT D'ENTRÉE

#### `AuthServiceApplication.java`
**Rôle** : Point d'entrée principal de l'application Spring Boot

**Code** :
```java
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
```

**Explication** :
- `@SpringBootApplication` : Annotation qui combine :
  - `@Configuration` : Déclare la classe comme source de configuration
  - `@EnableAutoConfiguration` : Active la configuration automatique de Spring Boot
  - `@ComponentScan` : Scanne les composants dans le package et sous-packages
- `main()` : Méthode principale qui démarre l'application Spring Boot
- Spring Boot va automatiquement scanner et initialiser tous les composants annotés

---

### 📦 2. ENTITÉS (Entities)

#### `User.java`
**Rôle** : Représente un utilisateur dans la base de données MongoDB

**Champs** :
- `id` : Identifiant unique (généré par MongoDB)
- `username` : Nom d'utilisateur unique
- `email` : Email unique
- `password` : Mot de passe hashé (BCrypt)
- `firstName`, `lastName` : Prénom et nom
- `role` : Rôle de l'utilisateur (ADMIN, TEACHER, STUDENT)
- `active` : Statut actif/désactivé
- `createdAt` : Date de création
- `lastLogin` : Date de dernière connexion

**Utilité** :
- Modèle de données pour MongoDB
- Représente la structure d'un utilisateur dans la base
- Les getters/setters permettent l'accès aux propriétés

#### `Role.java`
**Rôle** : Enumération définissant les rôles disponibles

**Code** :
```java
public enum Role {
    ADMIN,      // Administrateur
    TEACHER,     // Enseignant
    STUDENT      // Étudiant
}
```

**Utilité** :
- Type-safe : Évite les erreurs de typage
- Limite les valeurs possibles
- Facilite la gestion des permissions

---

### 📥 3. DTOs (Data Transfer Objects)

Les DTOs sont des objets utilisés pour transférer des données entre les couches, sans exposer l'entité complète.

#### `LoginRequest.java`
**Rôle** : DTO pour recevoir les données de connexion

**Champs** :
- `username` : Nom d'utilisateur (obligatoire)
- `password` : Mot de passe (obligatoire)

**Validations** :
- `@NotBlank` : Vérifie que le champ n'est pas vide

**Utilité** :
- Structure les données reçues dans la requête HTTP POST `/api/auth/login`
- Validation automatique avant traitement

#### `RegisterRequest.java`
**Rôle** : DTO pour recevoir les données d'inscription

**Champs** :
- `username` : 3-20 caractères
- `email` : Format email valide
- `password` : Minimum 8 caractères, avec majuscule, minuscule, chiffre et caractère spécial
- `firstName`, `lastName` : Obligatoires
- `role` : Rôle de l'utilisateur

**Validations** :
- `@Size` : Longueur minimale/maximale
- `@Email` : Format email
- `@Pattern` : Expression régulière pour le mot de passe

**Utilité** :
- Validation stricte des données d'inscription
- Sécurité : Force un mot de passe fort

#### `AuthResponse.java`
**Rôle** : DTO pour la réponse après authentification

**Champs** :
- `token` : Token JWT généré
- `type` : Type de token (par défaut "Bearer")
- `expiresIn` : Durée de validité en millisecondes
- `user` : Informations de l'utilisateur (sans le mot de passe)

**Utilité** :
- Réponse standardisée après login
- Contient le token JWT nécessaire pour les requêtes authentifiées

#### `UserDTO.java`
**Rôle** : DTO pour représenter un utilisateur sans informations sensibles

**Champs** : Tous les champs de User sauf `password`

**Utilité** :
- Sécurité : N'expose jamais le mot de passe
- Utilisé dans les réponses API

---

### 🗄️ 4. REPOSITORY

#### `UserRepository.java`
**Rôle** : Interface pour l'accès aux données MongoDB

**Code** :
```java
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
```

**Explication** :
- `extends MongoRepository<User, String>` : 
  - `User` : Type de l'entité
  - `String` : Type de l'ID
  - Fournit automatiquement : `save()`, `findById()`, `delete()`, etc.
- Méthodes personnalisées :
  - Spring Data génère automatiquement l'implémentation basée sur le nom de la méthode
  - `findByUsername` → `SELECT * FROM users WHERE username = ?`
  - `existsByUsername` → Vérifie l'existence

**Utilité** :
- Abstraction de l'accès aux données
- Pas besoin d'écrire du code SQL/MongoDB
- Facilite les tests (peut être mocké)

---

### 🔧 5. SERVICE (Logique Métier)

#### `AuthService.java`
**Rôle** : Interface définissant les contrats du service d'authentification

**Méthodes** :
- `authenticate(LoginRequest)` : Authentifie un utilisateur
- `register(RegisterRequest)` : Enregistre un nouvel utilisateur

**Utilité** :
- Définit le contrat (interface)
- Permet l'injection de dépendances
- Facilite les tests et le remplacement d'implémentation

#### `AuthServiceImpl.java`
**Rôle** : Implémentation de la logique métier d'authentification

**Dépendances injectées** :
- `UserRepository` : Accès aux données
- `PasswordEncoder` : Hashage des mots de passe (BCrypt)
- `JwtTokenProvider` : Génération de tokens JWT
- `AuthenticationManager` : Gestion de l'authentification Spring Security

**Méthode `authenticate()`** :
1. Vérifie les credentials avec `AuthenticationManager`
2. Récupère l'utilisateur depuis la base
3. Met à jour `lastLogin`
4. Génère un token JWT
5. Retourne `AuthResponse` avec le token

**Méthode `register()`** :
1. Vérifie si username existe déjà
2. Vérifie si email existe déjà
3. Hash le mot de passe avec BCrypt
4. Crée et sauvegarde l'utilisateur
5. Retourne `UserDTO` (sans mot de passe)

**Utilité** :
- Centralise toute la logique métier
- Séparation des responsabilités
- Facile à tester

---

### 🌐 6. CONTROLLER (REST API)

#### `AuthController.java`
**Rôle** : Point d'entrée REST pour les requêtes HTTP

**Endpoints** (à implémenter) :
- `POST /api/auth/register` : Inscription
- `POST /api/auth/login` : Connexion
- `GET /api/auth/validate` : Validation du token
- `GET /api/auth/me` : Informations de l'utilisateur connecté

**Utilité** :
- Expose l'API REST
- Reçoit les requêtes HTTP
- Appelle le service approprié
- Retourne les réponses HTTP

---

### 🔐 7. SÉCURITÉ

#### `JwtTokenProvider.java`
**Rôle** : Gestion complète des tokens JWT

**Méthodes principales** :

1. **`generateToken(User user)`** :
   - Crée un token JWT contenant :
     - `subject` : username
     - `claims` : role, userId, email
     - `issuedAt` : Date d'émission
     - `expiration` : Date d'expiration
   - Signe le token avec une clé secrète (HMAC SHA-256)

2. **`validateToken(String token)`** :
   - Vérifie la signature
   - Vérifie l'expiration
   - Gère les exceptions (token invalide, expiré, etc.)

3. **`getUsernameFromToken()`**, **`getUserIdFromToken()`**, **`getRoleFromToken()`** :
   - Extrait les informations du token décodé

**Utilité** :
- Génération sécurisée de tokens
- Validation des tokens reçus
- Extraction d'informations depuis le token

#### `JwtAuthenticationFilter.java`
**Rôle** : Filtre qui intercepte chaque requête HTTP pour valider le token JWT

**Fonctionnement** :
1. Intercepte chaque requête
2. Extrait le token du header `Authorization: Bearer <token>`
3. Valide le token
4. Si valide : charge l'utilisateur et l'ajoute au contexte Spring Security
5. Passe la requête au filtre suivant

**Utilité** :
- Authentification automatique pour chaque requête
- Pas besoin de vérifier manuellement dans chaque endpoint
- Le token est validé une seule fois par requête

#### `CustomUserDetailsService.java`
**Rôle** : Service Spring Security pour charger les utilisateurs

**Méthode `loadUserByUsername()`** :
1. Récupère l'utilisateur depuis MongoDB
2. Construit un objet `UserDetails` Spring Security avec :
   - Username et password
   - Rôles (ROLE_ADMIN, ROLE_TEACHER, etc.)
   - Statut du compte (actif/désactivé)

**Utilité** :
- Interface standard de Spring Security
- Permet à Spring Security de charger les utilisateurs
- Gère les rôles et permissions

#### `SecurityConfig.java`
**Rôle** : Configuration de la sécurité Spring Security

**Configuration** :
- Désactive CSRF (pas nécessaire pour API stateless)
- Configure CORS
- Définit les endpoints publics (register, login)
- Définit les endpoints protégés (validate, me)
- Configure l'authentification stateless (pas de session)
- Ajoute le filtre JWT

**Utilité** :
- Centralise toute la configuration de sécurité
- Définit les règles d'accès
- Configure l'authentification JWT

#### `CorsConfig.java`
**Rôle** : Configuration CORS (Cross-Origin Resource Sharing)

**Utilité** :
- Permet aux applications frontend (sur d'autres domaines) d'appeler l'API
- Configure les origines autorisées, méthodes HTTP, headers

---

### ⚙️ 8. CONFIGURATION

#### `application.yml`
**Rôle** : Configuration de l'application

**Sections** :

1. **Spring Application** :
   - Nom du service : `auth-service`

2. **Server** :
   - Port : `8081`
   - Context path : `/api` (toutes les URLs commencent par `/api`)

3. **MongoDB** :
   - URI : `mongodb://localhost:27017/auth_db`
   - Database : `auth_db`

4. **JWT** :
   - `secret` : Clé secrète pour signer les tokens (256 bits minimum)
   - `expiration` : Durée de validité (24h = 86400000 ms)

5. **Logging** :
   - Niveau de log DEBUG pour le débogage

**Utilité** :
- Configuration centralisée
- Facile à modifier selon l'environnement (dev, prod)
- Pas besoin de recompiler pour changer la config

---

### ❌ 9. GESTION D'ERREURS

#### `AuthException.java`
**Rôle** : Exception personnalisée pour les erreurs d'authentification

**Utilité** :
- Distingue les erreurs d'authentification des autres erreurs
- Permet un traitement spécifique

#### `ErrorResponse.java`
**Rôle** : Structure standardisée pour les réponses d'erreur

**Champs** :
- `timestamp` : Date/heure de l'erreur
- `status` : Code HTTP (400, 401, 500, etc.)
- `error` : Type d'erreur
- `message` : Message d'erreur
- `path` : URL de la requête

**Utilité** :
- Format cohérent pour toutes les erreurs
- Facilite le débogage côté client

#### `GlobalExceptionHandler.java`
**Rôle** : Gestionnaire global des exceptions

**Fonctionnement** :
- Intercepte toutes les exceptions non gérées
- Les transforme en réponses HTTP appropriées
- Utilise `ErrorResponse` pour formater la réponse

**Utilité** :
- Évite les erreurs 500 non gérées
- Messages d'erreur cohérents
- Meilleure expérience utilisateur

---

## 🔄 Flux d'Authentification

### 1. Inscription (Register)
```
Client → POST /api/auth/register
    ↓
AuthController.register()
    ↓
AuthServiceImpl.register()
    ↓
    ├─ Vérifie username/email existants
    ├─ Hash le mot de passe (BCrypt)
    ├─ Crée l'utilisateur
    └─ Sauvegarde dans MongoDB
    ↓
Retourne UserDTO (sans password)
```

### 2. Connexion (Login)
```
Client → POST /api/auth/login
    ↓
AuthController.login()
    ↓
AuthServiceImpl.authenticate()
    ↓
    ├─ AuthenticationManager vérifie credentials
    ├─ Récupère l'utilisateur
    ├─ Met à jour lastLogin
    ├─ Génère token JWT (JwtTokenProvider)
    └─ Retourne AuthResponse avec token
```

### 3. Requête Authentifiée
```
Client → GET /api/auth/me
    Header: Authorization: Bearer <token>
    ↓
JwtAuthenticationFilter
    ├─ Extrait le token
    ├─ Valide le token (JwtTokenProvider)
    ├─ Charge l'utilisateur (CustomUserDetailsService)
    └─ Ajoute au contexte Spring Security
    ↓
AuthController.getCurrentUser()
    ├─ Récupère l'utilisateur depuis le contexte
    └─ Retourne UserDTO
```

---

## 🔑 Concepts Clés

### JWT (JSON Web Token)
- **Structure** : `header.payload.signature`
- **Contenu** : Informations de l'utilisateur (username, role, etc.)
- **Signature** : Garantit l'intégrité et l'authenticité
- **Stateless** : Pas besoin de session serveur

### Spring Security
- **AuthenticationManager** : Gère l'authentification
- **UserDetailsService** : Charge les utilisateurs
- **Filter Chain** : Chaîne de filtres pour chaque requête
- **SecurityContext** : Contexte de sécurité (utilisateur actuel)

### MongoDB
- **NoSQL** : Base de données orientée documents
- **Collections** : Équivalent des tables SQL
- **Documents** : Équivalent des lignes SQL
- **Spring Data MongoDB** : Abstraction pour l'accès aux données

---

## 📝 Notes Importantes

1. **Sécurité du Mot de Passe** :
   - Jamais stocké en clair
   - Toujours hashé avec BCrypt
   - Jamais retourné dans les réponses

2. **Token JWT** :
   - Contient des informations mais pas le mot de passe
   - Expire après 24h
   - Doit être envoyé dans le header `Authorization`

3. **Validation** :
   - Validation côté serveur (toujours)
   - Validation côté client (optionnelle, pour UX)

4. **Stateless** :
   - Pas de session serveur
   - Chaque requête contient le token
   - Scalable horizontalement

---

## 🚀 Prochaines Étapes

Pour compléter le service :
1. Implémenter `AuthController` avec tous les endpoints
2. Compléter `SecurityConfig` avec la configuration complète
3. Compléter `JwtAuthenticationFilter` pour intercepter les requêtes
4. Ajouter des tests unitaires et d'intégration



