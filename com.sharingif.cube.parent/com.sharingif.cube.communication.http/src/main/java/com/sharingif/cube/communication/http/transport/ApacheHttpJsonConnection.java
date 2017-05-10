package com.sharingif.cube.communication.http.transport;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;

import com.sharingif.cube.communication.exception.CommunicationException;
import com.sharingif.cube.communication.http.HttpMethod;
import com.sharingif.cube.communication.transport.Connection;
import com.sharingif.cube.core.request.RequestInfo;
import com.sharingif.cube.core.util.Charset;
import com.sharingif.cube.core.util.StringUtils;

/**   
 *  
 * @Description:  [http Transport]   
 * @Author:       [Joly]   
 * @CreateDate:   [2014年12月22日 下午2:11:27]   
 * @UpdateUser:   [Joly]   
 * @UpdateDate:   [2014年12月22日 下午2:11:27]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0] 
 *    
 */
public class ApacheHttpJsonConnection implements Connection<RequestInfo<String>, String>, InitializingBean {
	
	private static final Header APPLICATION_JSON_CONTENT_TYPE = new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
	
	private CloseableHttpClient httpclient;
	private String host;
	private int port;
	private String contextPath;
	
	private int connectionRequestTimeout = 1000;
	private int connectTimeout = 5000;
    private int soTimeout = 5000;
	private int maxTotal = 200;
	private int defaultMaxPerRoute = 200;
	
	public ApacheHttpJsonConnection(String host, int port, String contextPath) {
		this.host = host;
		this.port = port;
		this.contextPath = contextPath;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getSoTimeout() {
		return soTimeout;
	}
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}
	public int getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}
	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}
	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}
	
	
	@Override
	public String connect(RequestInfo<String> httpContext) throws CommunicationException {
		
		CloseableHttpResponse response = null;
		try {
			response = execute(httpContext);
		} catch (ClientProtocolException e) {
			throw new CommunicationException("client protocol exception", e);
		} catch (IOException e) {
			throw new CommunicationException("client io exception", e);
		}
		
		try {
			return EntityUtils.toString(response.getEntity(), Charset.UTF8.toString());
		} catch (ParseException e) {
			throw new CommunicationException("EntityUtils parse exception", e);
		} catch (IOException e) {
			throw new CommunicationException("EntityUtils io exception", e);
		}
		
	}
	
	protected CloseableHttpResponse execute(RequestInfo<String> httpContext) throws ClientProtocolException, IOException {
		String url = new StringBuffer("http://").append(host).append(":").append(port).append("/").append(contextPath).append("/").append(httpContext.getLookupPath()).toString();
		
		if(httpContext.getMethod().equals(HttpMethod.GET.name())) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader(APPLICATION_JSON_CONTENT_TYPE);
			try {
				return httpclient.execute(httpGet);
			} finally{
				httpGet.releaseConnection();
			}
		}
		if(httpContext.getMethod().equals(HttpMethod.POST.name())) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(APPLICATION_JSON_CONTENT_TYPE);
			if(!StringUtils.isEmpty(httpContext.getRequest())) {
				httpPost.setEntity(new StringEntity(httpContext.getRequest(), ContentType.APPLICATION_JSON));
			}
			try {
				return httpclient.execute(httpPost);
			} finally{
				httpPost.releaseConnection();
			}
		}
		return null;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<HttpResponse> create(
                SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }

                };
                return new DefaultHttpResponseParser(
                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                    @Override
                    protected boolean reject(final CharArrayBuffer line, int count) {
                        // try to ignore all garbage preceding a status line infinitely
                        return false;
                    }

                };
            }

        };
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
		
		// Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);

        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContexts.createSystemDefault();

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();

		
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory);
		
		// Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
        	.setSoTimeout(soTimeout)
            .setTcpNoDelay(true)
            .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setConnectionConfig(new HttpHost(host, port), connectionConfig);
        
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        
        
        RequestConfig requestConfig = RequestConfig.custom()
        		.setConnectionRequestTimeout(connectionRequestTimeout)
        		.setConnectTimeout(connectTimeout)
        		.build();
        
        
        // Create an HttpClient with the given custom dependencies and configuration.
        httpclient = HttpClients.custom()
            .setConnectionManager(connManager)
            .setDefaultRequestConfig(requestConfig)
            .build();
		
	}
	

	
}
