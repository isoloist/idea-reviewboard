/*
 * @(#)IntelliJPatchMaker.java
 *
 * ver 1.0 Apr 10, 2009 plumpy
 */

package org.review_board.idea.plugin.patchmaker;

import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.PatchBuilder;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.review_board.client.ReviewBoardException;

public class IntelliJPatchMaker implements PatchMaker
{
    @NotNull
    private Project m_project;
    
    public IntelliJPatchMaker( @NotNull final Project project )
    {
        m_project = project;
    }

    public String getPatch( Collection<Change> changes )
        throws ReviewBoardException, VcsException, IOException
    {
        VirtualFile baseDir = getBaseDir();

        // Here's some IDEA code that isn't part of openapi, but which I was
        // encouraged to use:
        //   http://www.jetbrains.net/jira/browse/IDEADEV-21340
        //
        // The second argument is the path which will be stripped from the diff
        // filenames.  So if you are diffing two versions of
        // "/home/plumpy/hello/Foo.java" and the second argument is "/home/plumpy",
        // then the filename in the diff will be "hello/Foo.java".
        //
        // The third argument tells it to ignore the fact that a file moved, treating
        // it as a normal modification.  If this is false, the patch contains a
        // deletion of the old file and an addition of the new file.
        //
        // The fourth argument tells it whether or not to reverse the order of the
        // patch.
        final List<FilePatch> patches =
            PatchBuilder.buildPatch( changes, baseDir.getPresentableUrl(), true, false );

        fixRevisionMarkers( patches );

        final StringWriter writer = new StringWriter( 2048 );
        UnifiedDiffWriter.write( patches, writer, "\n" ); // more non-openAPI code
        return writer.toString();
    }

    void fixRevisionMarkers( List<FilePatch> patches )
        throws ReviewBoardException, VcsException
    {
    }

    @NotNull
    protected VirtualFile getBaseDir() throws ReviewBoardException
    {
        VirtualFile baseDir = m_project.getBaseDir();
        if ( baseDir == null )
        {
            throw new ReviewBoardException(
                "Couldn't get base directory from project!" );
        }
        return baseDir;
    }

    @NotNull
    public Project getProject()
    {
        return m_project;
    }
}
