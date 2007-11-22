/*
* @(#)RequestFactory.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class RequestFactory
{
    private final String m_baseUri;

    public RequestFactory( String baseUri )
    {
        m_baseUri = baseUri + "/api/json/";
    }

    public GroupsRequest getGroupsRequest()
    {
        return new GroupsRequest( m_baseUri );
    }

    public LoginRequest getLoginRequest( final String username, final String password )
    {
        return new LoginRequest( m_baseUri, username, password );
    }

    public RepositoriesRequest getRepositoriesRequest()
    {
        return new RepositoriesRequest( m_baseUri );
    }

    public RepositoryUuidRequest getRepositoryUuidRequest( final int repositoryId )
    {
        return new RepositoryUuidRequest( m_baseUri, repositoryId );
    }

    public UsersRequest getUsersRequest()
    {
        return new UsersRequest( m_baseUri );
    }
}

// eof: RequestFactory.java