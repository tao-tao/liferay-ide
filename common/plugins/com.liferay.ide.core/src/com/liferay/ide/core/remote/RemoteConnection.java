/*******************************************************************************
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Contributors:
 * 		Gregory Amerson - initial implementation and ongoing maintenance
 *******************************************************************************/

package com.liferay.ide.core.remote;

import com.liferay.ide.core.LiferayCore;
import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.core.util.StringPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.resources.ResourcesPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Gregory Amerson
 * @author Tao Tao
 */
public class RemoteConnection implements IRemoteConnection
{

    private String hostname;
    private HttpClient httpClient;
    private int httpPort;
    private String password;
    private String username;
    private FileWriter traceLog;

    protected Object deleteJSONAPI( Object... args ) throws APIException
    {
        trace("deleteJSONAPI(): "+ args+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        if( !( args[0] instanceof String ) )
        {
            throw new IllegalArgumentException( "First argument must be a string." ); //$NON-NLS-1$
        }

        HttpDelete deleteAPIMethod = new HttpDelete();

        trace("deleteJSONAPI() return: "+httpJSONAPI( deleteAPIMethod, args )+"\n"); //$NON-NLS-1$ //$NON-NLS-2$)
        
        return httpJSONAPI( deleteAPIMethod, args );
    }

    public void trace( String string )
    {
        String filePath = ResourcesPlugin.getWorkspace().getRoot().getLocation() + "/.metadata"; //$NON-NLS-1$
        String fileName = ".remoteLog"; //$NON-NLS-1$
        File log =new File( filePath, fileName );
        
        try
        {
            traceLog = new FileWriter( log, true);

            traceLog.write( string );
            
            traceLog.flush();

        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    public String getHost()
    {
        trace( "getHost(): "+hostname+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return hostname;
    }

    private HttpClient getHttpClient()
    {
        trace("getHttpClient()"+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        
        if( this.httpClient == null )
        {
            trace("httpClient == null"+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
            
            DefaultHttpClient newDefaultHttpClient = null;

            if( getUsername() != null || getPassword() != null )
            {
                trace("getUsername(): "+getUsername()+"\n\t getPassword(): "+getPassword()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                try
                {
                    final IProxyService proxyService = LiferayCore.getProxyService();
                    trace("IproxyServce: "+proxyService); //$NON-NLS-1$

                    URI uri = new URI( "http://" + getHost() + ":" + getHttpPort()); //$NON-NLS-1$ //$NON-NLS-2$
                    trace("Http proxy uri: "+uri+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    IProxyData[] proxyDataForHost = proxyService.select( uri );
                    trace("get http proxy data: "+proxyDataForHost+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                    for( IProxyData data : proxyDataForHost )
                    {
                        if( data.getHost() != null && data.getPort() > 0 )
                        {
                            trace("IProxyData's host and port for http proxy: "+data.getHost()+":"+data.getPort()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            SchemeRegistry schemeRegistry = new SchemeRegistry();
                            schemeRegistry.register( new Scheme(
                                "http", data.getPort(), PlainSocketFactory.getSocketFactory() ) ); //$NON-NLS-1$
                            trace("The schemeRegistry value: "+schemeRegistry+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                            PoolingClientConnectionManager cm = new PoolingClientConnectionManager( schemeRegistry );

                            trace("PoolingClientConnectionManager creating: "+cm+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                            cm.setMaxTotal( 200 );
                            cm.setDefaultMaxPerRoute( 20 );
                            
                            trace("PoolingClientConnectionManager: "+cm.getTotalStats()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                            DefaultHttpClient newHttpClient = new DefaultHttpClient( cm );
                            trace("HttpClient: "+newHttpClient.getRequestInterceptorCount()+"\n\tgetResponseInterceptorCount(): "+newHttpClient.getResponseInterceptorCount()+"\n\tParams: "+newHttpClient.getParams()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                            HttpHost proxy = new HttpHost( data.getHost(), data.getPort() );
                            trace("HttpHost: "+proxy.getHostName()+":"+proxy.getPort()+"\n\t Scheme: "+proxy.getSchemeName()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

                            newHttpClient.getParams().setParameter( ConnRoutePNames.DEFAULT_PROXY, proxy );
                            trace("Httphost parameter: "+newHttpClient.getParams()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                            newDefaultHttpClient = newHttpClient;
                            break;
                        }
                    }

                    if( newDefaultHttpClient == null )
                    {
                        trace("newDefualtHttpClient: "+newDefaultHttpClient+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                        uri = new URI( "SOCKS://" + getHost() + ":" + getHttpPort() ); //$NON-NLS-1$ //$NON-NLS-2$
                        trace("uri for socks proxyData: "+uri+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                        proxyDataForHost = proxyService.select( uri );
                        trace("socks proxy data: "+proxyDataForHost+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                        for( IProxyData data : proxyDataForHost )
                        {
                            trace("IProxyData's host and port for socks proxy: "+data.getHost()+":"+data.getPort()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            if( data.getHost() != null )
                            {
                                DefaultHttpClient newHttpClient = new DefaultHttpClient();
                                newHttpClient.getParams().setParameter( "socks.host", data.getHost() ); //$NON-NLS-1$
                                newHttpClient.getParams().setParameter( "socks.port", data.getPort() ); //$NON-NLS-1$
                                newHttpClient.getConnectionManager().getSchemeRegistry().register(
                                    new Scheme( "socks", data.getPort(), PlainSocketFactory.getSocketFactory() ) ); //$NON-NLS-1$
                                trace( "socks proxy authSchemes: "+newHttpClient.getAuthSchemes()+"\n\tsocks proxy params: "+newHttpClient.getParams());  //$NON-NLS-1$//$NON-NLS-2$

                                newDefaultHttpClient = newHttpClient;
                                break;
                            }
                        }
                    }
                }
                catch( URISyntaxException e )
                {
                    LiferayCore.logError( "Unable to read proxy data", e ); //$NON-NLS-1$
                }

                if( newDefaultHttpClient == null )
                {
                    newDefaultHttpClient = new DefaultHttpClient();
                }

                newDefaultHttpClient.getCredentialsProvider().setCredentials(
                    new AuthScope( getHost(), getHttpPort() ),
                    new UsernamePasswordCredentials( getUsername(), getPassword() ) );
                trace( "newDefaultHttpClient authSchemes: " + newDefaultHttpClient.getAuthSchemes() + "\n\tcredentialsProvider: " + newDefaultHttpClient.getCredentialsProvider() + "\n\tparams: "+newDefaultHttpClient.getParams() + "\n\tProxyAuthenticationStrategy(): "+newDefaultHttpClient.getProxyAuthenticationStrategy() +"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

                this.httpClient = newDefaultHttpClient;
                trace( "httpClient params: " + this.httpClient.getParams() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return this.httpClient;
    }

    public int getHttpPort()
    {
        trace("getHttpPort(): "+httpPort+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return httpPort;
    }

    protected String getHttpResponse( HttpUriRequest request ) throws Exception
    {
        HttpResponse response = getHttpClient().execute( request );
        trace("HttpResponse params: "+response.getParams()+"\n\tprotocalVersion: "+response.getProtocolVersion()+"\n\tstatusLine: "+response.getStatusLine().getStatusCode()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        int statusCode = response.getStatusLine().getStatusCode();

        if( statusCode == HttpStatus.SC_OK )
        {
            HttpEntity entity = response.getEntity();
            trace("getEntity(): "+ entity+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

            String body = CoreUtil.readStreamToString( entity.getContent() );
            trace("entity value: "+ body+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

            EntityUtils.consume( entity );

            return body;
        }
        else
        {
            return response.getStatusLine().getReasonPhrase();
        }
    }

    protected Object getJSONAPI( Object... args ) throws APIException
    {
        if( !( args[0] instanceof String ) )
        {
            throw new IllegalArgumentException( "First argument must be a string." +"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        HttpGet getAPIMethod = new HttpGet();
        trace("HttpGet: "+getAPIMethod.getMethod()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

        trace("The return value in httpJSONAPI: "+ httpJSONAPI( getAPIMethod, args ) +"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return httpJSONAPI( getAPIMethod, args );
    }

    private Object getJSONResponse( String response )
    {
        Object retval = null;

        try
        {
            retval = new JSONObject( response );
            trace("getJSONResponse returns: "+ retval+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch( JSONException e )
        {
            try
            {
                retval = new JSONArray( response );
            }
            catch( JSONException e1 )
            {
            }
        }

        return retval;
    }

    public String getPassword()
    {
        trace("getPassword(): "+password+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return password;
    }

    public String getUsername()
    {
        trace("getUsername(): "+username+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return username;
    }

    protected Object httpJSONAPI( Object... args ) throws APIException
    {
        trace("httpJSONAPI() params: "+ args+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        if( !( args[0] instanceof HttpRequestBase ) )
        {
            throw new IllegalArgumentException( "First argument must be a HttpRequestBase." ); //$NON-NLS-1$
        }

        Object retval = null;
        String api = null;
        Object[] params = new Object[0];

        trace("value in httpJSONAPI args[0]: "+ args[0]+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        final HttpRequestBase request = (HttpRequestBase) args[0];
        trace("HttpRequestBase getMethod(): "+request.getMethod()+"\n\tHttpRequestBase: "+request+"\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if( args[1] instanceof String )
        {
            trace("if args[1] instanceof String: "+ (args[1] instanceof String)+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
            api = args[1].toString();
            trace("\tvalue in httpJSONAPI params args[1]: "+api+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if( args[1] instanceof Object[] )
        {
            trace("if args[1] instancof Obejct: "+ ( args[1] instanceof Object[] ) +"\n"); //$NON-NLS-1$ //$NON-NLS-2$
            params = (Object[]) args[1];
            api = params[0].toString();
            trace("\tvalue in httpJSONAPI params args[1]: "+ api+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
            throw new IllegalArgumentException( "2nd argument must be either String or Object[]" ); //$NON-NLS-1$
        }

        try
        {
            final URIBuilder builder = new URIBuilder();
            builder.setScheme( "http" ); //$NON-NLS-1$
            builder.setHost( getHost() );
            builder.setPort( getHttpPort() );
            builder.setPath( api );
            trace("URIBuilder properties: "+"\n\tbuilder fragment: "+builder.getFragment()+"\n\tbuiderHost: "+builder.getHost()+"\n\tbuilderPath: "+builder.getPath()+"\n\tbuilderPort: "+builder.getPort()+"\n\tbuilderScheme: "+builder.getScheme()+"\n\tbuilderUserInfo: "+builder.getUserInfo()+"\n");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

            if( params.length >= 2 )
            {
                for( int i = 0; i < params.length; i += 2 )
                {
                    String name = null;
                    String value = StringPool.EMPTY;

                    if( params[i] != null )
                    {
                        name = params[i].toString();
                        trace("params name: "+name+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    }

                    if( params[i + 1] != null )
                    {
                        value = params[i + 1].toString();
                        trace("params value: "+value+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    }

                    builder.setParameter( name, value );
                    trace("URIbuilder properties after set parameters: "+"\n\tbuilder fragment: "+builder.getFragment()+"\n\tbuiderHost: "+builder.getHost()+"\n\tbuilderPath: "+builder.getPath()+"\n\tbuilderPort: "+builder.getPort()+"\n\tbuilderScheme: "+builder.getScheme()+"\n\tbuilderUserInfo: "+builder.getUserInfo()+"\n\tbuilderQueryParams: "+builder.getQueryParams());  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                }
            }

            request.setURI( builder.build() );
            trace("Http setURI: "+request.getURI()+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

            String response = getHttpResponse( request );
            trace("getHttpResponse: "+response+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

            if( response != null && response.length() > 0 )
            {
                Object jsonResponse = getJSONResponse( response );
                trace("jsonResponse: "+jsonResponse+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

                if( jsonResponse == null )
                {
                    throw new APIException( api, "Unable to get response: " + response ); //$NON-NLS-1$
                }
                else
                {
                    retval = jsonResponse;
                }
            }
        }
        catch( APIException e )
        {
            throw e;
        }
        catch( Exception e )
        {
            throw new APIException( api, e );
        }
        finally
        {
            try
            {
                request.releaseConnection();
                trace("request.releaseConnection()"+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            finally
            {
                // no need to log error
            }
        }

        trace("return value of httpJSONAPI: "+retval+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        return retval;
    }

    protected Object postJSONAPI( Object... args ) throws APIException
    {
        trace("postJSONAPI() params: "+ args+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        if( !( args[0] instanceof String ) )
        {
            throw new IllegalArgumentException( "First argument must be a string." ); //$NON-NLS-1$
        }

        HttpPost post = new HttpPost();
        trace("return value of postJSONAPI(httpJSONAPI( post, args )): "+httpJSONAPI( post, args )+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

        return httpJSONAPI( post, args );
    }

    public void setHost( String host )
    {
        this.hostname = host;
        trace("setHost(): "+host+"\n"); //$NON-NLS-1$ //$NON-NLS-2$

        releaseHttpClient();
    }

    public void setHttpPort( String httpPort )
    {
        trace("setHttpPort(): "+ httpPort+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        if( httpPort != null )
        {
            this.httpPort = Integer.parseInt( httpPort );
        }
        else
        {
            this.httpPort = -1;
        }

        releaseHttpClient();
    }

    public void setPassword( String password )
    {
        trace( "setPassword(): " + password + "\n" ); //$NON-NLS-1$ //$NON-NLS-2$
        this.password = password;

        releaseHttpClient();
    }

    public void setUsername( String username )
    {
        trace("setUsername(): "+ username+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
        this.username = username;

        releaseHttpClient();
    }

    public void releaseHttpClient()
    {
        if( httpClient != null )
        {
            this.httpClient.getConnectionManager().shutdown();
            this.httpClient = null;
        }
    }
}
