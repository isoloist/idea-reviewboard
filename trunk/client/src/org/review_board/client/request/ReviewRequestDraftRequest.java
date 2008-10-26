/**
 * @(#)ReviewRequestDraftRequest.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.client.request;

import org.apache.commons.httpclient.methods.GetMethod;
import org.review_board.client.json.ReviewRequestDraft;
import org.review_board.client.json.Response;
import org.review_board.client.ReviewBoardException;

public class ReviewRequestDraftRequest extends ReviewBoardRequest
{
    public ReviewRequestDraftRequest( final String baseUri, final int reviewRequestId )
    {
        m_method = new GetMethod(
            baseUri + "reviewrequests/" + reviewRequestId + "/draft/" );
    }

    public ReviewRequestDraft getDraft() throws ReviewBoardException
    {
        final Response response = getResponse();
        return new ReviewRequestDraft( response.getJSONObject( "review_request_draft" ) );
    }
}
