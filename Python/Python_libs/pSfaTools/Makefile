# https://www.pythoniste.fr/python/ameliorer-lexploitation-de-votre-projet-python-avec-un-makefile/

# Créer un environnement virtuel
venv:
	python -m venv .venv
	cp .venv/Scripts/activate .venv/Scripts/activate.sh
	
# Installer les dépendances
install: venv
	.venv/Scripts/python.exe -m pip install --upgrade pip
	pip install -r requirements.txt
	mkdocs new .

	
# Installer les dépendances
dev-env:
	./scripts/setup_dev_env.sh

start: venv install
	@echo "start"

# tests
tests: install 
	pytest tests/

# tests
build: install 
	pyright
	pip install -e .
	
# Nettoyer le projet
clean:
	rm -rf .venv
	find . -name "*.pyc" -exec rm -f {} \;
	
help:
	@echo "dev-env                  create the dev environnement"
	@echo "build                    build stack"
	@echo "build-no-cache           build stack with --no-cache flag"
	@echo "up                       up stack"
	@echo "up-detach                up stack with --detach flag"
	@echo "up-dev                   up stack in dev mode"
	@echo "down                     down stack"
	@echo "pre-commit               make all checks to do before commit"
	@echo "tests                    make pytest"
	@echo "clean                    cleanup pyc files inside the project"


	