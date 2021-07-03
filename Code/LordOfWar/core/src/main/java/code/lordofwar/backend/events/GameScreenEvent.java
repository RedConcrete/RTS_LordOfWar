package code.lordofwar.backend.events;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.backend.Team;
import code.lordofwar.backend.interfaces.CombatEntity;
import code.lordofwar.main.LOW;
import code.lordofwar.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static code.lordofwar.backend.MessageIdentifier.*;

public class GameScreenEvent extends Events {
    private int points;
    private final String lobbyID;
    private GameScreenEvent gameScreenEvent;
    private ArrayList connectedPlayers;
    private HashMap<String, Team> teamHashMap = new HashMap<String, Team>();
    private GameScreen gameScreen;
    private ArrayList<String> enemyUnits;


    public GameScreenEvent(LOW aGame, String lobbyID, GameScreen gameScreen) {
        super(aGame);
        points = 0;
        this.lobbyID = lobbyID;
        this.gameScreen = gameScreen;
        enemyUnits = new ArrayList<>();

    }

    public void sendPointRequest() {
        ArrayList<String> data = new ArrayList<>();
        data.add(game.getSessionID());
        data.add(lobbyID);
        webSocket.send(DataPacker.packData(GET_GAME_POINTS, DataPacker.stringCombiner(data)));
    }

    public void updatePoints(String[] arr) {
        if (Integer.parseInt(arr[1]) >= 0) {
            this.points = Integer.parseInt(arr[1]);
        }
    }

    public void sendLeaveGameNotice() {
        ArrayList<String> leaveData = new ArrayList<>();
        leaveData.add(game.getSessionID());
        leaveData.add(lobbyID);
        webSocket.send(DataPacker.packData(LEAVE_LOBBY, DataPacker.stringCombiner(leaveData)));
    }

    public void createTeams(int startingPosition) {
        for (int i = 0; i < connectedPlayers.size(); i++) {
            Team team = new Team(i);
            teamHashMap.put((String) connectedPlayers.get(i), team);
            team.setStartingPos(startingPosition);
        }
    }

