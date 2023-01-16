package com.rasmoo.client.financescontroll.oauth;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@RequiredArgsConstructor
public class OAuthConfiguration {


    private  final AuthenticationManager authenticationManager;

    public static final String RESOURCE_ID = "financesControll";

    @EnableAuthorizationServer
    public  class AuthorizationServer extends AuthorizationServerConfigurerAdapter{


        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("cliente-web")
                    .secret("$2a$10$xD06tjPVtCaBSpjmSn.7z.rJqjHxvaLKxytdsql.zPiJ0YETCZO5K")
                    .authorizedGrantTypes("password")
                    .scopes("read", "write")
                    .accessTokenValiditySeconds(3600)
                    .resourceIds(RESOURCE_ID);
        }

    }

    @EnableResourceServer
    public static class ResourceServer extends ResourceServerConfigurerAdapter{
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated().and()
                    .requestMatchers()
                    .antMatchers("/v2/categoria");
        }

    }
}
