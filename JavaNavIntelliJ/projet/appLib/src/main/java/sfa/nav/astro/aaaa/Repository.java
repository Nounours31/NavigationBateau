package sfa.voile.nav.astro.aaaa;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class Repository {
	private static File _Repo = null; 
	
	public static synchronized File getRepository() {
		if (_Repo == null) {
			Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
		    FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
			try {
				Path Repo = Files.createTempDirectory(java.util.UUID.randomUUID().toString(), attr);
				_Repo = Repo.toFile();
			} 
			catch (Exception e) {
				try {
					File pipo = File.createTempFile(java.util.UUID.randomUUID().toString(), java.util.UUID.randomUUID().toString());
		            _Repo = pipo.getParentFile();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return _Repo;
	}
}
