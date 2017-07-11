package com.barthezzko.cars;

import static org.junit.Assert.assertEquals;
import static com.barthezzko.web.Server.success;
import static com.barthezzko.web.Server.error;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.barthezzko.cars.CarPositionCalculator.Direction;
import com.barthezzko.cars.CarPositionCalculator.Position;
import com.barthezzko.web.Config;
import com.barthezzko.web.Server;
import com.google.gson.Gson;

public class RestAPIIntegrationTest {

	private static String SERVER_BASE_URL = "http://localhost:9000/";
	private Logger logger = Logger.getLogger(RestAPIIntegrationTest.class);
	private Gson gson = new Gson();
	private static Map<String, Server.Response> cases = new HashMap<>();
	
	@FunctionalInterface
	interface HttpRequestCallback {
		void onResponse(String response);
	}

	@BeforeClass
	public static void beforeClass() {
		//positive cases
		cases.put("5,5:RFLFRFLF", success(Position.of(7, 7, Direction.NORTH)));
		cases.put("6,6:FFLFFLFFLFF", success(Position.of(6, 6, Direction.EAST)));
		cases.put("5,5:FLFLFFRFFF", success(Position.of(1, 4, Direction.WEST)));
		cases.put("5,5:", success(Position.of(5, 5, Direction.NORTH)));
		//errors
		cases.put("5,10", error("Error during processing your request, cause: Initial position should be set. Valid format is X,Y:[F,L,R]*"));
		cases.put("16,5:RFLFRFLF", error("Error during processing your request, cause: Position [Position [x\u003d16, y\u003d5, direction\u003dNORTH]] is invalid (is out of grid boundaries)"));
		cases.put("5,-1:RFLFRFLF", error("Error during processing your request, cause: Position [Position [x\u003d5, y\u003d-1, direction\u003dNORTH]] is invalid (is out of grid boundaries)"));
		cases.put("5,10:FFFFFF", error("Error during processing your request, cause: Position [Position [x\u003d5, y\u003d15, direction\u003dNORTH]] is invalid (is out of grid boundaries)"));
		cases.put("510:F", error("Error during processing your request, cause: Initial position should be set with exactly 2 numbers splitted by comma corresponding to coordinates, for ex. [4,5]"));
		cases.put("", error("Error during processing your request, cause: Initial position should be set. Valid format is X,Y:[F,L,R]*"));
		Server.main(new String[] {});
	}

	private void invoke(String url, HttpMethod method, HttpRequestCallback callback) {
		invoke(url, method, null, callback);
	}

	private void invoke(String url, HttpMethod method, Map<String, String> payload, HttpRequestCallback callback) {
		try {
			HttpClient client = new HttpClient();
			client.start();
			ContentResponse response = null;
			switch (method) {
			case GET:
				response = client.GET(SERVER_BASE_URL + url);
				break;
			case POST:
				Request request = client.POST(SERVER_BASE_URL + url);
				payload.entrySet().forEach(e -> {
					request.param(e.getKey(), e.getValue());
				});
				response = request.send();
				break;
			case PUT:
				break;
			case CONNECT:
			case DELETE:
			case HEAD:
			case MOVE:
			case OPTIONS:
			case PRI:
			case PROXY:
			case TRACE:
				throw new RuntimeException("HttpMethod = " + method.name() + " is not supported by test client");
			default:
				break;
			}

			if (callback != null) {
				callback.onResponse(response.getContentAsString());
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
			throw new RuntimeException(ex);
		}

	}

	private Map<String, String> map(String... strings) {
		if (strings.length % 2 != 0) {
			throw new RuntimeException("Invalid payload array");
		}
		Map<String, String> payload = new HashMap<>();
		for (int i = 0; i < strings.length; i += 2) {
			payload.put(strings[i], strings[i + 1]);
		}
		return payload;
	}
	
	@Test
	public void testSuite(){
		logger.info("Total cases to run: " + cases.size());
		cases.entrySet().forEach(testCase->{
			logger.info("Running testCase: " + testCase.getKey() + ", expected: " + testCase.getValue());
			invoke("calc", HttpMethod.POST, map("instruction", testCase.getKey()), (content) -> {
				assertEquals(gson.toJson(testCase.getValue()), content);
			});
		});
	}
	
	@Test
	public void test(){
		logger.info("Total cases to run: " + cases.size());
		invoke("/config", HttpMethod.GET, (content) -> {
			assertEquals(gson.toJson(new Config(15)), content);
		});
	}
}
