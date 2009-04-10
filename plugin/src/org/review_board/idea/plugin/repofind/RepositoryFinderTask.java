/*
* @(#)RepositoryFinderTask.java
*
* ver 1.0 Dec 1, 2007 plumpy
*/
package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.ReviewBoardPlugin;

public class RepositoryFinderTask extends Task.Modal
{
    private final Project m_project;

    private boolean m_finished = false;

    private ReviewBoardException m_error;

    private Collection<Repository> m_repositories;

    private RepositoryFinder.FoundRepositoryInfo m_result;

    public RepositoryFinderTask( @Nullable Project project )
    {
        super( project, "Finding Repository...", true );
        m_project = project;
    }

    public void run( @NotNull ProgressIndicator indicator )
    {
        indicator.setIndeterminate( true );
        m_finished = false;
        m_repositories = null;
        m_result = null;
        try
        {
            indicator.setText( "Determining repository..." );

            ReviewBoardClient client = ReviewBoardPlugin.getClient( m_project );

            indicator.setText2( "Getting list of repositories on server" );
            if ( indicator.isCanceled() )
                return;

            m_repositories = client.getRepositories();

            RepositoryFinder repofinder =
                RepositoryFinderFactory.getInstance().getRepositoryFinder( m_project );
            if ( repofinder == null )
                return;

            m_result = repofinder.findRepository( m_repositories, indicator );
        }
        catch ( ReviewBoardException e )
        {
            m_error = e;
        }
        finally
        {
            m_finished = true;
        }
    }

    @Nullable
    public RepositoryFinder.FoundRepositoryInfo getResult() throws ReviewBoardException
    {
        if ( !m_finished )
        {
            throw new IllegalStateException(
                "can't get result because task isn't finished!" );
        }

        if( m_error != null )
            throw m_error;

        return m_result;
    }

    @NotNull
    public Collection<Repository> getRepositories() throws ReviewBoardException
    {
        if ( !m_finished )
        {
            throw new IllegalStateException(
                "can't get repositories because task isn't finished!" );
        }

        if( m_error != null )
            throw m_error;

        return m_repositories;
    }
}