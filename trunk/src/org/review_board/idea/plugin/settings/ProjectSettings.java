/*
* @(#)ProjectSettings.java
*
* ver 1.0 Sep 21, 2007 plumpy
*/
package org.review_board.idea.plugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(
    name = "ReviewBoard.ProjectSettings",
    storages = {
    @Storage(
        id = "other",
        file = "$PROJECT_FILE$"
    )}
)
public class ProjectSettings implements PersistentStateComponent<ProjectSettings>
{
    @NotNull
    private String m_serverUrl = "";

    @NotNull
    public String getServerUrl()
    {
        return m_serverUrl;
    }

    public void setServerUrl( @NotNull String serverUrl )
    {
        m_serverUrl = serverUrl;
    }

    public ProjectSettings getState()
    {
        return this;
    }

    public void loadState( ProjectSettings state )
    {
        XmlSerializerUtil.copyBean( state, this );
    }
}