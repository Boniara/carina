package com.qaprosoft.carina.core.ws.utils;

import com.google.common.base.Preconditions;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils
{

	public static Path buildResourcesPath(String relativePath) {

		Path root = Paths.get("src/test/resources").toAbsolutePath();
		Preconditions.checkNotNull(root, "Unable to find root of project");

		Path path = root.resolve(relativePath);

		if (! Files.exists(path)) {
			new FileNotFoundException("Path " + relativePath + " is not exist");
		}
		return path;
	}
}
