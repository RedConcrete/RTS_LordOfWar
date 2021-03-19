package code.lordofwar.main;

import code.lordofwar.screens.LoginScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static code.lordofwar.backend.Constants.WORLD_HEIGHT_PIXEL;
import static code.lordofwar.backend.Constants.WORLD_WIDTH_PIXEL;


/*
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
*/


public class LOW extends Game {
	private Stage stage;
	private Skin skin;
	WebSocketListener listener = new WebSocketListener() {

		@Override
		public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
			super.onClosed(webSocket, code, reason);
		}

		@Override
		public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
			super.onClosing(webSocket, code, reason);
		}

		@Override
		public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
			super.onFailure(webSocket, t, response);
		}

		@Override
		public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
			super.onMessage(webSocket, text);
		}

		@Override
		public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
			super.onMessage(webSocket, bytes);
		}

		@Override
		public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
			webSocket.send("Hello...");
			webSocket.send("...World!");
			webSocket.send(ByteString.decodeHex("deadbeef"));
			webSocket.close(10, "Goodbye, World!");
		}
	};

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

		FitViewport fitViewport = new FitViewport(WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
		stage = new Stage(fitViewport);

		 //TODO progressbar machen !! und Assetloader

		buildWebSocketConnection();

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

		OkHttpClient client = new OkHttpClient();

		HttpUrl httpUrl = HttpUrl.parse("http://localhost:8080/");

		Request request = new Request.Builder()
				.url(httpUrl)
				.build();

		WebSocket webSocket = client.newWebSocket(request,listener);


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