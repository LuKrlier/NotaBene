# 📋 Phase 2 — Plan Détaillé — Projet de Notes

## 1. Vue d’ensemble

### 1.1 Objectif & Portée
Application de gestion de notes collaborative, temps réel, multi-dossier, avec recherche avancée, versioning, partage, analytics, et personnalisation.

### 1.2 Stack technique

| Couche      | Techno principale                   | Notes                          |
|-------------|-------------------------------------|--------------------------------|
| Backend     | Spring WebFlux (Java 17)            | Reactive, non-blocking         |
| Frontend    | Vue.js 2 + Element UI               | SPA, responsive, Vuex          |
| Base de données | MongoDB                        | Modèle documentaire, réactif   |
| Cache       | Redis                               | Sessions, temps réel           |
| Temps réel  | WebSocket                           | Collaboration & notifications  |
| Auth        | JWT                                 | Sécurité, permissions          |
| Recherche   | ElasticSearch                       | Full-text, suggestions         |

### 1.3 Risques & Points d’attention
- Scalabilité avec gros volume de notes
- Gestion des conflits d’édition temps réel
- Complexité recherche & indexation

---

## 2. Spécifications Fonctionnelles

### 2.1 Modèles de données & Entités

| Entité           | Champs clés / propriétés                                                 | Relations principales      |
|------------------|-------------------------------------------------------------------------|---------------------------|
| **Note**         | id, title, content, createdAt, updatedAt, userId, status, version        | Tag (N-N), Folder (N-N)   |
| **Tag**          | id, name, color, createdAt, userId                                       | Note (N-N)                |
| **Folder**       | id, name, parentId, path, createdAt, updatedAt, userId                   | Note (N-N), Folder (N-1)  |
| **SearchHistory**| id, query, userId, timestamp, resultCount                                | User (N-1)                |
| **UserPreferences**| userId, defaultView, theme, notificationSettings, autoSaveInterval     | User (1-1)                |

#### Relations, Indexation, Versioning
- Relations N-N via collections NoteTag & NoteFolder
- Indexes full-text sur Note.title, Note.content
- Indexes sur Tag.name, Folder.path
- Versioning sur Note (gestion conflits & historique)

---

### 2.2 Fonctionnalités principales

#### Notes
- CRUD (création, édition, suppression, lecture, archivage)
- Autosave & versioning
- Partage & export
- Realtime update via WebSocket

#### Organisation
- Système de tags (gestion, suggestions, nuage, stats)
- Dossiers hiérarchiques (navigation arborescente, drag & drop, permissions)

#### Recherche & Historique
- Moteur de recherche full-text (ElasticSearch)
- Filtres avancés & suggestions dynamiques
- Historique personnel & analytics

#### Personnalisation & Préférences
- Thèmes, layout, notifications
- Paramètres utilisateur persistés

---

## 3. Architecture Technique

### 3.1 Diagramme d’architecture

```mermaid
graph TD
  subgraph Frontend [Frontend (Vue.js + Element UI)]
    FE[SPA Application]<-->|WebSocket, REST|BE
  end
  subgraph Backend [Backend (Spring WebFlux)]
    BE[API Gateway & Services]
    BE-->|REST/JSON|DB[(MongoDB)]
    BE-->|Cache/Session|REDIS[(Redis)]
    BE-->|Recherche|ES[(ElasticSearch)]
    BE-->|WebSocket|CLIENT[Clients]
    BE-->|Notifications|WS[WebSocket Hub]
  end
  subgraph Infra [Infrastructure]
    DB
    REDIS
    ES
    BKP[Backups]
    MON[Monitoring/Alerting]
  end

  BE-->|Metrics/Logs|MON
  DB-->|Backups|BKP
  REDIS-->|Backups|BKP
  FE-->|Assets|CDN
  BE-->|CI/CD|CI[CI/CD Pipeline]
```

### 3.2 Backend (Spring WebFlux, MongoDB, Redis)

#### Modèles/Entités (Java)
```java
// Note
- id: String
- title: String
- content: String
- createdAt: DateTime
- updatedAt: DateTime
- userId: String
- status: Enum (DRAFT, PUBLISHED)
- version: Long

// Tag
- id: String
- name: String
- color: String
- createdAt: DateTime
- userId: String

// Folder
- id: String
- name: String
- parentId: String
- path: String
- createdAt: DateTime
- updatedAt: DateTime
- userId: String

// Relations N-N
NoteTag: { noteId, tagId }
NoteFolder: { noteId, folderId }

// SearchHistory
- id: String
- query: String
- userId: String
- timestamp: DateTime
- resultCount: Integer

// UserPreferences
- userId: String
- defaultView: Enum (LIST, GRID)
- theme: String
- notificationSettings: JSON
- autoSaveInterval: Integer
```

#### Services principaux

