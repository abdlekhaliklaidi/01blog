# **01Blog – README Technique**

## **Présentation du Projet**

**01Blog** est une plateforme sociale permettant aux étudiants de partager leur parcours, leurs découvertes et leurs apprentissages.
Les utilisateurs peuvent publier du contenu, interagir entre eux, suivre d’autres profils et signaler les comportements inappropriés.
Les administrateurs disposent d’un espace dédié pour modérer les utilisateurs et les publications.

L’application est développée en **Fullstack** avec :

* **Backend :** Spring Boot (Java)
* **Frontend :** Angular
* **Base de données :** PostgreSQL / MySQL

---

## **Objectifs Pédagogiques**

Le projet permet d’apprendre et maîtriser :

### 🔧 Backend – Spring Boot

* Création d’API REST
* Authentification JWT & Spring Security
* Services, controllers, repositories
* Upload de fichiers (images/vidéos)
* Gestion des rôles et permissions
* Manipulation d’une base de données relationnelle (JPA/Hibernate)

### 🎨 Frontend – Angular

* Architecture modulaire et composants
* Services et consommation d’API REST
* Routing, guards, interceptors (JWT)
* UI avec Angular Material ou Bootstrap
* Gestion des formulaires et upload de médias

### 🌐 Fullstack

* Intégration frontend-backend
* Modélisation de relations (likes, comments, subscriptions)
* Gestion du contenu utilisateur
* Pratiques Git/GitHub et organisation agile

---

## **Fonctionnalités Backend**

### 🔐 **Authentification & Sécurité**

* Inscription et connexion via email + mot de passe
* Hash sécurisé (BCrypt)
* Gestion des rôles :

  * **USER**
  * **ADMIN**
* Protection des routes par JWT + Spring Security

---

### 👤 **User Block Page (Profil)**

Chaque utilisateur dispose d’une page publique affichant :

* Ses informations de profil
* La liste de ses posts
* Le bouton *S’abonner / Se désabonner*

Les abonnés :

* Reçoivent des notifications lors d’un nouveau post
* Peuvent consulter le profil et liker/commenter

---

### 📝 **Posts**

* CRUD complet : créer, modifier, supprimer
* Post contenant :

  * Texte / description
  * Image ou vidéo en pièce jointe
  * Timestamp
* Interactions :

  * Like/Unlike
  * Commentaires (CRUD basique)
* Stockage des médias dans :

  * Le système de fichier, **ou**
  * Un bucket cloud (S3 recommandé)

---

### 🚨 **Reports (Signalements)**

* Un utilisateur peut signaler un autre utilisateur
* Chaque signalement contient :

  * Profil signalé
  * Raison du signalement (texte)
  * Timestamp
  * Auteur du signalement
* Visible uniquement par les administrateurs

---

### 🛡️ **Admin Panel (Back-office)**

Accessible uniquement aux administrateurs.

Fonctionnalités :

* Gestion des utilisateurs (ban, delete)
* Visualisation & suppression de posts
* Consultation et traitement des signalements

Toutes les routes admin sont protégées par un filtre de rôle (**ROLE_ADMIN**).

---

## **Fonctionnalités Frontend**

### 🏠 **Homepage / Feed**

* Affiche les posts des profils suivis
* Infinite scroll (optionnel)
* Like/comment directement depuis le feed

---

### 👤 **Page Profil**

* Gestion des posts (CRUD)
* Aperçu des médias avant upload
* Navigation fluide entre les profils

---

### ❤️ **Interactions**

* Likes en un clic
* Commentaires (ajout, liste, suppression par propriétaire)
* Actualisation automatique ou par refresh

---

### 🔔 **Notifications**

* Icône de notifications dans la navbar
* Marquer comme lues / non lues
* Notifications reçues :

  * Lorsqu’un utilisateur suivi publie un nouveau post

---

### 🚨 **Reporting / Signalement**

* Bouton “Signaler”
* Modal demandant le motif
* Confirmation avant envoi
* Envoi au backend → visible par les admins uniquement

---

### 🛡️ **Admin Dashboard**

* Liste des utilisateurs
* Liste des posts (avec suppression possible)
* Liste des signalements
* Actions :

  * Bannir un utilisateur
  * Supprimer un post
  * Supprimer un signalement

UI simple, claire, avec accès rapide aux informations clés.

---

## **Contraintes Techniques**

* Authentification obligatoire : **JWT + Spring Security**
* Code source clair, propre, sans générateurs automatiques (pas de JHipster)
* UI responsive (mobile/desktop)
* Stockage média sécurisé
* Routes protégées selon rôle

Le projet doit inclure un **README détaillé** contenant :

* Instructions d’installation (backend + frontend)
* Technologies utilisées
* Configuration du `.env` ou `application.properties`
* Schéma de base de données (recommandé)

## **Ressources Utiles**

* Spring Boot Docs
* Angular Docs
* Angular Material UI
* Auth0 – JWT Guide
* Java Guides – Spring Security
* Baeldung – JPA
* PostgreSQL Tutorial
