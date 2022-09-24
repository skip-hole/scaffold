package config;

import enhance.repository.RepositoryAnnotationAdvisor;
import enhance.repository.RepositoryAnnotationInterceptor;
import enhance.webflux.WebFluxAop;
import io.netty.channel.ChannelDuplexHandler;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import util.ContextUtils;
import wiretap.repository.RepositoryFinishWiretap;
import wiretap.webclient.WebClientFinishWiretap;
import wiretap.webflux.WebFluxFinishWiretap;

/**
 * @author hui.zhang
 * @date 2022年09月01日 22:23
 */
@Configuration
@ConditionalOnProperty(prefix = "com.glodon.repeater.wiretap", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WiretapAutoConfiguration {

    @Bean
    public ContextUtils context() {
        return new ContextUtils();
    }

    @Bean
    @ConditionalOnClass(ServerWebExchange.class)
    public WebFluxFinishWiretap webFluxFinishWiretap() {
        return new WebFluxFinishWiretap();
    }

    @Bean
    @ConditionalOnClass(DispatcherHandler.class)
    public WebFluxAop webFluxAop() {
        return new WebFluxAop();
    }

    @Bean
    @ConditionalOnProperty(prefix = "com.glodon.repeater.wiretap.repository", name = "enabled")
    @ConditionalOnBean({RepositoryFinishWiretap.class})
    public Advisor dynamicDatasourceAnnotationAdvisor() {
        RepositoryAnnotationInterceptor interceptor = new RepositoryAnnotationInterceptor();
        RepositoryAnnotationAdvisor advisor = new RepositoryAnnotationAdvisor(interceptor, Repository.class);
        return advisor;
    }

    @Bean
    @ConditionalOnClass(SimpleReactiveMongoRepository.class)
    public RepositoryFinishWiretap repositoryFinishWiretap() {
        return new RepositoryFinishWiretap();
    }

    @Bean
    @ConditionalOnClass(ChannelDuplexHandler.class)
    public WebClientFinishWiretap webClientFinishWiretap() {
        return new WebClientFinishWiretap();
    }



}
