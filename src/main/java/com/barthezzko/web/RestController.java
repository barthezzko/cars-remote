package com.barthezzko.web;
import static com.barthezzko.web.Server.error;
import static com.barthezzko.web.Server.json;
import static com.barthezzko.web.Server.param;
import static com.barthezzko.web.Server.success;
import static com.barthezzko.web.Server.toJson;

import static spark.Spark.*;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.barthezzko.cars.CarPositionCalculator;

import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;


public class RestController {

	private Logger logger = Logger.getLogger(RestController.class);
	
	public RestController(CarPositionCalculator carPosCalc){
		//staticFiles.location("static"); // Static files
		externalStaticFileLocation("src/main/resources/static/");
		get("/", (req, res) -> new ModelAndView(new HashMap<String, Object>(), "index"), new JadeTemplateEngine());
		post("/calc", (req, res) -> {
			return success(carPosCalc.calculate(param(req, "instruction")));
		}, json());
		before("/*", (q, a) -> {
			StringBuilder sb = new StringBuilder("IN: ").append(q.requestMethod()).append(" | ").append(q.pathInfo())
					.append(" | payload: [");
			if (q.queryParams() != null) {
				q.queryParams().forEach(key -> {
					sb.append(String.format("%s=%s; ", key, q.queryParams(key)));
				});
			}
			if (q.params() != null) {
				q.params().entrySet().forEach(entry -> {
					sb.append(String.format("%s=%s; ", entry.getKey(), entry.getValue()));
				});
			}
			logger.info(sb.append("]").toString());

		});
		after("/*", (q, a) -> {
			logger.info("OUT: " + a.body());
		});
		exception(Exception.class, (e, req, res) -> {
			logger.error(e, e);
			String errorMessage = toJson(error("Error during processing your request, cause: " + e.getMessage()));
			logger.error("OUT: " + errorMessage);
			res.body(errorMessage);
			res.status(200);
		});
		get("serverStatus", (req, res) -> "ok");
	}
}
