# 📝 NotaBene

> Une application légère, rapide et réactive pour gérer ses notes efficacement, où que vous soyez.

---

## 🚀 Présentation

**NotaBene** est une application web full-stack conçue pour créer, organiser et gérer des notes personnelles de manière fluide et sécurisée. Elle est construite avec une architecture réactive (Spring WebFlux) et une interface simple basée sur Vue.js 2. Le tout est conteneurisé avec Docker pour une portabilité maximale.

---

## 🧰 Stack Technique

| Layer            | Technologie               |
| ---------------- | ------------------------- |
| Frontend         | Vue.js 2 + Element UI     |
| Backend          | Spring WebFlux (Reactive) |
| Base de données  | MongoDB (NoSQL)           |
| CI/CD            | GitHub Actions            |
| Tests            | JUnit 5, Cypress (E2E)    |
| Authentification | Spring Security + JWT     |
| Conteneurs       | Docker                    |
| DevTools         | GitLens, Volar, ESLint    |

---

## 📦 Fonctionnalités

### 🔐 Authentification & Sécurité

- Connexion / inscription via email
- JWT + Spring Security
- Rôles (admin / user)
- Support internationalisation (i18n) – 🇫🇷/🇬🇧 prêt

### 📝 Gestion des notes

- CRUD de notes (titre, contenu, tags, date)
- Notes privées par utilisateur
- Recherche par mot-clé
- Notes triables et filtrables

### ⚙️ Administration

- Interface admin auto-générée
- Dashboard utilisateur
- Gestion des comptes
- Journalisation des événements (optionnelle via Audit logs)

### 📊 Outils & DevOps

- GitHub Actions pour build/test/deploy
- Conteneurs Docker individuels pour chaque service
- Fichiers `docker-compose.yml` pour un setup local simple
- Monitoring (optionnel) : Prometheus / Grafana (étape avancée)

---

## 🛠️ Installation locale

> Prérequis : Docker, Node.js 16+, Java 17+, Git

```bash
# 1. Clonez le repo
git clone https://github.com/votre-org/notabene.git
cd notabene

# 2. Lancez tous les services
docker-compose up -d

# 3. Accès
Frontend : http://localhost:9000
Backend  : http://localhost:8080/api
MongoDB  : mongodb://localhost:27017/notabene
```

---

## ⚙️ Configuration Docker

Structure simplifiée :

```
/docker
  ├── app.yml             # Docker Compose global
  ├── mongodb.yml         # MongoDB service
  ├── keycloak.yml        # (Optionnel) Auth externalisée
  └── ...
```

---

## 🔁 CI/CD avec GitHub Actions

Fichier `.github/workflows/main.yml`

- Compilation Maven + tests
- Build Docker image
- Push sur DockerHub
- Déploiement distant (SSH ou k8s)

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up Java
        uses: actions/setup-java@v3
        with:
          java-version: 17
      - run: ./mvnw verify
```

---

## ✅ Tests

| Type              | Outil       |
| ----------------- | ----------- |
| Unitaires         | JUnit 5     |
| Intégration       | Spring Test |
| End-to-End        | Cypress     |
| Charge (optional) | Gatling     |

Tests Cypress sont exécutés automatiquement en CI + snapshot d’écran à chaque erreur.

---

## 🌍 Roadmap complète

### 🧱 Phase 1 – Setup Technique

- [x] Initialisation du projet via JHipster avec WebFlux + MongoDB + Vue 2
- [x] Dockerisation complète
- [x] Authentification JWT
- [x] Génération UI admin
- [x] Support i18n (fr/en)
- [x] GitHub Actions de base (build + test)

### ✍️ Phase 2 – Fonctionnalités Métier

- [x] CRUD Note (titre, contenu, tag, user)
- [x] Sécurité : accès restreint à ses propres notes
- [x] Tri et recherche par mots-clés
- [ ] Export PDF / Markdown d'une note
- [ ] Ajout de tags dynamiques

### 🔒 Phase 3 – Admin & Sécurité

- [ ] Dashboard admin
- [ ] Page analytics simple (nb de notes, utilisateurs)
- [ ] Audit des actions critiques (connexion, suppression, etc.)

### 🚀 Phase 4 – Déploiement & Monitoring

- [ ] CI avancée : staging + prod
- [ ] Intégration Prometheus + Grafana
- [ ] Déploiement sur serveur distant / VPS
- [ ] Sauvegarde automatique MongoDB (cron Docker)

---

## 📋 TODO Technique

- [ ] Ajouter les tests unitaires manquants
- [ ] Ajouter la configuration ESLint / Prettier
- [ ] Ajouter Cucumber si tests BDD souhaités
- [ ] Setup Volar + GitLens dans VSC
- [ ] Ajouter script init de base MongoDB

---

## 🤝 Bonnes pratiques

- Commits clairs avec convention (feat, fix, chore...)
- Branches : `main`, `develop`, `feature/*`
- Tests obligatoires avant PR
- Docker obligatoire pour tout lancement local

---

## 👨‍💻 Développeurs

| Nom           | Rôle               |
| ------------- | ------------------ |
| Toi 🧑‍💻        | Lead Dev Fullstack |
| 🚀 À recruter | Dev frontend Vue   |
| 🚀 À recruter | Dev backend Java   |

---

## 📄 Licence

MIT

---

## 🧠 Mémos utiles

```bash
# Générer une entité JHipster
jhipster entity Note

# Lancer les tests
./mvnw test

# Lancer Cypress
npm run e2e
```
