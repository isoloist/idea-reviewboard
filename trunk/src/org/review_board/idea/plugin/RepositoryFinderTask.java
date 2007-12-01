/*
* @(#)RepositoryFinderTask.java
*
* ver 1.0 Dec 1, 2007 plumpy
*/
package org.review_board.idea.plugin;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardException;

public class RepositoryFinderTask extends Task.Modal
{
    private Project m_project;

    private boolean m_finished = false;

    private Map<String, Object> m_result;

    public RepositoryFinderTask( @Nullable Project project )
    {
        super( project, "Finding Repository...", true );
        m_project = project;
    }

    public void run( ProgressIndicator indicator )
    {
        indicator.setIndeterminate( true );
        try
        {
            RepositoryFinder repofinder =
                RepositoryFinderFactory.getInstance().getRepositoryFinder( m_project );
            if( repofinder == null )
                return;
            
            m_result = repofinder.findRepository( indicator );
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

    public Map<String, Object> getResult() throws ReviewBoardException
    {
        if( !m_finished )
            throw new ReviewBoardException( "can't get result because it's not done!" );

        return m_result;
    }
}