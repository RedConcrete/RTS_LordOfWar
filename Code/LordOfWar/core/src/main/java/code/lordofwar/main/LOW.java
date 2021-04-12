package code.lordofwar.main;

import code.lordofwar.backend.Constants;
import code.lordofwar.backend.GameWebSocketListener;
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

	private String sessionID;

	public LOW() {
		con = new Constants();
		buildWebSocketConnection();
	}

	@Override
	public void create() {

		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

		FitViewport fitViewport = new FitViewport(WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
		stage = new Stage(fitViewport);

		 //TODO progressbar machen !! und Assetloader



		this.setScreen(new LoginScreen(this, skin));

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
		//HttpUrl httpUrl = HttpUrl.parse("http://93.221.8.85:25565/api/v1/ws");


		Request request = new Request.Builder()
				.url(httpUrl)
				.addHeader("Authorization", "Basic " + "MToxMjM0")
				.build();

		GameWebSocketListener gameWebSocketListener = new GameWebSocketListener(this);
		webSocket = client.newWebSocket(request,gameWebSocketListener);
		gameWebSocketListener.setWebSocket(webSocket);
	}

	public Constants getCon() {
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
}
