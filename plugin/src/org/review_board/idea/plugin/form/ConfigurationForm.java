package org.review_board.idea.plugin.form;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import org.review_board.idea.plugin.settings.ProjectSettings;
import org.review_board.idea.plugin.settings.UserSettings;

public class ConfigurationForm
{
    private JTextField m_serverUrl;

    private JTextField m_username;

    private JPasswordField m_password;

    private JPanel m_rootComponent;

    private final UserSettings m_userSettings;

    private final ProjectSettings m_projectSettings;

    public ConfigurationForm( UserSettings userSettings,
        ProjectSettings projectSettings )
    {
        m_userSettings = userSettings;
        m_projectSettings = projectSettings;
    }

    public JComponent getRootComponent()
    {
        return m_rootComponent;
    }

    public void setData()
    {
        m_serverUrl.setText( m_projectSettings.getServerUrl() );
        m_username.setText( m_userSettings.getUsername() );
        m_password.setText( m_userSettings.getPassword() );
    }

    public void getData()
    {
        m_projectSettings.setServerUrl( m_serverUrl.getText() );
        m_userSettings.setUsername( m_username.getText() );
        m_userSettings.setPassword( new String( m_password.getPassword() ) );
    }

    @SuppressWarnings({"RedundantIfStatement"})
    public boolean isModified()
    {
        if( !equals( m_serverUrl.getText(), m_projectSettings.getServerUrl() ) )
            return true;
        if( !equals( m_username.getText(), m_userSettings.getUsername() ) )
            return true;
        if ( !equals( new String( m_password.getPassword() ),
            m_userSettings.getPassword() ) )
            return true;

        return false;
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    boolean equals( final String s1, final String s2 )
    {
        if( s1 == null || s1.equals( "" ) )
            return s2 == null || s2.equals( "" );
        return s1.equals( s2 );
    }
}
