/*
* @(#)RepositoryFinder.java
*
* Copyright 2007 Tripwire, Inc. All Rights Reserved.
*
* ver 1.0 Nov 22, 2007 plumpy
*/
package org.review_board.idea.plugin;

import java.util.Collection;
import org.review_board.client.json.Repository;

public interface RepositoryFinder
{
    public Repository findRepository( Collection<Repository> repositories );
}

// eof: RepositoryFinder.java