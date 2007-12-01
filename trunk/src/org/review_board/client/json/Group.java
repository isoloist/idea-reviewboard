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

    public Group( JSONObject jsonObject )
    {
        super( jsonObject );
    }

    public int getId() throws ReviewBoardException
    {
        return (Integer)get( ID_KEY );
    }

    public String getMailingList() throws ReviewBoardException
    {
        return get( MAILING_LIST_KEY ).toString();
    }

    public String getName() throws ReviewBoardException
    {
        return get( NAME_KEY ).toString();
    }

    public String getUrl() throws ReviewBoardException
    {
        return get( URL_KEY ).toString();
    }
}