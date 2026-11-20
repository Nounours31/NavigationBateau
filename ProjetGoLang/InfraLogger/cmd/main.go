package main

import (
	"fmt"
	"log/slog"
	"os"
	"strings"
	"time"
	"toto/pkg/logger"

	flag "github.com/spf13/pflag"
)

func main() {

	// 1. Définition des drapeaux (flags)
	// Méthode A : retourne un pointeur
	debug := flag.BoolP("debug", "d", false, "En mode debug ?")
	logbackFilePath := flag.StringP("logback", "D", "./logger.yaml", "Chemin ver le fichier de config du logger")
	port := flag.IntP("port", "p", 8080, "Port d'écoute du serveur")
	listeFichiers := flag.StringSliceP("AllConf", "a", []string{}, "Test d'une liste de nom")
	help := flag.BoolP("help", "h", false, "Help")

	// 2. Personnalisation du message d'aide (-h / --help)
	flag.Usage = func() {
		fmt.Fprintf(os.Stderr, "Usage: %s [options] <fichier1> [fichier2...]\n\nOptions:\n", os.Args[0])
		flag.PrintDefaults()
	}

	// 3. Parsing des arguments (obligatoire avant de lire les valeurs)
	flag.Parse()

	if *help {
		flag.Usage()
		os.Exit(0)
	}

	// 4. Récupération des arguments positionnels (placés après les drapeaux)
	fichiers := flag.Args()

	// Exemple de validation : rendre au moins un argument positionnel obligatoire
	if len(fichiers) == 0 {
		fmt.Fprintln(os.Stderr, "Erreur : vous devez spécifier au moins un fichier à traiter.\n")
		flag.Usage()
	}

	// 5. Utilisation des valeurs récupérées
	fmt.Println("=== Configuration ===")
	fmt.Printf("Port          : %d\n", *port)
	fmt.Printf("Mode Debug    : %t\n", *debug)
	fmt.Printf("Config        : %s\n", *logbackFilePath)
	fmt.Printf("Config (%d)  : %v\n", len(*listeFichiers), strings.Join(*listeFichiers, ", "))

	// Initialise slog avec le fichier dynamique
	_, err := logger.Init("../config/logger.yaml")
	if err != nil {
		panic(err)
	}

	// Simulation d'activité serveur
	for {
		slog.Debug("Message de debug détaillé (invisible si level=info)")
		slog.Info("Requête traitée", "user_id", 42, "status", 200)
		time.Sleep(3 * time.Second)
	}
}
