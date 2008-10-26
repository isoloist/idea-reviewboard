/**
 * @(#)ReviewRequestsFromUserRequest.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.client.request;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;
import org.review_board.client.json.ReviewRequest;

public class ReviewRequestsRequest extends ReviewBoardRequest
{
    public enum GetType { ALL, TO_USER, TO_USER_DIRECTLY, FROM_USER, TO_GROUP }

    public ReviewRequestsRequest( final String baseUri, final GetType type,
        final String argument )
    {
        m_method = new GetMethod(
            baseUri + MessageFormat.format( getFormatForType( type ), argument ) );
    }

    public List<ReviewRequest> getReviewRequests() throws ReviewBoardException
    {
        final Response response = getResponse();
        final JSONArray array = response.getJSONArray( "review_requests" );
        
        final ArrayList<ReviewRequest> reviewRequests = new ArrayList<ReviewRequest>();

        try
        {
            for ( int i = 0; i < array.length(); ++i )
            {
                reviewRequests.add( new ReviewRequest( array.getJSONObject( i ) ) );
            }
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }

        return reviewRequests;
    }

    private static String getFormatForType( final GetType type )
    {
        switch( type )
        {
            case ALL:
                return "reviewrequests/all/";
            case TO_USER:
                return "reviewrequests/to/user/{0}/";
            case TO_USER_DIRECTLY:
                return "reviewrequests/to/user/{0}/directly/";
            case FROM_USER:
                return "reviewrequests/from/user/{0}";
            case TO_GROUP:
                return "reviewrequests/to/group/{0}/";
            default: // Will never be reached.
                return null;
        }
    }
}
