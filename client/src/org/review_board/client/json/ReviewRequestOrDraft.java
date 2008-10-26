/**
 * @(#)AbstractReviewRequest.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.client.json;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.review_board.client.ReviewBoardException;

public abstract class ReviewRequestOrDraft extends AbstractReviewBoardObject
{
    private static final String ID_KEY = "id";

    private static final String SUMMARY_KEY = "summary";

    private static final String BRANCH_KEY = "branch";

    private static final String PEOPLE_KEY = "target_people";

    private static final String GROUPS_KEY = "target_groups";

    private static final String BUGS_KEY = "bugs_closed";

    private static final String DESCRIPTION_KEY = "description";

    private static final String TESTING_DONE_KEY = "testing_done";

    private static final String UPDATED_KEY = "last_updated";

    private static DateFormat sm_dateFormat =
        new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

    private final int m_id;

    private final String m_summary;

    private final String m_branch;

    private final List<User> m_people = new ArrayList<User>();

    private final List<Group> m_groups = new ArrayList<Group>();

    private final Object m_bugs;

    private final String m_description;

    private final String m_testingDone;

    private final Date m_lastUpdated;

    protected ReviewRequestOrDraft( JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_id = getInt( ID_KEY );
        m_summary = getString( SUMMARY_KEY );
        m_branch = getString( BRANCH_KEY );
        final Object bugs = get( BUGS_KEY );
        m_description = getString( DESCRIPTION_KEY );
        m_testingDone = getString( TESTING_DONE_KEY );
        try
        {
            m_lastUpdated = sm_dateFormat.parse( getString( UPDATED_KEY ) );
        }
        catch( ParseException e )
        {
            throw new ReviewBoardException( e );
        }

        // TODO I was sending these over as strings, but apparently the New Wisdom is to
        // send them as arrays. Probably they should be split on the server end, just like
        // users and groups. Once that happens, they will all be arrays. Until then, they
        // could come back either way.
        if( bugs instanceof JSONArray )
        {
            final JSONArray bugsArray = (JSONArray)bugs;
            m_bugs = new ArrayList<String>();
            for( int i = 0; i < bugsArray.length(); ++i )
            {
                try
                {
                    //noinspection unchecked
                    ((ArrayList)m_bugs).add( bugsArray.getString( i ) );
                }
                catch( JSONException e )
                {
                    throw ReviewBoardException.jsonException( e );
                }
            }
        }
        else
        {
            m_bugs = bugs;
        }

        JSONArray users = getJSONArray( PEOPLE_KEY );
        for ( int i = 0; i < users.length(); ++i )
        {
            try
            {
                m_people.add( new User( users.getJSONObject( i ) ) );
            }
            catch ( JSONException e )
            {
                throw ReviewBoardException.jsonException( e );
            }
        }

        JSONArray groups = getJSONArray( GROUPS_KEY );
        for ( int i = 0; i < groups.length(); ++i )
        {
            try
            {
                m_groups.add( new Group( groups.getJSONObject( i ) ) );
            }
            catch ( JSONException e )
            {
                throw ReviewBoardException.jsonException( e );
            }
        }
    }

    public abstract int getReviewRequestId();

    public int getId()
    {
        return m_id;
    }

    public String getSummary()
    {
        return m_summary;
    }

    public String getBranch()
    {
        return m_branch;
    }

    public List<User> getPeople()
    {
        return m_people;
    }

    public List<Group> getGroups()
    {
        return m_groups;
    }

    public Object getBugs()
    {
        return m_bugs;
    }

    public String getDescription()
    {
        return m_description;
    }

    public String getTestingDone()
    {
        return m_testingDone;
    }

    public Date getLastUpdated()
    {
        return m_lastUpdated;
    }

    public String toString()
    {
        return getSummary();
    }
}
