/**
 * @(#)ReviewRequest.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class ReviewRequest extends ReviewRequestOrDraft
{
    private static final String REPOSITORY_KEY = "repository";

//    private static final String STATUS_KEY = "status";
//
//    private static final String ADDED_KEY = "time_added";
//
//    private static final String PUBLIC_KEY = "public";
//
//    private static final String CHANGENUM_KEY = "changenum";
//
//    private static final String SUBMITTER_KEY = "submitter";

    private Repository m_repository;

    public ReviewRequest( JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_repository = new Repository( getJSONObject( REPOSITORY_KEY ) );
    }

    public int getReviewRequestId()
    {
        return getId();
    }

    public Repository getRepository()
    {
        return m_repository;
    }
}
