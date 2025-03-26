package hi.verkefni.vidmot;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Aðalklasi forritsins fyrir flösku- og dósamóttöku.
 * Sér um að ræsa og birta JavaFX notendaviðmótið.
 */
public class FloskurApplication extends Application {
    /**
     * Ræsir JavaFX forritið og stillir upp aðalsviðinu.
     * Hleður notendaviðmótinu úr FXML skránni og setur upp helsta útlitið.
     * @param stage aðalsviðið fyrir forritið
     * @throws IOException ef ekki tekst að hlaða FXML skrána
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FloskurApplication.class.getResource("/hi/verkefni/vidmot/flosku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Aðal aðferð sem ræsir JavaFX forritið.
     * @param args skipanalínuargögn
     */
    public static void main(String[] args) {
        launch();
    }
}