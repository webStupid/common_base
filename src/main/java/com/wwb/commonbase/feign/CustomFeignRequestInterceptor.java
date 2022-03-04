package com.wwb.commonbase.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weibo
 */
public class CustomFeignRequestInterceptor implements RequestInterceptor {

    /**
     * 拦截Feign请求，判断请求环境，并将 X-REQUEST-ENV 传递到下一个服务
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            String envHeader = request.getHeader("X-REQUEST-ENV");
            if (StringUtils.isNotBlank(envHeader)) {
                template.header("X-REQUEST-ENV", envHeader);
                String serviceName=template.feignTarget().name();
                String uri = "/" + serviceName+ template.url();
                template.uri(uri);
                template.target("http://GATEWAY");
            }
        }
    }
}