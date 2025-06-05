# NotaBene - Spécifications Fonctionnelles et Techniques

## Table des matières
- [1. Fonctionnalités Métiers](#1-fonctionnalités-métiers)
- [2. Stratégie de Branches Git](#2-stratégie-de-branches-git)
- [3. Workflow Git](#3-workflow-git)
- [4. Règles de Gestion](#4-règles-de-gestion)

## 1. Fonctionnalités Métiers

### 1.1 Gestion des Notes
- **Création et édition**
  - Création de notes (titre, contenu, tags, date)
  - Édition en temps réel avec auto-save
  - Suppression avec corbeille et restauration
  - Système de favoris
  - Organisation par dossiers/catégories
  - Support Markdown
  - Historique des versions

### 1.2 Organisation et Recherche
- **Système de tags**
  - Tags dynamiques avec suggestions
  - Recherche full-text avancée
  - Filtres multiples (date, tags, statut)
  - Tri personnalisable
  - Vue liste/grille
  - Labels colorés

### 1.3 Collaboration et Partage
- **Fonctionnalités collaboratives**
  - Partage par lien sécurisé
  - Édition collaborative temps réel
  - Système de commentaires
  - Contrôle des permissions
  - Mentions utilisateurs (@user)

### 1.4 Import/Export
- **Gestion des données**
  - Export (PDF, Markdown, HTML)
  - Import depuis autres apps
  - Sauvegarde personnelle
  - Sync multi-appareils

### 1.5 Fonctionnalités Avancées
- **Outils additionnels**
  - Mode hors-ligne + sync
  - Rappels et notifications
  - Templates de notes
  - Checklists intégrées
  - Attachements fichiers
  - Mode sombre/clair

## 2. Stratégie de Branches Git

### 2.1 Branches Principales
- `main` : Production stable
- `develop` : Développement principal

### 2.2 Branches de Fonctionnalités
- Format : `feature/[NUMERO-TICKET]-[nom-court]`
- Exemple : `feature/NOT-123-system-tags`
- Une branche par fonctionnalité
- Durée max : 1-2 semaines

### 2.3 Branches de Correction
- Format : `fix/[NUMERO-TICKET]-[description]`
- Exemple : `fix/NOT-456-search-performance`

### 2.4 Branches de Release
- Format : `release/v[X.Y.Z]`
- Exemple : `release/v1.2.0`

### 2.5 Branches de Hotfix
- Format : `hotfix/[NUMERO-TICKET]-[description]`
- Exemple : `hotfix/NOT-789-security-patch`

## 3. Workflow Git

### 3.1 Nouvelle Fonctionnalité
```
develop → feature/XXX → develop
```

### 3.2 Process de Release
```
develop → release/vX.Y.Z → main
```

### 3.3 Correction Urgente
```
main → hotfix/XXX → main + develop
```

## 4. Règles de Gestion

### 4.1 Création de Branches
- Partir de `develop` pour les features
- Nommage avec numéro de ticket
- Une branche = une fonctionnalité

### 4.2 Pull Requests
- Review obligatoire
- Tests passants requis
- Résolution des conflits
- Description détaillée

### 4.3 Stratégie de Merge
- Features : "Squash and merge"
- Releases : "Merge commit"
- Suppression post-merge

### 4.4 Protection
- Branches `main` et `develop` protégées
- Push direct interdit
- Reviews obligatoires
- CI/CD passant requis 