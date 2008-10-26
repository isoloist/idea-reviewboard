/*
* @(#)Group.java
*
* ver 1.0 Nov 21, 2007 plumpy
*/
package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class Group extends AbstractReviewBoardObject
{
    private static final String NAME_KEY = "name";

    private static final String DISPLAY_NAME_KEY = "display_name";

    private final String m_name;

    private final String m_displayName;

    public Group( JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_name = getString( NAME_KEY );
        m_displayName = getString( DISPLAY_NAME_KEY );
    }

    public String getName()
    {
        return m_name;
    }

    public String getDisplayName()
    {
        return m_displayName;
    }

    public String toString()
    {
        return getName();
    }
}