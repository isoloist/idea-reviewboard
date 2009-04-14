package org.review_board.idea.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.diff.impl.patch.PatchBuilder;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitSession;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ide.BrowserUtil;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.request.SetFieldsRequest;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.form.ReviewForm;
import org.review_board.idea.plugin.repofind.RepositoryFinder;
import org.review_board.idea.plugin.repofind.RepositoryFinderTask;
import org.review_board.idea.plugin.patchmaker.PatchMakerFactory;
import org.review_board.idea.plugin.patchmaker.PatchMaker;

class ReviewBoardCommitSession implements CommitSession
{
    @NotNull
    private final Project m_project;

    private ReviewForm m_form;

    private boolean m_errorFindingRepositories = false;

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
        if( ReviewBoardPlugin.showErrorIfUnconfigured( m_project ) )
        {
            return null;
        }

        if ( m_form == null )
        {
            final RepositoryFinderTask task = new RepositoryFinderTask( m_project );
            final ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.run( task );

            final Collection<Repository> repositories;
            final RepositoryFinder.FoundRepositoryInfo repoinfo;
            try
            {
                repositories = task.getRepositories();
                repoinfo = task.getResult();
            }
            catch( ReviewBoardException e )
            {
                ReviewBoardPlugin.showErrorDialog( m_project, e.getMessage(),
                    "Error retrieving repositories" );
                m_errorFindingRepositories = true;
                return null;
            }

            if ( repositories.size() == 0 )
            {
                ReviewBoardPlugin.showErrorDialog( m_project,
                    "No repositories are configured on the Review Board server",
                    "Error retrieving repositories" );
                m_errorFindingRepositories = true;
                return null;
            }

            m_form = new ReviewForm( m_project );
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
        {
            throw new ReviewBoardException(
                "You must enter a summary to submit a review!" );
        }

        if( !m_form.hasRecipient() )
        {
            throw new ReviewBoardException(
                "You must enter either a person or a group!" );
        }
    }

    /** {@inheritDoc} */
    public void execute( final Collection<Change> changes, final String commitMessage )
    {
        // We already popped an error in getAdditionalConfigurationUI()
        if( !ReviewBoardPlugin.isConfigured( m_project ) || m_errorFindingRepositories )
            return;
        
        try
        {
            checkExecute();

            final SetFieldsRequest.ReviewRequestData review =
                m_form.createReviewRequestData();

            final PatchMaker patchMaker =
                PatchMakerFactory.getInstance().getPatchMaker( m_project );
            review.setDiff( patchMaker.getPatch( changes ) );

            if( ReviewBoardPlugin.DEBUG )
                System.out.println( "got patch of size " + review.getDiff().length() );

            final ReviewBoardClient client = ReviewBoardPlugin.getClient( m_project );

            final boolean publish = m_form.publishReviewRequest();
            final int reviewRequestId;
            
            if( m_form.updateExistingReviewRequest() )
            {
                reviewRequestId = m_form.getExistingReviewRequest().getReviewRequestId();
                client.updateExistingReviewRequest( reviewRequestId, review, publish );
            }
            else
            {
                reviewRequestId = client.newReviewRequest( review, publish );
            }

            if ( m_form.openInWebBrowser() )
            {
                BrowserUtil.launchBrowser(
                    client.getUri() + "/r/" + reviewRequestId + "/" );
            }
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
        final List<FilePatch> patches =
            PatchBuilder.buildPatch( changes, baseDir.getPresentableUrl(), true, false );

        fixRevisionMarkers( patches );

        final StringWriter writer = new StringWriter( 2048 );
        UnifiedDiffWriter.write( patches, writer, "\n" ); // more non-openAPI code
        return writer.toString();
    }

    private void fixRevisionMarkers( List<FilePatch> patches )
    {
        for( FilePatch patch : patches )
        {
            if( !patch.getBeforeVersionId().startsWith( "(" ) )
            {
                // If the "before" version isn't a revision number, then this is an added
                // file. In SVN diff, those get marked as revision 0.
                patch.setBeforeVersionId( "(revision 0)" );
                patch.setAfterVersionId( "(revision 0)" );
            }
            else if( !patch.getAfterVersionId().startsWith( "(" ) )
            {
                // Any other non-revision string is a date in IntelliJ but shows up as
                // "(working copy)" in SVN diffs. Review Board actually handles that fine,
                // but as long as we're hacking up the patch, let's fix it.
                patch.setAfterVersionId( "(working copy)" );
            }
        }
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
