/*
 * @(#)PatchMakerFactory.java
 *
 * ver 1.0 Apr 10, 2009 plumpy
 */

package org.review_board.idea.plugin.patchmaker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.SvnVcs;
import org.review_board.idea.plugin.repofind.RepositoryFinder;
import org.review_board.idea.plugin.repofind.SvnRepositoryFinder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;

public class PatchMakerFactory
{
    @NotNull
    private static final PatchMakerFactory sm_instance =
        new PatchMakerFactory();

    @NotNull
    public static PatchMakerFactory getInstance()
    {
        return sm_instance;
    }

    @NotNull
    public PatchMaker getPatchMaker( @NotNull final Project project )
    {
        final VirtualFile baseDir = project.getBaseDir();
        if( baseDir == null )
            return new IntelliJPatchMaker( project );

        final AbstractVcs vcs = VcsUtil.getVcsFor( project, baseDir );
        if( vcs == null )
        {
            return new IntelliJPatchMaker( project );
        }
        else if ( vcs instanceof SvnVcs )
        {
            return new SvnPatchMaker( project );
        }
        else if ( vcs.getClass().getSimpleName().equals( "BzrVcs" ) )
        {
            return new BazaarPatchMaker( project );
        }
        else
        {
            return new IntelliJPatchMaker( project );
        }
    }
}
