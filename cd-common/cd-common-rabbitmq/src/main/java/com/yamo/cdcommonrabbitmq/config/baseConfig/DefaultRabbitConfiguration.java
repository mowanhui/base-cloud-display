package com.yamo.cdcommonrabbitmq.config.baseConfig;


import com.yamo.cdcommonrabbitmq.service.ConfirmCallbackService;
import com.yamo.cdcommonrabbitmq.service.ReturnCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * 默认的RabbitMQ配置
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 1:27
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rabbitmq.default")
public class DefaultRabbitConfiguration extends BaseRabbitProperties {
    private final ConfirmCallbackService confirmCallbackService;
    private final ReturnCallbackService returnCallbackService;

    //这里因为使用自动配置的connectionFactory，所以把自定义的connectionFactory注解掉
    // 存在此名字的bean 自带的连接工厂会不加载（也就是说yml中rabbitmq下一级不生效），如果想自定义来区分开 需要改变bean 的名称
    @Bean(name = "connectionFactory")
    @Primary
    public ConnectionFactory connectionFactory() throws Exception {
        //创建工厂类
        CachingConnectionFactory cachingConnectionFactory=this.getConnectionFactory();
        //设置发布消息后回调
        cachingConnectionFactory.setPublisherReturns(this.isPublisherReturns());
        //设置发布后确认类型，此处确认类型为交互
        cachingConnectionFactory.setPublisherConfirmType(this.getPublisherConfirmType());
        cachingConnectionFactory.setCacheMode(this.getConnectionFactory().getCacheMode());
        return  cachingConnectionFactory;
    }


    /**
     * 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的listener下的simple配置），如果想自定义来区分开 需要改变bean 的名称
     * @return
     */
    @Bean(name = "rabbitListenerContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        // 并发消费者数量
        containerFactory.setConcurrentConsumers(1);
        containerFactory.setMaxConcurrentConsumers(20);
        // 预加载消息数量 -- QOS
        containerFactory.setPrefetchCount(5);
        // 应答模式（此处设置为手动）
        containerFactory.setAcknowledgeMode(this.getListener().getSimple().getAcknowledgeMode());
        //消息序列化方式
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置通知调用链 （这里设置的是重试机制的调用链）
        containerFactory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rabbitRetryTemplate())
                        .build()
        );
        return containerFactory;
    }

    // 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的template的配置），如果想自定义来区分开 需要改变bean 的名称
    @Bean(name = "rabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)//必须是prototype类型
    @Primary
    public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        //默认是用jdk序列化
        //数据转换为json存入消息队列，方便可视化界面查看消息数据
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        //此处设置重试template后，会再生产者发送消息的时候，调用该template中的调用链
        rabbitTemplate.setRetryTemplate(rabbitRetryTemplate());
        //CorrelationData correlationData, boolean b, String s
        rabbitTemplate.setConfirmCallback(confirmCallbackService);
        //Message message, int i, String s, String s1, String s2
        rabbitTemplate.setReturnCallback(returnCallbackService);

        return rabbitTemplate;
    }

    //重试的Template
    public RetryTemplate rabbitRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置监听  调用重试处理过程
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                // 执行之前调用 （返回false时会终止执行）
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // 重试结束的时候调用 （最后一次重试 ）
                //log.info("---------------最后一次调用");
                return ;
            }
            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                //  异常 都会调用
                log.error("-----第{}次调用:{}",retryContext.getRetryCount(),throwable.getMessage());
            }
        });
        retryTemplate.setBackOffPolicy(backOffPolicyBythis());
        retryTemplate.setRetryPolicy(retryPolicyBythis());
        return retryTemplate;
    }

    public ExponentialBackOffPolicy backOffPolicyBythis() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        long maxInterval = this.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
        long initialInterval = this.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
        double multiplier = this.getListener().getSimple().getRetry().getMultiplier();
        this.getListener().getSimple().setDefaultRequeueRejected(true);
        // 重试间隔
        backOffPolicy.setInitialInterval(initialInterval * 1000);
        // 重试最大间隔
        backOffPolicy.setMaxInterval(maxInterval * 1000);
        // 重试间隔乘法策略
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    public SimpleRetryPolicy retryPolicyBythis() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        int maxAttempts = this.getListener().getSimple().getRetry().getMaxAttempts();
        retryPolicy.setMaxAttempts(maxAttempts);
        return retryPolicy;
    }
}
