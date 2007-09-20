package org.review_board.client.method;

import org.review_board.client.ReviewBoardException;

public class LoginMethod extends ReviewBoardMethod
{
    private static final String LOGIN_LOCATION = "accounts/login/";

    private static final String USERNAME_KEY = "username";

    private static final String PASSWORD_KEY = "password";

    public LoginMethod( final String baseUrl, final String username,
        final String password ) throws ReviewBoardException
    {
        super( baseUrl );
        setParameter( USERNAME_KEY, username );
        setParameter( PASSWORD_KEY, password );
    }

    protected String getMethodApiUrl()
    {
        return LOGIN_LOCATION;
    }
}
