/*
 * @(#)BazaarPatchMaker.java
 *
 * Copyright 2006 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Apr 10, 2009 plumpy
 */

package org.review_board.idea.plugin.patchmaker;

import org.jetbrains.annotations.NotNull;
import org.vcs.bazaar.client.commandline.internal.CommandRunner;
import org.vcs.bazaar.client.commandline.internal.ShellCommandRunner;
import org.vcs.bazaar.client.commandline.commands.VersionInfo;
import org.vcs.bazaar.client.core.BranchLocation;
import org.vcs.bazaar.client.core.BazaarClientException;
import org.review_board.client.ReviewBoardException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.vcs.VcsException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

public class BazaarPatchMaker extends IntelliJPatchMaker
{
    private static final String ADDED_FILE_REVISION = "1970-01-01 00:00:00 +0000";

    private static final Pattern REVID_PATTERN =
        Pattern.compile( "^revision-id:\\s*(\\S*)$" );

    private String m_revid;

    public BazaarPatchMaker( @NotNull final Project project )
    {
        super( project );
    }

    @Override
    void fixRevisionMarkers( List<FilePatch> patches )
        throws ReviewBoardException, VcsException
    {
        for ( FilePatch patch : patches )
        {
            if ( patch.isNewFile() )
                patch.setBeforeVersionId( ADDED_FILE_REVISION );
            else
                patch.setBeforeVersionId( getRevisionId() );
        }
    }

    private String getRevisionId() throws ReviewBoardException, VcsException
    {
        if ( m_revid == null )
        {
            final CommandRunner runner = new ShellCommandRunner( true );
            final String basePath = getBaseDir().getPath();
            final VersionInfo vinfo =
                new VersionInfo( new File( basePath ), new BranchLocation( basePath ) );
            try
            {
                vinfo.execute( runner );
                final String[] output = vinfo.getStandardOutputSplit();
                if ( output.length < 1 )
                {
                    throw new VcsException( "error getting revision ID from bzr" );
                }
                final String revidOutput = output[0];
                final Matcher revidMatcher = REVID_PATTERN.matcher( revidOutput );
                if ( !revidMatcher.find() )
                {
                    throw new VcsException( "error getting revision ID from bzr" );
                }
                m_revid = "revid:" + revidMatcher.group( 1 );
            }
            catch ( BazaarClientException e )
            {
                throw new VcsException( e );
            }
        }

        return m_revid;
    }
}
