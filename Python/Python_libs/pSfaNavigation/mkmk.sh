. ./tck_profile.sh
ruff format
ruff check

pyright

pip install pSfaNavigation -e .
pytest
./coverage.sh
