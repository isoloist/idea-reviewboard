/*
* @(#)SetFieldsRequest.java
*
* ver 1.0 Feb 7, 2008 plumpy
*/

package org.review_board.client.request;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;
import org.review_board.client.json.Repository;

public class SetFieldsRequest extends ReviewBoardRequest
{
    public SetFieldsRequest( final String baseUri, final int reviewRequestId,
        final ReviewRequestData review )
    {
        final PostMethod method = new PostMethod(
            baseUri + "reviewrequests/" + reviewRequestId + "/draft/set/" );

        setFieldsOnMethod( method, review );

        m_method = method;
    }

    private void setFieldsOnMethod( final PostMethod method,
        final ReviewRequestData review )
    {
        method.setParameter( "summary", review.getSummary() );
        method.setParameter( "branch", review.getBranch() );
        method.setParameter( "target_groups", review.getGroups() );
        method.setParameter( "target_people", review.getPeople() );
        method.setParameter( "bugs_closed", review.getBugs() );
        method.setParameter( "description", review.getDescription() );
        method.setParameter( "testing_done", review.getTestingDone() );
    }

    public int getDraftId() throws ReviewBoardException
    {
        try
        {
            final JSONObject draft = getResponse().getJSONObject( "draft" );
            return draft.getInt( "id" );
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }
    }

    public Response execute( HttpClient client ) throws IOException, ReviewBoardException
    {
        final Response response = super.execute( client );

        String error = "";
        boolean hasError = false;

        try
        {
            final JSONArray invalidGroups =
                response.getJSONArray( "invalid_target_groups" );
            if ( invalidGroups.length() > 0 )
            {
                hasError = true;
                error = "The following groups were not valid: ";
                error = appendInvalidItems( error, invalidGroups );
            }

            final JSONArray invalidPeople =
                response.getJSONArray( "invalid_target_people" );
            if ( invalidPeople.length() > 0 )
            {
                hasError = true;
                error += "The following people were not valid: ";
                error = appendInvalidItems( error, invalidPeople );
            }
        }
        catch ( JSONException e )
        {
            throw ReviewBoardException.jsonException( e );
        }

        if ( hasError )
        {
            throw new ReviewBoardException( error );
        }

        return response;
    }

    private String appendInvalidItems( String error, JSONArray ivalidItems )
        throws JSONException
    {
        for ( int i = 0; i < ivalidItems.length(); ++i )
        {
            if ( i != 0 ) error += ", ";

            error += ivalidItems.get( i );
        }
        error += '.';
        return error;
    }

    public static class ReviewRequestData
    {
        private String m_summary;

        private Repository m_repository;

        private String m_baseDiffPath;

        private String m_branch;

        private String m_groups;

        private String m_people;

        private String m_bugs;

        private String m_description;

        private String m_testingDone;

        private String m_diff;

        public String getBaseDiffPath()
        {
            return m_baseDiffPath;
        }

        public void setBaseDiffPath( String baseDiffPath )
        {
            m_baseDiffPath = baseDiffPath;
        }

        public String getBranch()
        {
            return m_branch;
        }

        public void setBranch( String branch )
        {
            m_branch = branch;
        }

        public String getBugs()
        {
            return m_bugs;
        }

        public void setBugs( String bugs )
        {
            m_bugs = bugs;
        }

        public String getDescription()
        {
            return m_description;
        }

        public void setDescription( String description )
        {
            m_description = description;
        }

        public String getGroups()
        {
            return m_groups;
        }

        public void setGroups( String groups )
        {
            m_groups = groups;
        }

        public String getPeople()
        {
            return m_people;
        }

        public void setPeople( String people )
        {
            m_people = people;
        }

        public Repository getRepository()
        {
            return m_repository;
        }

        public void setRepository( Repository repository )
        {
            m_repository = repository;
        }

        public String getSummary()
        {
            return m_summary;
        }

        public void setSummary( String summary )
        {
            m_summary = summary;
        }

        public String getTestingDone()
        {
            return m_testingDone;
        }

        public void setTestingDone( String testingDone )
        {
            m_testingDone = testingDone;
        }

        public String getDiff()
        {
            return m_diff;
        }

        public void setDiff( String diff )
        {
            m_diff = diff;
        }
    }
}