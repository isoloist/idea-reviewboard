/*
* @(#)SvnRepositoryFinder.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.svn.SvnVcs;
import org.review_board.client.json.Repository;
import org.tmatesoft.svn.core.wc.SVNInfo;

public class SvnRepositoryFinder implements RepositoryFinder
{
    private final Project m_project;

    private final SvnVcs m_vcs;

    public SvnRepositoryFinder( final Project project, final SvnVcs vcs )
    {
        m_project = project;
        m_vcs = vcs;
    }

    public Repository findRepository( Collection<Repository> repositories )
    {
        String url =
            m_vcs.getInfoWithCaching( m_project.getBaseDir() ).getURL().toString();

        for( Repository repository : repositories )
        {

        }

        return null;
    }
}

// eof: SvnRepositoryFinder.java