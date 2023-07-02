package lk.ijse.dep10.pos;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    static {
        System.setProperty("jansi.passthrough", "true");
    }

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebRootConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebAppConfig.class, WebSocketConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
