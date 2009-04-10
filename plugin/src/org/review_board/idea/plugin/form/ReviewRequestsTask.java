/**
 * @(#)ReviewRequestsTask.java
 *
 * Copyright 2008 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Oct 26, 2008 plumpy
 */

package org.review_board.idea.plugin.form;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import java.util.List;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.ReviewRequest;
import org.review_board.client.json.ReviewRequestDraft;
import org.review_board.client.json.ReviewRequestOrDraft;
import org.review_board.client.request.ReviewRequestsRequest;
import org.review_board.idea.plugin.ReviewBoardPlugin;

public class ReviewRequestsTask extends Task.Modal
{
    private final Project m_project;

    private boolean m_finished = false;

    private ReviewBoardException m_error;

    private List<ReviewRequestOrDraft> m_reviewRequestOrDrafts;

    public ReviewRequestsTask( @Nullable Project project )
    {
        super( project, "Loading Review Requests...", true );
        m_project = project;
    }

    public void run( @NotNull ProgressIndicator indicator )
    {
        indicator.setIndeterminate( true );
        m_finished = false;
        m_reviewRequestOrDrafts = null;
        try
        {
            indicator.setText( "Loading Review Requests..." );

            ReviewBoardClient client = ReviewBoardPlugin.getClient( m_project );

            if ( indicator.isCanceled() )
                return;

            final List<ReviewRequest> reviewRequests = client.getReviewRequests(
                ReviewRequestsRequest.GetType.FROM_USER, client.getUsername() );

            final List<ReviewRequestOrDraft> reviewRequestsOrDrafts =
                new ArrayList<ReviewRequestOrDraft>();
            for( ReviewRequest reviewRequest : reviewRequests )
            {
                final ReviewRequestDraft draft =
                    client.getReviewRequestDraft( reviewRequest.getId() );
                reviewRequestsOrDrafts.add( draft == null ? reviewRequest : draft );
            }
            m_reviewRequestOrDrafts = reviewRequestsOrDrafts;
        }
        catch ( ReviewBoardException e )
        {
            m_error = e;
        }
        finally
        {
            m_finished = true;
        }
    }

    @NotNull
    public List<ReviewRequestOrDraft> getReviewRequests() throws ReviewBoardException
    {
        if ( !m_finished )
        {
            throw new IllegalStateException(
                "can't get review requests because task isn't finished!" );
        }

        if( m_error != null )
            throw m_error;

        return m_reviewRequestOrDrafts;
    }
}