package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class User extends AbstractReviewBoardObject
{
    private static final String ID_KEY = "id";

    private static final String USERNAME_KEY = "username";

    private static final String EMAIL_KEY = "email";

    private static final String FULL_NAME_KEY = "fullname";

    private static final String URL_KEY = "url";

    private final int m_id;

    private final String m_username;

    private final String m_email;

    private final String m_fullName;

    private final String m_url;

    public User( final JSONObject object ) throws ReviewBoardException
    {
        super( object );
        m_id = (Integer)get( ID_KEY );
        m_username = (String)get( USERNAME_KEY );
        m_email = (String)get( EMAIL_KEY );
        m_fullName = (String)get( FULL_NAME_KEY );
        m_url = (String)get( URL_KEY );
    }

    public String getEmail()
    {
        return m_email;
    }

    public String getFullName()
    {
        return m_fullName;
    }

    public int getId()
    {
        return m_id;
    }

    public String getUrl()
    {
        return m_url;
    }

    public String getUsername()
    {
        return m_username;
    }
}
