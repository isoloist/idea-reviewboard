package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class Repository extends AbstractReviewBoardObject
{
    private static final String ID_KEY = "id";

    private static final String PATH_KEY = "path";

    private static final String TOOL_KEY = "tool";

    private static final String NAME_KEY = "name";

    private final int m_id;

    private final String m_path;

    private final String m_name;

    private final String m_tool;

    public Repository( final JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_id = (Integer)get( ID_KEY );
        m_path = (String)get( PATH_KEY );
        m_name = (String)get( NAME_KEY );
        m_tool = (String)get( TOOL_KEY );
    }

    public int getId()
    {
        return m_id;
    }

    public String getName()
    {
        return m_name;
    }

    public String getPath()
    {
        return m_path;
    }

    public String getTool()
    {
        return m_tool;
    }

    public String toString()
    {
        return getName();
    }
}
