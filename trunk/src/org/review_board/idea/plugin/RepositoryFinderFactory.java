/*
* @(#)RepositoryFinderFactory.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.idea.svn.SvnVcs;

public class RepositoryFinderFactory
{
    private static final RepositoryFinderFactory sm_instance =
        new RepositoryFinderFactory();

    public static RepositoryFinderFactory getInstance()
    {
        return sm_instance;
    }

    public RepositoryFinder getRepositoryFinder( final Project project )
    {
        AbstractVcs vcs = VcsUtil.getVcsFor( project, project.getBaseDir() );
        if ( vcs instanceof SvnVcs )
        {
            return new SvnRepositoryFinder( project, (SvnVcs)vcs );
        }
        else
        {
            return null;
        }
    }
}

// eof: RepositoryFinderFactory.java