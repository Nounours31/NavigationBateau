@echo off

cd E:\home\git\NavigationBateau\NavigationBateau\Python\Python_libs\pSimulateurPourIpad
call .venv\Scripts\activate.bat

call python.exe -m pip install --upgrade pip

call pip install -e ..\pSfaTools
call pip install -e ..\pSfaNmea
call pip install -e ..\pSfaNavigation
call pip install -r requirements.txt
