/**
 * @(#)ReviewRequestDraft.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.client.json;

import org.json.JSONObject;
import org.review_board.client.ReviewBoardException;

public class ReviewRequestDraft extends ReviewRequestOrDraft
{
    private static final String REVIEW_REQUEST_KEY = "review_request";

    private ReviewRequest m_reviewRequest;

    public ReviewRequestDraft( JSONObject jsonObject ) throws ReviewBoardException
    {
        super( jsonObject );
        m_reviewRequest = new ReviewRequest( getJSONObject( REVIEW_REQUEST_KEY ) );
    }

    public int getReviewRequestId()
    {
        return getReviewRequest().getId();
    }

    public ReviewRequest getReviewRequest()
    {
        return m_reviewRequest;
    }
}
