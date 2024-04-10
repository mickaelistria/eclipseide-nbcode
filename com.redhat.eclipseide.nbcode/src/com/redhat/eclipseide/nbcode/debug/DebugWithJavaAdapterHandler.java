/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc. All rights reserved..
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode.debug;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.lsp4e.LSPEclipseUtils;
import org.eclipse.lsp4e.debug.DSPPlugin;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate.DSPLaunchDelegateLaunchBuilder;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.gson.Gson;
import com.redhat.eclipseide.nbcode.NBCodeStreamConnectionProvider;
public class DebugWithJavaAdapterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor == null) {
			return null;
		}
		URI uri = LSPEclipseUtils.toUri(editor.getEditorInput());
		if (uri == null || !(uri.getPath().endsWith(".java") || uri.getPath().endsWith(".class"))) {
			return null;
		}
		IFile f = LSPEclipseUtils.getFileHandle(uri);
//		try {
//			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
//			// no API in java debug adapter to start the Debug Adapter, using the command that's contributed to jdt-ls instead
//			ILaunchConfigurationType dapLaunchType = manager.getLaunchConfigurationType("org.eclipse.lsp4e.debug.launchType");
//			ILaunchConfigurationWorkingCopy dapLaunchConfig = dapLaunchType.newInstance(null, newInstance + " (NBCode Debug Adapter)");
//			dapLaunchConfig.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, f.getProject().getLocation().toString());
//			dapLaunchConfig.setAttribute(DSPPlugin.ATTR_DSP_MODE, DSPPlugin.DSP_MODE_CONNECT);
//			dapLaunchConfig.setAttribute(DSPPlugin.ATTR_DSP_SERVER_HOST, "localhost");
//			dapLaunchConfig.setAttribute(DSPPlugin.ATTR_DSP_SERVER_PORT, NBCodeStreamConnectionProvider.debugPort);
//			dapLaunchConfig.setAttribute(DSPPlugin.ATTR_DSP_PARAM, new Gson().toJson(Map.of(//
//					/*Constants.MAIN_CLASS*/"mainClass", className,
//					/*Constants.PROJECT_NAME*/"projectName", f.getProject().getName(),
//					"classPaths", classpathAndModulepath[1],
//					"modulePaths", List.of(),
//					"cwd", f.getProject().getLocation().toString(),
//					"javaExec", javaLaunchDelegate.getVMInstall(newInstance).getInstallLocation() + "/bin/java"
//					)));
//			dapLaunchConfig.launch(ILaunchManager.DEBUG_MODE, new NullProgressMonitor());
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
		return null;
	}

}
