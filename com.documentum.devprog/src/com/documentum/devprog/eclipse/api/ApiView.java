/* ******************************************************************************
 * Copyright (c) 2005-2006, EMC Corporation
 * All rights reserved.

 * Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided that
 * the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - Neither the name of the EMC Corporation nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/

/*
 * Created on Mar 23, 2006
 *
 * EMC Documentum Developer Program 2005
 */
package com.documentum.devprog.eclipse.api;

import com.documentum.devprog.eclipse.DevprogPlugin;
import com.documentum.devprog.eclipse.common.DocbaseListDialog;
import com.documentum.devprog.eclipse.common.DocbaseLoginDialog;
import com.documentum.devprog.eclipse.common.PluginHelper;
import com.documentum.devprog.eclipse.common.PluginState;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * ApiView class.
 * <p>
 * This is used to combine the composite and actions. The main functionality is
 * in the ApiComposite.
 *
 * @author Aashish Patil(patil_aashish@emc.com)
 */
public class ApiView extends ViewPart {
	public static final String API_VIEW_ID = DevprogPlugin.VIEW_EXT_ID + ".apiView";

	private Action selectRepo = null;

	@Override
	public void createPartControl(Composite parent) {
		ApiComposite apiComp = new ApiComposite(parent, SWT.NONE);
		createActions();
		createToolbar();
	}

	@Override
	public void setFocus() {

	}

	private void createActions() {
		selectRepo = new Action("Select Repository") { //$NON-NLS-1$
			public void run() {
				DocbaseListDialog dld = new DocbaseListDialog(getSite().getShell());
				if (dld.open() == DocbaseListDialog.OK) {
					String selRepo = dld.getSelectedRepo();
					if (!PluginState.hasIdentity(selRepo)) {
						DocbaseLoginDialog loginDlg = new DocbaseLoginDialog(getSite().getShell());
						loginDlg.setDocbaseName(selRepo);
						if (loginDlg.open() == DocbaseLoginDialog.CANCEL) {
							// CANCEL means that user did not got successfully authenticated
							return;
						}
					}
					PluginState.setDocbase(selRepo);
					this.setToolTipText("Selected Repository: " + selRepo); //$NON-NLS-1$
					ApiView.this.setContentDescription("Selected Repository: " + selRepo);
				}
			}
		};
		selectRepo.setImageDescriptor(PluginHelper.getImageDesc("type/t_docbase_16.gif")); //$NON-NLS-1$
		selectRepo.setToolTipText("Choose a Repository");
		String curRepo = PluginState.getDocbase();
		if (curRepo != null && curRepo.length() > 0) {
			this.setContentDescription("Current Repository: " + curRepo);
		} else {
			this.setContentDescription("No Repository Selected");
		}
	}

	protected void createToolbar() {
		IToolBarManager tbMgr = getViewSite().getActionBars().getToolBarManager();
		tbMgr.add(selectRepo);
	}

	public Object getAdapter(Class aClass) {
		return super.getAdapter(aClass);
	}
}
