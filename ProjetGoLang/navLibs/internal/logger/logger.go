package logger

import (
	"fmt"
	"log/slog"
	"os"
	"strings"

	"github.com/fsnotify/fsnotify"
	"gopkg.in/yaml.v3"

	_ "embed"
	"errors"
)

type Config struct {
	Level     string `yaml:"level"`
	Format    string `yaml:"format"`
	AddSource bool   `yaml:"add_source"`
}

type DynamicLogger struct {
	levelVar   *slog.LevelVar
	configPath string
}

//go:embed logger.yaml
var configFile string

func fileExists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if errors.Is(err, os.ErrNotExist) {
		return false, nil
	}
	return false, err // Autre erreur (ex: permission refusée)
}

func init() {
	// fichier exist ?
	if exist, err := fileExists("./logger.yaml"); !exist && err == nil {
		msg, _ := os.Getwd()
		fmt.Fprintf(os.Stderr, "Creation du fichier %s/%s \n", msg, "./logger.yaml")
		os.WriteFile("./logger.yaml", []byte(configFile), 0644)
	}
	Init("./logger.yaml")
}

// Init initialise le logger global slog et lance la surveillance du fichier
func Init(configPath string) (*DynamicLogger, error) {

	dl := &DynamicLogger{
		levelVar:   new(slog.LevelVar),
		configPath: configPath,
	}

	// Chargement initial
	if err := dl.reload(); err != nil {
		msg, _ := os.Getwd()
		msg = fmt.Sprintf("Current wd: %s - erreur chargement config logger: %w", msg, err)
		return nil, fmt.Errorf("%s", msg)
	}

	// Démarrage du watcher en arrière-plan
	go dl.watchConfig()

	return dl, nil
}

// reload lit le fichier YAML et met à jour le logger
func (dl *DynamicLogger) reload() error {
	data, err := os.ReadFile(dl.configPath)
	if err != nil {
		return err
	}

	var cfg Config
	if err := yaml.Unmarshal(data, &cfg); err != nil {
		return err
	}

	// 1. Mise à jour dynamique et thread-safe du niveau de log
	dl.levelVar.Set(parseLevel(cfg.Level))

	// 2. Configuration des options du handler
	opts := &slog.HandlerOptions{
		Level:     dl.levelVar, // C'est ici que la magie opère : LevelVar est lu dynamiquement
		AddSource: cfg.AddSource,
		ReplaceAttr: func(groups []string, a slog.Attr) slog.Attr {
			// Cibler uniquement le champ natif "source"
			if a.Key == slog.SourceKey {
				// Récupérer la structure *slog.Source
				if src, ok := a.Value.Any().(*slog.Source); ok {
					// Option A : Garder le nom complet (ex: "main.(*MonService).Traiter")
					// return slog.String(slog.SourceKey, src.Function)

					// Option B : Extraire uniquement la fin après le dernier point (ex: "Traiter")
					parts := strings.Split(src.Function, ".")
					return slog.String(slog.SourceKey, parts[len(parts)-1])
				}
			}
			if a.Key == slog.TimeKey {
				return slog.String(slog.TimeKey, a.Value.Time().Format("06/01/02-15:04:05.0000"))
			}
			if a.Key == "user_id" {
				return slog.Attr{} // Retourner un Attr vide le supprime du log
			}
			if a.Key == "status" {
				return slog.Attr{}
			}
			if a.Key == slog.MessageKey {
				return slog.String("msg", "["+a.Value.String()+"]")
			}
			return a
		},
	}

	// 3. Choix du format (Text ou JSON)
	var handler slog.Handler
	if strings.ToLower(cfg.Format) == "json" {
		handler = slog.NewJSONHandler(os.Stdout, opts)
	} else {
		handler = slog.NewTextHandler(os.Stdout, opts)
	}

	// Définit ce logger comme le logger par défaut de toute l'application
	slog.SetDefault(slog.New(handler))
	return nil
}

// watchConfig écoute les événements du système de fichiers (comme Logback scan="true")
func (dl *DynamicLogger) watchConfig() {
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		slog.Error("Impossible de créer le watcher de logs", "error", err)
		return
	}
	defer watcher.Close()

	if err := watcher.Add(dl.configPath); err != nil {
		slog.Error("Impossible de surveiller le fichier de config logs", "error", err)
		return
	}

	for {
		select {
		case event, ok := <-watcher.Events:
			if !ok {
				return
			}
			// Certains éditeurs (vim, nano) suppriment et recréent le fichier à la sauvegarde
			if event.Has(fsnotify.Write) || event.Has(fsnotify.Create) {
				if err := dl.reload(); err != nil {
					slog.Error("Échec du rechargement de la conf des logs", "error", err)
				} else {
					slog.Info("Configuration des logs rechargée à chaud", "nouveau_niveau", dl.levelVar.Level())
				}
			}
		case err, ok := <-watcher.Errors:
			if !ok {
				return
			}
			slog.Error("Erreur watcher logs", "error", err)
		}
	}
}

func parseLevel(lvl string) slog.Level {
	switch strings.ToLower(lvl) {
	case "debug":
		return slog.LevelDebug
	case "warn", "warning":
		return slog.LevelWarn
	case "error":
		return slog.LevelError
	default:
		return slog.LevelInfo
	}
}
