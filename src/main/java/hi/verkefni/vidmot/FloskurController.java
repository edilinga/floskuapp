package hi.verkefni.vidmot;
import hi.verkefni.vinnsla.Floskur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import java.net.URL;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


import javafx.scene.control.Button;

/**
 * Stýriklasi fyrir notendaviðmót flösku- og dósamóttöku forritsins.
 * Sér um að athuga hvort inntak sé löglegt, uppfærir heildarmagn og skilagjald og tengist vinnsluklasanum, Floskur.
 */
public class FloskurController implements Initializable {
    @FXML
    private TextField fxDosir;
    @FXML
    private TextField fxFloskur;
    @FXML
    private Label fxDosirVirdi;
    @FXML
    private Label fxFloskurVirdi;
    @FXML
    private Label fxSamtalsVirdi;
    private Floskur floskur = new Floskur();
    private int heildarFjoldi = 0;
    private int heildarVirdi = 0;

    @FXML
    private CheckBox fxDarkMode;
    @FXML
    private ToggleButton fxEngButton;

    @FXML
    private ToggleButton fxIceButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup languageGroup = new ToggleGroup();
        fxEngButton.setToggleGroup(languageGroup);
        fxIceButton.setToggleGroup(languageGroup);
    }



    /**
     * Aðferð sem virkir "Greiða" takkann.
     * Bætir fjölda flaskna og dósa og virði þeirra við heildarfjölda og heildarvirði.
     * Núllstillir inntaksreitina og einstakar merkingar.
     * @param event atburður sem virkist þegar smellt er á "Greiða" hnappinn
     */
    @FXML
    protected void onGreida(ActionEvent event) {
        try {
            int dosir = Integer.parseInt(fxDosir.getText());
            int floskurCount = Integer.parseInt(fxFloskur.getText());
            //Ath hvort inntak sé neikvætt
            if (dosir < 0 || floskurCount < 0) {
                if (dosir < 0) {
                    fxDosir.setStyle("-fx-border-color: red;");
                }
                if (floskurCount < 0) {
                    fxFloskur.setStyle("-fx-border-color: red;");
                }
                return;
            }
            heildarFjoldi += dosir + floskurCount;
            heildarVirdi += floskurCount * floskur.getVerdFloskur() + dosir * floskur.getVerdDosir();
            //uppfæri label fyrir heildarvirði
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));
            //Hreinsar inntaksreitina og núllstillir labels
            fxDosir.clear();
            fxFloskur.clear();
            fxDosirVirdi.setText("0");
            fxFloskurVirdi.setText("0");
            //Fjarlægir villustyle úr reitum ef þeir eru gildar tölur
            fxDosir.setStyle(null);
            fxFloskur.setStyle(null);

        } catch (NumberFormatException e) {
            if (!fxDosir.getText().matches("\\d+")) {
                fxDosir.setStyle("-fx-border-color: red;");
            }
            if (!fxFloskur.getText().matches("\\d+")) {
                fxFloskur.setStyle("-fx-border-color: red;");
            }
        }
    }

    /**
     * Aðferð sem virkir "Hreinsa" takkann.
     * Hreinsar alla inntaksreiti, núllstillir merkingar og fjarlægir villustyle.
     * @param event atburður sem virkist þegar smellt er á "Hreinsa" takkann.
     */
    @FXML
    protected void onHreinsa(ActionEvent event) {
        fxDosir.clear();
        fxFloskur.clear();
        fxDosirVirdi.setText("0");
        fxFloskurVirdi.setText("0");
        fxSamtalsVirdi.setText("0");

        fxDosir.setStyle(null);
        fxFloskur.setStyle(null);

        // 💥 Reset internal counters
        heildarFjoldi = 0;
        heildarVirdi = 0;
        floskur.hreinsa(); // also clears virdiFloskur & virdiDosir
    }


    /**
     * Aðferð sem virkir innslátt í reit fyrir fjölda dósa.
     * Reiknar út virði dósa og uppfærir label.
     * @param actionEvent atburður sem virkist þegar notandi ýtir á enter
     */
    public void onDosir(ActionEvent actionEvent) {
        try {
            int fjoldiDosir = Integer.parseInt(fxDosir.getText());

            if (fjoldiDosir < 0) {
                fxDosir.setStyle("-fx-border-color: red;");
                return;
            }
            fxDosir.setStyle(null);
            floskur.setFjoldiDosir(fjoldiDosir);
            fxDosirVirdi.setText(""+floskur.getISKDosir());

        } catch (NumberFormatException e) {
            if (!fxDosir.getText().matches("\\d+")) {
                fxDosir.setStyle("-fx-border-color: red;");
            }
        }
    }
    /**
     * Aðferð sem virkir innslátt í reit fyrir fjölda flaska.
     * Reiknar út virði flaskna og uppfærir label.
     * @param actionEvent atburður sem virkist þegar notandi ýtir á enter.
     */
    public void onFloskur(ActionEvent actionEvent) {
        try {
            int fjoldiFloskur = Integer.parseInt(fxFloskur.getText());
            if (fjoldiFloskur < 0) {
                fxFloskur.setStyle("-fx-border-color: red;");
                return;
            }
            fxFloskur.setStyle(null);
            floskur.setFjoldiFloskur(fjoldiFloskur);
            floskur.getISKFloskur();
            fxFloskurVirdi.setText(""+floskur.getISKFloskur());
        } catch (NumberFormatException e) {
            if (!fxFloskur.getText().matches("\\d+")) {
                fxFloskur.setStyle("-fx-border-color: red;");
            }
        }
    }
    @FXML
    protected void toggleDarkMode(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        // URLs to both stylesheets
        URL darkModeURL = getClass().getResource("/hi/verkefni/vidmot/css/darkmode.css");
        URL lightModeURL = getClass().getResource("/hi/verkefni/vidmot/css/floskur.css");

        if (darkModeURL == null || lightModeURL == null) {
            System.err.println("Stylesheet not found!");
            return;
        }

        // Clear all styles and load the selected one
        scene.getStylesheets().clear();

        if (fxDarkMode.isSelected()) {
            scene.getStylesheets().add(darkModeURL.toExternalForm());
        } else {
            scene.getStylesheets().add(lightModeURL.toExternalForm());
        }
    }



}