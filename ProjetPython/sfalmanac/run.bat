set type=install

if "%type%" == "install" (
 echo install
 python -m pip uninstall skyfield pandas sfalmanac
 python -m pip install --upgrade skyfield pandas
 python -m pip install sfalmanac
)

"python -m sfalmanac" 
