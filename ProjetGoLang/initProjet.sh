#!/bin/bash

read -rp "Nom du projet: " projet

if [ -z "$projet" ]; then
    echo "Le nom du projet ne peut pas etre vide." >&2
    exit 1
fi

if [ -d "$projet" ]; then
    echo "Projet existe deja." >&2
    exit 1
fi

WhereCmd=whereis
${WhereCmd} > /dev/null 2>&1 
if (( $? != 0 )); then
	echo $?
	WhereCmd=where
	${WhereCmd} > /dev/null  2>&1
	if (( $? != 2)); then
		echo $?
		echo 'No Where commande'
		exit 1
	fi
fi
printf "whre cmd is: %s\n" "${WhereCmd}"
${WhereCmd} go > /dev/null 2>&1
if (( $? != 0)); then
	echo 'pas de go, je le rajoute depuis /e/bin/go/go'
	PATH=${PATH}:/e/bin/go/go:/e/bin/go/go/bin
fi


mkdir "$projet"
cd "$projet" || exit 1



go mod init "sfa.bzh/$projet"
printf "Lancez go mod tidy à la racine du projet : Go analyse vos fichiers, télécharge les packages manquants et nettoie ceux qui ne servent plus. \n"

# --------------------------------------------------
# Point d'entrée (assemblage des dépendances, démarrage)
# --------------------------------------------------
mkdir -p cmd/api
printf "\tCreate cmd/api \n"
touch cmd/api/main.go
touch cmd/api/logger.yaml
if [ -f ../main.go ]; then
	cp ../main.go cmd/api/main.go
	printf "\t\t - cp main.go \n"
fi
if [ -f ../logger.yaml ]; then
	cp ../logger.yaml cmd/api/logger.yaml
	printf "\t\t - cp logger.yaml \n"
fi

# --------------------------------------------------
# Code privé à l'application (inaccessible de l'extérieur)
# --------------------------------------------------
mkdir -p internal
printf "\tCreate internal \n"

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
printf "\tCreate pkg \n"

# --------------------------------------------------
# Scripts SQL de migration de base de données
# --------------------------------------------------
mkdir -p migrations
printf "\tCreate migrations \n"

# --------------------------------------------------
# --------------------------------------------------
touch Dockerfile
if [ -f ../Dockerfile ]; then
	cp ../Dockerfile .
	printf "cp Dockerfile \n"
fi

# --------------------------------------------------
# Commandes rapides (build, test, lint, migrate, odt)
# --------------------------------------------------
touch Makefile
if [ -f ../Makefile ]; then
	cp ../Makefile .
	printf "cp Makefile \n"
fi

