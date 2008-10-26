/*
* @(#)ReviewForm.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Dec 2, 2007 plumpy
*/

package org.review_board.idea.plugin.form;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.ServerConstants;
import org.review_board.client.ReviewBoardException;
import org.review_board.client.json.ReviewRequestOrDraft;
import org.review_board.client.json.Repository;
import org.review_board.client.request.SetFieldsRequest;
import org.review_board.idea.plugin.ReviewBoardPlugin;
import org.review_board.idea.plugin.repofind.RepositoryFinder;

public class ReviewForm
{
    private Project m_project;

    private JTextField m_summary;

    private JTextField m_branch;

    private JTextField m_bugs;

    private JTextField m_groups;

    private JTextField m_people;

    private JTextArea m_description;

    private JTextArea m_testingDone;

    private JPanel m_rootComponent;

    private JTextField m_baseDiffPath;

    private JComboBox m_repository;

    private JPanel m_reviewersPanel;

    private JPanel m_infoPanel;

    private JPanel m_repositoryPanel;

    private JCheckBox m_publishReviewRequest;

    private JCheckBox m_openInWebBrowser;

    private JCheckBox m_updateExistingReviewRequest;

    private JComboBox m_existingReviewRequest;

    private List<ReviewRequestOrDraft> m_reviewRequestOrDrafts = null;

