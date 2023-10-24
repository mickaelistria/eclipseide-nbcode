/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(NBCodeStreamConnectionProvider.PREF_NBCODE_LOCATION,
			System.getProperty("user.home") + "/.vscode/extensions/oracle.oracle-java-1.0.0/nbcode/bin/nbcode" + (Platform.OS_WIN32.equals(Platform.getOS()) ? "64.exe" : ".sh"));
	}

}
