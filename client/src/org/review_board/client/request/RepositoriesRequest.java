/*
* @(#)RepositoriesRequest.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.ReviewBoardClient;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Collection;

public class RepositoriesRequest extends ReviewBoardRequest
{
    public RepositoriesRequest( final String baseUri )
    {
        m_method = new GetMethod( baseUri + "repositories/" );
    }

    public ArrayList<Repository> getRepositories() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = (JSONArray)response.get( "repositories" );

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

    @SuppressWarnings({"UnusedAssignment"})
    public static void main( String[] args )
    {
        try
        {
            final ReviewBoardClient client = new ReviewBoardClient( "plumpy", "foobar",
                "http://localhost" );
            //noinspection UnusedDeclaration
            final Collection<Repository> repositories = client.getRepositories();
        }
        catch ( ReviewBoardException e )
        {
            e.printStackTrace();
        }
    }
}