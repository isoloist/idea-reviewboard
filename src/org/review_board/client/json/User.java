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

    public User( final JSONObject object )
    {
        super( object );
    }

    public int getId() throws ReviewBoardException
    {
        return (Integer)get( ID_KEY );
    }

    public String getUsername() throws ReviewBoardException
    {
        return get( USERNAME_KEY ).toString();
    }

    public String getEmail() throws ReviewBoardException
    {
        return get( EMAIL_KEY ).toString();
    }

    public String getFullName() throws ReviewBoardException
    {
        return get( FULL_NAME_KEY ).toString();
    }

    public String getUrl() throws ReviewBoardException
    {
        return get( URL_KEY ).toString();
    }
}
