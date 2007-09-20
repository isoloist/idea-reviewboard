package org.review_board.client.method;

import java.io.IOException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;

public abstract class ReviewBoardMethod extends PostMethod
{
    protected static final String JSON_API_LOCATION = "/api/json/";

    protected ReviewBoardMethod( final String baseUri )
        throws ReviewBoardException
    {
        super();
        final String uri = baseUri + JSON_API_LOCATION + getMethodApiUrl();
        try
        {
            setURI( new URI( uri, true, getParams().getUriCharset() ) );
        }
        catch ( URIException e )
        {
            throw new ReviewBoardException(
                "Invalid uri '" + uri + "': " + e.getMessage() );
        }
    }

    public Response getResponse() throws ReviewBoardException
    {
        try
        {
            // TODO figure out how to cache this somehow so we don't parse it every time
            // getResponse() is called...
            return new Response( new String( getResponseBody() ) );
        }
        catch ( IOException e )
        {
            throw new ReviewBoardException( e );
        }
    }

    protected abstract String getMethodApiUrl();
}
