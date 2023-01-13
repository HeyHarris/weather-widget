# Weather Widget

Weather Widget is a Java Application that uses OpenWeather API to display weather information of select cities
![long-weather-widget-1](https://user-images.githubusercontent.com/102763206/212425417-117327c3-de53-4b31-9612-4749c65c0f30.png)




## Usage
Example: Chicago

```java
//Get API data
public void getAPIData(String city) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + city + ",us&units=imperial&mode=JSON&appid=23b5d8c50cb7d4ed6dd57df088885d7f")).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)//lamda expression, use body method from HttpResponse on the received response
			.thenApply(Controller::parseJson)
			.join();
	}

//Parse API JSON data
public static String parseJson(String responseBody) {
		JSONObject data = new JSONObject(responseBody);
		double longitude = data.getJSONObject("coord").getDouble("lon");
		double latitude = data.getJSONObject("coord").getDouble("lat");
		double temp = data.getJSONObject("main").getDouble("temp");
		double humidity = data.getJSONObject("main").getDouble("humidity");
		double visibility = data.getDouble("visibility");
		double windSpeed = data.getJSONObject("wind").getDouble("speed");
		double windDeg = data.getJSONObject("wind").getDouble("deg");
		double dates = data.getDouble("dt");
		
		visibility = visibility /1000.0;
		Controller.numMap.put("lon", longitude);
		Controller.numMap.put("lat", latitude);
		Controller.numMap.put("temp", temp);
		Controller.numMap.put("humidity", humidity);
		Controller.numMap.put("visibility", visibility);
		Controller.numMap.put("wind", windSpeed);
		Controller.numMap.put("windDeg", windDeg);
		Controller.numMap.put("dates", dates);
		
		JSONArray array = (JSONArray) data.get("weather");
		JSONObject mainData = array.getJSONObject(0);
		String main = mainData.getString("main");
//		System.out.println(main);
		Controller.map.put("main", main);
		return "";
	}
```
Display as shown

![long-weather-widget-2](https://user-images.githubusercontent.com/102763206/212425401-b7beaf15-2d10-4926-84e0-7b884be8ae9d.png)

