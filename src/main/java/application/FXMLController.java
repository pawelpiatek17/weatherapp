package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import dataStructure.City;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.core.OWM.Language;
import net.aksingh.owmjapis.core.OWM.Unit;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.System;
import net.aksingh.owmjapis.model.param.Weather;
import net.aksingh.owmjapis.model.param.WeatherData;
import util.CSV;
import util.Units;
import util.WeatherDescriptionTranslator;

public class FXMLController implements Initializable{
	
	public FXMLController() {
	}
	@FXML
	private TextField  filterInputtf;
	@FXML
	private ListView<City> listOfCities;
	@FXML
	private Label cityNameValueLabel;
	@FXML
	private Label countryValueLabel;
	@FXML
	private Label weatherDescriptionValueLabel;
	@FXML
	private Label temperatureValueLabel;
	@FXML
	private Label pressureValueLabel;
	@FXML
	private Label humidityValueLabel;
	@FXML
	private Label windSpeedValueLabel;
	@FXML
	private Label rainValueLabel;
	@FXML
	private Label cloudsValueLabel;
	@FXML
	private Label snowValueLabel;
	@FXML
	private VBox weatherDescriptionVbox;
	@FXML
	private Label dateLabel;
	@FXML
	private VBox weatherValuesVBox;
	@FXML
	private Label cityNameValueFiveDaysLabel;
	@FXML
	private VBox dateFiveDaysVBox;
	@FXML
	private VBox temperatureFiveDaysVBox;
	@FXML
	private VBox pressureFiveDaysVBox;
	@FXML
	private VBox humidityFiveDaysVBox;
	@FXML
	private VBox windSpeedFiveDaysVBox;
	@FXML
	private VBox rainFiveDaysVBox;
	@FXML
	private VBox cloudsFiveDaysVBox;
	@FXML
	private VBox weatherDescriptionFiveDaysVBox;
	@FXML
	private Button saveToFileBtn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			//Fetching city list from json
			Gson gson = new Gson();
			JsonReader jsonReader;
			jsonReader = new JsonReader(new FileReader("city.list.json"));
			Type listType = new TypeToken<ArrayList<City>>(){}.getType();
			ArrayList<City> cities = gson.fromJson(jsonReader, listType);
			ObservableList<City> observableCities = FXCollections.observableArrayList(cities);
			//Add listener to listen for item selection in listView
			listOfCities.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<City>() {
				@Override
				public void changed(ObservableValue<? extends City> observable, City oldValue, City newValue) {
					if (newValue != null) {
						FXMLController.this.getDataForCity(newValue);
					}
				}
			});
			//ListView filter logic
			FilteredList<City> filteredData = new FilteredList<>(observableCities, s -> true);
			filterInputtf.textProperty().addListener(((observable) -> {
				String filter = filterInputtf.getText();
				if(filter == null || filter.length() ==0) {
					filteredData.setPredicate(s -> true);
				}else {
					filteredData.setPredicate(s -> s.getName().contains(filter) || s.getCountry().contains(filter));
				}
			}));
			saveToFileBtn.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					FXMLController.this.saveDataToFile();
				}
				
			});
			listOfCities.setItems(filteredData);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void saveDataToFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Zapisz dane");
		File file = fileChooser.showSaveDialog(listOfCities.getScene().getWindow());
		if (file != null) {
			CSV.create(file, prepareCurrentData());
			
		}
	}
	
	private HashMap<String, String> prepareCurrentData(){
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("city_name", cityNameValueLabel.getText());
		hashMap.put("temperature", temperatureValueLabel.getText());
		hashMap.put("pressure", pressureValueLabel.getText());
		hashMap.put("humidity", humidityValueLabel.getText());
		hashMap.put("wind", windSpeedValueLabel.getText());
		hashMap.put("rain", rainValueLabel.getText());
		hashMap.put("clouds", cloudsValueLabel.getText());
		hashMap.put("snow", snowValueLabel.getText());
		return hashMap;
	}
	/** Gets data from OpenWeatherMap using OWM JAPI */
	protected void getDataForCity(City newValue) {
		try {
			OWM owm = new OWM("22c3aa2fa466840af8a8981419c3e5f4");
			owm.setLanguage(Language.POLISH); // NOT WORKING
			owm.setUnit(Unit.METRIC);
			CurrentWeather currentWeather;
			currentWeather = owm.currentWeatherByCityId(newValue.getId());
			if (currentWeather.hasRespCode() && currentWeather.getRespCode() == 200) {
				for (Node l : weatherValuesVBox.getChildren()) {
					((Label) l).setText("");
				}
				bindCurrentDataToControls(currentWeather);
			}
			HourlyWeatherForecast forecastData = owm.hourlyWeatherForecastByCityId(newValue.getId());
			if (forecastData.hasRespCode() && forecastData.getRespCode().equals("200")) {
				bindForecastDataToControls(forecastData);
			}
			
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void bindForecastDataToControls(HourlyWeatherForecast forecastData) {
		if (forecastData.hasCityData()) {
			if (forecastData.getCityData().hasName() && forecastData.getCityData().hasCountryCode()) {
				cityNameValueFiveDaysLabel.setText(forecastData.getCityData().getName() + "," + forecastData.getCityData().getCountryCode());
			}
		}
		if (forecastData.hasDataList()) {
			dateFiveDaysVBox.getChildren().remove(1, dateFiveDaysVBox.getChildren().size());
			temperatureFiveDaysVBox.getChildren().remove(1, temperatureFiveDaysVBox.getChildren().size());
			humidityFiveDaysVBox.getChildren().remove(1, humidityFiveDaysVBox.getChildren().size());
			pressureFiveDaysVBox.getChildren().remove(1, pressureFiveDaysVBox.getChildren().size());
			cloudsFiveDaysVBox.getChildren().remove(1, cloudsFiveDaysVBox.getChildren().size());
			windSpeedFiveDaysVBox.getChildren().remove(1, windSpeedFiveDaysVBox.getChildren().size());
			weatherDescriptionFiveDaysVBox.getChildren().remove(1,weatherDescriptionFiveDaysVBox.getChildren().size());
			for (WeatherData wd : forecastData.getDataList()) {
				if (wd.hasDateTime()) {
					Label dateLabel = new Label(new SimpleDateFormat("yyyy.MM.d HH:mm:ss").format(wd.getDateTime()));
					dateLabel.setFont(new Font(14));
					dateFiveDaysVBox.getChildren().add(dateLabel);
				}
				if (wd.hasMainData()) {
					if (wd.getMainData().hasTemp()) {
						Label tempLabel = new Label(wd.getMainData().getTemp().toString() + Units.CELSIUS_DEGREES);
						tempLabel.setFont(new Font(14));
						temperatureFiveDaysVBox.getChildren().add(tempLabel);
					}
					if (wd.getMainData().hasPressure()) {
						Label pressureLabel = new Label(wd.getMainData().getPressure().toString() + Units.HECTOPASCAL);
						pressureLabel.setFont(new Font(14));
						pressureFiveDaysVBox.getChildren().add(pressureLabel);
					}
					if (wd.getMainData().hasHumidity()) {
						Label humidityLabel = new Label(wd.getMainData().getHumidity().toString() + "%");
						humidityLabel.setFont(new Font(14));
						humidityFiveDaysVBox.getChildren().add(humidityLabel);
					}
				}
				if (wd.hasCloudData()) {
					if (wd.getCloudData().hasCloud()) {
						Label cloudLabel = new Label(wd.getCloudData().getCloud().toString() + "%");
						cloudLabel.setFont(new Font(14));
						cloudsFiveDaysVBox.getChildren().add(cloudLabel);
					}
				}
				if (wd.hasWindData()) {
					if (wd.getWindData().hasSpeed()) {
						Label windLabel = new Label(wd.getWindData().getSpeed().toString() + Units.METERS_PER_SECOND);
						windLabel.setFont(new Font(14));
						windSpeedFiveDaysVBox.getChildren().add(windLabel);
					}
				}
				if (wd.hasWeatherList()) {
					List<Weather> weathers = wd.getWeatherList();
					Weather w = weathers.get(0);
					Label weatherLabel = new Label(WeatherDescriptionTranslator.translate(w.getDescription()));
					weatherLabel.setFont(new Font(14));
					weatherDescriptionFiveDaysVBox.getChildren().add(weatherLabel);
				}
				//Rain and snow not available in OWM JAPI
				//Possible workaround with retrofit 
			}
		}
		
	}
	private void bindCurrentDataToControls(CurrentWeather weather) {
		if (weather.hasCityName()) {
			cityNameValueLabel.setText(weather.getCityName());
		}
		if (weather.hassystemData()) {
			System system = weather.getSystemData();
			if (system.hasCountryCode()) {
				cityNameValueLabel.setText(cityNameValueLabel.getText() + ", " + system.getCountryCode());
			}
		}
		if (weather.hasDateTime()) {
			dateLabel.setText(new SimpleDateFormat("yyyy.MM.d HH:mm:ss").format(weather.getDateTime().getTime()));
		}
		if (weather.hasWeatherList()) {
			List<Weather> weathers = weather.getWeatherList();
			weatherDescriptionVbox.getChildren().clear();
			for (Weather w : weathers) {
				Label weatherLabel = new Label(WeatherDescriptionTranslator.translate(w.getDescription()));
				weatherLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/" + w.getIconCode() + ".png"))));
				weatherLabel.setContentDisplay(ContentDisplay.RIGHT);
				weatherLabel.setFont(new Font(14));
				weatherDescriptionVbox.getChildren().add(weatherLabel);
			}
			
		}
		if (weather.hasMainData()) {
			if (weather.getMainData().hasTemp()) {
				temperatureValueLabel.setText(weather.getMainData().getTemp().toString() + Units.CELSIUS_DEGREES);
			}
			if (weather.getMainData().hasPressure()) {
				pressureValueLabel.setText(weather.getMainData().getPressure().toString() + Units.HECTOPASCAL);
			}
			if (weather.getMainData().hasHumidity()) {
				humidityValueLabel.setText(weather.getMainData().getHumidity().toString() + "%");
			}
		}
		if (weather.hasWindData()) {
			if (weather.getWindData().hasSpeed()) {
				windSpeedValueLabel.setText(weather.getWindData().getSpeed().toString() + Units.METERS_PER_SECOND);
			}
		}
		if (weather.hasRainData()) {
			if (weather.getRainData().hasPrecipVol3h()) {
				rainValueLabel.setText(weather.getRainData().getPrecipVol3h().toString() + Units.MILLIMETERS);
			}
		}
		if (weather.hasCloudData()) {
			if (weather.getCloudData().hasCloud()) {
				cloudsValueLabel.setText(weather.getCloudData().getCloud().toString() + "%");
			}
		}
		if (weather.hasSnowData()) {
			if (weather.getSnowData().hasSnowVol3h()) {
				snowValueLabel.setText(weather.getSnowData().getSnowVol3h().toString() + Units.MILLIMETERS);
			}
		}
	}
}
