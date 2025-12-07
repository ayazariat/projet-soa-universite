# Architecture Visuelle du Service d'Authentification

## 📊 Diagramme de Flux

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT (Frontend/Postman)                │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            │ HTTP Request
                            │
┌───────────────────────────▼─────────────────────────────────┐
│              AuthController (REST Endpoints)                │
│  • POST /api/auth/register                                 │
│  • POST /api/auth/login                                    │
│  • GET  /api/auth/validate                                 │
│  • GET  /api/auth/me                                       │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            │ Appelle
                            │
┌───────────────────────────▼─────────────────────────────────┐
│            AuthServiceImpl (Logique Métier)                 │
│  • authenticate() : Vérifie credentials, génère token      │
│  • register() : Crée nouvel utilisateur                   │
└───────┬───────────────────────┬───────────────────────────┘
        │                        │
        │ Utilise                │ Utilise
        │                        │
┌───────▼────────┐    ┌─────────▼───────────────────────────┐
│ UserRepository │    │    JwtTokenProvider                 │
│ (MongoDB)      │    │  • generateToken()                  │
│                │    │  • validateToken()                  │
│ • findByUsername│   │  • getUsernameFromToken()           │
│ • save()       │    │  • getRoleFromToken()               │
│ • existsBy...  │    └─────────────────────────────────────┘
└───────┬────────┘
        │
        │ Accède à
        │
┌───────▼───────────────────────────────────────────────────┐
│                    MongoDB Database                        │
│              Collection: users                            │
│  {                                                         │
│    "_id": "...",                                           │
│    "username": "john",                                     │
│    "email": "john@univ.edu",                               │
│    "password": "$2a$10$...", (hashé)                      │
│    "role": "STUDENT",                                      │
│    "active": true                                          │
│  }                                                         │
└────────────────────────────────────────────────────────────┘
```

## 🔐 Flux de Sécurité (Filtre JWT)

```
Requête HTTP avec Header: Authorization: Bearer <token>
        │
        ▼
┌───────────────────────────────────────┐
│   JwtAuthenticationFilter            │
│   (Intercepte TOUTES les requêtes)   │
└───────────────┬───────────────────────┘
                │
                │ 1. Extrait le token
                │
                ▼
┌───────────────────────────────────────┐
│   JwtTokenProvider.validateToken()   │
│   • Vérifie la signature             │
│   • Vérifie l'expiration             │
└───────────────┬───────────────────────┘
                │
                │ Si valide
                │
                ▼
┌───────────────────────────────────────┐
│   CustomUserDetailsService            │
│   loadUserByUsername()                │
│   • Charge l'utilisateur depuis DB    │
│   • Construit UserDetails             │
└───────────────┬───────────────────────┘
                │
                │
                ▼
┌───────────────────────────────────────┐
│   Spring Security Context             │
│   • Ajoute l'utilisateur authentifié  │
│   • Disponible dans tous les endpoints│
└───────────────┬───────────────────────┘
                │
                │ Continue la requête
                │
                ▼
        Endpoint Controller
