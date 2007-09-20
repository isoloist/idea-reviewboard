package org.review_board.idea.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReviewBoardPlugin implements ProjectComponent, Configurable,
    JDOMExternalizable
{
    private static final String URI_ATTRIBUTE = "uri";
    private static final String USERNAME_ATTRIBUTE = "username";
    private static final String PASSWORD_ATTRIBUTE = "password";

    private String m_uri = "";

    private String m_username = "";

    private String m_password = "";

    private ReviewBoardConfigurationForm m_form;

    private final Project m_project;

    public ReviewBoardPlugin( final Project project )
    {
        m_project = project;
    }

    public String getUri()
    {
        return m_uri;
    }

    public void setUri( final String uri )
    {
        m_uri = uri;
    }

    public String getUsername()
    {
        return m_username;
    }

    public void setUsername( final String username )
    {
        m_username = username;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword( final String password )
    {
        m_password = password;
    }

    public void initComponent() {
        ChangeListManager.getInstance( m_project )
            .registerCommitExecutor( new ReviewBoardCommitExecutor( m_project ) );
    }

    public void disposeComponent() { }

    @NotNull
    public String getComponentName()
    {
        return "ReviewBoard";
    }

    public void projectOpened() { }

    public void projectClosed() { }

    @Nls
    public String getDisplayName()
    {
        return "Review Board";
    }

    @Nullable
    public Icon getIcon()
    {
        return null;
    }

    @Nullable
    @NonNls
    public String getHelpTopic()
    {
        return null;
    }

    public JComponent createComponent()
    {
        if( m_form == null )
            m_form = new ReviewBoardConfigurationForm();

        return m_form.getRootComponent();
    }

    public boolean isModified()
    {
        return m_form != null && m_form.isModified( this );
    }

    public void apply() throws ConfigurationException
    {
        if( m_form != null )
            m_form.getData( this );
    }

    public void reset()
    {
        if( m_form != null )
            m_form.setData( this );
    }

    public void disposeUIResources()
    {
        m_form = null;
    }

    public ReviewBoardPlugin getState()
    {
        return this;
    }

    public void loadState( final ReviewBoardPlugin object )
    {
        XmlSerializerUtil.copyBean( object, this );
    }

    public void readExternal( final Element element ) throws InvalidDataException
    {
        m_uri = JDOMExternalizer.readString( element, URI_ATTRIBUTE );
        m_uri = m_uri == null ? "" : m_uri;
        m_username = JDOMExternalizer.readString( element, USERNAME_ATTRIBUTE );
        m_username = m_username == null ? "" : m_username;
        m_password = JDOMExternalizer.readString( element, PASSWORD_ATTRIBUTE );
        m_password = m_password == null ? "" : m_password;
    }

    public void writeExternal( final Element element ) throws WriteExternalException
    {
        JDOMExternalizer.write( element, URI_ATTRIBUTE, m_uri );
        JDOMExternalizer.write( element, USERNAME_ATTRIBUTE, m_username );
        JDOMExternalizer.write( element, PASSWORD_ATTRIBUTE, m_password );
    }
}
