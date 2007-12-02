/*
* @(#)ReviewForm.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Dec 2, 2007 plumpy
*/

package org.review_board.idea.plugin.form;

import java.awt.Color;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.review_board.client.json.Repository;
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
        m_description.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
        m_testingDone.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
        m_repositoryPanel.setBorder( BorderFactory.createTitledBorder( "Repository" ) );
        m_reviewersPanel.setBorder( BorderFactory.createTitledBorder( "Reviewers" ) );
        m_infoPanel.setBorder( BorderFactory.createTitledBorder( "Info" ) );
    }

    public JPanel getRootComponent()
    {
        return m_rootComponent;
    }

    public void setRepositories( final Collection<Repository> repositories,
        final RepositoryFinder.FoundRepositoryInfo repositoryInfo )
    {
        for( Repository repository : repositories )
        {
            m_repository.addItem( repository );
        }

        m_repository.setSelectedItem( repositoryInfo.getRepository() );
        m_baseDiffPath.setText( repositoryInfo.getBaseDiffPath() );
    }

    public void setCommitMessage( final String commitMessage )
    {
        if( commitMessage.indexOf( '\n' ) == -1 && commitMessage.length() <= 300 )
            m_summary.setText( commitMessage );
        else
            m_description.setText( commitMessage );
    }
}