- **NoteService** : CRUD, versioning, autosave, bulk ops
- **TagService** : CRUD, suggestions, nuage, fusion
- **FolderService** : CRUD, gestion paths, arborescence, permissions
- **SearchService** : full-text, filtres, indexation, historique
- **UserPreferencesService** : gestion préférences, thèmes, notifications, autosave

#### API REST & WebSocket

```text
Notes API
POST   /api/notes
GET    /api/notes/{id}
PUT    /api/notes/{id}
DELETE /api/notes/{id}
GET    /api/notes
POST   /api/notes/bulk
GET    /api/notes/versions/{id}

Tags API
POST   /api/tags
GET    /api/tags
PUT    /api/tags/{id}
DELETE /api/tags/{id}
GET    /api/tags/suggestions
GET    /api/tags/cloud

Folders API
POST   /api/folders
GET    /api/folders
PUT    /api/folders/{id}
DELETE /api/folders/{id}
GET    /api/folders/tree
POST   /api/folders/move

Search API
POST   /api/search
GET    /api/search/history
DELETE /api/search/history/{id}
GET    /api/search/suggestions

Preferences API
GET    /api/preferences
PUT    /api/preferences
POST   /api/preferences/reset

WebSocket Endpoints
/ws/notes/{noteId}/edit      # Real-time editing
/ws/notes/{noteId}/presence  # User presence
/ws/notifications            # Real-time notifications
```

#### Repositories & Indexation
- NoteRepository, TagRepository, FolderRepository, SearchHistoryRepository, UserPreferencesRepository
- Requêtes custom : recherche, agrégations, suggestions, permission, path, popularité

#### Sécurité
- Auth JWT, gestion sessions, permissions
- CORS, XSS, CSRF, rate limiting
- Validation (Note, Tag, Folder, Search, Preferences)

#### Tâches planifiées (Scheduler)
- AutosaveCleanup (quotidien)
- SearchHistoryCleanup (mensuel)
- TagSuggestionUpdate (quotidien)
- IndexOptimization (hebdo)
- DatabaseBackup (quotidien)
- InactiveNotesArchival (mensuel)
- UserActivityStats (hebdo)
- StorageCleanup (quotidien)
- HealthCheck (5 min)

#### Monitoring
- Metrics API, DB, frontend, infra
- Logs structurés, rotation, alerting

---

### 3.3 Frontend (Vue.js 2, Element UI, Vuex)

#### Architecture composants

- **Atoms** : el-button, el-input, icons
- **Molecules** : search bar, note card, tag input
- **Organisms** : note editor, folder tree, tag cloud
- **Templates** : layouts (header, sidebar, main, responsive)
- **Pages** : note view, search results, dashboard

#### Composants principaux

- **NoteEditor** : toolbar, édition, preview, autosave
- **NoteList** : vue liste/grille, tri, pagination, filtres
- **NoteSidebar** : navigation rapide, métadonnées, tags, statuts
- **SearchBar/SearchResults** : autocomplete, filtres, historique
- **FolderNavigation** : tree view, drag & drop, gestion dossiers
- **TagManagement** : suggestions, nuage, stats

#### Gestion d’état (Vuex)
- Modules : notes, autosave, editor, tags, folders, preferences

#### Services Frontend
- NoteApiService, AutosaveService, WebSocketService

#### UI/UX
- Layout Element UI, responsive
- Feedback (loading, errors, notifications)
- Personnalisation (thèmes, préférences, raccourcis)
- Accessibilité, i18n

---

## 4. Validation & Qualité

### 4.1 Stratégie de tests

| Type de test              | Backend                        | Frontend                        | E2E                      |
|---------------------------|-------------------------------|----------------------------------|--------------------------|
| Unitaire                  | NoteService, TagService, ...  | NoteEditor, NoteList, ...        | Parcours utilisateur     |
| Intégration               | API, WebSocket, DB            | Store modules, API, ...          | Flux complet             |
| Performance               | Load, stress, endurance       | Lighthouse, bundle size          | Latence, temps de réponse|
| Sécurité                  | Scans, penetration            | XSS, CSRF                        | Scénarios attaques       |
| Compatibilité             | -                             | Navigateurs, mobile              | Multi-environnement      |
| Backup/Restore            | Validation, restore           | -                                | Données                 |

### 4.2 Monitoring & Alerting

- Techniques : API (latence, erreur), DB (requêtes, mémoire), Front (loading, TTI)
- Métiers : activité utilisateur, engagement, conflits, autosave
- Outils : Prometheus, Grafana, alertes, dashboards

---

## 5. Exploitation & Opérations

### 5.1 Déploiement & CI/CD

- Pipelines CI/CD, environnements (dev, staging, prod)
- Variables d’environnement, gestion secrets
- Rollback, monitoring post-déploiement

