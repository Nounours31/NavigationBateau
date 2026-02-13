. ./tck_profile.sh
ruff format
ruff check

pip install sfa_navigation -e .
pytest
./coverage.sh

