package sfa.nav.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfa.nav.lib.model.Latitude;
import sfa.nav.lib.model.Longitude;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MenuDistanceCap {
    private final static Logger logger = LoggerFactory.getLogger(MenuDistanceCap.class);

    public enum eDistanceCapType {
        loxo, ortho;
    }
    public void menu() {
            boolean fin = false;
            while (!fin) {
                logger.debug("Start top loop ...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("\t\t0. Sortie");
                System.out.println("\t\t1. Loxodromie");
                System.out.println("\t\t2. Orthodromie");
                System.out.println("\t\t3. Sortie");

                Scanner sc = new Scanner(System.in);
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
    }

    private void CaculCapDirection(eDistanceCapType eDistanceCapType) {
        try {
            System.out.println("Position de depart:");
            System.out.print("\tlat: ");

            Scanner sc = new Scanner(System.in, Tools.appCharSet);
            String slat = sc.nextLine();
            logger.debug("lat {}, ->{}<-", slat, slat.getBytes(StandardCharsets.ISO_8859_1));

            Latitude lat = Latitude.fromString(slat);
            logger.debug("lat {}", lat.toString());

            System.out.print("\n\tsLong: ");
            String sLongitude = sc.nextLine();
            logger.debug("sLong {}", sLongitude);
            Longitude L = Longitude.fromString(sLongitude);
            logger.debug("sLong {}", L.toString());
        }
        catch (Exception e) {
            logger.error("Exception ... ", e);
        }
    }
}
