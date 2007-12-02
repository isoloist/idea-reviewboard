/*
* @(#)LoginRequest.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.client.request;

import org.apache.commons.httpclient.methods.PostMethod;

public class LoginRequest extends ReviewBoardRequest
{
    public LoginRequest( final String baseUri, final String username, final String password )
    {
        final PostMethod method = new PostMethod( baseUri + "accounts/login/" );
        method.setParameter( "username", username );
        method.setParameter( "password", password );
        m_method = method;
    }
}