package sfa.nav.maree.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.HandlerOnCapDistance;
import sfa.nav.lib.tools.NavException;
import sfa.nav.lib.tools.SensRouteFondParQuart;
import sfa.nav.model.Angle;
import sfa.nav.model.Cap;
import sfa.nav.model.Distance;
import sfa.nav.model.PointGeographique;


/*
================================================================================
http://ressources.univ-lemans.fr/AccesLibre/UM/Pedago/physique/02/divers/ortholoxo.html


L'orthodromie (du grec orthos : droit et dromos : course) désigne le chemin le plus court entre deux points d'une sphère, c'est-à-dire le plus petit des deux arcs 
du grand cercle passant par ces deux points. La route orthodromique entre deux points A et B du globe terrestre correspond à la route la plus courte entre eux.

Calcul de la distance orthodromique :
Pour calculer cette distance, on utilise la formule des cosinus de la trigonométrie sphérique.
Soient la et lb les latitudes de A et B et La et Lb leurs longitudes.
Pour cela on considère le triangle ABC tel que 
	C soit le pôle nord. 
	Les cotés du triangle sont a (complémentaire de la pour A), b (complémentaire de lb pour B), et l'angle γ du triangle en C vaut LB − LA (différence des longitudes). 
	
	L'arc AB vaut :

c = arccos[sin(la).sin(lb) + cos(la).cos(lb).cos(LB − LB)]

Si on exprime c en radian, la distance entre A et B est dAB = R.c km (R = 6371 km est le rayon terrestre moyen).
Un mille nautique correspondant à une minute de grand cercle, la distance entre A et B est dAB = 60.c milles nautiques si on exprime c en degrés.
Pour suivre rigoureusement la route orthodromique (tracée en vert), il faut modifier le cap en permanence. En fait, on calcule une série de caps moyens qui sont suivi pendant un certain temps.
Sur un planisphère la route orthodromique décrit une courbe complexe fonction du type de la projection utilisée. Dans le programme elle est tracée point par point.

La loxodromie (du grec loxos : oblique et dromos : course) désigne le chemin à cap constant entre deux points d'une sphère.
Sur un planisphère c'est une droite qui coupe les méridiens avec un angle constant.
Soient lx et Lx les coordonnées d'un point de la trajectoire. On a : Lx = a.lx + b avec a = (Lb − La) / (lb − la) et b = Lb − a.lb.
L'angle entre la trajectoire et les méridiens est donc α avec a = tan(α).
La route loxodromique perd tout intérêt au voisinage des pôles car elle devient alors une spirale qui s'enroule autour du pôle.
Calcul de la distance loxodromique :
On considère un point C de la trajectoire (L et l), le point voisin D (L + dL, l + dl) et le point E situé sur le même méridien que C (L, l + dl). Soit α l'angle ECD. On a ED = dl.tan(α) = cos(l).dL et dl / cos(l) = dL / tan(α).
Par hypothèse l'angle α est constant.
Par intégration, on tire : ln|tan(l/2 + π/4)| = L / tan(α) + Constante
Finalement, on obtient :


Si A et B sont sur un même parallèle (la = lb) cette relation conduit à une forme indéterminée 0 / 0. La distance est égale à la longueur de l'arc du parallèle soit Arc(AB) = (Lb − La) / cos(la).
Pour des points assez voisins, on peut utiliser la formule approchée :
Arc(AB) = (Lb − La) / cos(α) avec tan(α) = (lb − la). cos[(la + lb) /2] / (Lb − La)
Si on exprime l'arc en radian, la distance entre A et B est DAB = R.Arc(AB) km (R = 6371 km est le rayon terrestre moyen).
La valeur en milles nautiques est égale à la longueur de l'arc exprimée en minutes.
Remarque :
Cette formule n'est valable que si la différence des longitudes est inférieure à 180°. Dans le cas contraire, la trajectoire loxodromique est constituée de deux segments de droite qui joignent les points A et B aux points C (latitude lc et longitude 180°) et D (latitude lc et longitude −180°).




================================================================================
https://www.nauticalalmanac.it/fr/orthodromie-loxodromie-calculs
formules utilisées dans les calculs
e (Hayford) 	e = 0,081991890…
φ c 	φ c = (7915,7 * log tan(45°+ φ /2))-(3437,7 *e*e*sin φ)
Route Vraire 	Rv = = Δ Länge (λb-λa) / Δφc (φcb-φca)
Distance Loxodromique 	

Rv < 45°      dist. = Δφ (φb-φa)  * 1/cos Rv

45° < Rv  < 87°     dist. =  Δφ (φb-φa)  * tan Rv * 1/sin Rv

Rv >87°     dist. = Δ Longitude (λb-λa) * cos ((φa+φb)/2) * 1/sin Rv

Distance Orthodromique 	cos dist. = (sin φa * sin φb) + (cos φa * cos φb * cos Δ Longitude (λb-λa))
Angle de route au depart (Ard) 	ctg (Ard) * 1/cos φa  = 1/sin Δ Longitude * tan φb - (ctg Δ Longitude * tan φa)



================================================================================
https://fr.wikipedia.org/wiki/Orthodromie


================================================================================
https://coordinates-converter.com/fr/decimal/48.850000,2.350000?karte=OpenStreetMap&zoom=8


================================================================================
http://serge.mehl.free.fr/anx/loxodromie.html

 */

public class CalculsMaree {
	private static Logger logger = LoggerFactory.getLogger(CalculsMaree.class);

	public CalculsMaree() {
	}

}
