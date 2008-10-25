/**
 * @(#)DeleteReviewRequestRequest.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 25, 2008 plumpy
 */

package org.review_board.client.request;

import org.apache.commons.httpclient.methods.PostMethod;

public class DeleteReviewRequestRequest extends ReviewBoardRequest
{
    public DeleteReviewRequestRequest( final String baseUri, final int reviewRequestId )
    {
        m_method = new PostMethod(
            baseUri + "reviewrequests/" + reviewRequestId + "/delete/" );
    }
}
