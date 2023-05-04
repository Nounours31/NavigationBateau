package sfa.nav.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.File;

@Command(
		name = "myCli",
		mixinStandardHelpOptions = true,
		version = "1.0.0",
		description = "Utilitaires de navigation"
)
public class myCli implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(myCli.class);


	@Option(names = {"-a", "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...")
	private String algorithm = "SHA-256";

	@Option(names = "-c", description = "create a new archive")
	boolean create;

	@Option(names = { "-f", "--file" }, paramLabel = "ARCHIVE", description = "the archive file")
	File archive;

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "display a help message")
	private boolean helpRequested = false;

	@Parameters(paramLabel = "FILE", description = "one or more files to archive")
	File[] files;

	@Parameters(index = "0", description = "The file whose checksum to calculate.")
	private File file;

	public static void main(String[] args) {
		System.out.println("myCli");
		CommandLine.run(new myCli(), args);
	}

	@Override
	public void run() {
		System.out.println("Hello World!");
		String str = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Hello, colored world!|@");
		System.out.println("--" + str);
	}
}
