package com.redhat.eclipseide.nbcode.filesystem;

/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
import java.net.URI;
import java.nio.file.Path;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileSystem;

public class JarFileSystem extends FileSystem {

	@Override
	public IFileStore getStore(URI uri) {
		if (!"jar".equals(uri.getScheme())) {
			return null;
		}
		String[] parts = uri.getSchemeSpecificPart().split("!");
		URI jarUri = URI.create(parts[0]);
		Path pathInJar = Path.of(parts[1]);
		return new JarContentFileStore(jarUri, pathInJar);
	}

}
