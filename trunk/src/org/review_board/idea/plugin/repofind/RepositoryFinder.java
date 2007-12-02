/*
* @(#)RepositoryFinder.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.progress.ProgressIndicator;
import java.util.Collection;
import java.util.Map;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;

public interface RepositoryFinder
{
    public FoundRepositoryInfo findRepository( Collection<Repository> repositories,
        ProgressIndicator indicator ) throws ReviewBoardException;

    public class FoundRepositoryInfo
    {
        private Repository m_repository;

        private String m_baseDiffPath;

        public FoundRepositoryInfo( Repository repository, String baseDiffPath
        )
        {
            m_baseDiffPath = baseDiffPath;
            m_repository = repository;
        }

        public String getBaseDiffPath()
        {
            return m_baseDiffPath;
        }

        public Repository getRepository()
        {
            return m_repository;
        }
    }
}