package org.review_board.client.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class Response extends AbstractReviewBoardObject
{
    private static final String STATUS_KEY = "stat";

    private static final String FAILURE_VALUE = "fail";

    private static final String ERROR_KEY = "err";

    private static final String CODE_KEY = "code";

    private static final String MESSAGE_KEY = "msg";

    static final int NOT_LOGGED_IN_CODE = 103;

    public Response( final String s ) throws ReviewBoardException
    {
        super( s );

        System.out.println( s );
    }

    public boolean isFailure() throws ReviewBoardException
    {
        return get( STATUS_KEY ).equals( FAILURE_VALUE );
    }

    public boolean isNotLoggedInFailure() throws ReviewBoardException
    {
        return getErrorCode() == NOT_LOGGED_IN_CODE;
    }

    public int getErrorCode() throws ReviewBoardException
    {
        try
        {
            return (Integer)((JSONObject)get( ERROR_KEY )).get( CODE_KEY );
        }
        catch( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }

    public String getErrorMessage() throws ReviewBoardException
    {
        try
        {
            return ((JSONObject)get( ERROR_KEY )).get( MESSAGE_KEY ).toString();
        }
        catch( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }
}
