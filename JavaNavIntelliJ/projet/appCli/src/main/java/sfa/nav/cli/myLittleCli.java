package sfa.nav.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.SortedMap;

public class myLittleCli {
    private static Logger logger = LoggerFactory.getLogger(myLittleCli.class);

    public static void main(String[] args) {
        logger.debug("Start nav tools");
        Tools.testCharSet();

        new MenuLevel0().menu();
        logger.debug("Bye ...");
    }


}
