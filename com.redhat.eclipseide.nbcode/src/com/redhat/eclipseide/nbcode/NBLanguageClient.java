/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat, Inc.. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package com.redhat.eclipseide.nbcode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4e.LanguageClientImpl;
import org.eclipse.lsp4j.ConfigurationParams;

public class NBLanguageClient extends LanguageClientImpl {

	@Override
	public CompletableFuture<List<Object>> configuration(ConfigurationParams params) {
		return CompletableFuture.completedFuture(List.of());
	}
}
