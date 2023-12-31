/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.internal.browser.WebBrowserUIPlugin;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class NBCodePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "com.redhat.eclipseide.nbcode";

	public NBCodePreferencePage() {
		super("NetBeans Language Server", SWT.DEFAULT);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public Composite createContents(Composite parent) {
		Composite res = new Composite(parent, SWT.NONE);
		res.setLayout(new GridLayout(1, false));
		Link label = new Link(res, SWT.WRAP);
		String downloadURL = "https://downloads.apache.org/netbeans/netbeans-vscode-ext/";
		label.setText("""
			Enable Java edition assistance using the Netbeans Language Server (nbcode) by defining the path to the nbcode launcher.
			
			The default value matches the installation of VSCode Oracle Java extension, if installed.
			You can also extract a recent release of nbcode from <A>""" + downloadURL + "</A>");
		label.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			try {
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser(null).openURL(new URL(downloadURL));
			} catch (PartInitException | MalformedURLException e1) {
				e1.printStackTrace();
			}
		}));
		Control fields = super.createContents(res);
		fields.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		Link link = new Link(res, SWT.WRAP);
		link.setText("Go to the <A>Language Servers</A> page to can disable/re-enable nbcode.");
		link.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (getContainer() instanceof IWorkbenchPreferenceContainer dialog) {
				dialog.openPage("org.eclipse.lsp4e.preferences", null);
			} else {
				PreferencesUtil.createPreferenceDialogOn(getShell(), NBCodePreferencePage.PAGE_ID, null, null).open();
			}
		}));
		return res;
	}

	@Override
	protected void createFieldEditors() {
		addField(new FileFieldEditor(NBCodeStreamConnectionProvider.PREF_NBCODE_LOCATION, "nbcode launcher", getFieldEditorParent()));
	}

}