    /**
     * The Method processCameraMovement moves the Camera in the direction the mouse is pointing at.
     *
     * @author Robin Hefner, Franz Klose
     */
    public void processCameraMovement(float xClicked, float yClicked, Camera camera, float CAMERASPEED, ShapeRenderer debugMovement, boolean cameraDebug, Vector3 posCameraDesired) {

        //oben links

        if (xClicked <= 50 && yClicked <= 5) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(0, 0, 50, 5);
                debugMovement.end();
            }
            camera.update();
        }
        if (xClicked <= 5 && yClicked <= 50) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(0, 0, 5, 50);
                debugMovement.end();
            }
            camera.update();
        }

        //unten links

        else if (xClicked <= 50 && yClicked >= camera.viewportHeight - 5) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(0, 0, 50, 5);
                debugMovement.end();
            }

            camera.update();
        } else if (xClicked <= 5 && yClicked >= camera.viewportHeight - 50) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(0, 0, 5, 50);
                debugMovement.end();
            }

            camera.update();
        }


        //oben rechts

        else if (xClicked >= camera.viewportWidth - 50 && yClicked <= 5) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(camera.viewportWidth - 50, 0, 50, 5);
                debugMovement.end();
            }

            camera.update();
        } else if (xClicked >= camera.viewportWidth - 5 && yClicked <= 50) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(camera.viewportWidth - 5, 0, 5, 50);
                debugMovement.end();
            }

            camera.update();
        }


        //unten rechts

        else if (xClicked >= camera.viewportWidth - 50 && yClicked >= camera.viewportHeight - 5) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(camera.viewportWidth - 5, camera.viewportHeight, 50, 5);
                debugMovement.end();
            }

            camera.update();
        } else if (xClicked >= camera.viewportWidth - 5 && yClicked >= camera.viewportHeight - 50) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(camera.viewportWidth - 5, camera.viewportHeight, 5, 50);
                debugMovement.end();
            }

            camera.update();
        }

        /**
         * moves the Camera left
         */
        //mitte links
        if (xClicked <= 5 && yClicked >= 50 && yClicked <= camera.viewportHeight - 50) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(0, 50, 5, camera.viewportHeight - 50);
                debugMovement.end();
            }

            camera.update();
        }

        //mitte rechts
        else if (xClicked >= camera.viewportWidth - 5 && yClicked >= 50 && yClicked <= camera.viewportHeight - 50) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(camera.viewportWidth - 5, 50, 5, camera.viewportHeight - 50);
                debugMovement.end();
            }

            camera.update();
        }

        //mitte oben
        else if (xClicked >= 50 && xClicked <= camera.viewportWidth - 50 && yClicked <= 5) {
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(50, camera.viewportHeight - 5, camera.viewportWidth - 100, 5);
                debugMovement.end();
            }

            camera.update();
        }

        //mitte unten
        else if (xClicked >= 50 && xClicked <= camera.viewportWidth - 50 && yClicked >= camera.viewportHeight - 5) {
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

            if (cameraDebug) {
                debugMovement.begin();
                debugMovement.rect(50, 0, camera.viewportWidth - 100, 5);
                debugMovement.end();
            }

            camera.update();
        } else {
            camera.update();
        }

    }

    public void CameraKeyEvents(Camera camera, float CAMERASPEED, Vector3 posCameraDesired) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
    }

    public void StageKeyEvents(Window windowExit, TextButton yesButton, TextButton noButton, Stage stage) {

        if (Gdx.input.isKeyPressed(Input.Keys.F10)) {

            windowExit.setVisible(true);
            yesButton.getLabel().setFontScale(2f);
            noButton.getLabel().setFontScale(2f);
            stage.addActor(windowExit);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            System.out.println("sdfadssa");
        }

    }

    public GameScreenEvent getGameScreenEvent() {

        return gameScreenEvent;
    }

    public void getConnectedPlayer(ArrayList<String> conPlayers, int startingPosition) {
        this.connectedPlayers = conPlayers;
        createTeams(startingPosition);
    }

    public HashMap<String, Team> getTeamHashMap() {
        return teamHashMap;
    }

    public int getPoints() {
        return points;
    }

    public void updateSoldierPos(ArrayList<String> a) {
        webSocket.send(DataPacker.packData(UPDATE_SOLDIER_POS, DataPacker.stringCombiner(a)));
    }

    public void sendAtkRequest(Pair<Integer, Integer> dmgPair, String enemyHashcode, CombatEntity entity) {
        //Syntax: [MI,lobbyID,starting STARTING POSITION (of enemy),UNITTYPE(Soldier or castle),enemy hashcode, DAMAGE TYPE,ATK]
        //      UPDATE_UNIT_HEALTH,entity.getTeam(),entity.getUnitType(),dmgPair.getFirst(),dmgPair.getSecond();
        ArrayList<String> atkData = new ArrayList<>();
        atkData.add(String.valueOf(entity.getTeam().getStartingPos()));
        atkData.add(lobbyID);
        atkData.add(entity.getUnitType());
        atkData.add(enemyHashcode);
        atkData.add(String.valueOf(dmgPair.getFirst()));
        atkData.add(String.valueOf(dmgPair.getSecond()));
        webSocket.send(DataPacker.packData(UPDATE_UNIT_HEALTH, DataPacker.stringCombiner(atkData)));
    }

    public void receiveDmg(String[] dmgData) {
        //Syntax: [MI,UNITTYPE(Soldier or castle),enemy hashcode, DAMAGE TYPE,ATK]
        String unitType = dmgData[1];
        Integer unitHash = Integer.parseInt(dmgData[2]);
        Integer dmgType = Integer.parseInt(dmgData[3]);
        Integer dmg = Integer.parseInt(dmgData[4]);
        gameScreen.processDmg(unitType, unitHash, dmgType, dmg);
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void processSoldiers(String[] data) {

        // data [0] = MessageID / data [1] = startigPos / data[2] = x Pos,y Pos / data[3] = SoldatenHash
        String[] enemyArray = new String[data.length - 1];
        System.arraycopy(data, 1, enemyArray, 0, data.length - 1);
        this.enemyUnits.addAll(Arrays.asList(enemyArray));
        //System.out.println(Arrays.toString(enemyArray));
        gameScreen.createSoldiers(new ArrayList<>(Arrays.asList(enemyArray)));

    }

    public ArrayList<String> getEnemyUnits() {
        return enemyUnits;
    }
}
