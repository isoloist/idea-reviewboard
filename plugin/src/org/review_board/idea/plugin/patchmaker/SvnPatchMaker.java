/*
 * @(#)SvnPatchMaker.java
 *
 * ver 1.0 Apr 10, 2009 plumpy
 */

package org.review_board.idea.plugin.patchmaker;

import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.review_board.client.ReviewBoardException;

public class SvnPatchMaker extends IntelliJPatchMaker
{
    public SvnPatchMaker( @NotNull final Project project )
    {
        super( project );
    }

    @Override
    void fixRevisionMarkers( List<FilePatch> patches )
    {
        for ( FilePatch patch : patches )
        {
            if ( !patch.getBeforeVersionId().startsWith( "(" ) )
            {
                // If the "before" version isn't a revision number, then this is an added
                // file. In SVN diff, those get marked as revision 0.
                patch.setBeforeVersionId( "(revision 0)" );
                patch.setAfterVersionId( "(revision 0)" );
            }
            else if ( !patch.getAfterVersionId().startsWith( "(" ) )
            {
                // Any other non-revision string is a date in IntelliJ but shows up as
                // "(working copy)" in SVN diffs. Review Board actually handles that fine,
                // but as long as we're hacking up the patch, let's fix it.
                patch.setAfterVersionId( "(working copy)" );
            }
        }
    }
}
