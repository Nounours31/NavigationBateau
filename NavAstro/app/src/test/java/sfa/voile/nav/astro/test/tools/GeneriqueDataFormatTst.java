/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sfa.voile.nav.astro.test.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sfa.voile.nav.astro.modele.AngleParser;
import sfa.voile.nav.astro.modele.GeneriqueDataFormat;
import sfa.voile.nav.astro.modele.HeureParser;

/**
 *
 * @author pfs
 */
public class GeneriqueDataFormatTst {
    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
    }

    @BeforeEach
    public void setupBefore() {
        System.out.println("setupBefore");
    }

    
    @AfterAll
    static public void cleanAfter() {
        System.out.println("cleanAfter");
    }

    @AfterEach
    public void cleanAfterClass() {
        System.out.println("cleanAfterClass");
    }

    @Test
    public void test1() {
    	GeneriqueDataFormat d = new GeneriqueDataFormat(1, "\\d*", "1235");
    	boolean rc = d.isMatch("1254");
    	assertTrue(rc);
    	rc = d.isMatch("1254.2");
    	assertFalse(rc);
    }

    @Test
    public void test2() {
    	GeneriqueDataFormat d = new GeneriqueDataFormat(1, "\\d*\\.\\d*", "1235.23");
    	boolean rc = d.isMatch("1254");
    	assertFalse(rc);
    	rc = d.isMatch("1254.2");
    	assertTrue(rc);
    }

    @Test
    public void test3() {
    	GeneriqueDataFormat d = new GeneriqueDataFormat(1, "\\d*\\.\\d*", "1235.23");
    	int i = d.getIndice();
    	assertEquals(i, 1);

    	String s = d.toString();
    	assertEquals(s, "Format:[1]>\\d*\\.\\d*< ex1235.23");
    }
}
