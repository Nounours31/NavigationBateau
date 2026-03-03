. ./tck_profile.sh
ruff format
ruff check

pyright

pip install sfa_navigation -e .
pytest
./coverage.sh
