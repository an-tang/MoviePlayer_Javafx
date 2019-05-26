package application;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;

public class MySceneController implements Initializable {
	@FXML
	private MediaView mv;
	@FXML
	private Slider sliderTime;
	@FXML
	private Slider sliderVolume;
	@FXML
	private BorderPane mainPane;
	@FXML
	private Label lbTime;
	@FXML
	private Label lbMaxTime;
	private MediaPlayer mediaplayer;

	private double rate = 1;
	private double volume = 5;
	private double skip = 5;
	private String filePath;

	@FXML
	private void handleButtonAction(ActionEvent event) throws MalformedURLException, ClassNotFoundException {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select only mp4 file", "*.mp4");
		filechooser.getExtensionFilters().add(filter);
		File file = filechooser.showOpenDialog(null);

		filePath = file.toURI().toURL().toString();

		if (filePath != null) {
			Media media = new Media(filePath);
			mediaplayer = new MediaPlayer(media);

			mediaplayer.setAutoPlay(true);
			mv.setMediaPlayer(mediaplayer);
			DoubleProperty width = mv.fitWidthProperty();
			DoubleProperty height = mv.fitHeightProperty();
			width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

			// mediapplayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaplayer.play();
			mediaplayer.setOnReady(new Runnable() {

				@Override
				public void run() {
					sliderTime.setMaxWidth(mediaplayer.getMedia().getWidth() - 100);
					sliderTime.setMin(0.0);
					sliderTime.setValue(0.0);
					sliderTime.setMax(mediaplayer.getTotalDuration().toSeconds());
					double maxTime = mediaplayer.getTotalDuration().toSeconds();
					lbMaxTime.setText(convertTime((String.valueOf(maxTime))));

				}
			});

			mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
						Duration current) {
					sliderTime.setValue(current.toSeconds());
					lbTime.setText(convertTime(String.valueOf(sliderTime.getValue())));
				}
			});

			sliderTime.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					mediaplayer.seek(Duration.seconds(sliderTime.getValue()));

				}
			});

			sliderVolume.setValue(mediaplayer.getVolume() * 100);
			sliderVolume.valueProperty().addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					mediaplayer.setVolume(sliderVolume.getValue() / 100);
				}
			});

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		String path = new File("src/video/Careless Whisper.mp4").getAbsolutePath();
//		media = new Media(new File(path).toURI().toString());
//		mp = new MediaPlayer(media);
//		mv.setMediaPlayer(mp);
//		mp.setAutoPlay(true);
//		DoubleProperty width = mv.fitWidthProperty();
//		DoubleProperty height = mv.fitHeightProperty();
//		width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
//		height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
//		//slVolume.setValue(mp.getVolume());

//	
		// sliderTime.setMaxWidth(Double.MAX_VALUE);

	}

	public void Play(ActionEvent event) {
		mediaplayer.play();
	}

	public void Pause(ActionEvent event) {
		mediaplayer.pause();
	}

	public void Fast(ActionEvent event) {
		rate += 0.25;
		mediaplayer.setRate(rate);
	}

	public void Slow(ActionEvent event) {
		rate -= 0.25;
		mediaplayer.setRate(rate);
	}

	public void Reload(ActionEvent event) {
		mediaplayer.seek(mediaplayer.getStartTime());
		mediaplayer.play();
	}

	public void volumeUp(ActionEvent event) {
		sliderVolume.setValue(sliderVolume.getValue() + volume);
		sliderVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				
				mediaplayer.setVolume(sliderVolume.getValue() / 100);
			}
		});
	}

	public void volumeDown(ActionEvent event) {
		sliderVolume.setValue(sliderVolume.getValue() - volume);
		sliderVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {

				mediaplayer.setVolume(sliderVolume.getValue() / 100);
			}
		});
	}

	public void skipForward(ActionEvent event) {
		sliderTime.setValue(sliderTime.getValue() + skip);
		mediaplayer.seek(Duration.seconds(sliderTime.getValue()));
		
		mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
					Duration current) {
				
				//sliderTime.setValue(current.toSeconds());
				lbTime.setText(convertTime(String.valueOf(sliderTime.getValue())));
			}
		});
	}
	
	public void skipBack(ActionEvent event) {
		sliderTime.setValue(sliderTime.getValue() - skip);
		mediaplayer.seek(Duration.seconds(sliderTime.getValue()));
		
		mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
					Duration current) {
				
				//sliderTime.setValue(current.toSeconds());
				lbTime.setText(convertTime(String.valueOf(sliderTime.getValue())));
			}
		});
	}
	
	public String convertTime(String times) {
		int hour = 0, min, sec;
		String[] parts = times.split("\\.");
		String temp = parts[0];
		int time = Integer.valueOf(temp);
		String result = "";
		if (time / 3600 == 0) {
			hour = 0;
			min = time / 60;
			sec = time % 60;

		} else {
			hour = time / 3600;
			time = time % 3600;
			min = time / 60;
			sec = time % 60;
		}
		if (hour > 0)
			result += String.valueOf(hour);
		if (min >= 10)
			result += String.valueOf(min);
		else if (min < 10) {
			if (hour == 0)
				result += String.valueOf(min);
			else
				result += ":0" + String.valueOf(min);
		}

		if (sec >= 10)
			result += ":" + String.valueOf(sec);
		else
			result += ":0" + String.valueOf(sec);
		return result;
	}

//	public void VolumeUp(ActionEvent event) {
//		mp.setVolume(slVolume.getValue()*100);
//	}
//	public void VolumeDown(ActionEvent event) {
//		mp.setVolume(slVolume.getValue()/100);
//	}

}
