package org.review_board.idea.plugin;

import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.PatchBuilder;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitSession;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.form.ReviewForm;
import org.review_board.idea.plugin.repofind.RepositoryFinder;
import org.review_board.idea.plugin.repofind.RepositoryFinderTask;

public class ReviewBoardCommitSession implements CommitSession
{
    @NotNull
    private Project m_project;

    private ReviewForm m_form;

    public ReviewBoardCommitSession( @NotNull final Project project )
    {
        m_project = project;
    }

    @Nullable
    @Deprecated
    /** {@inheritDoc} */
    public JComponent getAdditionalConfigurationUI()
    {
        return null;
    }

    @Nullable
    /** {@inheritDoc} */
    public JComponent getAdditionalConfigurationUI( Collection<Change> changes,
        String commitMessage )
    {
        if ( m_form == null )
        {
            final RepositoryFinderTask task = new RepositoryFinderTask( m_project );
            ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.run( task );

            Collection<Repository> repositories = null;
            RepositoryFinder.FoundRepositoryInfo repoinfo = null;
            try
            {
                repositories = task.getRepositories();
                repoinfo = task.getResult();
            }
            catch ( ReviewBoardException e )
            {
                e.printStackTrace();
            }
            if( repositories == null )
            {
                return null; // OHFUCKY
            }
            if ( repoinfo == null )
            {
                System.out.println( "couldn't find a repository!" );
            }

            m_form = new ReviewForm();
            m_form.setCommitMessage( commitMessage );
            
            if ( repoinfo != null )
            {
                m_form.setRepositories( repositories, repoinfo );
                System.out.println( "repository: " + repoinfo.getRepository().getName() );
                System.out.println( "base path: " + repoinfo.getBaseDiffPath() );
            }
            else
            {
                System.out.println( "couldn't find a repository!" );
            }
        }
        return m_form.getRootComponent();
    }

    public boolean canExecute( final Collection<Change> changes,
        final String commitMessage )
    {
        return true;
    }

    /** {@inheritDoc} */
    public void execute( final Collection<Change> changes, final String commitMessage )
    {
        try
        {
            /* Here's some IDEA code that isn't part of openapi, but which I was
             * encouraged to use:
             *   http://www.jetbrains.net/jira/browse/IDEADEV-21340
             *
             * The second argument is the path which will be stripped from the diff
             * filenames.  So if you are diffing two versions of
             * "/home/plumpy/hello/Foo.java" and the second argument is "/home/plumpy",
             * then the filename in the diff will be "hello/Foo.java".
             *
             * The third argument tells it to ignore the fact that a file moved, treating
             * it as a normal modification.  If this is false, the patch contains a
             * deletion of the old file and an addition of the new file.
             *
             * The fourth argument tells it whether or not to reverse the order of the
             * patch.
             */
            // TODO PatchBulder throws VcsException if you try to get a patch when the VCS is updating.  Do something useful?
            List<FilePatch> patches = PatchBuilder
                .buildPatch( changes, m_project.getBaseDir().getPath(), true, false );
            StringWriter writer = new StringWriter( 2048 );
            UnifiedDiffWriter.write( patches, writer, "\n" );
            System.out.println( "executed commit!" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void executionCanceled()
    {
    }
}