    public ReviewForm( final Project project )
    {
        m_project = project;

        m_repositoryPanel.setBorder( BorderFactory.createTitledBorder( "Repository" ) );
        m_reviewersPanel.setBorder( BorderFactory.createTitledBorder( "Reviewers" ) );
        m_infoPanel.setBorder( BorderFactory.createTitledBorder( "Info" ) );
        m_summary
            .setDocument( new SizeLimitedDocument( ServerConstants.MAX_SUMMARY_LENGTH ) );

        // If the user unchecks the "publish" checkbox, we'll default to opening it in a
        // web browser (since they probably want to make some changes and then publish
        // it).
        m_publishReviewRequest.getModel().addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( !m_publishReviewRequest.getModel().isSelected() )
                {
                    m_openInWebBrowser.getModel().setSelected( true );
                }
            }
        } );

        m_updateExistingReviewRequest.getModel().addActionListener(
            new UpdateReviewRequestsActionListener() );
        m_existingReviewRequest.addItemListener( new ReviewRequestItemListener( this ) );
    }

    public JPanel getRootComponent()
    {
        return m_rootComponent;
    }

    public String getBaseDiffPath()
    {
        return m_baseDiffPath.getText();
    }

    public String getBranch()
    {
        return m_branch.getText();
    }

    public String getBugs()
    {
        return m_bugs.getText();
    }

    public String getDescription()
    {
        return m_description.getText();
    }

    public String getGroups()
    {
        return m_groups.getText();
    }

    public String getPeople()
    {
        return m_people.getText();
    }

    public Repository getRepository()
    {
        return (Repository)m_repository.getSelectedItem();
    }

    String getSummary()
    {
        return m_summary.getText();
    }

    public boolean hasSummary()
    {
        return !StringUtil.isEmptyOrSpaces( getSummary() );
    }

    public boolean hasRecipient()
    {
        // must have either a user or a group
        return !StringUtil.isEmptyOrSpaces( getPeople() )
            || !StringUtil.isEmptyOrSpaces( getGroups() );
    }

    public String getTestingDone()
    {
        return m_testingDone.getText();
    }

    public boolean publishReviewRequest()
    {
        return m_publishReviewRequest.getModel().isSelected();
    }

    public boolean openInWebBrowser()
    {
        return m_openInWebBrowser.getModel().isSelected();
    }

    public boolean updateExistingReviewRequest()
    {
        return m_updateExistingReviewRequest.getModel().isSelected();
    }

    public ReviewRequestOrDraft getExistingReviewRequest()
    {
        return (ReviewRequestOrDraft)m_existingReviewRequest.getSelectedItem();
    }

    public void setRepositories( @NotNull final Collection<Repository> repositories,
        @Nullable final RepositoryFinder.FoundRepositoryInfo repositoryInfo )
    {
        for ( Repository repository : repositories )
        {
            m_repository.addItem( repository );
        }

        if ( repositoryInfo != null )
        {
            m_repository.setSelectedItem( repositoryInfo.getRepository() );
            m_baseDiffPath.setText( repositoryInfo.getBaseDiffPath() );
        }
    }

    public void setCommitMessage( final String commitMessage )
    {
        if ( commitMessage.indexOf( '\n' ) == -1
            && commitMessage.length() <= ServerConstants.MAX_SUMMARY_LENGTH )
        {
            m_summary.setText( commitMessage );
        }
        else
        {
            m_description.setText( commitMessage );
        }
    }

    public SetFieldsRequest.ReviewRequestData createReviewRequestData()
    {
        final SetFieldsRequest.ReviewRequestData request =
            new SetFieldsRequest.ReviewRequestData();
        request.setSummary( m_summary.getText() );
        request.setRepository( (Repository)m_repository.getSelectedItem() );
        request.setBaseDiffPath( m_baseDiffPath.getText() );
        request.setBranch( m_branch.getText() );
        request.setGroups( m_groups.getText() );
        request.setPeople( m_people.getText() );
        request.setBugs( m_bugs.getText() );
        request.setDescription( m_description.getText() );
        request.setTestingDone( m_testingDone.getText() );
        return request;
    }

    private List<ReviewRequestOrDraft> getReviewRequests()
    {
        if ( m_reviewRequestOrDrafts == null )
        {
            final ReviewRequestsTask task = new ReviewRequestsTask( m_project );
            final ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.run( task );

            try
            {
                m_reviewRequestOrDrafts = task.getReviewRequests();
                Collections
                    .sort( m_reviewRequestOrDrafts, new ReviewRequestUpdatedDateComparator() );
            }
            catch ( ReviewBoardException e )
            {
                ReviewBoardPlugin.showErrorDialog( m_project, e.getMessage(),
                    "Error loading Review Requests" );
            }
        }

        return m_reviewRequestOrDrafts;
    }

    private class UpdateReviewRequestsActionListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            if ( m_updateExistingReviewRequest.getModel().isSelected() )
            {
                List<ReviewRequestOrDraft> reviewRequestOrDrafts = getReviewRequests();
                if ( reviewRequestOrDrafts == null )
                {
                    // If this returned null, then an error dialog should have popped
                    // up, so it's okay to reset this without further explanation.
                    m_updateExistingReviewRequest.setSelected( false );
                }
                else
                {
                    for ( ReviewRequestOrDraft reviewRequestOrDraft : reviewRequestOrDrafts )
                        m_existingReviewRequest.addItem( reviewRequestOrDraft );

                    m_existingReviewRequest.setEnabled( true );
                }
            }
            else
            {
                m_existingReviewRequest.setEnabled( false );
                m_existingReviewRequest.removeAllItems();
            }
        }
    }

    private class ReviewRequestItemListener implements ItemListener
    {
        private final ReviewForm m_form;

        private ReviewRequestItemListener( final ReviewForm form )
        {
            m_form = form;
        }

        public void itemStateChanged( ItemEvent e )
        {
            if ( e.getStateChange() == ItemEvent.SELECTED )
            {
                final ReviewRequestOrDraft reviewRequestOrDraft =
                    (ReviewRequestOrDraft)e.getItem();
                m_form.m_summary.setText( reviewRequestOrDraft.getSummary() );
                m_form.m_description.setText( reviewRequestOrDraft.getDescription() );
                m_form.m_testingDone.setText( reviewRequestOrDraft.getTestingDone() );
                m_form.m_people.setText(
                    joinListWithCommas( reviewRequestOrDraft.getPeople() ) );
                m_form.m_groups.setText(
                    joinListWithCommas( reviewRequestOrDraft.getGroups() ) );

                // TODO more messiness because of the bugs confusion. See ReviewRequestOrDraft
                final Object bugs = reviewRequestOrDraft.getBugs();
                if( bugs instanceof String )
                    m_form.m_bugs.setText( (String)bugs );
                else
                    m_form.m_bugs.setText( joinListWithCommas( (List)bugs ) );
            }
        }

        private String joinListWithCommas( final List list )
        {
            boolean needsComma = false;
            final StringBuilder sb = new StringBuilder();
            for( Object item : list )
            {
                if( needsComma )
                {
                    sb.append( ", " );
                }

                sb.append( item.toString() );
                needsComma = true;
            }
            return sb.toString();
        }
    }

    private class ReviewRequestUpdatedDateComparator implements Comparator<ReviewRequestOrDraft>
    {
        // Sort them backwards (from newest to oldest)
        public int compare( ReviewRequestOrDraft o1, ReviewRequestOrDraft o2 )
        {
            return o2.getLastUpdated().compareTo( o1.getLastUpdated() );
        }
    }

    private class SizeLimitedDocument extends PlainDocument
    {
        private final int m_sizeLimit;

        private SizeLimitedDocument( int sizeLimit )
        {
            m_sizeLimit = sizeLimit;
        }

        public void insertString( int offs, String str, AttributeSet a )
            throws BadLocationException
        {
            if ( str == null )
                return;

            if ( getLength() + str.length() <= m_sizeLimit )
                super.insertString( offs, str, a );
        }
    }
}