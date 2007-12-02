package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.components.ProjectComponent;
import javax.swing.Icon;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

class ReviewBoardCommitExecutor implements CommitExecutor, ProjectComponent
{
    private final Project m_project;

    private final ChangeListManager m_changeListManager;

    public ReviewBoardCommitExecutor( final Project project,
        final ChangeListManager changeListManager )
    {
        m_project = project;
        m_changeListManager = changeListManager;
    }

    public static ReviewBoardCommitExecutor getInstance( Project project )
    {
        return project.getComponent( ReviewBoardCommitExecutor.class );
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

    public void projectOpened() {
        m_changeListManager.registerCommitExecutor( this );
    }

    public void projectClosed() { }

    @NonNls
    @NotNull
    public String getComponentName()
    {
        return "ReviewBoardCommitExecutor";
    }

    public void initComponent() { }

    public void disposeComponent() { }
}
