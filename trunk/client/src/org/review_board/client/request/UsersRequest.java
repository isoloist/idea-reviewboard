/*
* @(#)UsersRequest.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.review_board.client.json.User;
import org.review_board.client.json.Response;
import org.review_board.client.ReviewBoardException;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class UsersRequest extends ReviewBoardRequest
{
    public UsersRequest(final String baseUri)
    {
        m_method = new GetMethod( baseUri + "users/" );
    }

    public ArrayList<User> getUsers() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = response.getJSONArray( "users" );

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
}