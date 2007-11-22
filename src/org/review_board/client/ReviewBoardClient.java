package org.review_board.client;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONException;
import org.review_board.client.json.Group;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;
import org.review_board.client.json.User;
import org.review_board.client.method.GroupsMethod;
import org.review_board.client.method.LoginMethod;
import org.review_board.client.method.RepositoriesMethod;
import org.review_board.client.method.ReviewBoardMethod;
import org.review_board.client.method.UsersMethod;

public class ReviewBoardClient
{
    private HttpClient m_httpClient;

    private String m_username = "";

    private String m_password = "";

    private String m_uri = "";

    public ReviewBoardClient( final String username, final String password,
        final String uri )
    {
        this.m_username = username;
        this.m_password = password;
        this.m_uri = uri;
        this.m_httpClient = new HttpClient();
    }

    public ArrayList<User> getUsers() throws ReviewBoardException, JSONException
    {
        final UsersMethod method = new UsersMethod( m_uri );
        processRequest( method );
        return method.getUsers();
    }

    public ArrayList<Group> getGroups() throws ReviewBoardException, JSONException
    {
        final GroupsMethod method = new GroupsMethod( m_uri );
        processRequest( method );
        return method.getGroups();
    }

    public ArrayList<Repository> getRepositories()
        throws ReviewBoardException, JSONException
    {
        final RepositoriesMethod method = new RepositoriesMethod( m_uri );
        processRequest( method );
        return method.getRepositories();
    }

    public void login() throws ReviewBoardException
    {
        final LoginMethod login = new LoginMethod( m_uri, m_username, m_password );
        processRequest( login );
    }

    private void processRequest( final ReviewBoardMethod method )
        throws ReviewBoardException
    {
        try
        {
            executeMethod( method );

            final Response response = method.getResponse();

            if ( response.isFailure() )
            {
                if ( response.isNotLoggedInFailure() )
                {
                    login();
                    processRequest( method );
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

    private void executeMethod( final ReviewBoardMethod method )
        throws IOException, ReviewBoardException
    {
        final int responseCode = m_httpClient.executeMethod( method );
        if ( responseCode >= 300 )
        {
            throw new ReviewBoardException(
                "HTTP error " + responseCode + ": " + HttpStatus
                    .getStatusText( responseCode ) );
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
    }
}
