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
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.ReviewBoardPlugin;

public class RepositoryFinderTask extends Task.Modal
{
    private Project m_project;

    private boolean m_finished = false;

    private Collection<Repository> m_repositories;

    private RepositoryFinder.FoundRepositoryInfo m_result;

    public RepositoryFinderTask( @Nullable Project project )
    {
        super( project, "Finding Repository...", true );
        m_project = project;
    }

    public void run( ProgressIndicator indicator )
    {
        indicator.setIndeterminate( true );
        m_finished = false;
        m_repositories = null;
        m_result = null;
        try
        {
            indicator.setText( "Determining repository...");

            ReviewBoardClient client = ReviewBoardPlugin.getInstance( m_project ).getClient();

            indicator.setText2( "Getting list of repositories on server" );
            if( indicator.isCanceled() )
                return;
            
            m_repositories = client.getRepositories();

            RepositoryFinder repofinder =
                RepositoryFinderFactory.getInstance().getRepositoryFinder( m_project );
            if( repofinder == null )
                return;

            m_result = repofinder.findRepository( m_repositories, indicator );
        }
        catch ( ReviewBoardException e )
        {
            e.printStackTrace();
        }
        finally
        {
            m_finished = true;
        }
    }

    public RepositoryFinder.FoundRepositoryInfo getResult() throws ReviewBoardException
    {
        if( !m_finished )
            throw new ReviewBoardException( "can't get result because it's not done!" );

        return m_result;
    }

    public Collection<Repository> getRepositories() throws ReviewBoardException
    {
        if( !m_finished )
            throw new ReviewBoardException( "can't get result because it's not done!" );

        return m_repositories;
    }
}