package com.wuliaozhiyuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.wuliaozhiyuan.config.datasouce.dynamic.DynamicDataSourceRegister;
import com.wuliaozhiyuan.config.system.SystemSetting;
import com.wuliaozhiyuan.config.system.WebSocketSetting;

/**
 * @desc springboot的启动类
 * @author wuliaozhiyuan
 *
 */
@SpringBootApplication
@ServletComponentScan//这个就是扫描相应的Servlet包;
@EnableConfigurationProperties({SystemSetting.class, WebSocketSetting.class})  
@MapperScan("com.wuliaozhiyuan.mapper")  
@Import({DynamicDataSourceRegister.class})
public class App {
	/**配置httpMessageConverters，通过fastJson*/
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
