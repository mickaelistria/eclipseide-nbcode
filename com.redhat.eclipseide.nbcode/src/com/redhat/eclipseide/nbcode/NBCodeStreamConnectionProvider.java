/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class NBCodeStreamConnectionProvider extends ProcessStreamConnectionProvider implements IPropertyChangeListener {

	final static int port = 9123; // currently static, should be made dynamic
	public static final String PREF_NBCODE_LOCATION = "nbcodeLocation"; //$NON-NLS-1$
	private boolean alreadyWarned = false;
	private InputStream in;
	private OutputStream out;
	private Socket clientSocket;

	public NBCodeStreamConnectionProvider() {
		super(List.of("path to nbcode not set!"));
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	@Override
	protected ProcessBuilder createProcessBuilder() {
		String nbcodeLocation = Activator.getDefault().getPreferenceStore().getString(PREF_NBCODE_LOCATION);
		if (nbcodeLocation != null && !nbcodeLocation.isEmpty()) {
			this.setCommands(List.of(nbcodeLocation, "--start-java-language-server=listen:" + port));
		} else {
			if (!alreadyWarned) {
				Display.getDefault().asyncExec(() -> {
					PreferenceDialog prefDialog = new PreferenceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), PlatformUI.getWorkbench().getPreferenceManager());
					prefDialog.setSelectedNode(NBCodePreferencePage.PAGE_ID);
					prefDialog.open();
				});
			}
		}
		return super.createProcessBuilder(); 
	}

	@Override
	public void start() throws IOException {
		super.start();
		try {
			Thread.sleep(3000); // give some time for LS to start
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProcessHandle handle = getAdapter(ProcessHandle.class);
		if (handle == null || !handle.isAlive() && !alreadyWarned) {
			Display.getDefault().asyncExec(() -> {
				PreferenceDialog prefDialog = new PreferenceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), PlatformUI.getWorkbench().getPreferenceManager());
				prefDialog.setSelectedNode(NBCodePreferencePage.PAGE_ID);
				prefDialog.open();
			});
		}
		this.clientSocket = new Socket(InetAddress.getLocalHost(), port);
		this.in = this.clientSocket.getInputStream();
		this.out = this.clientSocket.getOutputStream();
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
			this.alreadyWarned = false;
			this.stop();
		}
	}
}
