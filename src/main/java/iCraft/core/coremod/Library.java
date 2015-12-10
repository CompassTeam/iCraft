package iCraft.core.coremod;

import cpw.mods.fml.common.versioning.ComparableVersion;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.mutable.MutableObject;

public class Library
{
	public static void init(File mcDir, String mcVersion)
	{
		File destination = new File(new File(mcDir, "mods"), "iCraft");
		if (!destination.exists())
		{
			destination.mkdir();
		}
		if (!destination.exists() || !destination.isDirectory())
		{
			throw new RuntimeException("can't create mods/iCraft dir");
		}
		try {
			extractFiles(mcDir, mcVersion, destination);
		} catch (Exception e) {
			throw new RuntimeException("library/mod extraction failed", e);
		}
		try {
			loadFiles(destination);
		} catch (Exception e) {
			throw new RuntimeException("library loading failed", e);
		}
	}

	private static void extractFiles(File mcDir, String mcVersion, File destination) throws IOException, URISyntaxException
	{
		URL location = Library.class.getProtectionDomain().getCodeSource().getLocation();
		String protocol = location.getProtocol();
		Set<String> validLibFiles = new HashSet<String>();
		if (protocol.equals("file"))
		{
			File source = new File(location.toURI());
			for (int i = Library.class.getPackage().getName().replaceAll("[^\\.]", "").length() + 1; i >= 0; i--)
			{
				source = source.getParentFile();
			}
			File[] files = new File(source, "lib").listFiles();
			if (files == null)
			{
				ICraftCoreMod.log.warn("The iCraft/lib directory doesn't exist.");
			}
			else
			{
				for (File srcFile : files)
				{
					File dstFile = new File(destination, srcFile.getName());
					if (!dstFile.exists() || dstFile.length() != srcFile.length())
					{
						FileUtils.copyFile(srcFile, dstFile);

						ICraftCoreMod.log.info("Extracted library " + srcFile.getName() + ".");
					}
					validLibFiles.add(srcFile.getName());
				}
			}
			files = new File(source, "mod").listFiles();
			if (files == null)
			{
				ICraftCoreMod.log.warn("The iCraft/mod directory doesn't exist.");
			}
			else
			{
				for (File srcFile : files)
				{
					File dstFile = prepareModExtraction(mcDir, mcVersion, srcFile.getName());
					if (dstFile != null)
					{
						FileUtils.copyFile(srcFile, dstFile);

						ICraftCoreMod.log.info("Extracted mod " + srcFile.getName() + ".");
					}
					validLibFiles.add(srcFile.getName());
				}
			}
		}
		else
		{
			JarFile source;
			Enumeration<JarEntry> e;
			if (protocol.equals("jar"))
			{
				source = ((JarURLConnection) location.openConnection()).getJarFile();
				for (e = source.entries(); e.hasMoreElements();)
				{
					JarEntry entry = e.nextElement();
					String name = entry.getName();
					if (!entry.isDirectory())
					{
						String path = FilenameUtils.getPathNoEndSeparator(name);
						if (path.equals("lib"))
						{
							String fileName = FilenameUtils.getName(name);
							File dstFile = new File(destination, fileName);
							if (!dstFile.exists() || dstFile.length() != entry.getSize())
							{
								FileUtils.copyInputStreamToFile(source.getInputStream(entry), dstFile);

								ICraftCoreMod.log.info("Extracted library " + fileName + ".");
							}
							validLibFiles.add(fileName);
						}
						else if (path.equals("mod"))
						{
							String fileName = FilenameUtils.getName(name);
							File dstFile = prepareModExtraction(mcDir, mcVersion, fileName);
							if (dstFile != null)
							{
								FileUtils.copyInputStreamToFile(source.getInputStream(entry), dstFile);

								ICraftCoreMod.log.info("Extracted mod " + fileName + ".");
							}
						}
					}
				}
			}
			else
			{
				throw new RuntimeException("invalid protocol (" + location + ").");
			}
		}
		for (File file : destination.listFiles())
		{
			if (!validLibFiles.contains(file.getName()))
			{
				if (file.delete())
				{
					ICraftCoreMod.log.info("Removed old library " + file.getName() + ".");
				}
				else
				{
					ICraftCoreMod.log.warn("Can't remove old library " + file.getName() + ".");
				}
			}
		}
	}

	private static void loadFiles(File dir) throws MalformedURLException
	{
		LaunchClassLoader classLoader = (LaunchClassLoader) Library.class.getClassLoader();

		File[] files = dir.listFiles();
		if (files == null)
		{
			ICraftCoreMod.log.warn("The directory " + dir + " doesn't exist, can't load libraries.");
		}
		else
		{
			for (File file : files)
			{
				classLoader.addURL(file.toURI().toURL());

				ICraftCoreMod.log.info("Loaded library " + file.getName() + ".");
			}
		}
	}

	private static String[] splitVersion(String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (!Character.isLetter(c))
			{
				String[] ret = new String[2];
				ret[0] = str.substring(0, i);
				ret[1] = (Character.isDigit(c) ? str.substring(i) : str.substring(i + 1));

				return ret;
			}
		}
		return null;
	}

	private static File prepareModExtraction(File mcDir, String mcVersion, String name)
	{
		String[] nameParts = splitVersion(FilenameUtils.getBaseName(name));
		if (nameParts == null)
		{
			throw new RuntimeException("invalid bundled mod filename: " + name);
		}
		File modsDir = new File(mcDir, "mods");
		File modsVersionDir = new File(modsDir, mcVersion);
		if (!modsVersionDir.exists())
		{
			modsVersionDir.mkdir();
		}
		String prefix = nameParts[0].toLowerCase();
		ComparableVersion version = new ComparableVersion(nameParts[1]);
		MutableObject<File> oldFile = new MutableObject<File>();

		boolean inModsDir = checkDestination(modsDir, prefix, name, version, oldFile);
		boolean inModsVersionDir = checkDestination(modsVersionDir, prefix, name, version, oldFile);
		if (inModsDir || inModsVersionDir)
		{
			return null;
		}
		if (oldFile.getValue() != null)
		{
			if (oldFile.getValue().delete())
			{
				ICraftCoreMod.log.info("Removed old mod " + oldFile.getValue().getName());
			}
			else
			{
				ICraftCoreMod.log.warn("Can't remove old mod " + oldFile.getValue().getName());
			}
		}
		return new File(modsVersionDir, name);
	}

	private static boolean checkDestination(File destination, final String prefix, String name, ComparableVersion newVersion, MutableObject<File> oldFile)
	{
		boolean found = false;
		for (File dstFile : destination.listFiles(new FileFilter() {
			public boolean accept(File file)
			{
				return (!file.isDirectory()) && (file.getName().toLowerCase().startsWith(prefix));
			}
		}))
		{
			if (found)
			{
				return true;
			}
			found = true;
			if (dstFile.getName().equalsIgnoreCase(name))
			{
				return true;
			}
			String[] dstNameParts = splitVersion(FilenameUtils.getBaseName(dstFile.getName()));
			if (dstNameParts == null)
			{
				return true;
			}
			ComparableVersion dstVersion = new ComparableVersion(dstNameParts[1]);
			if (dstVersion.compareTo(newVersion) < 0)
			{
				if (oldFile.getValue() != null)
				{
					return true;
				}
				oldFile.setValue(dstFile);
			}
			else
			{
				return true;
			}
		}
		return false;
	}
}