/*
* @(#)RequestFactory.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

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

    public RepositoryInfoRequest getRepositoryInfoRequest( final int repositoryId )
    {
        return new RepositoryInfoRequest( m_baseUri, repositoryId );
    }

    public UsersRequest getUsersRequest()
    {
        return new UsersRequest( m_baseUri );
    }
}

// eof: RequestFactory.java