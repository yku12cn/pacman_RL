package pacman;

import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.game.Constants;
import java.util.EnumMap;
import java.util.Random;
import pacman.entries.ghosts.*;
import pacman.entries.pacman.CustomFeatureSet;

import pacman.entries.pacman.FeatureSet;
import pacman.entries.pacman.QPacMan;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class PacmanDemo {

    public static void init(){
        defaultC = new Constants();
        defaultC.MAZE_NUM = maze_num;
        defaultC.GHOST_SPEED_REDUCTION = ghostSlowdown;
        defaultC.NUM_GHOSTS = ghost_num;
    }

    private static EnumMap<GHOST, MOVE> getGhostMove(Game game){
        return ghostsS.getMove(game.copy(), -1);
    }

    /** Human controller demo **/
    public static void humanDemo(){
        HumanController human = new HumanController(new KeyBoardInput());
        init();
        defaultC.DELAY = defaultC.DELAY*10;

        Game game=new Game(random.nextLong(), defaultC);
        GameView gv=new GameView(game).showGame();
        gv.getFrame().addKeyListener(human.getKeyboardInput());

        while(!game.gameOver()) {
            game.advanceGame(human.getMove(game, -1), getGhostMove(game));
            try {
                Thread.sleep(defaultC.DELAY);
            } catch (Exception e) {
            }
            gv.repaint();
        }
    }

    /** RL demo **/
    public static void RLdemo(){

        init();
        defaultC.DELAY = defaultC.DELAY*5;
        FeatureSet feature;

        feature = new CustomFeatureSet();

        QPacMan pacman = new QPacMan(feature);
        double[] initialData = new double[0];

        // evaluate the random policy
        double [] result = perform(pacman, test_time);

        System.out.println("score at start = " + result[0]);

        int num = 0;
        for (int x = 1; x <= train_time; x++) {
            double[] data = new double[initialData.length];
            train(pacman);
            num++;
            double[] episode_data = new double[0];

            for (int d=0; d<data.length; d++) data[d] += episode_data[d];

            double [] result1 = perform(pacman, test_time) ;
            double score = result1[0];		// average score over test number of games
            System.out.println("episode: " + num + "   score: " + score);
        }
        // vision of the game
        Game game=new Game(random.nextLong(), defaultC);
        pacman.initialize(game, true);
        GameView gv=new GameView(game).showGame();
        while(!game.gameOver()) {
            game.advanceGame(pacman.getMove(game.copy(), -1), getGhostMove(game));
            pacman.prepare(game);
            try{
                Thread.sleep(defaultC.DELAY);
            }catch(Exception e){
                e.printStackTrace();
            }
            gv.repaint();
        }
        System.out.println("Training Done");
    }

    /** Train a learner */

    public static void train(QPacMan pacman) {

        Game game = new Game(random.nextLong(), defaultC);
        pacman.initialize(game, false);
        while(!game.gameOver()) {
            game.advanceGame(pacman.getMove(game.copy(), -1), getGhostMove(game));
            pacman.prepare(game);
        }
    }

    /** Estimate the current performance of a learner. */
    public static double[] perform(QPacMan pacman, int num) {

        double[] performance = new double[2];
        double scores = 0;
        double steps = 0;

        for(int i = 0; i < num; i++) {
            Game game = new Game(random.nextLong(), defaultC);
            pacman.initialize(game, true);
            while(!game.gameOver()) {
                game.advanceGame(pacman.getMove(game.copy(), -1), getGhostMove(game));
                pacman.prepare(game);
            }
            scores += game.getScore();
            steps += game.getTotalTime();
        }
        performance[0] = scores/num;
        performance[1] = steps/num;
        return performance;
    }


    public static String Choice = "Q";
    public static int train_time = 400;
    public static int test_time = 1; // test episodes
    public static Random random = new Random();
    public static StandardGhosts ghostsS = new StandardGhosts();

    public static int maze_num = 2;
    // ghost speed: 1 = frozen
    public static int ghostSlowdown = 2;
    public static int ghost_num = 4;
    public static Constants defaultC;
}
