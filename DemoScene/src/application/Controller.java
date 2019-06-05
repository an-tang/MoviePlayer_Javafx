package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXSlider;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {

	@FXML
	private MediaView mv;
	@FXML
	private JFXSlider slVolume;
	@FXML
	private JFXSlider slTime;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private AnchorPane controlPane;
	@FXML
	private Label lbTime;
	@FXML
	private Label lbMaxTime;
	@FXML
	private Button btnVolume;
	@FXML
	private FontAwesomeIconView iconPlay;
	@FXML
	private FontAwesomeIconView iconVolume;
	@FXML
	private MenuItem item025;
	@FXML
	private MenuItem item075;
	@FXML
	private MenuItem item05;
	@FXML
	private MenuItem item1;
	@FXML
	private MenuItem item125;
	@FXML
	private MenuItem item15;
	@FXML
	private MenuItem item175;
	@FXML
	private MenuItem item2;
	@FXML
	private MenuItem item5s;
	@FXML
	private MenuItem item10s;
	@FXML
	private MenuItem item15s;
	@FXML
	private MenuItem item30s;
	@FXML
	private Button btnBrowse;
	@FXML
	private Button btnFullScreen;

	private Timer timer;
	private long countdown = 2000;
	private LinkedList<TimerTask> lsTask = new LinkedList<TimerTask>();
	private MediaPlayer mediaplayer;
	private boolean isPlaying = false;
	private double rate = 1;
	private double volume = 5;
	private double skip = 5;
	private String filePath;
	private double oldVolume = 0;
	TimerTask task = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		slVolume.setValue(50);
		slTime.setValue(0);
		iconPlay.setGlyphName("PLAY");
		isPlaying = true;
		slVolume.setVisible(false);
		timer = new Timer();

		btnVolume.hoverProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (btnVolume.isHover())
					slVolume.setVisible(true);
				if (!btnVolume.isHover()) {
					task = createTask(task);
					timer.schedule(task, countdown);
					lsTask.add(task);
				}
				if (lsTask.size() > 1)
					lsTask.removeFirst().cancel();
			}
		});

		slVolume.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				slVolume.setVisible(true);
			}
		});

		slVolume.hoverProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (slVolume.isHover())
					slVolume.setVisible(true);
				if (!slVolume.isHover()) {
					task = createTask(task);
					timer.schedule(task, countdown);
					lsTask.add(task);
				}
				if (lsTask.size() > 1)
					lsTask.removeFirst().cancel();
			}
		});

		controlPane.hoverProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				controlPane.setVisible(true);
				if (!controlPane.isHover()) {
					System.out.println("control");
					task = createPaneTask(task);
					timer.schedule(task, countdown);
					lsTask.add(task);
				}
				if (lsTask.size() > 1)
					lsTask.removeFirst().cancel();
			}
		});

		mainPane.hoverProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				controlPane.setVisible(true);
				if (!mainPane.isHover() || !controlPane.isHover()) {
					System.out.println("main   ");
					task = createPaneTask(task);
					timer.schedule(task, countdown);
					lsTask.add(task);
				}
				if (lsTask.size() > 1)
					lsTask.removeFirst().cancel();
			}
		});

		mainPane.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				controlPane.setVisible(true);
				task = createPaneTask(task);
				timer.schedule(task, countdown);
				lsTask.add(task);
				if (lsTask.size() > 1)
					lsTask.removeFirst().cancel();
			}
		});
	}

	@FXML
	private void handleButtonAction(ActionEvent event) throws MalformedURLException, ClassNotFoundException {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select only mp4 file", "*.mp4");
		filechooser.getExtensionFilters().add(filter);
		File file = filechooser.showOpenDialog(null);
		if (file != null)
			filePath = file.toURI().toURL().toString();

		if (filePath != null) {
			if (mediaplayer != null)
				mediaplayer.stop();
			Media media = new Media(filePath);
			mediaplayer = new MediaPlayer(media);

			mediaplayer.setAutoPlay(true);
			mv.setMediaPlayer(mediaplayer);

			DoubleProperty width = mv.fitWidthProperty();
			DoubleProperty height = mv.fitHeightProperty();
			width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

			mv.setPreserveRatio(false);
			mediaplayer.play();
			Initialize();
		}
	}

	public void Play(ActionEvent event) {
		if (mediaplayer != null) {
			if (isPlaying) {
				mediaplayer.pause();
				isPlaying = false;
				iconPlay.setGlyphName("PLAY");
			} else {
				mediaplayer.play();
				isPlaying = true;
				iconPlay.setGlyphName("PAUSE");
			}
		}
	}

	public void Reload(ActionEvent event) {
		if (mediaplayer != null) {
			mediaplayer.seek(mediaplayer.getStartTime());
			mediaplayer.play();
		}
	}

	public void volumeUp(ActionEvent event) {
		slVolume.setValue(slVolume.getValue() + volume);
		iconVolume.setGlyphName("VOLUME_UP");
		slVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				mediaplayer.setVolume(slVolume.getValue() / 100);
			}
		});
	}

	public void volumeDown(ActionEvent event) {
		slVolume.setValue(slVolume.getValue() - volume);
		slVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				mediaplayer.setVolume(slVolume.getValue() / 100);
				if (slVolume.getValue() == 0) {
					iconVolume.setGlyphName("VOLUME_OFF");
				}
			}
		});
	}

	public void skipForward(ActionEvent event) {
		if (mediaplayer != null) {
			slTime.setValue(slTime.getValue() + skip);
			mediaplayer.seek(Duration.seconds(slTime.getValue()));

			mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
						Duration current) {

					// sliderTime.setValue(current.toSeconds());
					lbTime.setText(convertTime(String.valueOf(slTime.getValue())));
				}
			});
		}
	}

	public void skipBack(ActionEvent event) {
		if (mediaplayer != null) {
			slTime.setValue(slTime.getValue() - skip);
			mediaplayer.seek(Duration.seconds(slTime.getValue()));

			mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
						Duration current) {

					// sliderTime.setValue(current.toSeconds());
					lbTime.setText(convertTime(String.valueOf(slTime.getValue())));
				}
			});
		}
	}

	@FXML
	private void fullScreen(ActionEvent event) {
		Stage stage = (Stage) btnFullScreen.getScene().getWindow();
		if (!Main.isFullScreen) {
			stage.setFullScreen(true);
			Main.isFullScreen = true;
		} else {
			stage.setFullScreen(false);
			Main.isFullScreen = false;
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case LEFT:
		case KP_LEFT:
		case F:
			mainPane.requestFocus();
			this.skipBack(new ActionEvent());
			break;
		case RIGHT:
		case KP_RIGHT:
		case J:
			mainPane.requestFocus();
			this.skipForward(new ActionEvent());
			break;
		case UP:
			mainPane.requestFocus();
			this.volumeUp(new ActionEvent());
			break;
		case DOWN:
			mainPane.requestFocus();
			this.volumeDown(new ActionEvent());
			break;
		case SPACE:
			mainPane.requestFocus();
			this.Play(new ActionEvent());
			break;
		default:
			break;
		}
	}

	@FXML
	void mouseClicked(MouseEvent event) {
		controlPane.setVisible(false);
	}

	private void Initialize() {
		item025.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 0.25;
				mediaplayer.setRate(rate);
			}
		});

		item05.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 0.5;
				mediaplayer.setRate(rate);
			}
		});

		item075.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 0.75;
				mediaplayer.setRate(rate);
			}
		});

		item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 1;
				mediaplayer.setRate(rate);
			}
		});

		item125.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 1.25;
				mediaplayer.setRate(rate);
			}
		});

		item15.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 1.5;
				mediaplayer.setRate(rate);
			}
		});

		item175.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 1.75;
				mediaplayer.setRate(rate);
			}
		});

		item2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rate = 2;
				mediaplayer.setRate(rate);
			}
		});

		item5s.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				skip = 5;
			}
		});

		item10s.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				skip = 10;
			}
		});

		item15s.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				skip = 15;
			}
		});

		item30s.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				skip = 30;
			}
		});

		slVolume.setValue(mediaplayer.getVolume() * 100 / 2);
		slVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				mediaplayer.setVolume(slVolume.getValue() / 100);
			}
		});

		slTime.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mediaplayer.seek(Duration.seconds(slTime.getValue()));

			}
		});

		mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration,
					Duration current) {
				slTime.setValue(current.toSeconds());
				lbTime.setText(convertTime(String.valueOf(slTime.getValue())));
			}
		});

		mediaplayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				slTime.setMaxWidth(mediaplayer.getMedia().getWidth() - 100);
				slTime.setMin(0.0);
				slTime.setValue(0.0);
				slTime.setMax(mediaplayer.getTotalDuration().toSeconds());
				slTime.prefWidthProperty().bind(mainPane.widthProperty());
				double maxTime = mediaplayer.getTotalDuration().toSeconds();
				lbMaxTime.setText(convertTime((String.valueOf(maxTime))));
			}
		});

