package org.review_board.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.review_board.client.json.Group;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;
import org.review_board.client.json.User;
import org.review_board.client.json.ReviewRequest;
import org.review_board.client.json.ReviewRequestDraft;
import org.review_board.client.request.AttachDiffRequest;
import org.review_board.client.request.GroupsRequest;
import org.review_board.client.request.LoginRequest;
import org.review_board.client.request.NewReviewRequestRequest;
import org.review_board.client.request.PublishRequest;
import org.review_board.client.request.RepositoriesRequest;
import org.review_board.client.request.RepositoryInfoRequest;
import org.review_board.client.request.RequestFactory;
import org.review_board.client.request.ReviewBoardRequest;
import org.review_board.client.request.SetFieldsRequest;
import org.review_board.client.request.UsersRequest;
import org.review_board.client.request.DeleteReviewRequestRequest;
import org.review_board.client.request.ReviewRequestsRequest;
import org.review_board.client.request.ReviewRequestDraftRequest;

public class ReviewBoardClient
{
    public static final boolean DEBUG = Boolean.getBoolean( "review_board.client.debug" );
    
    private final HttpClient m_httpClient;

    private RequestFactory m_requestFactory;

    private String m_username = "";

    private String m_password = "";

    private String m_uri = "";

    public ReviewBoardClient( final String username, final String password,
        final String uri )
    {
        m_username = username;
        m_password = password;
        m_uri = uri;
        MultiThreadedHttpConnectionManager manager =
            new MultiThreadedHttpConnectionManager();
        m_httpClient = new HttpClient( manager );
        m_requestFactory = new RequestFactory( uri );
    }

    public List<User> getUsers() throws ReviewBoardException
    {
        final UsersRequest request = m_requestFactory.getUsersRequest();
        processRequest( request );
        return request.getUsers();
    }

    public List<Group> getGroups() throws ReviewBoardException
    {
        final GroupsRequest request = m_requestFactory.getGroupsRequest();
        processRequest( request );
        return request.getGroups();
    }

    public List<ReviewRequest> getReviewRequests(
        final ReviewRequestsRequest.GetType type, final String argument )
        throws ReviewBoardException
    {
        final ReviewRequestsRequest request =
            m_requestFactory.getReviewRequestsRequest( type, argument );
        processRequest( request );
        return request.getReviewRequests();
    }

    public ReviewRequestDraft getReviewRequestDraft( final int reviewRequestId )
        throws ReviewBoardException
    {
        final ReviewRequestDraftRequest request =
            m_requestFactory.getReviewRequestDraftRequest( reviewRequestId );
        return processReviewRequestDraftRequest( request );
    }

    private ReviewRequestDraft processReviewRequestDraftRequest(
        final ReviewRequestDraftRequest request ) throws ReviewBoardException
    {
        final Response response = processRequest( request, true );

        // We don't mind a "Does not exist" error. We were just checking anyway, okay?
        if( response.isFailure() && response.isDoesNotExistFailure() )
        {
            return null;
        }

        throwOnFailure( response );

        return request.getDraft();
    }

    public ArrayList<Repository> getRepositories() throws ReviewBoardException
    {
        final RepositoriesRequest request = m_requestFactory.getRepositoriesRequest();
        processRequest( request );
        return request.getRepositories();
    }

    public Map<String, String> getRepositoryInfo( final int repositoryId )
        throws ReviewBoardException
    {
        final RepositoryInfoRequest request =
            m_requestFactory.getRepositoryInfoRequest( repositoryId );
        processRequest( request );
        return request.getRepositoryInfo();
    }

    public int newReviewRequest( final SetFieldsRequest.ReviewRequestData review,
        final boolean publish ) throws ReviewBoardException
    {
        final NewReviewRequestRequest newReviewRequestRequest =
            m_requestFactory.getNewReviewRequestRequest( review.getRepository().getId() );
        processRequest( newReviewRequestRequest );
        final int reviewRequestId = newReviewRequestRequest.getReviewId();

        try
        {
            updateExistingReviewRequest( reviewRequestId, review, publish );

            return reviewRequestId;
        }
        catch( ReviewBoardException createException )
        {
            try
            {
                final DeleteReviewRequestRequest delete =
                    m_requestFactory.getDeleteReviewRequestRequest( reviewRequestId );
                processRequest( delete );
            }
            catch( ReviewBoardException deleteException )
            {
                // Forget it. We want to throw the outer exception anyway.
            }
            throw createException;
        }
    }

    public void updateExistingReviewRequest( int reviewRequestId,
        SetFieldsRequest.ReviewRequestData review, boolean publish )
        throws ReviewBoardException
    {
        final SetFieldsRequest setFields =
            m_requestFactory.getSetFieldsRequest( reviewRequestId, review );
        processRequest( setFields );

        final AttachDiffRequest attachDiff =
            m_requestFactory.getAttachDiffRequest( reviewRequestId, review );
        processRequest( attachDiff );

        if( publish )
        {
            final PublishRequest publishRequest =
                m_requestFactory.getPublishRequest( reviewRequestId );
            processRequest( publishRequest );
        }
    }

    public void login() throws ReviewBoardException
    {
        final LoginRequest request =
            m_requestFactory.getLoginRequest( m_username, m_password );
        throwOnFailure( processRequest( request, false ) );
    }

    private void processRequest( final ReviewBoardRequest request )
        throws ReviewBoardException
    {
        throwOnFailure( processRequest( request, true ) );
    }

    private void throwOnFailure( Response response ) throws ReviewBoardException
    {
        if( response.isFailure() )
        {
            throw new ReviewBoardException(
                "Error from server: " + response.getErrorMessage() );
        }
    }

    private Response processRequest( final ReviewBoardRequest request,
        final boolean tryLoggingIn ) throws ReviewBoardException
    {
        try
        {
            final Response response = request.execute( m_httpClient );

            if( response.isFailure() && response.isNotLoggedInFailure() && tryLoggingIn )
            {
                login();
                return processRequest( request, false );
            }
            else
            {
                return response;
            }
        }
        catch ( IOException e )
        {
            throw new ReviewBoardException( e );
        }
    }

    public String getUsername()
    {
        return m_username;
    }

    public void setUsername( final String username )
    {
        m_username = username;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword( final String password )
    {
        m_password = password;
    }

    public String getUri()
    {
        return m_uri;
    }

    public void setUri( final String uri )
    {
        m_uri = uri;
        m_requestFactory = new RequestFactory( uri );
    }
}
