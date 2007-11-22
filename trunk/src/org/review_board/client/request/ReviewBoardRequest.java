/*
* @(#)ReviewBoardRequest.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.review_board.client.json.Response;
import org.review_board.client.ReviewBoardException;
import java.io.IOException;

public abstract class ReviewBoardRequest
{
    protected HttpMethod m_method;

    private Response m_response;

    public Response execute( final HttpClient client )
        throws IOException, ReviewBoardException
    {
        final int responseCode = client.executeMethod( m_method );

        if ( responseCode >= 300 )
        {
            throw new ReviewBoardException(
                "HTTP error " + responseCode + ": " + HttpStatus
                    .getStatusText( responseCode ) );
        }

        m_response = new Response( new String( m_method.getResponseBody() ) );

        return m_response;
    }

    public Response getResponse() throws ReviewBoardException
    {
        if ( m_response == null )
        {
            throw new ReviewBoardException(
                "Can't call getResponse() without calling execute()!" );
        }

        return m_response;
    }
}

// eof: ReviewBoardRequest.java