package com.iyin.sign.system.config;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author panshx
 * @Description FeignDownloadDecoder即feign下载编码器
 * feign调用时要求返回类型为MultipartFile
 */
@Slf4j
public class FeignDownloadDecoder implements Decoder {

    @Autowired
    ObjectFactory<HttpMessageConverters> messageConverters;

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		log.debug("sign-core-system FeignDownloadDecoder: {}", type);
		// 如果返回是文件流
        if (type instanceof Class && MultipartFile.class.isAssignableFrom((Class<?>) type)) {
            Collection<String> contentTypes = response.headers().get("content-type");
            String contentType = "application/octet-stream";
            if (!ObjectUtils.isEmpty(contentTypes) && contentTypes.size() > 0) {
                String[] temp = new String[contentTypes.size()];
                contentTypes.toArray(temp);
                contentType = temp[0];
            }
            log.debug("sign-core-system content-type: {}", contentType);
            String fileName = null;
            Collection<String> contentDispositions = response.headers().get("Content-Disposition");
            if (!ObjectUtils.isEmpty(contentDispositions) && contentDispositions.size() > 0) {
                String[] temp = new String[contentDispositions.size()];
                contentDispositions.toArray(temp);
                fileName = temp[0];
                String[] splits = fileName.split("filename=");
                log.info("splits: {}", JSON.toJSONString(splits));
                if (! ObjectUtils.isEmpty(splits) && splits.length > 1) {
                	fileName = splits[1];
                }
            }
            log.debug("sign-core-system Content-Disposition: {}", fileName);
            byte[] bytes = StreamUtils.copyToByteArray(response.body().asInputStream());
            return new InMemoryMultipartFile("file", fileName, contentType, bytes);
        }
        // 否则按默认处理
        return new SpringDecoder(messageConverters).decode(response, type);
	}

}
