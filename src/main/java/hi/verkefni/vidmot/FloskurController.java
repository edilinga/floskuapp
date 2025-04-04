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
import javafx.scene.layout.VBox;


import javafx.scene.control.Button;

/**
 * Stýriklasi fyrir notendaviðmót flösku- og dósamóttöku forritsins.
 * Sér um samskipti við notanda, athugar inntak, uppfærir viðmót, heldur utan um stöðu og tengist vinnsluklasanum, Floskur.
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
    private ResourceBundle bundle;
    @FXML private Button fxUndo;
    @FXML private Button fxRedo;
    @FXML
    private TextField fxEmailField;
    @FXML
    private Label fxEmailConfirmation;
    @FXML
    private VBox fxEmailSection;
    @FXML
    private Button fxSendReceipt;
    @FXML
    private Button fxShowEmail;

    private final Stack<State> undoStack = new Stack<>();
    private final Stack<State> redoStack = new Stack<>();

    /**
     * Upphafsstillir viðmótið
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup languageGroup = new ToggleGroup();
        fxEngButton.setToggleGroup(languageGroup);
        fxIceButton.setToggleGroup(languageGroup);

        this.bundle = resources;
        loadLanguage("is");

        fxDosir.textProperty().addListener((observable, oldValue, newValue) -> {
            fxDosir.setStyle(null);
        });

        fxFloskur.textProperty().addListener((observable, oldValue, newValue) -> {
            fxFloskur.setStyle(null);
        });
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
        fxEmailField.setPromptText(currentBundle.getString("email.prompt"));
        fxSendReceipt.setText(currentBundle.getString("email.send"));
        fxShowEmail.setText(currentBundle.getString("email.button"));
        fxEmailConfirmation.setText(currentBundle.getString("email.invalid"));
        fxEmailConfirmation.setText("");
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


            undoStack.push(new State(heildarVirdi));
            redoStack.clear();


            heildarFjoldi += dosir + floskurCount;
            heildarVirdi += floskurCount * floskur.getVerdFloskur() + dosir * floskur.getVerdDosir();


            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));


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


        heildarFjoldi = 0;
        heildarVirdi = 0;
        floskur.hreinsa();
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

    public void undo() {
        if (!undoStack.isEmpty()) {
            State last = undoStack.pop();


            redoStack.push(new State(heildarVirdi));


            heildarVirdi = last.total;
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            State next = redoStack.pop();


            undoStack.push(new State(heildarVirdi));


            heildarVirdi = next.total;
            fxSamtalsVirdi.setText(String.valueOf(heildarVirdi));
        }
    }

    @FXML
    protected void onSendReceipt(ActionEvent event) {
        String email = fxEmailField.getText().trim();

        if (!isValidEmail(email)) {
            fxEmailConfirmation.setText(currentBundle.getString("email.invalid"));
            return;
        }

        sendFakeReceipt(email);

        fxEmailConfirmation.setText(currentBundle.getString("email.sent") + " " + email + " 📧");
        fxEmailField.clear();
    }


    @FXML
    protected void onShowEmailInput(ActionEvent event) {
        fxEmailSection.setVisible(true);
        fxEmailSection.setManaged(true);
    }
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void sendFakeReceipt(String email) {
        System.out.println("=== Kvittun send á " + email + " ===");
        System.out.println("Heildarfjöldi: " + heildarFjoldi);
        System.out.println("Heildarvirði: " + heildarVirdi + " kr.");
        System.out.println("===========================");
    }

}