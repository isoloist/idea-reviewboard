/*
* @(#)RepositoriesRequest.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import java.util.ArrayList;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;

public class RepositoriesRequest extends ReviewBoardRequest
{
    public RepositoriesRequest( final String baseUri )
    {
        m_method = new GetMethod( baseUri + "repositories/" );
    }

    public ArrayList<Repository> getRepositories() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = response.getJSONArray( "repositories" );

        final ArrayList<Repository> repositories = new ArrayList<Repository>();

        try
        {
            for ( int i = 0; i < array.length(); ++i )
            {
                repositories.add( new Repository( array.getJSONObject( i ) ) );
            }
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }

        return repositories;
    }
}