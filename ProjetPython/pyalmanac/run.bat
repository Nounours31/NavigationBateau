set type=install

if "%type%" == "install" (
 echo install
 python -m pip uninstall pyephem ephem pyalmanac
 python -m pip install pyalmanac
)

"python -m pyalmanac" 
