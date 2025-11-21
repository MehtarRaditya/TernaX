import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.print.DocFlavor;
import java.io.File;
import java.net.URL;

import static javafx.application.Application.launch;

public class Main extends Application {
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/Views/login.fxml").toURI().toURL();
        Scene s = new Scene(FXMLLoader.load(url));
        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}