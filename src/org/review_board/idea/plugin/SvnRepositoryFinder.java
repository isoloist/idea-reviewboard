/*
* @(#)SvnRepositoryFinder.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin;

import com.intellij.openapi.project.Project;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.idea.svn.SvnVcs;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
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

    public Map<String, Object> findRepository()
        throws ReviewBoardException
    {
        SVNInfo localInfo = m_vcs.getInfoWithCaching( m_project.getBaseDir() );
        if ( localInfo == null )
            return null;

        String localRelative = getRelativePath( localInfo.getURL().toString(),
            localInfo.getRepositoryRootURL().toString() );
        if ( localRelative == null )
            return null;

        String localUuid = localInfo.getRepositoryUUID();

        ReviewBoardClient client = ReviewBoardPlugin.getInstance( m_project ).getClient();

        Collection<Repository> repositories = client.getRepositories();
        for ( Repository repository : repositories )
        {
            if( ! repository.getTool().equals( "Subversion" ) )
                continue;

            try
            {
                JSONObject info = client.getRepositoryInfo( repository.getId() );
                String remoteUuid = (String)info.get( "uuid" );
                String remoteRelative = getRelativePath( (String)info.get( "url" ),
                    (String)info.get( "root_url" ) );

                if( remoteRelative == null )
                    continue;

                if( ! localUuid.equals( remoteUuid ) )
                    continue;

                String relative = getRelativePath( localRelative, remoteRelative );
                if( relative == null )
                    continue;

                Map<String, Object> result = new HashMap<String, Object>();
                result.put( "repositoryId", repository.getId() );
                result.put( "baseurl", relative );
                return result;
            }
            catch ( Exception e )
            {
                // I guess we won't use this repository.
                e.printStackTrace();
            }
        }

        return null;
    }

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