package br.com.webflux.stockquota;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import reactor.tools.agent.ReactorDebugAgent;

@Order(1)
@Slf4j
@SpringBootApplication
public class StockQuotaApplication {
//	static {
//		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.io.FileInputStream", "readBytes"));
//		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.io.RandomAccessFile", "readBytes"));
//	}

	@Value("${target.uri}")
	private String targetUri;

	public static void main(String[] args) {
//		BlockHound.install();
		ReactorDebugAgent.init();
		SpringApplication.run(StockQuotaApplication.class, args);
	}
}
