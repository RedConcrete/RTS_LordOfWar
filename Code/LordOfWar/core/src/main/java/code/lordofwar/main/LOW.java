package code.lordofwar.main;

import code.lordofwar.backend.CommunicationHandler;
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

	public Constants getCon() {
		return con;
	}

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


		Request request = new Request.Builder()
				.url(httpUrl)
				.addHeader("Authorization", "Basic " + "MToxMjM0")
				.build();

		GameWebSocketListener gameWebSocketListener = new GameWebSocketListener(this);
		webSocket = client.newWebSocket(request,gameWebSocketListener);
		CommunicationHandler cHandler = new CommunicationHandler();

		gameWebSocketListener.setCommunicationHandler(cHandler);
		gameWebSocketListener.setWebSocket(webSocket);
		cHandler.setGameWebSocketListener(gameWebSocketListener);


	}

	public WebSocket getWebSocket() {
		if(webSocket == null){
			System.out.println("ist Null");
			return webSocket;
		}
		return webSocket;
	}
}


/*
		Window window = new Window("Example screen", skin, "border");
		window.defaults().pad(4f);
		window.add("This is a simple Scene2D view.").row();
		final TextButton button = new TextButton("Click me!", skin);
		button.pad(8f);
		button.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				button.setText("Clicked.");
			}
		});

		window.add(button);
		window.pack();
		window.setPosition(stage.getWidth() / 2f - window.getWidth() / 2f,
				stage.getHeight() / 2f - window.getHeight() / 2f);
		window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
		stage.addActor(window);

		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f); 		//<----------- der einzige weg alles zu löschen :|  <---------
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}

 */