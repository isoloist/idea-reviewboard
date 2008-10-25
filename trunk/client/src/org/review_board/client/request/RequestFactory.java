/*
* @(#)RequestFactory.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.review_board.client.json.ReviewRequest;

public class RequestFactory
{
    private final String m_jsonBase;

    public RequestFactory( String baseUri )
    {
        m_jsonBase = baseUri + "/api/json/";
    }

    public AttachDiffRequest getAttachDiffRequest( final int reviewRequestId,
        final ReviewRequest review )
    {
        return new AttachDiffRequest( m_jsonBase, reviewRequestId, review );
    }

    public GroupsRequest getGroupsRequest()
    {
        return new GroupsRequest( m_jsonBase );
    }

    public LoginRequest getLoginRequest( final String username, final String password )
    {
        return new LoginRequest( m_jsonBase, username, password );
    }

    public NewReviewRequestRequest getNewReviewRequestRequest( final int repositoryId )
    {
        return new NewReviewRequestRequest( m_jsonBase, repositoryId );
    }

    public PublishRequest getPublishRequest( final int reviewRequestId )
    {
        return new PublishRequest( m_jsonBase, reviewRequestId );
    }

    public DeleteReviewRequestRequest getDeleteReviewRequestRequest(
        final int reviewRequestId )
    {
        return new DeleteReviewRequestRequest( m_jsonBase, reviewRequestId );
    }

    public RepositoriesRequest getRepositoriesRequest()
    {
        return new RepositoriesRequest( m_jsonBase );
    }

    public RepositoryInfoRequest getRepositoryInfoRequest( final int repositoryId )
    {
        return new RepositoryInfoRequest( m_jsonBase, repositoryId );
    }

    public SetFieldsRequest getSetFieldsRequest( final int reviewRequestId,
        final ReviewRequest review )
    {
        return new SetFieldsRequest( m_jsonBase, reviewRequestId, review );
    }

    public UsersRequest getUsersRequest()
    {
        return new UsersRequest( m_jsonBase );
    }
}