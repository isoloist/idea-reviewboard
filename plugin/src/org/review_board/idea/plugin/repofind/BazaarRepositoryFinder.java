/*
 * @(#)BazaarRepositoryFinder.java
 *
 * Copyright 2006 Tripwire, Inc. All Rights Reserved.
 *
 * ver 1.0 Apr 13, 2009 plumpy
 */

package org.review_board.idea.plugin.repofind;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;
import org.jetbrains.annotations.NotNull;
import org.review_board.client.json.Repository;
import org.review_board.idea.plugin.ReviewBoardPlugin;
import org.vcs.bazaar.client.commandline.commands.Info;
import org.vcs.bazaar.client.commandline.internal.CommandRunner;
import org.vcs.bazaar.client.commandline.internal.ShellCommandRunner;
import org.vcs.bazaar.client.core.BranchLocation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BazaarRepositoryFinder implements RepositoryFinder
{
    // These two are from reading RFC 2396. Surely there's an easier way?
    private static final String SCHEMA = "[a-zA-Z][a-zA-Z0-9+\\-.]*";

    private static final String AUTH =
        "(?:[a-zA-Z0-9\\-_.!~*'();:&=+$,]|(?:%[0-9a-fA-F]{2}))+";

    // The part before the auth will be in group 1 and the part after in group 2.
    private static final Pattern STRIP_AUTH_PATTERN =
        Pattern.compile( "^(" + SCHEMA + "://)" + AUTH + "@(.*)$");

    @NotNull
    private final Project m_project;

    public BazaarRepositoryFinder( @NotNull final Project project )
    {
        m_project = project;
    }

    public FoundRepositoryInfo findRepository(
        Collection<Repository> repositories, ProgressIndicator indicator )
    {
        indicator.setText2( "Getting info for local checkout" );
        if ( indicator.isCanceled() )
            return null;

        final VirtualFile baseDir = m_project.getBaseDir();
        if ( baseDir == null )
            return null;

        if( ReviewBoardPlugin.DEBUG )
            System.out.println( "Getting URI of local branch/checkout from bzr." );

        String localUri = getLocalUri( baseDir.getPath() );

        if( ReviewBoardPlugin.DEBUG )
        {
            System.out.println( "Retrieved URI of local branch/checkout from bzr: "
                + localUri );
        }

        if ( localUri == null )
            return null;

        localUri = stripAuthFromUri( localUri );

        for ( Repository repository : filterBzrRepositories( repositories ) )
        {
            final String repositoryUri = stripAuthFromUri( repository.getPath() );
            String relative =
                RepoFindUtil.getRelativePath( localUri, repositoryUri );

            if ( relative != null )
                return new FoundRepositoryInfo( repository, relative );
        }

        return null;
    }

    private String getLocalUri( final String basePath )
    {
        final CommandRunner runner = new ShellCommandRunner( true );
        final Info info = new Info( new File( basePath ),
            new BranchLocation( basePath ) );
        try
        {
            if( ReviewBoardPlugin.DEBUG )
                System.out.println( "Executing bzr xmlinfo command." );

            info.execute( runner );

            if( ReviewBoardPlugin.DEBUG )
                System.out.println( "Done executing bzr xmlinfo command." );

            final String output = info.getStandardOutput();

            Document doc = getDocument( output );

            if( ReviewBoardPlugin.DEBUG )
                System.out.println( "Parsing output of xmlinfo command." );

            XPath xpath = XPathFactory.newInstance().newXPath();

            String result = xpath.evaluate( "//checkout_of_branch", doc );
            if( result != null && !result.equals( "" ) )
                return result + getAdditionalPathForCheckout( doc, xpath, basePath );

            return xpath.evaluate( "//parent_branch", doc )
                + getAdditionalPathForBranch( doc, xpath, basePath );
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    private String getAdditionalPathForCheckout( final Document doc,
        final XPath xpath, final String basePath ) throws XPathExpressionException
    {
        return getAdditionalPath( doc, xpath, basePath, "//repository_checkout_root" );
    }

    private String getAdditionalPathForBranch( final Document doc,
        final XPath xpath, final String basePath ) throws XPathExpressionException
    {
        return getAdditionalPath( doc, xpath, basePath, "//repository_branch" );
    }

    private String getAdditionalPath( final Document doc, final XPath xpath,
        final String basePath, final String xmlPath ) throws XPathExpressionException
    {
        String checkoutRoot = xpath.evaluate( xmlPath, doc );

        if ( checkoutRoot == null || checkoutRoot.equals( "" ) || checkoutRoot
            .equals( "." ) )
        {
            return "";
        }

        return RepoFindUtil.getRelativePath( basePath, checkoutRoot );
    }

    private Document getDocument( String output )
        throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse( new ByteArrayInputStream( output.getBytes( "UTF-8" ) ) );
    }

    @NotNull
    private Collection<Repository> filterBzrRepositories(
        final Collection<Repository> repositories )
    {
        return RepoFindUtil.filterRepositories( repositories, "Bazaar" );
    }

    @NotNull
    private static String stripAuthFromUri( final String uri )
    {
        final Matcher matcher = STRIP_AUTH_PATTERN.matcher( uri );
        if( matcher.find() )
        {
            return matcher.group( 1 ) + matcher.group( 2 );
        }
        else
        {
            return uri;
        }
    }
}
