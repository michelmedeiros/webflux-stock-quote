package br.com.webflux.stockquota.config;

import org.springframework.context.annotation.Configuration;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

@Configuration
public class BlockHoundConfig implements BlockHoundIntegration {

    @Override
    public void applyTo(BlockHound.Builder builder) {
            builder.allowBlockingCallsInside("sun.security.ssl.SSLHandshake", "consume")
                    .allowBlockingCallsInside("io.netty.util.concurrent.GlobalEventExecutor", "addTask")
                    .allowBlockingCallsInside("java.io.RandomAccessFile","readBytes")
                    .allowBlockingCallsInside("java.io.FileInputStream.readBytes","readBytes");
    }
}