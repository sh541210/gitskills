package com.iyin.sign.system.service.impl;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.iyin.sign.system.service.OpenOfficeService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: OpenOfficeServiceImpl
 * @Description: OpenOffice服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix="openoffice.converter")
public class OpenOfficeServiceImpl implements OpenOfficeService {
	private String host = "127.0.0.1";
	private int port = 8100;

	@Override
	public OpenOfficeConnection getConnection() {
        return getConnection(host, port);
	}

	@Override
	public OpenOfficeConnection getConnection(String host, int port) {
		log.info("host: {}:{}", host, port);
        // connect to an OpenOffice.org instance running on port 8100
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, port);
        return connection;
	}

	@Override
	public void releaseConnection(OpenOfficeConnection conn) {
			conn.disconnect();
	}

}
