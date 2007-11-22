package org.review_board.client.method;

import java.io.IOException;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.Response;

public abstract class ReviewBoardMethod extends PostMethod
{
    protected static final String JSON_API_LOCATION = "/api/json/";

    private Response m_response;

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

    public int execute( HttpState state, HttpConnection conn )
        throws IOException
    {
        m_response = null;
        return super.execute( state, conn );
    }

    public Response getResponse() throws ReviewBoardException
    {
        if ( m_response == null )
        {
            try
            {
                m_response = new Response( new String( getResponseBody() ) );
            }
            catch ( IOException e )
            {
                throw new ReviewBoardException( e );
            }
        }

        return m_response;
    }

    protected abstract String getMethodApiUrl();
}
