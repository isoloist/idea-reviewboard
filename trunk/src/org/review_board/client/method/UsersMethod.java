package org.review_board.client.method;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;
import org.review_board.client.json.User;

public class UsersMethod extends ReviewBoardMethod
{
    public static final String USERS_LOCATION = "users/";

    public static final String USERS_KEY = "users";

    public UsersMethod( final String baseUri ) throws ReviewBoardException
    {
        super( baseUri );
    }

    public ArrayList<User> getUsers() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = (JSONArray)response.get( USERS_KEY );

        final ArrayList<User> users = new ArrayList<User>();
        
        try
        {
            for ( int i = 0; i < array.length(); ++i )
            {
                users.add( new User( array.getJSONObject( i ) ) );
            }
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }

        return users;
    }

    protected String getMethodApiUrl()
    {
        return USERS_LOCATION;
    }
}
