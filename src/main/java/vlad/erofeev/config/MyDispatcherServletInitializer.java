package vlad.erofeev.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import vlad.erofeev.controllers.IndexController;

public class MyDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {IndexController.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // указание класса с конфигом mvc
        return new Class[]{SpringMVCconfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        // указание того, что все запросы будут передаваться на dispatcher servlet
        return new String[]{"/"};
    }
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
