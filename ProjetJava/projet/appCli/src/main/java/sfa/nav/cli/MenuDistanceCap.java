package sfa.nav.cli;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;

public class MenuDistanceCap {
    private final static Logger logger = LoggerFactory.getLogger(MenuDistanceCap.class);

    public enum eDistanceCapType {
        loxo, ortho;
    }
    public void menu() {
            boolean fin = false;
            Scanner sc = null;
            while (!fin) {
                logger.debug("Start top loop ...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("\t\t0. Sortie");
                System.out.println("\t\t1. Loxodromie");
                System.out.println("\t\t2. Orthodromie");
                System.out.println("\t\t3. Sortie");

                sc = new Scanner(System.in);
                String input = sc.nextLine();
                switch (input) {
                    case "0":  logger.debug("Exit");
                        fin = true;
                        break;
                    case "1":  logger.debug("Loxo");
                        this.CaculCapDirection (eDistanceCapType.loxo);
                        break;
                    case "2":  logger.debug("Ortho");
                        this.CaculCapDirection (eDistanceCapType.ortho);
                        break;
                    case "3":  logger.debug("Exit");
                        fin = true;
                        break;
                    default: System.err.println("Cas inconnu");
                }
                if (fin)
                    break;
            }
            sc.close();
    }

    private void CaculCapDirection(eDistanceCapType eDistanceCapType) {
        try {
            System.out.println("Position de depart:");
            System.out.print("\tlat: ");

            Scanner sc = new Scanner(System.in, Tools.appCharSet);
            String slat = sc.nextLine();
            logger.debug("lat {}, ->{}<-", slat, slat.getBytes(StandardCharsets.ISO_8859_1));

            Latitude lat = LatitudeFactory.fromString(slat);
            logger.debug("lat {}", lat.toString());

            System.out.print("\n\tsLong: ");
            String sLongitude = sc.nextLine();
            logger.debug("sLong {}", sLongitude);
            Longitude L = LongitudeFactory.fromString(sLongitude);
            logger.debug("sLong {}", L.toString());
            sc.close();
        }
        catch (Exception e) {
            logger.error("Exception ... ", e);
        }
    }
}
