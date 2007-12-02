package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.CommitSession;
import javax.swing.Icon;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

class ReviewBoardCommitExecutor implements CommitExecutor
{
    private final Project m_project;

    public ReviewBoardCommitExecutor( final Project project )
    {
        m_project = project;
    }

    @NotNull
    public Icon getActionIcon()
    {
        return IconLoader.getIcon("/actions/back.png");
    }

    @Nls
    public String getActionText()
    {
        return "Submit to Review Board...";
    }

    @Nls
    public String getActionDescription()
    {
        return "Send a code review to Review Board";
    }

    @NotNull
    public CommitSession createCommitSession()
    {
        return new ReviewBoardCommitSession( m_project );
    }
}