### 5.2 Migrations & Data Management

- Migrations schéma, transformations, nettoyage
- Sauvegardes : snapshot (quotidien), incrémental (temps réel), vérification

### 5.3 Sécurité & Audits

- Contrôles OWASP, scans dépendances, audit logs, input validation, session management

### 5.4 Cache Management

- Application cache (préférences, queries)
- Redis cache (sessions, real-time)
- Browser cache (assets, API)

---

## 6. Documentation

### 6.1 Documentation technique

- API (Swagger/OpenAPI, Postman)
- Architecture (diagrammes, flux de données)
- Déploiement (config, env, scripts)
- Monitoring (dashboards, alerting)

### 6.2 Documentation utilisateur

- Guides (premiers pas, fonctionnalités)
- FAQ, troubleshooting
- Tutoriels (cas d’usage, bonnes pratiques)
- Release notes

---

## 7. Planning & Suivi

### 7.1 Roadmap (indicative)

| Semaines     | Objectifs principaux                          |
|--------------|-----------------------------------------------|
| 1-2          | CRUD base, composants principaux, note editor |
| 3-4          | WebSocket, autosave, gestion conflits         |
| 5-6          | Recherche, tags, dossiers, analytics          |

### 7.2 Critères de succès

| Domaine      | Indicateur           | Cible                   |
|--------------|----------------------|-------------------------|
| Performance  | Temps API            | < 200ms                 |
|              | Chargement initial   | < 2s                    |
|              | Score Lighthouse     | > 90                    |
| Qualité      | Couverture tests     | > 80%                   |
|              | Vulnérabilités       | 0 critique              |
|              | Dette technique      | Faible                  |
| UX           | Temps d’édition note | < 30s                   |
|              | Recherche complétée  | > 90%                   |
|              | Satisfaction         | > 4/5                   |

---

## 8. Processus de Review & Checklist

### 8.1 Code review

- Performance, sécurité, accessibilité, tests, documentation

### 8.2 QA process

- Test plan, scénarios, jeux de données, acceptation (fonctionnel, non-fonctionnel, perf)

---

## 9. Annexe : Checklists

- [ ] Back/Front CRUD complet notes/tags/dossiers
- [ ] WebSocket temps réel fonctionnel
- [ ] Autosave & versioning OK
- [ ] Recherche full-text déployée
- [ ] Analytics & monitoring actifs
- [ ] Sécurité (auth, CORS, XSS, CSRF) validée
- [ ] Test coverage > 80%
- [ ] Documentation à jour
- [ ] Backups et restore testés
- [ ] Alerting mis en place

---

## 10. Améliorations potentielles & recommandations

### Ce qui pourrait être amélioré ou approfondi pour renforcer le projet :

1. **Scalabilité et cloud-native**
   - Ajouter une réflexion sur le déploiement Kubernetes, l’auto-scaling, le load balancing, les blue/green deployments.
   - Préciser la gestion du scale horizontal (sharding MongoDB, ElasticSearch clusters).

2. **Observabilité avancée**
   - Intégrer le tracing distribué (OpenTelemetry, Jaeger).
   - Centraliser les logs (ELK/Loki) pour faciliter le debug en production.

3. **Accessibilité (a11y)**
   - Détailler la démarche accessibilité côté frontend (navigation clavier, contrastes, ARIA, tests a11y).

4. **Conformité RGPD & Privacy**
   - Prendre en compte la conformité RGPD : suppression des données, consentement cookies, gestion des exports, politique de confidentialité.

5. **Expérience mobile**
   - Ajouter des wireframes ou user flows pour mobile, ou envisager une PWA ou app dédiée si la cible le justifie.

6. **Gestion des fichiers joints**
   - Permettre l’ajout de médias ou fichiers dans les notes, et prévoir leur stockage sécurisé (cloud/object storage).

7. **Internationalisation Backend**
   - Prévoir la gestion multi-langues côté backend (emails, logs, messages d’erreur).

8. **Gestion des quotas/limites**
   - Définir des quotas par utilisateur/groupe (nombre de notes, stockage…).

9. **API publique et écosystème**
   - Si besoin, prévoir la documentation, la gestion de clés, la limitation de débit (throttling) pour une API exposée à des tiers.

10. **Automatisation du provisioning**
    - Scripts d’init, gestion automatisée des secrets, infrastructure as code (Terraform/Ansible).

11. **Sécurité avancée**
    - Ajout de 2FA, gestion des sessions concurrentes, audit des accès, gestion de la rotation des clés/secrets.

12. **Diagrammes complémentaires**
    - Ajouter des diagrammes de séquence pour illustrer les principaux parcours (édition, autosave, recherche temps réel).
    - Ajouter un diagramme d’état pour le versioning des notes.

---

**Ce document sert de référence phase 2, à mettre à jour au fil des sprints.**