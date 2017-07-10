package com.barthezzko.web;

import static spark.Spark.port;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.barthezzko.cars.CarPositionCalculator;
import com.barthezzko.cars.CarPositionCalculatorImpl;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

import spark.Request;
import spark.ResponseTransformer;

public class Server extends AbstractModule {

	private static Logger logger = Logger.getLogger(Server.class);
	private static final Gson gson = new Gson();
	private CarPositionCalculator carPosCalculator;
	private static final String PROPS_FILE = "src/main/resources/application.properties";
	private static Properties props = new Properties();

	public static void main(String[] args) {
		readProperties();
		Server server = new Server();
		server.prepareInjection();
		server.runServer();
	}

	private static void readProperties() {
		InputStream input;
		try {
			input = new FileInputStream(PROPS_FILE);
			props.load(input);
			logger.info("Running server with configuration: " + props);
		} catch (IOException e1) {
			logger.error("Couldn't load properties from file from [" + PROPS_FILE + "]", e1);
			throw new RuntimeException("Server is shutdown due to improper configuration, check more for details");
		}
	}

	private void runServer() {
		port(propInt("server.port"));
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
		bind(Config.class).toInstance(new Config(propInt("gridSize")));
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
		String value = "GET".equals(req.requestMethod()) ? req.params(name) : req.queryParams(name);
		Objects.requireNonNull(value, "Input parameter [" + name + "] should not be empty");
		return value;
	}
	
	private int propInt(String key){
		String strValue = props.getProperty(key);
		Objects.requireNonNull(strValue);
		return Integer.valueOf(strValue);
	}
}
