/*
* @(#)GroupsMethod.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 21, 2007 plumpy
*/
package org.review_board.client.method;

import java.util.ArrayList;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Group;
import org.review_board.client.json.Response;

public class GroupsMethod extends ReviewBoardMethod
{
    public static final String GROUPS_LOCATION = "groups/";

    public static final String GROUPS_KEY = "groups";

    public GroupsMethod( final String baseUri ) throws ReviewBoardException
    {
        super( baseUri );
    }

    public ArrayList<Group> getGroups() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = (JSONArray)response.get( GROUPS_KEY );

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

    protected String getMethodApiUrl()
    {
        return GROUPS_LOCATION;
    }

    public static void main( final String[] args )
    {
        try
        {
            final ReviewBoardClient client = new ReviewBoardClient( "plumpy", "foobar",
                "http://localhost" );
            final Collection<Group> groups = client.getGroups();
        }
        catch ( ReviewBoardException e )
        {
            e.printStackTrace();
        }
        catch ( JSONException e )
        {
            e.printStackTrace();
        }
    }
}

// eof: GroupsMethod.java