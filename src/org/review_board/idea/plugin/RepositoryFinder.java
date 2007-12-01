/*
* @(#)RepositoryFinder.java
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin;

import java.util.Map;
import org.review_board.client.ReviewBoardException;

public interface RepositoryFinder
{
    public Map<String, Object> findRepository()
        throws ReviewBoardException;
}