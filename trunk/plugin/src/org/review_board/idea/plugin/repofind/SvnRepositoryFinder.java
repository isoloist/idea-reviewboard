/*
* @(#)SvnRepositoryFinder.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import java.util.ArrayList;
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
    private final Project m_project;

    private final SvnVcs m_vcs;

    public SvnRepositoryFinder( final Project project,
        final SvnVcs vcs )
    {
        m_project = project;
        m_vcs = vcs;
    }

    @SuppressWarnings({"ConstantConditions"})
    @Nullable
    public FoundRepositoryInfo findRepository( final Collection<Repository> repositories,
        final ProgressIndicator indicator )
    {
        indicator.setText2( "Getting info for local checkout" );
        if( indicator.isCanceled() )
            return null;
        SVNInfo localInfo = m_vcs.getInfoWithCaching( m_project.getBaseDir() );
        if ( localInfo == null )
            return null;

        String localRelative = getRelativePath( localInfo.getURL().toString(),
            localInfo.getRepositoryRootURL().toString() );
        if ( localRelative == null )
            return null;

        String localUuid = localInfo.getRepositoryUUID();

        ReviewBoardClient client = ReviewBoardPlugin.getInstance( m_project ).getClient();

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
                String remoteRelative = getRelativePath( info.get( "url" ),
                    info.get( "root_url" ) );

                if( remoteRelative == null )
                    continue;

                if( ! localUuid.equals( remoteUuid ) )
                    continue;

                String relative = getRelativePath( localRelative, remoteRelative );
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
        final Collection<Repository> svnRepositories = new ArrayList<Repository>();
        for( Repository repository : repositories )
        {
            if( repository.getTool().equals( "Subversion" ) )
            {
                svnRepositories.add( repository );
            }
        }
        return svnRepositories;
    }

    @Nullable
    private static String getRelativePath( String url, String root )
    {
        url = url.replaceFirst( "/$", "" );
        root = root.replaceFirst( "/*$", "" );

        if( root.equals( "" ) )
            return url;
        
        String[] urlPaths = url.split( "/" );
        String[] rootPaths = root.split( "/" );

        if( urlPaths.length < rootPaths.length )
            return null;
        
        for( int i = 0; i < rootPaths.length; ++i )
        {
            if( ! rootPaths[i].equals( urlPaths[i] ) )
                return null;
        }

        StringBuilder sb = new StringBuilder();
        for( int i = rootPaths.length; i < urlPaths.length; ++i )
        {
            sb.append( urlPaths[i] );
            if( i + 1 < urlPaths.length )
                sb.append( "/" );
        }
        return sb.toString();
    }
}