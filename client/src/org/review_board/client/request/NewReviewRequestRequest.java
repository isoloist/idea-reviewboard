/*
* @(#)NewReviewRequestRequest.java
*
* ver 1.0 Feb 7, 2008 plumpy
*/

package org.review_board.client.request;

import org.apache.commons.httpclient.methods.PostMethod;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;
import org.json.JSONObject;
import org.json.JSONException;

public class NewReviewRequestRequest extends ReviewBoardRequest
{
    public NewReviewRequestRequest( final String baseUrl, final int repositoryId )
    {
        final PostMethod method = new PostMethod( baseUrl + "reviewrequests/new/" );
        method.setParameter( "repository_id", Integer.toString( repositoryId ) );
        m_method = method;
    }

    public int getReviewId() throws ReviewBoardException
    {
        try
        {
            final Response response = getResponse();
            final JSONObject reviewRequest = (JSONObject)response.get( "review_request" );
            return reviewRequest.getInt( "id" );
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }
}