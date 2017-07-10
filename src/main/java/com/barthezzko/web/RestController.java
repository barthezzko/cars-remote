package com.barthezzko.web;
import static spark.Spark.*;
import static com.barthezzko.web.Server.*;

import org.apache.log4j.Logger;

import com.barthezzko.cars.CarPositionCalculator;


public class RestController {

	private Logger logger = Logger.getLogger(RestController.class);
	
	public RestController(CarPositionCalculator carPosCalc){
		get("/calc", (req, res) -> {
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
			String errorMessage = toJson(error("Error during processing your request, cause: " + e.getMessage()));
			logger.error("OUT: " + errorMessage);
			res.body(errorMessage);
			res.status(500);
		});
		get("serverStatus", (req, res) -> "ok");
	}
}
