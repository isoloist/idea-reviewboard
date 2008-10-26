package org.review_board.client;

import org.json.JSONException;

public class ReviewBoardException extends Exception
{
    public ReviewBoardException()
    {
    }

    public ReviewBoardException( String s )
    {
        super( s );
    }

    private ReviewBoardException( String s, Throwable throwable )
    {
        super( s, throwable );
    }

    public ReviewBoardException( Throwable throwable )
    {
        super( throwable );
    }

    public static ReviewBoardException jsonException( JSONException e )
    {
        //noinspection ThrowableInstanceNeverThrown
        return new ReviewBoardException( "Unknown response from server", e );
    }
}
