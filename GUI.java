import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {

    public static Stage window = new Stage();
    public static SpaceScene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Titanic by group 18");
        window.setX(0);
        window.setY(0);
        window.setWidth(1400);
        window.setHeight(800);

        scene = new SpaceScene(window);
        window.setScene(scene);
        window.show();
    }
}
