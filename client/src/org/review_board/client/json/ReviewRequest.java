/*
* @(#)ReviewRequest.java
*
* ver 1.0 Feb 7, 2008 plumpy
*/

package org.review_board.client.json;

public class ReviewRequest
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