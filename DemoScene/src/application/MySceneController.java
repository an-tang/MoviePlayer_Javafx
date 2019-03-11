package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;

public class MySceneController implements Initializable {
	@FXML private MediaView mv;
	@FXML private Slider sliderTime;
	@FXML private Slider slVolume;
	private MediaPlayer mediapplayer;
	private Media media;
	private double rate = 1;
	private String filePath;
	@FXML
	private void handleButtonAction(ActionEvent event) throws MalformedURLException{
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select only mp4 file", "*.mp4", "*.mkv");
		filechooser.getExtensionFilters().add(filter);
		File file = filechooser.showOpenDialog(null);
		filePath = file.toURI().toURL().toString();
		if(filePath!=null) {
			media = new Media(filePath);
			mediapplayer = new MediaPlayer(media);
			mv.setMediaPlayer(mediapplayer);
			mediapplayer.setAutoPlay(true);
			DoubleProperty width = mv.fitWidthProperty();
			DoubleProperty height = mv.fitHeightProperty();
			width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
			mediapplayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediapplayer.play();
			sliderTime.setMin(0.0);
			sliderTime.setValue(0.0);
			sliderTime.setMax(mediapplayer.getTotalDuration().toSeconds());
			
			
			mediapplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
					sliderTime.setValue(current.toSeconds());
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
//		slVolume.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> ov,
//                    Number old_val, Number new_val) {
//                       slVolume.setValue((new_val.doubleValue()));
//                }
//            });
//	
		//sliderTime.setMaxWidth(Double.MAX_VALUE);
		sliderTime.prefWidth(Double.MAX_VALUE);
		
	}
	public void Play(ActionEvent event) {
		mediapplayer.play();
	}
	public void Pause(ActionEvent event) {
		mediapplayer.pause();
	}
	public void Fast(ActionEvent event) {
		rate+=0.25;
		mediapplayer.setRate(rate);
	}
	public void Slow(ActionEvent event) {
		rate-=0.25;
		mediapplayer.setRate(rate);
	}
	public void Reload(ActionEvent event) {
		mediapplayer.seek(mediapplayer.getStartTime());
		mediapplayer.play();
	}
	
//	public void VolumeUp(ActionEvent event) {
//		mp.setVolume(slVolume.getValue()*100);
//	}
//	public void VolumeDown(ActionEvent event) {
//		mp.setVolume(slVolume.getValue()/100);
//	}
	
}
