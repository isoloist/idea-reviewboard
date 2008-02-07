/*
* @(#)PublishRequest.java
*
* ver 1.0 Feb 7, 2008 plumpy
*/

package org.review_board.client.request;

import org.apache.commons.httpclient.methods.PostMethod;

public class PublishRequest extends ReviewBoardRequest
{
    public PublishRequest( final String baseUri, final int reviewRequestId )
    {
        m_method = new PostMethod(
            baseUri + "r/" + reviewRequestId + "/publish/" );
    }
}