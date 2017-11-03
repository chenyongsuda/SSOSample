package com.tony;

import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Created by chnho02796 on 2017/11/3.
 */
@Configuration
public class OAuth2ServerConfig {
    private static final String SERVER_RESOURCE_ID = "oauth2server";
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(new InMemoryTokenStore())
                    .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            clients.inMemory().withClient("tony")
//                    .resourceIds(SERVER_RESOURCE_ID)
//                    .authorizedGrantTypes("authorization_code")
//                    .authorities("ROLE_CLIENT")
//                    .scopes("read", "write","trust","自定义权限")//这个scope是自定义的么？？
//                    .accessTokenValiditySeconds(100)
//                    .secret("secret")
//                    .redirectUris("redirect_uri");
            clients.inMemory().withClient("client_1")
                    .resourceIds(SERVER_RESOURCE_ID)
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456")
                    .and().withClient("client_2")
                    .resourceIds(SERVER_RESOURCE_ID)
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456");
        }


    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(SERVER_RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
//            http
//                    .requestMatchers().antMatchers("/product")
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/product").access("#oauth2.hasScope('read')");
            http
                    // Since we want the protected resources to be accessible in the UI as well we need
                    // session creation to be allowed (it's disabled by default in 2.0.6)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .requestMatchers().anyRequest()
                    .and()
                    .anonymous()
                    .and()
                    .authorizeRequests()
//                    .antMatchers("/product/**").access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
                    .antMatchers("/product/**").authenticated();//配置order访问控制，必须认证过后才可以访问
        }
    }
}
