package org.review_board.idea.plugin;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JComponent;

public class ReviewBoardConfigurationForm
{
    private JTextField m_uri;

    private JTextField m_username;

    private JTextField m_password;

    private JPanel m_rootComponent;

    public JComponent getRootComponent()
    {
        return m_rootComponent;
    }

    public void setData( ReviewBoardPlugin data )
    {
        m_uri.setText( data.getUri() );
        m_username.setText( data.getUsername() );
        m_password.setText( data.getPassword() );
    }

    public void getData( ReviewBoardPlugin data )
    {
        data.setUri( m_uri.getText() );
        data.setUsername( m_username.getText() );
        data.setPassword( m_password.getText() );
    }

    public boolean isModified( ReviewBoardPlugin data )
    {
        if ( m_uri.getText() != null ? !m_uri.getText().equals( data.getUri() )
            : data.getUri() != null ) return true;
        if ( m_username.getText() != null ? !m_username.getText()
            .equals( data.getUsername() ) : data.getUsername() != null ) return true;
        if ( m_password.getText() != null ? !m_password.getText()
            .equals( data.getPassword() ) : data.getPassword() != null ) return true;
        return false;
    }
}
