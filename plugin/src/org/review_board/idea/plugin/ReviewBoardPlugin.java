package org.review_board.idea.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.xmlb.XmlSerializerUtil;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.review_board.client.ReviewBoardClient;
import org.review_board.client.ReviewBoardException;
import org.review_board.idea.plugin.form.ConfigurationForm;
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
    public static final boolean DEBUG = Boolean.getBoolean( "review_board.plugin.debug" );

    private ConfigurationForm m_form;

    private final ProjectSettings m_projectSettings;

    private final UserSettings m_userSettings;

    private final ReviewBoardClient m_client;

    public ReviewBoardPlugin( final UserSettings userSettings,
        final ProjectSettings projectSettings )
    {
        m_projectSettings = projectSettings;
        m_userSettings = userSettings;
        m_client = new ReviewBoardClient( userSettings.getUsername(),
            userSettings.getPassword(), projectSettings.getServerUrl() );
    }

    public static ReviewBoardPlugin getInstance( Project project )
    {
        return project.getComponent( ReviewBoardPlugin.class );
    }

    public static ReviewBoardClient getClient( Project project )
    {
        return getInstance( project ).getClient();
    }

    public ReviewBoardClient getClient()
    {
        return m_client;
    }

    public void initComponent() { }

    public void disposeComponent() { }

    @NotNull
    public String getComponentName()
    {
        return "ReviewBoardPlugin";
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
            m_form = new ConfigurationForm( m_userSettings, m_projectSettings );
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

        final String url = m_projectSettings.getServerUrl();

        m_client.setUsername( m_userSettings.getUsername() );
        m_client.setPassword( m_userSettings.getPassword() );
        m_client.setUri( url );

        if( StringUtil.isEmptyOrSpaces( url ) )
            return;
        
        try
        {
            m_client.login();
        }
        catch ( ReviewBoardException e )
        {
            throw new ConfigurationException( "Error logging in!" );
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

    public UserSettings getUserSettings()
    {
        return m_userSettings;
    }

    public ProjectSettings getProjectSettings()
    {
        return m_projectSettings;
    }

    /**
     * Return whether or not the URL, username, and password have been set for Review
     * Board on this project.
     * @param project the project
     * @return whether or not the fields have been set
     */
    public static boolean isConfigured( final Project project )
    {
        final ReviewBoardPlugin plugin = ReviewBoardPlugin.getInstance( project );
        final ProjectSettings proj = plugin.getProjectSettings();
        final UserSettings user = plugin.getUserSettings();

        return !("".equals( proj.getServerUrl() )
            || "".equals( user.getUsername().trim() )
            || "".equals( user.getPassword().trim() ));
    }

    /**
     * Show the user an error dialog if the URL, username, or password are unconfigured
     * in the Review Board settings.
     * @param project the project to check for settings
     * @return whether or not an error dialog was shown
     */
    public static boolean showErrorIfUnconfigured( final Project project )
    {
        if ( isConfigured( project ) )
        {
            return false;
        }
        else
        {
            ApplicationManager.getApplication().invokeLater( new Runnable()
            {
                public void run()
                {
                    Messages.showErrorDialog( project,
                        "You must configure the Review Board plugin in the "
                            + "\"Review Board\" section of the Project settings.",
                        "Review Board plugin isn't configured!" );
                }
            } );
            return true;
        }
    }
}
