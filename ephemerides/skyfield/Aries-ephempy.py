# depuis stack over flow
# https://stackoverflow.com/questions/13664935/is-this-how-to-compute-greenwich-hour-angle-with-pyephem-under-python-3
#
# I have been using python3 and pyephem to study celestial navigation mostly working through some calculations 
# that would have to be done by hand in any case.
# For the sight reduction part I am trying to have pyephem output data comparable to the nautical almanac, 
# mostly greenwich hour angle and declination. 
# To get the hour angle of the first point of aries, I tried adding a body using ephem.readdb at 0.0 RA and 0.0 dec. 
# But after reading the doc further I think this is working.


import math , ephem
#  zero longitude
gmt_long = '0:0:0'          #  deg, min, sec
myloc_date = ( '2012/12/02 22:00:00' )

# observer for greenwich  gst
utcz = ephem.Observer()
utcz.date = myloc_date
utcz.long = gmt_long

print ( utcz.date )
print (" gst hours", utcz.sidereal_time() )
print (" gst   deg", ephem.degrees( utcz.sidereal_time() ) )