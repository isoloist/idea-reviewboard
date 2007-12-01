/*
* @(#)RepositoryInfoRequest.java
*
* ver 1.0 Nov 24, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;

public class RepositoryInfoRequest extends ReviewBoardRequest
{
    public RepositoryInfoRequest( final String baseUrl, final int repositoryId )
    {
        m_method = new GetMethod( baseUrl + "repositories/" + repositoryId + "/info" );
    }

    public JSONObject getRepositoryInfo() throws ReviewBoardException
    {
        final Response response = getResponse();

        return (JSONObject)response.get( "info" );
    }
}