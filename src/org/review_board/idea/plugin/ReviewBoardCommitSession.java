package org.review_board.idea.plugin;

import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import java.util.Collection;

public class ReviewBoardCommitSession implements CommitSession
{
    private Project m_project;

    public ReviewBoardCommitSession( final Project project )
    {
        m_project = project;
    }

    @Nullable
    public JComponent getAdditionalConfigurationUI()
    {
        return null;
    }

    public boolean canExecute( final Collection<Change> changes,
        final String commitMessage )
    {
        return true;
    }

    public void execute( final Collection<Change> changes, final String commitMessage )
    {
        for( final Change change: changes )
        {
            final ContentRevision beforeContent = change.getBeforeRevision();
            final ContentRevision afterContent = change.getAfterRevision();
            final String[] before, after;
            try
            {
                before = beforeContent.getContent().split( "\n" );
                after = afterContent.getContent().split( "\n" );
            }
            catch ( VcsException e )
            {
                e.printStackTrace();
                return;
            }
            final Diff diff = new Diff( before, after );
            final Diff.change script = diff.diff_2( false ); 
            final DiffPrint.UnifiedPrint differ =
                new DiffPrint.UnifiedPrint( before, after );
            differ.print_header( beforeContent.getFile().getPath(),
                afterContent.getFile().getPath() );
            differ.print_script( script );
        }
    }

    public void executionCanceled()
    {
    }
}
