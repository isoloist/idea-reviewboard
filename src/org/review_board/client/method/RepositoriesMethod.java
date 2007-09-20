package org.review_board.client.method;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;

public class RepositoriesMethod extends ReviewBoardMethod
{
    public static final String REPOSITORIES_LOCATION = "repositories/";

    public static final String REPOSITORIES_KEY = "repositories";

    public RepositoriesMethod( final String baseUri ) throws ReviewBoardException
    {
        super( baseUri );
    }

    public ArrayList<Repository> getRepositories() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = (JSONArray)response.get( REPOSITORIES_KEY );

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
    protected String getMethodApiUrl()
    {
        return REPOSITORIES_LOCATION;
    }
}
