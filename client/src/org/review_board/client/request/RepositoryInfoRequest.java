/*
* @(#)RepositoryInfoRequest.java
*
* ver 1.0 Nov 24, 2007 plumpy
*/
package org.review_board.client.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;

public class RepositoryInfoRequest extends ReviewBoardRequest
{
    public RepositoryInfoRequest( final String baseUrl, final int repositoryId )
    {
        m_method = new GetMethod( baseUrl + "repositories/" + repositoryId + "/info/" );
    }

    public Map<String, String> getRepositoryInfo() throws ReviewBoardException
    {
        final Response response = getResponse();

        final Map<String, String> info = new HashMap<String, String>();
        JSONObject json = response.getJSONObject( "info" );
        for( Iterator iter =  json.keys(); iter.hasNext(); )
        {
            final String key = (String)iter.next();
            try
            {
                info.put( key, json.getString( key ) );
            }
            catch ( JSONException e )
            {
                throw new ReviewBoardException( "error parsing!" );
            }
        }
        return info;
    }
}