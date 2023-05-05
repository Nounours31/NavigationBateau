/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.lib.tools;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class Utiitaires {
    static public void dumpString (String s) {
        byte[] x = s.getBytes(StandardCharsets.UTF_8);
        for (byte b : x) {
            System.out.println("->"+ String.format("0x%08X", (((int)b) & 0xff)) +"<-");
        }
    }

    static public String stringUTF82Ascii (String s) {
        byte[] x = s.getBytes(StandardCharsets.UTF_8);
        return new String(x, StandardCharsets.US_ASCII);
    }
}
