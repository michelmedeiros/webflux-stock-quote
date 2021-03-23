package br.com.webflux.stockquota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;

@Order(1)
@SpringBootApplication
public class StockQuotaApplication {
//	static {
//		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.io.FileInputStream", "readBytes"));
//		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.io.RandomAccessFile", "readBytes"));
//	}

	public static void main(String[] args) {
//		BlockHound.install();
		ReactorDebugAgent.init();
		SpringApplication.run(StockQuotaApplication.class, args);
	}
}
