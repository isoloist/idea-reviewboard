package org.review_board.client.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
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

    public JSONObject getJSONObject( String key ) throws ReviewBoardException
    {
        return (JSONObject)get(key);
    }

    public JSONArray getJSONArray( String key ) throws ReviewBoardException
    {
        return (JSONArray)get(key);
    }

    public String getString( String key ) throws ReviewBoardException
    {
        return (String)get(key);
    }

    public int getInt( String key ) throws ReviewBoardException
    {
        try
        {
            return m_jsonObject.getInt( key );
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }
}
