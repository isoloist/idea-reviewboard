/*
* @(#)UserSettings.java
*
* Copyright 2006 Tripwire, Inc. All Rights Reserved.
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
    name = "ReviewBoard.UserSettings",
    storages = {
    @Storage(
        id = "other",
        file = "$WORKSPACE_FILE$"
    )}
)
public class UserSettings implements PersistentStateComponent<UserSettings>
{
    @NotNull
    private String m_username = "";

    @NotNull
    private String m_password = "";

    @NotNull
    public String getUsername()
    {
        return m_username;
    }

    public void setUsername( @NotNull String username )
    {
        m_username = username;
    }

    @NotNull
    public String getPassword()
    {
        return m_password;
    }

    public void setPassword( @NotNull String password )
    {
        m_password = password;
    }

    public UserSettings getState()
    {
        return this;
    }

    public void loadState( UserSettings state )
    {
        XmlSerializerUtil.copyBean( state, this );
    }
}

// eof: UserSettings.java