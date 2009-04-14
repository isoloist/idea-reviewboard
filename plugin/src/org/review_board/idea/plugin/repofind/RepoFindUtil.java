/*
 * @(#)RepoFindUtil.java
 *
 * Copyright 2006 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Apr 13, 2009 plumpy
 */

package org.review_board.idea.plugin.repofind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.json.Repository;
import java.util.Collection;
import java.util.ArrayList;

public class RepoFindUtil
{
    @NotNull
    public static Collection<Repository> filterRepositories(
        final Collection<Repository> repositories, final String tool )
    {
        final Collection<Repository> svnRepositories = new ArrayList<Repository>();
        for( Repository repository : repositories )
        {
            if( repository.getTool().equals( tool ) )
            {
                svnRepositories.add( repository );
            }
        }
        return svnRepositories;
    }

    @Nullable
    static String getRelativePath( String url, String root )
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
