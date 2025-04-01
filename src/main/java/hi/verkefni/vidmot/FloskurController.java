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
import java.util.Locale;
import java.util.Stack;


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
    private Label fxDosirLabel;

    @FXML
    private Label fxFloskurLabel;

    @FXML
    private Label fxSamtalsLabel;

    @FXML
    private Button fxGreida;

    @FXML
    private Button fxHreinsa;

    @FXML
    private CheckBox fxDarkMode;

    @FXML
    private ToggleButton fxEngButton;

    @FXML
    private ToggleButton fxIceButton;

    private ResourceBundle currentBundle;
    @FXML private Button fxUndo;
    @FXML private Button fxRedo;

    @FXML
    private TextField fxEmailField;

    @FXML
    private Label fxEmailConfirmation;

    private final Stack<State> undoStack = new Stack<>();
    private final Stack<State> redoStack = new Stack<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup languageGroup = new ToggleGroup();
        fxEngButton.setToggleGroup(languageGroup);
        fxIceButton.setToggleGroup(languageGroup);
        loadLanguage("is");
    }
    public void loadLanguage(String langCode) {
        currentBundle = ResourceBundle.getBundle("lang.lang", Locale.forLanguageTag(langCode));

        fxDosirLabel.setText(currentBundle.getString("label.dosir"));
        fxFloskurLabel.setText(currentBundle.getString("label.floskur"));
        fxSamtalsLabel.setText(currentBundle.getString("label.samtals"));
        fxGreida.setText(currentBundle.getString("button.greida"));
        fxHreinsa.setText(currentBundle.getString("button.hreinsa"));
        fxDarkMode.setText(currentBundle.getString("checkbox.darkmode"));
        fxDosir.setPromptText(currentBundle.getString("textfield.prompt"));
        fxFloskur.setPromptText(currentBundle.getString("textfield.prompt"));
        fxUndo.setText(currentBundle.getString("button.undo"));
        fxRedo.setText(currentBundle.getString("button.redo"));

    }
    public void switchToIcelandic() {
        loadLanguage("is");
    }

    public void switchToEnglish() {
        loadLanguage("en");
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

            // Check for negative values
            if (dosir < 0 || floskurCount < 0) {
                if (dosir < 0) {
                    fxDosir.setStyle("-fx-border-color: red;");
                }
                if (floskurCount < 0) {
                    fxFloskur.setStyle("-fx-border-color: red;");
                }
                return;
            }

            // ✅ Save current state before updating
            undoStack.push(new State(heildarVirdi));
            redoStack.clear();

            // Update total values
            heildarFjoldi += dosir + floskurCount;
            heildarVirdi += floskurCount * floskur.getVerdFloskur() + dosir * floskur.getVerdDosir();

            // Update label
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));

            // Clear fields + reset styles
            fxDosir.clear();
            fxFloskur.clear();
            fxDosirVirdi.setText("0");
            fxFloskurVirdi.setText("0");
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
    private static class State {
        int total;

        State(int total) {
            this.total = total;
        }
    }

    private int parseInput(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void undo() {
        if (!undoStack.isEmpty()) {
            State last = undoStack.pop();

            // Save current to redo
            redoStack.push(new State(heildarVirdi));

            // Restore previous value
            heildarVirdi = last.total;
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            State next = redoStack.pop();

            // Save current to undo
            undoStack.push(new State(heildarVirdi));

            // Restore value
            heildarVirdi = next.total;
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));
        }
    }

    @FXML
    protected void onSendReceipt(ActionEvent event) {
        String email = fxEmailField.getText().trim();

        if (email.isEmpty() || !email.contains("@")) {
            fxEmailConfirmation.setText("Vinsamlegast sláðu inn gilt netfang.");
            return;
        }

        // Simulate sending email (you can log it to console)
        System.out.println("Sending receipt to: " + email);
        System.out.println("==== KVITTUN ====");
        System.out.println("Heildarfjöldi: " + heildarFjoldi);
        System.out.println("Heildarvirði: " + heildarVirdi + " kr.");
        System.out.println("=================");

        fxEmailConfirmation.setText("Kvittun hefur verið send á " + email + " 📧");

        // Optional: clear field after sending
        fxEmailField.clear();
    }

}