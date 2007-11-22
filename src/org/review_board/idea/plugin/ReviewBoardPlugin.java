package org.review_board.idea.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.idea.plugin.settings.ProjectSettings;
import org.review_board.idea.plugin.settings.UserSettings;

@State(
    name = "ReviewBoard",
    storages = {
    @Storage(
        id = "other",
        file = "$WORKSPACE_FILE$"
    )}
)
public class ReviewBoardPlugin implements ProjectComponent, Configurable
{
    private ReviewBoardConfigurationForm m_form;

    private final Project m_project;

    private final ProjectSettings m_projectSettings;

    private final UserSettings m_userSettings;

    public ReviewBoardPlugin( final Project project, final UserSettings userSettings,
        final ProjectSettings projectSettings )
    {
        m_project = project;
        m_projectSettings = projectSettings;
        m_userSettings = userSettings;
    }

    public static ReviewBoardPlugin getInstance( Project project )
    {
        return project.getComponent( ReviewBoardPlugin.class );
    }

    public void initComponent()
    {
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
        if ( m_form == null )
        {
            m_form =
                new ReviewBoardConfigurationForm( m_userSettings, m_projectSettings );
        }

        return m_form.getRootComponent();
    }

    public boolean isModified()
    {
        return m_form != null && m_form.isModified();
    }

    public void apply() throws ConfigurationException
    {
        if ( m_form != null )
            m_form.getData();

        final ReviewBoardClient client = new ReviewBoardClient(
            m_userSettings.getUsername(), m_userSettings.getPassword(),
            m_projectSettings.getServerUrl() );
        try
        {
            client.login();
        }
        catch( ReviewBoardException e )
        {
            throw new ConfigurationException( "Error loggin in!" );
        }
    }

    public void reset()
    {
        if ( m_form != null )
            m_form.setData();
    }

    public void disposeUIResources()
    {
        m_form = null;
    }

    public ReviewBoardPlugin getState()
    {
        return this;
    }

    public void loadState( final ReviewBoardPlugin state )
    {
        XmlSerializerUtil.copyBean( state, this );
    }
}
