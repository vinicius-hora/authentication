package com.rasmoo.client.financescontroll.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(Boolean.TRUE);
        endpoints
                .authenticationManager(authenticationManager)
                .requestFactory(requestFactory)
                .accessTokenConverter(this.accessTokenConverter())
                .tokenStore(this.jwtTokenStore());
    }

    @Override
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

    @Autowired
    @Qualifier("dsOauth")
    private DataSource dataSource;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("assinatura-financesControll");
        return accessTokenConverter;
    }

    @Bean
    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(this.accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(this.jwtTokenStore());
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        return tokenServices;
    }


}
