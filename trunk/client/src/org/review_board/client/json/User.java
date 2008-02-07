package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class User extends AbstractReviewBoardObject
{
    private static final String USERNAME_KEY = "username";

    private static final String FIRST_NAME_KEY = "first_name";

    private static final String LAST_NAME_KEY = "last_name";

    private final String m_username;

    private final String m_firstName;

    private final String m_lastName;

    public User( final JSONObject object ) throws ReviewBoardException
    {
        super( object );
        m_username = (String)get( USERNAME_KEY );
        m_firstName = (String)get( FIRST_NAME_KEY );
        m_lastName = (String)get( LAST_NAME_KEY );
    }

    public String getUsername()
    {
        return m_username;
    }

    public String getFirstName()
    {
        return m_firstName;
    }

    public String getLastName()
    {
        return m_lastName;
    }
}
