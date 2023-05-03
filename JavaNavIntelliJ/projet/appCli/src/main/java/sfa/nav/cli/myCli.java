package sfa.nav.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "myCli")
public class myCli implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(myCli.class);

	public static void main(String[] args) {
		System.out.println("myCli");
		CommandLine.run(new myCli(), args);
	}

	@Override
	public void run() {
		System.out.println("Hello World!");
		String str = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Hello, colored world!|@");
		System.out.println(str);
	}
}
