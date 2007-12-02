package org.review_board.idea.plugin.form;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.review_board.idea.plugin.settings.ProjectSettings;
import org.review_board.idea.plugin.settings.UserSettings;

public class ConfigurationForm
{
    private JTextField m_serverUrl;

    private JTextField m_username;

    private JTextField m_password;

    private JPanel m_rootComponent;

    private UserSettings m_userSettings;

    private ProjectSettings m_projectSettings;

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
        m_userSettings.setPassword( m_password.getText() );
    }

    public boolean isModified()
    {
        if ( m_serverUrl.getText() != null ? !m_serverUrl.getText()
            .equals( m_projectSettings.getServerUrl() )
            : m_projectSettings.getServerUrl() != null ) return true;
        if ( m_username.getText() != null ? !m_username.getText()
            .equals( m_userSettings.getUsername() )
            : m_userSettings.getUsername() != null ) return true;
        if ( m_password.getText() != null ? !m_password.getText()
            .equals( m_userSettings.getPassword() )
            : m_userSettings.getPassword() != null ) return true;
        return false;
    }
}
