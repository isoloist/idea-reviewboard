/*
* @(#)RepositoryFinderFactory.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.SvnVcs;

public class RepositoryFinderFactory
{
    @NotNull
    private static final RepositoryFinderFactory sm_instance =
        new RepositoryFinderFactory();

    @NotNull
    public static RepositoryFinderFactory getInstance()
    {
        return sm_instance;
    }

    @Nullable
    public RepositoryFinder getRepositoryFinder( @NotNull final Project project )
    {
        final VirtualFile baseDir = project.getBaseDir();

        if( baseDir == null )
            return null;
        
        final AbstractVcs vcs = VcsUtil.getVcsFor( project, baseDir );
        if( vcs == null )
        {
            return null;
        }
        else if ( vcs instanceof SvnVcs )
        {
            return new SvnRepositoryFinder( project, (SvnVcs)vcs );
        }
        else if ( vcs.getClass().getSimpleName().equals( "BzrVcs" ) )
        {
            return new BazaarRepositoryFinder( project );
        }
        else
        {
            return null;
        }
    }
}