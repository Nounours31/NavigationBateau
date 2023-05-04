package sfa.nav.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MenuLevel0 {
    private final static Logger logger = LoggerFactory.getLogger(MenuLevel0.class);
    public MenuLevel0() {
    }

    public void menu() {
        boolean fin = false;
        while (!fin) {
            logger.debug("Start top loop ...");
            try {
                System.out.println("-------------------------------------------------------------");
                System.out.println("\t0. Sortie");
                System.out.println("\t1. Distance et cap entre 2 points");
                System.out.println("\t2. Sortie");

                Scanner sc = new Scanner(System.in, Tools.appCharSet);
                String input = sc.nextLine();
                    switch (input) {
                        case "0":  logger.debug("Exit");
                            fin = true;
                            break;
                        case "1":  logger.debug("Distance/Cap 2 points");
                            new MenuDistanceCap().menu();
                            break;
                        default: System.err.println("Cas inconnu");
                    }
                    if (fin)
                        break;

            }
            catch (Exception e) {
            }
        }
    }
}
