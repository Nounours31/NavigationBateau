/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.astro.toolsUI.poubelle;

/**
 *
 * @author pierr
 */
public class CalculsAstroSoleil {
/*
    private final static Logger _logger = LoggerFactory.getLogger(CalculsAstroSoleil.class);
    private final int PAR_INTERVAL = 1;
    private final int PAR_GRADIANT = 2;

    private CalculsAstroSoleil() {
    }

    public enum eArgDeclinaisonSoleil {

        public String getDisplay() {
            return _display;
        }

        final private String _display;

        private eArgDeclinaisonSoleil(String s) {
            this._display = s;
        }
    }

    private Hashtable<eArgDeclinaisonSoleil, Object> readStdInForDeclinaisonSolaire(eChoixMethodeSoleil iChoix) {
        Hashtable<eArgDeclinaisonSoleil, Object> retour = new Hashtable<eArgDeclinaisonSoleil, Object>();
        UtilitairesUI _toolsUI = new UtilitairesUI();

        System.out.println(eArgDeclinaisonSoleil.HeureMeusure.getDisplay());
        double x = _toolsUI.readHeure();
        Heure H = new Heure(x);
        retour.put(eArgDeclinaisonSoleil.HeureMeusure, H);

        switch (iChoix) {
            case ParInterval:
                retour.put(eArgDeclinaisonSoleil.Type, PAR_INTERVAL);

                System.out.println(eArgDeclinaisonSoleil.HeureUT1.getDisplay());
                x = _toolsUI.readHeure();
                H = new Heure(x);
                retour.put(eArgDeclinaisonSoleil.HeureUT1, H);

                System.out.println(eArgDeclinaisonSoleil.Declinaison1.getDisplay());
                Declinaison D = new Declinaison (_toolsUI.readLatitude());
                retour.put(eArgDeclinaisonSoleil.Declinaison1, D);

                System.out.println(eArgDeclinaisonSoleil.HeureUT2.getDisplay());
                x = _toolsUI.readHeure();
                H = new Heure(x);
                retour.put(eArgDeclinaisonSoleil.HeureUT2, H);

                System.out.println(eArgDeclinaisonSoleil.Declinaison2.getDisplay());
                D = new Declinaison (_toolsUI.readLatitude());
                retour.put(eArgDeclinaisonSoleil.Declinaison2, D);
                break;

            case PartGradiant:
                retour.put(eArgDeclinaisonSoleil.Type, PAR_GRADIANT);

                System.out.println(eArgDeclinaisonSoleil.HeureUTRef.getDisplay());
                x = _toolsUI.readHeure();
                H = new Heure(x);
                retour.put(eArgDeclinaisonSoleil.HeureUTRef, H);

                System.out.println(eArgDeclinaisonSoleil.DeclinaisonRef.getDisplay());
                Latitude L = _toolsUI.readLatitude();
                retour.put(eArgDeclinaisonSoleil.DeclinaisonRef, L);

                System.out.println(eArgDeclinaisonSoleil.PasDeDeclinaison.getDisplay());
                Angle A = new Angle (_toolsUI.readDegreMinuteSeconde());
                retour.put(eArgDeclinaisonSoleil.PasDeDeclinaison, A);

                break;

            case TheEnd:
                return retour;
            default:
                _logger.error("cas inconnu");
                return retour;
        }
        return retour;
    }

    private Declinaison calculDeclinaison(eChoixMethodeSoleil iChoix, Hashtable<eArgDeclinaisonSoleil, Object> methodeArgs) {
        Declinaison D = new Declinaison();
        switch (iChoix) {
            case ParInterval:
               _logger.debug("Calcul declinaison par interval");
                Heure H1 = (Heure) methodeArgs.get(eArgDeclinaisonSoleil.HeureUT1);
                Heure H2 = (Heure) methodeArgs.get(eArgDeclinaisonSoleil.HeureUT2);
                Heure H = (Heure) methodeArgs.get(eArgDeclinaisonSoleil.HeureMeusure);

                if ((H.apres (H2)) || H.avant(H1)){
                    _logger.error("Heure de meusure ou interval incorrect: on devrai avoir Hd < H < Hf [et on a: {} < {} < {}]", H1.toString(), H.toString(), H2.toString());
                    return D;
                }
              
                double taux = H.moins(H1) / H2.moins(H1);
                
                Declinaison D1 = new Declinaison ((Latitude) methodeArgs.get(eArgDeclinaisonSoleil.Declinaison1));
                Declinaison D2 = new Declinaison ((Latitude) methodeArgs.get(eArgDeclinaisonSoleil.Declinaison2));

                double declinaison1AsDouble = D1.getLatitude() * ((D1.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
                double declinaison2AsDouble = D2.getLatitude() * ((D2.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
                
                double newDeclinaison = declinaison1AsDouble + taux * (declinaison2AsDouble - declinaison1AsDouble);
                D.setLatitude(Math.abs(newDeclinaison));
                D.setSens((newDeclinaison < 0.0) ? SensLatitude.Sud : SensLatitude.Nord);

               _logger.debug("H1 {}", H1);
               _logger.debug("H2 {}", H2);
               _logger.debug("H  {}", H);
               _logger.debug("Taux: {}", taux);
               _logger.debug("Declinaison D1 {}", D1);
               _logger.debug("Declinaison D2 {}", D2);
               _logger.debug("Declinaison D {} == {}", D, newDeclinaison);

                break;
                
            case PartGradiant:
               _logger.debug("Calcul declinaison par gradiant");
                H1 = (Heure) methodeArgs.get(eArgDeclinaisonSoleil.HeureUTRef);
                H = (Heure) methodeArgs.get(eArgDeclinaisonSoleil.HeureMeusure);
                D1 = new Declinaison ((Latitude) methodeArgs.get(eArgDeclinaisonSoleil.DeclinaisonRef));
                Angle tauxDeVariation = (Angle) methodeArgs.get(eArgDeclinaisonSoleil.PasDeDeclinaison);
              
                double NbHeure = H.moins(H1);
                
                declinaison1AsDouble = D1.getLatitude() * ((D1.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
                
                newDeclinaison = Math.abs(declinaison1AsDouble) + tauxDeVariation.multiply(NbHeure);
                D.setLatitude(Math.abs(newDeclinaison));
                D.setSens((newDeclinaison < 0.0) ? SensLatitude.inverse(D1.getSens()) : D1.getSens());
               _logger.debug("H  {}", H);
               _logger.debug("H1 {}", H1);
               _logger.debug("Declinaison D1 {}", D1);
               _logger.debug("tauxDeVariation  {} / jour", tauxDeVariation);
               _logger.debug("NbHeure: {}", NbHeure);
               _logger.debug("Declinaison D {} == {}", D, newDeclinaison);
                break;

            case TheEnd:
                return null;
            default:
                _logger.error("cas inconnu");
                return null;
        }
        return D;
    }

    public static void DeclinaisonSolaire() {
        _logger.debug("Calcul declinaison solaire ");

        MenuPrincipal _menu = new MenuPrincipal();
        MenuPrincipal.eChoixMethodeSoleil iChoix = _menu.choixMethodeSoleil();

        CalculsAstroSoleil x = new CalculsAstroSoleil();
        Hashtable<eArgDeclinaisonSoleil, Object> methodeArgs = x.readStdInForDeclinaisonSolaire(iChoix);
        Declinaison D = x.calculDeclinaison(iChoix, methodeArgs);

        _logger.debug("Declinaison D:" + D.toString());
    }*/

}
