package sfa.voile.nav.astro.toolsUI.poubelle;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class myIO {
	static public void printf(String s) {
		PrintStream out = new PrintStream( System.out, true, StandardCharsets.UTF_8 );
		out.print(s);
		out.close();
	}

	public static void println(String s) {
		PrintStream out = new PrintStream( System.out, true, StandardCharsets.UTF_8 );
		out.print(s);
		out.close();
	}
}
