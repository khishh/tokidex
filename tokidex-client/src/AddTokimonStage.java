import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Constant;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddTokimonStage extends Stage{

    public AddTokimonStage() {
        init();
    }

    private void init() {
        Stage subStage = new Stage();
        subStage.initModality(Modality.WINDOW_MODAL);

        subStage.setTitle("Add Tokimon");
        Label addMsg = new Label("Add a new Tokimon by filling all fields below:");
        Label nameMsg = new Label("Tokimon Name:");
        TextField nameInput = new TextField();
        Label weightMsg = new Label("Tokimon Weight (kg):");
        Slider sliderWeight = new Slider(0.0, 100.0, 50.0);
        Label weightValue = new Label(String.valueOf(sliderWeight.getValue()));
        sliderWeight.setMajorTickUnit(10);
        sliderWeight.setMinorTickCount(9);
        sliderWeight.setShowTickMarks(true);
        sliderWeight.setShowTickLabels(true);
        sliderWeight.valueProperty().addListener((observableValue, number, newNumber) -> weightValue.setText(String.valueOf(Constant.round(newNumber.doubleValue(),1))));
        Label heightMsg = new Label("Tokimon Height (m):");
        Slider sliderHeight = new Slider(0.0, 100.0, 50.0);
        Label heightValue = new Label(String.valueOf(sliderHeight.getValue()));
        sliderHeight.setMajorTickUnit(10);
        sliderHeight.setMinorTickCount(9);
        sliderHeight.setShowTickMarks(true);
        sliderHeight.setShowTickLabels(true);
        sliderHeight.valueProperty().addListener((observableValue, number, newNumber) -> heightValue.setText(String.valueOf(Constant.round(newNumber.doubleValue(),1))));
        Label typeMsg = new Label("Tokimon Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(Constant.types);
        Label strengthMsg = new Label("Tokimon Strength (0-100)");
        Slider sliderStrength = new Slider(0, 100, 50);
        Label strengthValue = new Label(String.valueOf((int)sliderStrength.getValue()));
        sliderStrength.setMajorTickUnit(10);
        sliderStrength.setMinorTickCount(9);
        sliderStrength.setShowTickMarks(true);
        sliderStrength.setShowTickLabels(true);
        sliderStrength.valueProperty().addListener((observableValue, number, newNumber) -> strengthValue.setText(String.valueOf(newNumber.intValue())));
        Label colorMsg = new Label("Tokimon Color");
        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.getItems().addAll(Constant.COLORS);

        Label errorMsg = new Label();

        Button addBtn = new Button("Add");
        addBtn.setOnAction(actionEvent1 -> {
            String name = nameInput.getText();
            double weight = sliderWeight.getValue();
            double height = sliderHeight.getValue();
            String type = typeBox.getValue();
            int strength = (int) sliderStrength.getValue();
            String color = colorBox.getValue();
            if(Constant.isAllInputsValid(name, type, color)) {
                addTokimonRequest(name, weight, height, type, strength, color);
                //updateTokimonGrid();
                subStage.close();
            }
            else{
                errorMsg.setText("Please fill in all the fields above to add a new Tokimon!");
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(actionEvent1 -> subStage.close());
        HBox hBox = new HBox(10, addBtn, cancelBtn);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(addMsg, nameMsg, nameInput, weightMsg, sliderWeight, weightValue, heightMsg,sliderHeight, heightValue, typeMsg,typeBox, strengthMsg, sliderStrength, strengthValue, colorMsg, colorBox, errorMsg, hBox);
        vBox.setAlignment(Pos.CENTER);

        Scene subSence = new Scene(vBox, 1000, 500);
        subStage.setScene(subSence);
        subStage.show();
    }

    private void addTokimonRequest(String name, double weight, double height, String type, int strength, String color){
        try{
            URL url = new URL("http://localhost:8080/api/tokimon/add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            String query = String.format("name=%s&weight=%.1f&height=%.1f&type=%s&strength=%d&color=%s",
                    name, weight, height, type, strength, color);
            System.out.println(query);

            OutputStream os = connection.getOutputStream();
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));

            br.write(query);
            br.flush();
            br.close();
            os.close();

            System.out.println(connection.getResponseCode());
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
