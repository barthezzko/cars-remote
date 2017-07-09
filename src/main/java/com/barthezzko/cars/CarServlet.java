package com.barthezzko.cars;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarServlet extends HttpServlet {
	private static final long serialVersionUID = -6603341234998397663L;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		arg1.getWriter().write("some response");
	}
}
