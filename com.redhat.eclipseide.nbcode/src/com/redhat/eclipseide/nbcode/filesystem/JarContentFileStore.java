package com.redhat.eclipseide.nbcode.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.filesystem.provider.FileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;

public class JarContentFileStore extends FileStore {

	final URI jarUri;
	final Path pathInJar;
	final File jarFile;
	private FileInfo fileInfo;

	public JarContentFileStore(URI jarUri, Path pathInJar) {
		this.jarUri = jarUri;
		this.jarFile = new File(jarUri);
		this.pathInJar = pathInJar;
	}

	@Override
	public String[] childNames(int options, IProgressMonitor monitor) throws CoreException {
		try (JarInputStream jar = openJar()) {
			var res = new ArrayList<>();
			JarEntry entry = null;
			while ((entry = jar.getNextJarEntry()) != null) {
				Path entryPath = Path.of('/' + entry.getName());
				if (Objects.equals(entryPath.getParent(), pathInJar)) {
					res.add(new JarContentFileStore(jarUri, entryPath));
				}
			}
			return res.toArray(String[]::new);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public IFileInfo fetchInfo(int options, IProgressMonitor monitor) throws CoreException {
		if (this.fileInfo == null || this.fileInfo.getLastModified() < jarFile.lastModified()) {
			this.fileInfo = new FileInfo(pathInJar.getFileName().toString());
			fileInfo.setLastModified(jarFile.lastModified());
			try (var jar = openJar()) {
				JarEntry entry = null;
				while ((entry = jar.getNextJarEntry()) != null) {
					Path entryPath = Path.of('/' + entry.getName());
					if (entryPath.equals(pathInJar)) {
						fileInfo.setExists(true);
						if (entry.getSize() > 0) {
							fileInfo.setLength(entry.getSize());
						}
					} else if (entryPath.startsWith(pathInJar)) {
						fileInfo.setDirectory(true);
						fileInfo.setExists(true);
					}
				}
			} catch (IOException ex) {
				throw new CoreException(Status.error(ex.getMessage(), ex));
			}
		}
		return this.fileInfo;
	}

	@Override
	public IFileStore getChild(String name) {
		return new JarContentFileStore(jarUri, pathInJar.resolve(name));
	}

	@Override
	public String getName() {
		return pathInJar.getFileName().toString();
	}

	@Override
	public IFileStore getParent() {
		Path parent = pathInJar.getParent();
		if (!Objects.equals(parent, pathInJar.getRoot())) {
			return new JarContentFileStore(jarUri, parent);
		}
		return null;
	}

	@Override
	public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException {
		try {
			JarInputStream jar = openJar();
			JarEntry entry = null;
			while ((entry = jar.getNextJarEntry()) != null) {
				Path entryPath = Path.of('/' + entry.getName());
				if (Objects.equals(entryPath, pathInJar)) {
					return jar;
				}
			}
			jar.close(); // close only if no entry found
		} catch (IOException ex) {
			throw new CoreException(Status.error(ex.getMessage(), ex));
		}
		return null;
	}

	private JarInputStream openJar() throws IOException {
		return new JarInputStream(new FileInputStream(new File(jarUri)));
	}

	@Override
	public URI toURI() {
		return URI.create("jar:" + jarUri + '!' + pathInJar);
	}

}
