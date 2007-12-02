package org.review_board.client.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class Response extends AbstractReviewBoardObject
{
    private static final String STATUS_KEY = "stat";

    private static final String FAILURE_VALUE = "fail";

    private static final String ERROR_KEY = "err";

    private static final String ERROR_CODE_KEY = "code";

    private static final String ERROR_MESSAGE_KEY = "msg";

    private static final int NOT_LOGGED_IN_CODE = 103;

    private final boolean m_failure;

    private int m_errorCode;

    private String m_errorMessage;

    public Response( final String s ) throws ReviewBoardException
    {
        super( s );
        m_failure = get( STATUS_KEY ).equals( FAILURE_VALUE );

        if ( m_failure )
        {
            JSONObject error = (JSONObject)get( ERROR_KEY );
            try
            {
                m_errorCode = (Integer)error.get( ERROR_CODE_KEY );
                m_errorMessage = (String)error.get( ERROR_MESSAGE_KEY );
            }
            catch ( JSONException e )
            {
                throw ReviewBoardException.jsonException( e );
            }
        }

        System.out.println( s );
    }

    public boolean isFailure()
    {
        return m_failure;
    }

    public boolean isNotLoggedInFailure()
    {
        return getErrorCode() == NOT_LOGGED_IN_CODE;
    }

    public int getErrorCode()
    {
        return m_errorCode;
    }

    public String getErrorMessage()
    {
        return m_errorMessage;
    }
}
