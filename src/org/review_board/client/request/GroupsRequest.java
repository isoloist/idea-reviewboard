/*
* @(#)GroupsRequest.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.review_board.client.json.Group;
import org.review_board.client.json.Response;
import org.review_board.client.ReviewBoardException;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GroupsRequest extends ReviewBoardRequest
{
    public GroupsRequest( final String baseUri )
    {
        m_method = new GetMethod( baseUri + "groups/" );
    }

    public ArrayList<Group> getGroups() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = (JSONArray)response.get( "groups" );

        final ArrayList<Group> groups = new ArrayList<Group>();

        try
        {
            for ( int i = 0; i < array.length(); ++i )
            {
                groups.add( new Group( array.getJSONObject( i ) ) );
            }
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }

        return groups;
    }
}