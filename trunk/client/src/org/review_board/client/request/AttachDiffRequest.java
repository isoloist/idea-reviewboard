/*
* @(#)AttachDiffRequest.java
*
* ver 1.0 Feb 7, 2008 plumpy
*/

package org.review_board.client.request;

import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class AttachDiffRequest extends ReviewBoardRequest
{
    public AttachDiffRequest( final String baseUrl, final int reviewRequestId,
        final SetFieldsRequest.ReviewRequestData review )
    {
        final String baseDir = review.getBaseDiffPath();
        final String diff = review.getDiff();
        
        final PostMethod method = new PostMethod(
            baseUrl + "reviewrequests/" + reviewRequestId + "/diff/new/" );

        try
        {
            final ByteArrayPartSource source =
                new ByteArrayPartSource( "diff", diff.getBytes( "UTF-8" ) );
            final FilePart filePart =
                new FilePart( "path", source, FilePart.DEFAULT_CONTENT_TYPE, "UTF-8" );

            // We have one parameter and one file.
            final Part[] parts = new Part[]{
                new StringPart( "basedir", baseDir ),
                filePart
            };

            method.setRequestEntity(
                new MultipartRequestEntity( parts, method.getParams() ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            // No "UTF-8"? Okay, whatever, you have some weird-ass Java implementation.
        }

        m_method = method;
    }
}