//		mv.fitWidthProperty().bind(mainPane.widthProperty());
//		mv.fitHeightProperty().bind(mainPane.heightProperty());

//		mediapplayer.setCycleCount(MediaPlayer.INDEFINITE);
		iconPlay.setGlyphName("PAUSE");
	}

	public String convertTime(String times) {
		int hour, min, sec;
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
			if (hour != 0)
				result += ":" + String.valueOf(min);
			else
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

	private TimerTask createTask(TimerTask task) {
		task = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task timer");
				slVolume.setVisible(false);
			}
		};
		return task;
	}

	private TimerTask createPaneTask(TimerTask task) {
		task = new TimerTask() {
			@Override
			public void run() {
				System.out.println("pane task timer");
				controlPane.setVisible(false);
				if (!(slVolume.isHover() && btnVolume.isHover()))
					slVolume.setVisible(false);
			}
		};
		return task;
	}

	@FXML
	void muteVolume(ActionEvent event) {
		System.out.println("mute volume");
		double temp = slVolume.getValue();
		if (temp == 0) {
			slVolume.setValue(oldVolume);
			iconVolume.setGlyphName("VOLUME_UP");
		} else {
			oldVolume = slVolume.getValue();
			slVolume.setValue(0);
			iconVolume.setGlyphName("VOLUME_OFF");
		}
		slVolume.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				mediaplayer.setVolume(slVolume.getValue() / 100);
			}
		});
	}
}
