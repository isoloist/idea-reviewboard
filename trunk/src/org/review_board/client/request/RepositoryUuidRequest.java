/*
* @(#)RepositoryUuidRequest.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;

public class RepositoryUuidRequest extends ReviewBoardRequest
{
    public RepositoryUuidRequest( final String baseUri, final int repositoryId )
    {
        m_method = new GetMethod( baseUri + "repositories/" + repositoryId + "/uuid/" );
    }

    public String getRepositoryUuid() throws ReviewBoardException
    {
        final Response response = getResponse();

        return (String)response.get( "uuid" );
    }
}

// eof: RepositoryUuidRequest.java