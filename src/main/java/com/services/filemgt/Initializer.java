package com.services.filemgt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
public class Initializer extends HttpServlet {

    private static Initializer selfRef;

    private String uploadHome;

    @Override
    public void init(ServletConfig config) throws ServletException {
        selfRef = this;
        super.init(config);

        System.out.println("<Initializer> Initializing FileService");
        /**
         * Set data file home which hosts outside of the app server context
         */
        ServletContext context = this.getServletContext();
        this.uploadHome = context.getInitParameter("uploadHome");
        System.out.println("<Initializer> TTM File home set to: " + this.uploadHome);
    }

    public static Initializer getSelfRef() {
        return selfRef;
    }

    public String getUploadHome() {
        return uploadHome;
    }
}
