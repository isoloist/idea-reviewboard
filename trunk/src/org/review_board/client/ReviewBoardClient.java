package org.review_board.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONException;
import org.review_board.client.json.Repository;
import org.review_board.client.json.Response;
import org.review_board.client.json.User;
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

    private boolean m_needsLogin = true;

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

    public ArrayList<Repository> getRepositories()
        throws ReviewBoardException, JSONException
    {
        final RepositoriesMethod method = new RepositoriesMethod( m_uri );
        processRequest( method );
        return method.getRepositories();
    }

    public void login() throws ReviewBoardException
    {
        m_needsLogin = false;
        final LoginMethod login = new LoginMethod( m_uri, m_username, m_password );
        processRequest( login );
    }

    private void processRequest( final ReviewBoardMethod method )
        throws ReviewBoardException
    {
        if( m_needsLogin )
            login();

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
        if( !username.equals( m_username ) )
            m_needsLogin = true;

        m_username = username;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword( final String password )
    {
        if( !password.equals( m_password ) )
            m_needsLogin = true;
        
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
