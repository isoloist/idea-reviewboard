/*
* @(#)SvnRepositoryFinder.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.SvnVcs;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.ReviewBoardPlugin;
import org.tmatesoft.svn.core.wc.SVNInfo;

public class SvnRepositoryFinder implements RepositoryFinder
{
    @NotNull
    private final Project m_project;

    private final SvnVcs m_vcs;

    public SvnRepositoryFinder( @NotNull final Project project, final SvnVcs vcs )
    {
        m_project = project;
        m_vcs = vcs;
    }

    @Nullable
    public FoundRepositoryInfo findRepository( final Collection<Repository> repositories,
        final ProgressIndicator indicator )
    {
        indicator.setText2( "Getting info for local checkout" );
        if( indicator.isCanceled() )
            return null;

        final VirtualFile baseDir = m_project.getBaseDir();
        if( baseDir == null )
            return null;

        SVNInfo localInfo = m_vcs.getInfoWithCaching( baseDir );
        if ( localInfo == null )
            return null;

        String localRelative = RepoFindUtil.getRelativePath( localInfo.getURL().toString(),
            localInfo.getRepositoryRootURL().toString() );
        if ( localRelative == null )
            return null;

        String localUuid = localInfo.getRepositoryUUID();

        ReviewBoardClient client = ReviewBoardPlugin.getClient( m_project );

        final Collection<Repository> svnRepositories =
            filterSvnRepositories( repositories );

        for ( Repository repository : svnRepositories )
        {
            try
            {
                if( indicator.isCanceled() )
                    return null;
                indicator.setText2(
                    "Getting info for repository " + repository.getName() );
                Map<String, String> info = client.getRepositoryInfo( repository.getId() );
                String remoteUuid = info.get( "uuid" );
                String remoteRelative = RepoFindUtil.getRelativePath( info.get( "url" ),
                    info.get( "root_url" ) );

                if( remoteRelative == null )
                    continue;

                if( ! localUuid.equals( remoteUuid ) )
                    continue;

                String relative = RepoFindUtil.getRelativePath( localRelative, remoteRelative );
                if( relative == null )
                    continue;

                return new FoundRepositoryInfo( repository, relative );
            }
            catch ( Exception e )
            {
                // I guess we won't use this repository.
                e.printStackTrace();
            }
        }

        return null;
    }

    @NotNull
    private Collection<Repository> filterSvnRepositories(
        final Collection<Repository> repositories )
    {
        return RepoFindUtil.filterRepositories( repositories, "Subversion" );
    }
}