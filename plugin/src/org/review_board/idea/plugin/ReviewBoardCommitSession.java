package org.review_board.idea.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.PatchBuilder;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import java.io.StringWriter;
import java.io.IOException;
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

class ReviewBoardCommitSession implements CommitSession
{
    @NotNull
    private final Project m_project;

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
            final ProgressManager progressManager = ProgressManager.getInstance();
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
            if ( repositories == null )
            {
                return null; // OHFUCKY
            }
            if ( repositories.size() == 0 )
            {
                return null; // FUCKY HERE TOO
            }

            m_form = new ReviewForm();
            m_form.setCommitMessage( commitMessage );

            m_form.setRepositories( repositories, repoinfo );
        }
        return m_form.getRootComponent();
    }

    public boolean canExecute( final Collection<Change> changes,
        final String commitMessage )
    {
        try
        {
            checkExecute();
            return true;
        }
        catch ( ReviewBoardException e )
        {
            return false;
        }
    }

    private void checkExecute() throws ReviewBoardException
    {
        if ( !m_form.hasSummary() )
            throw new ReviewBoardException(
                "You must enter a summary to submit a review!" );
    }

    /** {@inheritDoc} */
    public void execute( final Collection<Change> changes, final String commitMessage )
    {
        try
        {
            checkExecute();
            final String patch = getPatch( changes );
            System.out.println( "got patch of size " + patch.length() );
        }
        catch ( final Exception e )
        {
            ApplicationManager.getApplication().invokeLater( new Runnable()
            {
                public void run()
                {
                    Messages.showErrorDialog( m_project, e.getMessage(),
                        "Exception caught while submitting review!" );
                }
            } );
            e.printStackTrace();
        }
    }

    private String getPatch( Collection<Change> changes )
        throws ReviewBoardException, VcsException, IOException
    {
        VirtualFile baseDir = getBaseDir();
        // Here's some IDEA code that isn't part of openapi, but which I was
        // encouraged to use:
        //   http://www.jetbrains.net/jira/browse/IDEADEV-21340
        //
        // The second argument is the path which will be stripped from the diff
        // filenames.  So if you are diffing two versions of
        // "/home/plumpy/hello/Foo.java" and the second argument is "/home/plumpy",
        // then the filename in the diff will be "hello/Foo.java".
        //
        // The third argument tells it to ignore the fact that a file moved, treating
        // it as a normal modification.  If this is false, the patch contains a
        // deletion of the old file and an addition of the new file.
        //
        // The fourth argument tells it whether or not to reverse the order of the
        // patch.
        final List<FilePatch> patches = PatchBuilder
            .buildPatch( changes, baseDir.getPresentableUrl(), false, false );
        final StringWriter writer = new StringWriter( 2048 );
        final String linebreak = CodeStyleSettingsManager.getInstance( m_project )
            .getCurrentSettings().getLineSeparator();
        UnifiedDiffWriter.write( patches, writer, linebreak );
        return writer.toString();
    }

    private VirtualFile getBaseDir() throws ReviewBoardException
    {
        VirtualFile baseDir = m_project.getBaseDir();
        if ( baseDir == null )
        {
            throw new ReviewBoardException(
                "Couldn't get base directory from project!" );
        }
        return baseDir;
    }

    public void executionCanceled()
    {
    }
}
