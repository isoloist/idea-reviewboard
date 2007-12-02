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
    private static final String ID_KEY = "id";

    private static final String MAILING_LIST_KEY = "mailing_list";

    private static final String NAME_KEY = "name";

    private static final String URL_KEY = "url";

    private final int m_id;

    private final String m_mailingList;

    private final String m_name;

    private final String m_url;

    public Group( JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_id = (Integer)get( ID_KEY );
        m_mailingList = (String)get( MAILING_LIST_KEY );
        m_name = (String)get( NAME_KEY );
        m_url = (String)get( URL_KEY );
    }

    public int getId()
    {
        return m_id;
    }

    public String getMailingList()
    {
        return m_mailingList;
    }

    public String getName()
    {
        return m_name;
    }

    public String getUrl()
    {
        return m_url;
    }
}