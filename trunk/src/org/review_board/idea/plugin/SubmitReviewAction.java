/*
* @(#)SubmitReviewAction.java
*
* ver 1.0 Dec 1, 2007 plumpy
*/
package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.actions.VcsContext;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.actions.AbstractCommitChangesAction;
import org.jetbrains.annotations.Nullable;

public class SubmitReviewAction extends AbstractCommitChangesAction
{
    protected String getActionName( VcsContext vcsContext )
    {
        return "Submit Changes to Review Board";
    }

    @Nullable
    protected CommitExecutor getExecutor( Project project )
    {
        return ReviewBoardCommitExecutor.getInstance( project );
    }
}
