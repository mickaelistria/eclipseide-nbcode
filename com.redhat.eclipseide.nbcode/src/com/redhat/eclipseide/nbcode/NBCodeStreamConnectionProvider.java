/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate.DSPLaunchDelegateLaunchBuilder;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class NBCodeStreamConnectionProvider extends ProcessStreamConnectionProvider implements IPropertyChangeListener {

	public static final String PREF_NBCODE_LOCATION = "nbcodeLocation"; //$NON-NLS-1$
	private static boolean alreadyWarned = false;
	private InputStream in;
	private OutputStream out;
	private Socket clientSocket;
	private int lsPort;
	public static int debugPort;

	public NBCodeStreamConnectionProvider() {
		super(List.of("path to nbcode not set!"));
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	@Override
	protected ProcessBuilder createProcessBuilder() {
		try (ServerSocket server = new ServerSocket(0); ServerSocket server2 = new ServerSocket(0)) {
			this.lsPort = server.getLocalPort();
			this.debugPort = server2.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
			this.lsPort = 9123;
		}
		String nbcodeLocation = Activator.getDefault().getPreferenceStore().getString(PREF_NBCODE_LOCATION);
		if (nbcodeLocation != null && !nbcodeLocation.isEmpty()) {
			this.setCommands(List.of(nbcodeLocation, "-J" + "--enable-preview", "--start-java-language-server=listen:" + lsPort, "--start-java-debug-adapter-server=listen:" + debugPort));
		} else {
			if (!alreadyWarned) {
				alreadyWarned = true;
				Display.getDefault().asyncExec(() -> {
					PreferencesUtil.createPreferenceDialogOn(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), NBCodePreferencePage.PAGE_ID, null, null).open();
				});
			}
		}
		return super.createProcessBuilder(); 
	}

	@Override
	public void start() throws IOException {
		try {
			super.start();
			Thread.sleep(3000); // give some time for LS to start
			this.clientSocket = new Socket(InetAddress.getLocalHost(), lsPort);
			this.in = this.clientSocket.getInputStream();
			this.out = this.clientSocket.getOutputStream();
		} catch (Exception e) {
			// most likely process hasn't started well: wrong path?
			e.printStackTrace();
			if (!alreadyWarned) {
				alreadyWarned = true;
				Display.getDefault().asyncExec(() -> {
					PreferencesUtil.createPreferenceDialogOn(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), NBCodePreferencePage.PAGE_ID, null, null).open();
				});
			}
		}
	}

	@Override
	public InputStream getInputStream() {
		return this.in;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.out;
	}

	@Override
	public void stop() {
		super.stop();
		if (this.clientSocket != null) {
			try {
				this.clientSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		this.clientSocket = null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (PREF_NBCODE_LOCATION.equals(event.getProperty())) {
			alreadyWarned = false;
			this.stop();
		}
	}
}
