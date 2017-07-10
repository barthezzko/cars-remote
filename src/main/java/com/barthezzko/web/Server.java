package com.barthezzko.web;

import static spark.Spark.port;

import java.util.Objects;

import org.apache.log4j.Logger;

import spark.Request;
import spark.ResponseTransformer;

import com.barthezzko.cars.CarPositionCalculator;
import com.barthezzko.cars.CarPositionCalculatorImpl;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

public class Server extends AbstractModule{
	
	private static int SERVER_PORT = 9000;
	private static Logger logger = Logger.getLogger(Server.class);
	private static final Gson gson = new Gson();
	private CarPositionCalculator carPosCalculator;

	public static void main(String[] args) {
		Server server = new Server();
		server.prepareInjection();
		server.runServer();
	}
	
	private void runServer() {
		port(SERVER_PORT);
		new RestController(carPosCalculator);
	}

	private void prepareInjection() {
		logger.info("Injecting dependencies from module");
		Injector injector = Guice.createInjector(this);
		logger.info("Bindings:");
		injector.getBindings().entrySet().forEach(entry -> {
			logger.info(entry);
		});
		carPosCalculator = injector.getInstance(CarPositionCalculator.class);
	}

	@Override
	protected void configure() {
		bind(Config.class).toInstance(new Config(15));
		bind(CarPositionCalculator.class).to(CarPositionCalculatorImpl.class).in(Scopes.SINGLETON);
	}
	
	public static Response success(Object obj) {
		return new Response(ResponseType.SUCCESS, obj);
	}

	public static Response error(String error) {
		return new Response(ResponseType.ERROR, error);
	}

	public static class Response {

		private ResponseType responseType;
		private Object payload;

		private Response(ResponseType responseType, Object payload) {
			this.responseType = responseType;
			this.payload = payload;
		}

		public ResponseType getResponseType() {
			return responseType;
		}

		public Object getPayload() {
			return payload;
		}
	}

	enum ResponseType {
		SUCCESS, ERROR
	}
	public static String toJson(Object object) {
		return gson.toJson(object);
	}

	public static ResponseTransformer json() {
		return Server::toJson;
	}
	public static String param(Request req, String name) {
		String value = "POST".equals(req.requestMethod()) ? req.params(name) : req.queryParams(name);
		Objects.requireNonNull(value, "Input parameter [" + name + "] should not be empty");
		return value;
	}
}
