package com.speedyllama.mtastatus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadmeServlet extends HttpServlet {

	private static final long serialVersionUID = 6706916338770840953L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
	    out.println("<h1>NYC Transit Alexa Skill</h1>");
	    out.println("<p>Powered by Speedy Llama, LLC</p>");
	    out.println("<a href=\"mailto:info@speedyllama.com\">Contact Us</a>");
	    out.flush();
	}

}
