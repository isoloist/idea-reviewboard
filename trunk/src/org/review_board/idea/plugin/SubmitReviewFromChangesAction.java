package org.review_board.idea.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeList;
import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.changes.ui.SessionDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* @(#)SubmitReviewFromChangesAction.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Dec 1, 2007 plumpy
*/
public class SubmitReviewFromChangesAction extends AnAction
{
    public void actionPerformed( AnActionEvent e )
    {
        Project project = e.getData( DataKeys.PROJECT );
        final List<Change> changes = getChangeList( e );
        ReviewBoardCommitExecutor executor = new ReviewBoardCommitExecutor( project );
        CommitSession session = executor.createCommitSession();
        SessionDialog dialog = new SessionDialog( executor.getActionText(), project, session, changes, "" );
        dialog.show();
        if( ! dialog.isOK() )
            return;

        session.execute( changes, "" );
    }

    private List<Change> getChangeList( AnActionEvent e )
    {
        Change changes[] = e.getData( DataKeys.CHANGES );
        final List<Change> changeList = new ArrayList<Change>();
        Collections.addAll( changeList, changes );
        return changeList;
    }

    public void update( AnActionEvent e )
    {
        Change changes[] = e.getData( DataKeys.CHANGES );
        ChangeList changelists[] = e.getData( DataKeys.CHANGE_LISTS );
        e.getPresentation().setEnabled(changes != null && changes.length > 0
            && changelists != null && changelists.length > 0 );
    }
}
