/*
 * @(#)PatchMaker.java
 *
 * ver 1.0 Apr 10, 2009 plumpy
 */

package org.review_board.idea.plugin.patchmaker;

import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.VcsException;
import java.util.Collection;
import java.io.IOException;
import org.review_board.client.ReviewBoardException;

public interface PatchMaker
{
    public String getPatch( Collection<Change> changes )
        throws ReviewBoardException, VcsException, IOException;
}