```

## 📦 Structure des Packages

```
com.university.auth_service/
│
├── AuthServiceApplication.java          [Point d'entrée]
│
├── controller/
│   └── AuthController.java             [REST API]
│
├── service/
│   ├── AuthService.java                [Interface]
│   └── impl/
│       └── AuthServiceImpl.java        [Implémentation]
│
├── repository/
│   └── UserRepository.java              [Accès MongoDB]
│
├── entity/
│   ├── User.java                       [Modèle de données]
│   └── Role.java                       [Enum des rôles]
│
├── dto/
│   ├── LoginRequest.java               [DTO Login]
│   ├── RegisterRequest.java            [DTO Register]
│   ├── AuthResponse.java               [DTO Réponse]
│   └── UserDTO.java                    [DTO Utilisateur]
│
├── security/
│   ├── JwtTokenProvider.java           [Gestion JWT]
│   ├── JwtAuthenticationFilter.java    [Filtre JWT]
│   └── CustomUserDetailsService.java   [Chargement users]
│
├── config/
│   ├── SecurityConfig.java             [Config Sécurité]
│   └── CorsConfig.java                 [Config CORS]
│
└── exception/
    ├── AuthException.java              [Exception custom]
    ├── ErrorResponse.java              [Format erreur]
    └── GlobalExceptionHandler.java     [Gestion erreurs]
```

## 🔄 Cycle de Vie d'une Requête

### 1. Requête d'Inscription
```
Client
  │ POST /api/auth/register
  │ Body: { username, email, password, ... }
  ▼
SecurityConfig
  │ ✅ Endpoint public (pas d'authentification)
  ▼
AuthController.register()
  │ @Valid → Validation automatique
  ▼
AuthServiceImpl.register()
  │ 1. Vérifie username existe ?
  │ 2. Vérifie email existe ?
  │ 3. Hash password (BCrypt)
  │ 4. Crée User
  ▼
UserRepository.save()
  │ Sauvegarde dans MongoDB
  ▼
Retourne UserDTO (201 Created)
```

### 2. Requête de Connexion
```
Client
  │ POST /api/auth/login
  │ Body: { username, password }
  ▼
SecurityConfig
  │ ✅ Endpoint public
  ▼
AuthController.login()
  ▼
AuthServiceImpl.authenticate()
  │ 1. AuthenticationManager.authenticate()
  │    └─> CustomUserDetailsService.loadUserByUsername()
  │        └─> UserRepository.findByUsername()
  │    └─> Vérifie password (BCrypt.compare)
  │
  │ 2. Si OK → Récupère User
  │ 3. Met à jour lastLogin
  │ 4. JwtTokenProvider.generateToken(user)
  │    └─> Crée JWT avec claims (username, role, userId)
  │
  ▼
Retourne AuthResponse (200 OK)
  {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "type": "Bearer",
    "expiresIn": 86400000,
    "user": { ... }
  }
```

### 3. Requête Authentifiée
```
Client
  │ GET /api/auth/me
  │ Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
  ▼
JwtAuthenticationFilter (intercepte)
  │ 1. Extrait token du header
  │ 2. JwtTokenProvider.validateToken()
  │ 3. JwtTokenProvider.getUsernameFromToken()
  │ 4. CustomUserDetailsService.loadUserByUsername()
  │ 5. Ajoute à SecurityContext
  ▼
SecurityConfig
  │ ✅ Endpoint protégé (authenticated)
  ▼
AuthController.getCurrentUser()
  │ Récupère Authentication depuis SecurityContext
  │ Récupère User depuis Repository
  ▼
Retourne UserDTO (200 OK)
```

## 🎯 Responsabilités par Couche

### Controller (AuthController)
**Responsabilités** :
- ✅ Recevoir les requêtes HTTP
- ✅ Valider les données d'entrée (@Valid)
- ✅ Appeler le service approprié
- ✅ Retourner les réponses HTTP
- ❌ NE PAS contenir de logique métier
- ❌ NE PAS accéder directement à la base de données

### Service (AuthServiceImpl)
**Responsabilités** :
- ✅ Contenir toute la logique métier
- ✅ Orchestrer les appels (Repository, JWT, etc.)
- ✅ Gérer les transactions
- ✅ Gérer les exceptions métier
- ❌ NE PAS gérer les détails HTTP
- ❌ NE PAS accéder directement à la base (utilise Repository)

### Repository (UserRepository)
**Responsabilités** :
- ✅ Accéder à la base de données
- ✅ Fournir des méthodes de recherche
- ✅ Gérer la persistance
- ❌ NE PAS contenir de logique métier
- ❌ NE PAS valider les données

### Security (JWT, Filters)
**Responsabilités** :
- ✅ Générer et valider les tokens JWT
- ✅ Intercepter les requêtes
- ✅ Authentifier les utilisateurs
- ✅ Gérer les rôles et permissions
- ❌ NE PAS contenir de logique métier

## 🔑 Points Clés à Retenir

1. **Séparation des Responsabilités** :
   - Chaque couche a un rôle précis
   - Facilite la maintenance et les tests

2. **Sécurité** :
   - Mot de passe toujours hashé (BCrypt)
   - Token JWT signé et expiré
   - Validation à chaque requête

3. **Stateless** :
   - Pas de session serveur
   - Token dans chaque requête
   - Scalable horizontalement

4. **Validation** :
   - Côté serveur (toujours)
   - Côté client (optionnel, pour UX)

5. **Gestion d'Erreurs** :
   - Exceptions personnalisées
   - Format de réponse standardisé
   - Gestionnaire global



