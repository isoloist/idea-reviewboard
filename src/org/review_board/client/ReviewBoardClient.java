package org.review_board.client;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.httpclient.HttpClient;
import org.review_board.client.json.Group;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;
import org.review_board.client.json.User;
import org.review_board.client.request.GroupsRequest;
import org.review_board.client.request.LoginRequest;
import org.review_board.client.request.RepositoriesRequest;
import org.review_board.client.request.RepositoryUuidRequest;
import org.review_board.client.request.RequestFactory;
import org.review_board.client.request.ReviewBoardRequest;
import org.review_board.client.request.UsersRequest;

public class ReviewBoardClient
{
    private HttpClient m_httpClient;

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
        m_httpClient = new HttpClient();
        m_requestFactory = new RequestFactory( uri );
    }

    public ArrayList<User> getUsers() throws ReviewBoardException
    {
        final UsersRequest request = m_requestFactory.getUsersRequest();
        processRequest( request );
        return request.getUsers();
    }

    public ArrayList<Group> getGroups() throws ReviewBoardException
    {
        final GroupsRequest request = m_requestFactory.getGroupsRequest();
        processRequest( request );
        return request.getGroups();
    }

    public ArrayList<Repository> getRepositories() throws ReviewBoardException
    {
        final RepositoriesRequest request = m_requestFactory.getRepositoriesRequest();
        processRequest( request );
        return request.getRepositories();
    }

    public String getRepositoryUuid( final int repositoryId ) throws ReviewBoardException
    {
        final RepositoryUuidRequest request =
            new RepositoryUuidRequest( m_uri, repositoryId );
        processRequest( request );
        return request.getRepositoryUuid();
    }

    public void login() throws ReviewBoardException
    {
        final LoginRequest request =
            m_requestFactory.getLoginRequest( m_username, m_password );
        processRequest( request );
    }

    private void processRequest( final ReviewBoardRequest request )
        throws ReviewBoardException
    {
        try
        {
            request.execute( m_httpClient );

            final Response response = request.getResponse();

            if ( response.isFailure() )
            {
                if ( response.isNotLoggedInFailure() )
                {
                    login();
                    processRequest( request );
                }
                else
                {
                    throw new ReviewBoardException(
                        "Error from server: " + response.getErrorMessage() );
                }
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
