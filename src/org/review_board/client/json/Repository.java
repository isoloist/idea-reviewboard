package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class Repository extends AbstractReviewBoardObject
{
    public static final String ID_KEY = "id";

    public static final String PATH_KEY = "path";

    public static final String TOOL_KEY = "tool";

    public static final String NAME_KEY = "name";

    public Repository( final JSONObject jsonObject )
    {
        super( jsonObject );
    }

    public int getId() throws ReviewBoardException
    {
        return (Integer)get( ID_KEY );
    }

    public String getPath() throws ReviewBoardException
    {
        return (String)get( PATH_KEY );
    }

    public String getName() throws ReviewBoardException
    {
        return (String)get( NAME_KEY );
    }

    public String getTool() throws ReviewBoardException
    {
        return (String)get( TOOL_KEY );
    }
}
