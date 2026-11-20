#!/bin/bash

read -rp "Nom du projet: " projet

if [ -z "$projet" ]; then
    echo "Le nom du projet ne peut pas etre vide." >&2
    exit 1
fi

mkdir "$projet"
cd "$projet" || exit 1

go mod init "sfa.bzh/$projet"
# --------------------------------------------------
# Point d'entrée (assemblage des dépendances, démarrage)
# --------------------------------------------------
mkdir -p cmd/api
touch cmd/api/main.go
touch cmd/api/loger.yaml


# --------------------------------------------------
# Code privé à l'application (inaccessible de l'extérieur)
# --------------------------------------------------
mkdir -p internal

# ----------------
# Couche de communication (HTTP, gRPC, WebSockets)
# ----------------
# Contrôleurs (parsing JSON, appels aux services)
mkdir -p internal/transport/http/handler

# Auth, Logging, Rate limiting, CORS
mkdir -p internal/transport/http/middleware
 
# Définition des routes
touch internal/transport/http/router.go

# ----------------
# Implémentations d'accès aux données (PostgreSQL, Redis)
# ----------------
mkdir -p internal/repository

# ----------------
# ==> Logique métier (Use Cases)
# ----------------
mkdir -p internal/service

# ----------------
# Entités métier pures et interfaces (Repository, Service)
# ----------------
mkdir -p internal/domain

# ----------------
# ==> Chargement des variables d'environnement et configs
# ----------------
mkdir -p internal/config

# --------------------------------------------------
# Bibliothèques utilitaires réutilisables (Logger, Validator)
# --------------------------------------------------
mkdir -p pkg

# --------------------------------------------------
# Scripts SQL de migration de base de données
# --------------------------------------------------
mkdir -p migrations

# --------------------------------------------------
# --------------------------------------------------
touch Dockerfile
touch go.mod

# --------------------------------------------------
# Commandes rapides (build, test, lint, migrate, odt)
# --------------------------------------------------
touch Makefile

