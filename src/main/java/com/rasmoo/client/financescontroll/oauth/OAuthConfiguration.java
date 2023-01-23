package com.rasmoo.client.financescontroll.oauth;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class OAuthConfiguration {

    public static final String RESOURCE_ID = "financesControll";

    @EnableAuthorizationServer
    public static class AuthorizationServer extends AuthorizationServerConfigurerAdapter{
        @Autowired
        private  AuthenticationManager authenticationManager;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        @Qualifier("dsOauth")
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore(){
            return new JdbcTokenStore(this.dataSource);
        }

        @Bean
        public ApprovalStore approvalStore(){
            return new JdbcApprovalStore(this.dataSource);
        }


        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
            requestFactory.setCheckUserScopes(Boolean.TRUE);
            endpoints
                    .authenticationManager(authenticationManager)
                    .requestFactory(requestFactory)
                    .approvalStore(this.approvalStore())
                    .tokenStore(this.tokenStore());
        }

        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //configuração em memoria
//            clients.inMemory()
//                    .withClient("cliente-web")
//                    .secret("$2a$10$xD06tjPVtCaBSpjmSn.7z.rJqjHxvaLKxytdsql.zPiJ0YETCZO5K")
//                    .authorizedGrantTypes("password", "client_credentials", "refresh_token")
//                    .scopes("cw_logado", "cw_naologado")
//                    .accessTokenValiditySeconds(121)
//                    .resourceIds(RESOURCE_ID)
//                    .and()
//                    .withClient("cliente-canva")
//                    .secret("$2a$10$xD06tjPVtCaBSpjmSn.7z.rJqjHxvaLKxytdsql.zPiJ0YETCZO5K")
//                    .authorizedGrantTypes("authorization_code", "implicit")
//                    .scopes("cc_logado")
//                    .redirectUris("https://www.canva.com/pt_br/")
//                    .accessTokenValiditySeconds(3600)
//                    .resourceIds(RESOURCE_ID);
            //configuração com banco
            clients.jdbc(this.dataSource);

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
//                    .requestMatchers()
//                    .antMatchers("/v2/categoria")
//                    .and()
                    .cors();
        }

    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class OAuthExpressionHandler extends GlobalMethodSecurityConfiguration{
        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            return new OAuth2MethodSecurityExpressionHandler();
        }

    }
}
