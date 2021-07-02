package code.lordofwar.main;

import code.lordofwar.backend.BackgroundMusic;
import code.lordofwar.backend.Constants;
import code.lordofwar.backend.GameWebSocketListener;
import code.lordofwar.backend.Sounds;
import code.lordofwar.screens.LoginScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.util.concurrent.TimeUnit;

import static code.lordofwar.backend.Constants.WORLD_HEIGHT_PIXEL;
import static code.lordofwar.backend.Constants.WORLD_WIDTH_PIXEL;


/*
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
*/

public class LOW extends Game {
	private Stage stage;
	private Skin skin;
	private WebSocket webSocket;
	private Constants con;
	private BackgroundMusic player;
	private String sessionID;

	public LOW() {
		con = new Constants();
		player=new BackgroundMusic(con);
		buildWebSocketConnection();
	}

	@Override
	public void create() {

		skin = new Skin(Gdx.files.internal("ui/skin/uiskin.json"));
		skin.getFont("default").getData().setScale(2f);

		FitViewport fitViewport = new FitViewport(WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
		stage = new Stage(fitViewport);

		 //TODO progressbar machen !! und Assetloader

		this.setScreen(new LoginScreen(this, skin));

		player.music("Ireland_2.wav");
		new Sounds();

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {


	}
	public void buildWebSocketConnection(){

		OkHttpClient client = new OkHttpClient.Builder()
				.readTimeout(0, TimeUnit.MILLISECONDS)
				.build();

		HttpUrl httpUrl = HttpUrl.parse("http://localhost:8080/api/v1/ws");
		//HttpUrl httpUrl = HttpUrl.parse("http://labswp-sose21-team4.it-stud.hs-heilbronn.de:8080/api/v1/ws");
		//HttpUrl httpUrl = HttpUrl.parse("http://redconcrete.sytes.net:25591/api/v1/ws");

		Request request = new Request.Builder()
				.url(httpUrl)
				.addHeader("Authorization", "Basic " + "MToxMjM0")
				.build();

		GameWebSocketListener gameWebSocketListener = new GameWebSocketListener(this);
		webSocket = client.newWebSocket(request,gameWebSocketListener);
		gameWebSocketListener.setWebSocket(webSocket);

	}

	public Constants getConstants() {
		return con;
	}

	public WebSocket getWebSocket() {
		if(webSocket == null){
			System.out.println("ist Null");
			return webSocket;
		}
		return webSocket;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public BackgroundMusic getPlayer() {
		return player;
	}
}
