package top.kthirty.core.web.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.web.utils.INetUtil;

/**
 * 服务器信息
 *
 * @author Kthirty
 */
@Getter
@Configuration(proxyBeanMethods = false)
@ToString
public class ServerInfo implements SmartInitializingSingleton {
	private final ServerProperties serverProperties;
	private String hostName;
	private String ip;
	private Integer port;
	private String ipWithPort;

	@Autowired(required = false)
	public ServerInfo(ServerProperties serverProperties) {
		this.serverProperties = serverProperties;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.hostName = INetUtil.getHostName();
		this.ip = INetUtil.getHostIp();
		this.port = serverProperties.getPort();
		this.ipWithPort = String.format("%s:%d", ip, port);
	}
}
