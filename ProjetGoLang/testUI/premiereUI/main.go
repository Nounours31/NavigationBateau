package main

import (
	_ "embed"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"net/http"
	"os/exec"
	"runtime"
)

//go:embed index.html
var pageHTML string

func main() {
	// Sert la page HTML complète
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		fmt.Fprint(w, pageHTML)
	})

	// Point d'entrée unique qui reçoit les formulaires de tous vos sous-menus
	http.HandleFunc("/api/calcul", func(w http.ResponseWriter, r *http.Request) {
		var donnees map[string]any
		if err := json.NewDecoder(r.Body).Decode(&donnees); err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		// Affiche dans le terminal les valeurs reçues depuis le formulaire
		fmt.Printf("Calcul demandé [%v] : %+v\n", donnees["type"], donnees)

		// Exemple de réponse renvoyée à la page HTML (clés = IDs des résultats)
		reponses := map[string]string{
			"res-dl-dist":   "42.50 NM (Test Go OK)",
			"res-cs-rf":     "087.5 °",
			"res-m-marnage": "5.25 m",
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(reponses)
	})

	listener, err := net.Listen("tcp", "127.0.0.1:0")
	if err != nil {
		log.Fatal(err)
	}
	url := fmt.Sprintf("http://%s", listener.Addr().String())
	fmt.Println("Interface ouverte sur :", url)
	ouvrirNavigateur(url)
	log.Fatal(http.Serve(listener, nil))
}

func ouvrirNavigateur(url string) {
	switch runtime.GOOS {
	case "windows":
		exec.Command("rundll32", "url.dll,FileProtocolHandler", url).Start()
	case "linux":
		exec.Command("xdg-open", url).Start()
	case "darwin":
		exec.Command("open", url).Start()
	}
}
