package org.review_board.idea.plugin;

import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.PatchBuilder;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitSession;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReviewBoardCommitSession implements CommitSession
{
    @NotNull
    private Project m_project;

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
    public JComponent getAdditionalConfigurationUI( Collection<Change> changes, String s )
    {
        return null;
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
            System.out.print( writer.toString() );
            RepositoryFinder repofinder =
                RepositoryFinderFactory.getInstance().getRepositoryFinder( m_project );
            final Map<String, Object> repoinfo = repofinder.findRepository();
            if( repoinfo == null )
            {
                System.out.println( "couldn't find a repository!" );
                return;
            }
            for( String key : repoinfo.keySet() )
            {
                System.out.println( key + ": " + repoinfo.get( key ) );
            }
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
