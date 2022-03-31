package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;


import java.util.HashMap;

import java.util.Map;
import java.util.ResourceBundle;
import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.*;

import org.json.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;  


public class Controller implements Initializable{

	@FXML
	private Label cityLabel;
	@FXML
	private Label tempLabel;
	@FXML
	private Label coordLabel;
	@FXML
	private Label dayLabel;
	@FXML
	private Label timeLabel;
	@FXML
	private Label valueHumidity;
	@FXML
	private Label valueWindSpeed;
	@FXML
	private Label valueVisibility;
	@FXML
	private Label humidityLabel;
	@FXML
	private Label windspeedLabel;
	@FXML
	private Label visibilityLabel;
	@FXML
	private Label mainDescriptionLabel;
	@FXML
	private ImageView picView;
	
	
	@FXML
	private AnchorPane mainPanel;
	@FXML
	private GridPane infoGridPane;
	
	@FXML
	private ChoiceBox<String> myChoiceBox;
	
	private String[] cities = {"Austin", "Chicago","Honolulu","Los Angeles", "Miami", "New York City", "Seattle", "Washington D.C."};

	private File file;
	private Image image;
	private Stop[] stops;
	private LinearGradient linear;
	private BackgroundFill bgFill;
	
	private static Map<String, String> map = new HashMap<>();
	private static Map<String, Double> numMap = new HashMap<>();
	private static final DecimalFormat df = new DecimalFormat("0.00");
	 
//	URL url;
//	HttpURLConnection connection;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		myChoiceBox.getItems().addAll(cities);
		myChoiceBox.setOnAction(this::chooseCity);
		getAPIData("Chicago");
		myChoiceBox.setValue("Chicago");
		coordLabel.setText("Longitude: " + (Controller.numMap.get("lon"))  + ", Latitude: " + (Controller.numMap.get("lat")));
		tempLabel.setText("" + Controller.numMap.get("temp").intValue() + " \u2109");
		mainDescriptionLabel.setText(Controller.map.get("main"));
		valueHumidity.setText("" + Controller.numMap.get("humidity").intValue() + "%");
		valueVisibility.setText("" + df.format(Controller.numMap.get("visibility")) + "km");
		valueWindSpeed.setText("" + Controller.numMap.get("wind") + "mph " + Controller.getCardinalDirection(Controller.numMap.get("windDeg")));
		this.changeBackground();
		
	}
	
	public void chooseCity(ActionEvent chose) {
		String chosenCity = myChoiceBox.getValue();
		String modChosenCity = myChoiceBox.getValue();
		if (modChosenCity.equals("Los Angeles")) {
			modChosenCity = "Los%20Angeles";
		}
		else if (modChosenCity.equals("New York City")) {
			modChosenCity = "New%20York%20City";
		}
		else if (modChosenCity.equals("Washington D.C.")) {
			modChosenCity = "Washington%20D.C.";
		}
		getAPIData(modChosenCity);
		cityLabel.setText(chosenCity);
//		System.out.println(numMap);
		coordLabel.setText("Longitude: " + (Controller.numMap.get("lon"))  + ", Latitude: " + (Controller.numMap.get("lat")));
		tempLabel.setText("" + Controller.numMap.get("temp").intValue() + " \u2109");
		mainDescriptionLabel.setText(Controller.map.get("main"));
		valueHumidity.setText("" + Controller.numMap.get("humidity").intValue() + "%");
		valueVisibility.setText("" + df.format(Controller.numMap.get("visibility")) + "km");
		valueWindSpeed.setText("" + Controller.numMap.get("wind") + "mph " + Controller.getCardinalDirection(Controller.numMap.get("windDeg")));
		
		

		  LocalTime CDT = LocalTime.now();
		  ZoneId zoneCDT = ZoneId.of("America/Chicago");
		  LocalDate dCDT = LocalDate.now(zoneCDT);
		  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd");
		  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		  
		  if(chosenCity.equals("Los Angeles") || chosenCity.equals("Seattle")) {
			  LocalTime PDT = CDT.minusHours(2);
			  String pdtTime = PDT.format(timeFormatter);
			  timeLabel.setText(pdtTime);
			  
			  ZoneId zonePDT = ZoneId.of("America/Los_Angeles");
			  LocalDate dPDT = LocalDate.now(zonePDT);
			  String pdtDate = dPDT.format(dateFormatter);
			  dayLabel.setText(pdtDate);
		  }
		  else if (chosenCity.equals("New York City") || chosenCity.equals("Miami") || chosenCity.equals("Washington D.C.")) {
			  LocalTime EDT = CDT.plusHours(1);
			  String edtTime = EDT.format(timeFormatter);
			  timeLabel.setText(edtTime);
			  
			  ZoneId zoneEST = ZoneId.of("US/Eastern");
			  LocalDate dEST = LocalDate.now(zoneEST);
			  String estDate = dEST.format(dateFormatter);
			  dayLabel.setText(estDate);
		  }
		  else if (chosenCity.equals("Honolulu")) {
			  LocalTime HST = CDT.minusHours(5);
			  String hstTime = HST.format(timeFormatter);
			  timeLabel.setText(hstTime);
			  
			  ZoneId zoneHST = ZoneId.of("Pacific/Honolulu");
			  LocalDate dHST = LocalDate.now(zoneHST);
			  String hstDate = dHST.format(dateFormatter);
			  dayLabel.setText(hstDate);
		  }
		  else if (chosenCity.equals("Chicago") || chosenCity.equals("Austin")) {
			  String cdtTime = CDT.format(timeFormatter);
			  timeLabel.setText(cdtTime);
			  String cdtDate = dCDT.format(dateFormatter);
			  dayLabel.setText(cdtDate);
		  }
		  this.changeBackground();
		 
	}
	public void changeBackground( ) {
		 switch (mainDescriptionLabel.getText()) {
		  	case "Clear":
		  		
		  		file = new File("src/weather_pics/clear.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		 stops = new Stop[] { new Stop(0, Color.web("#C2FFD8")), new Stop(1,Color.web("#465EFB"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		
		  		break;
		  	case "Clouds":
		  		file = new File("src/weather_pics/clouds.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#FFCF71")), new Stop(1,Color.web("#2376DD"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Fog":
		  		file = new File("src/weather_pics/fog.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#485461")), new Stop(1,Color.web("#28313b"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Mist":
		  		file = new File("src/weather_pics/fog.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#d2ccc4")), new Stop(1,Color.web("#2f4353"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Snow":
		  		file = new File("src/weather_pics/snow.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#81FFEF")), new Stop(1,Color.web("#F067B4"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Rain":
		  		file = new File("src/weather_pics/rain.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#04619f")), new Stop(1,Color.web("#000000"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Drizzle":
		  		file = new File("src/weather_pics/drizzle.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#006EFF")), new Stop(1,Color.web("#1100A2"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
		  	case "Thunderstorm":
		  		file = new File("src/weather_pics/thunderstorm.jpg");
		  		image = new Image(file.toURI().toString());
		  		picView.setImage(image);
		  		stops = new Stop[] { new Stop(0, Color.web("#130f40")), new Stop(1,Color.web("#000000"))};
				 linear = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);  
				 bgFill = new BackgroundFill (linear, CornerRadii.EMPTY, Insets.EMPTY);
				 mainPanel.setBackground(new Background(bgFill));
		  		break;
			  	
			 
		  }
	}
	
	public void getAPIData(String city) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + city + ",us&units=imperial&mode=JSON&appid=23b5d8c50cb7d4ed6dd57df088885d7f")).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)//lamda expression, use body method from HttpResponse on the received response
			.thenApply(Controller::parseJson)
			.join();
	}
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
	  private static String getCardinalDirection(double bearing) {
		  String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
		  int index = (int) Math.floor( ((bearing-22.5)%360) / 45 );
		  return directions[index+1];
		
	
	
}
}
