/*
* @(#)ReviewForm.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Dec 2, 2007 plumpy
*/

package org.review_board.idea.plugin.form;

import com.intellij.openapi.util.text.StringUtil;
import java.util.Collection;
import java.util.Collections;
import javax.swing.BorderFactory;
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
import org.review_board.client.json.Repository;
import org.review_board.client.json.ReviewRequest;
import org.review_board.idea.plugin.repofind.RepositoryFinder;

public class ReviewForm
{
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

    public ReviewForm()
    {
        m_repositoryPanel.setBorder( BorderFactory.createTitledBorder( "Repository" ) );
        m_reviewersPanel.setBorder( BorderFactory.createTitledBorder( "Reviewers" ) );
        m_infoPanel.setBorder( BorderFactory.createTitledBorder( "Info" ) );
        m_summary
            .setDocument( new SizeLimitedDocument( ServerConstants.MAX_SUMMARY_LENGTH ) );
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

    public ReviewRequest createReviewRequest()
    {
        final ReviewRequest request = new ReviewRequest();
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