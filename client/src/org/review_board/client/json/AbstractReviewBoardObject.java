package org.review_board.client.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public abstract class AbstractReviewBoardObject
{
    private JSONObject m_jsonObject;

    AbstractReviewBoardObject( final JSONObject jsonObject )
    {
        m_jsonObject = jsonObject;
    }

    AbstractReviewBoardObject( final String string ) throws ReviewBoardException
    {
        try
        {
            m_jsonObject = new JSONObject( string );
        }
        catch( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }

    public Object get( String key ) throws ReviewBoardException
    {
        try
        {
            return m_jsonObject.get( key );
        }
        catch( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }
}
