package sample;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Tokimon;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    private static final int COLUMN = 5;
    private static final int ROW = 5;

    private final Image KIRBY = new Image("file:img/kirby.png");

    private List<Tokimon> tokimons = new ArrayList<>();

    private GridPane tokimonGrid;

    private int selectedPos = -1;
    private RestRequest restRequest = new RestRequest();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        tokimons.addAll(restRequest.readTokimonFromRemote());

        Label topMsg = new Label("Welcome to Tokimon Manager!");
        Button addTokimonBtn = new Button("Add New Tokimon");
        addTokimonBtn.setOnAction(actionEvent -> {
            Stage subStage = new Stage();
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner(primaryStage);

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
                    restRequest.addTokimonRequest(name, weight, height, type, strength, color);
                    if(!restRequest.getRequestResponseMsg().equals("")){
                        errorMsg.setText(restRequest.getRequestResponseMsg());
                    }
                    else {
                        updateTokimonGrid();
                    }
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
        });
        tokimonGrid = new GridPane();
        tokimonGrid.setHgap(10);
        tokimonGrid.setVgap(10);
        tokimonGrid.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tokimonGrid);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        initTokimonGrid();

        scrollPane.setMinWidth(800);
        scrollPane.setMaxHeight(800);
        scrollPane.setMaxHeight(500);
        scrollPane.setMinHeight(500);

        Button viewSelectedBtn = new Button("ViewSelected Tokimon");
        viewSelectedBtn.setOnAction(actionEvent -> {
            if(selectedPos == -1){
                System.out.println("No selected Tokimon");
            }
            else{
                Stage subStage = new Stage();
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Tokimon selectedTokimon = tokimons.get(selectedPos);
                subStage.setTitle("Details of " + selectedTokimon.getName());
                BorderPane borderPane = new BorderPane();

                Label nameMsg = new Label("Tokimon Name: " + selectedTokimon.getName());
                Label weightMsg = new Label("Tokimon Weight (kg): " + selectedTokimon.getWeight());
                Label heightMsg = new Label("Tokimon Height (m): " + selectedTokimon.getHeight());
                Label typeMsg = new Label("Tokimon Type: " + selectedTokimon.getType());
                Label strengthMsg = new Label("Tokimon Strength (0-100): " + selectedTokimon.getStrength());
                Label colorMsg = new Label("Tokimon Color: " + selectedTokimon.getColor());

                VBox vBox = new VBox(nameMsg, weightMsg, heightMsg, typeMsg, strengthMsg, colorMsg);
                vBox.setAlignment(Pos.CENTER);
                ImageView tokimonImage = new ImageView(KIRBY);
                tokimonImage.setFitWidth(100*selectedTokimon.getWeight()/100+20);
                tokimonImage.setFitHeight(100*selectedTokimon.getHeight()/100+20);
                VBox vBox1 = new VBox(tokimonImage);
                vBox1.setMinWidth(300);
                vBox1.setMinHeight(300);
                vBox1.setAlignment(Pos.CENTER);
                borderPane.setLeft(vBox);
                borderPane.setRight(vBox1);
                Scene scene = new Scene(borderPane);
                subStage.setScene(scene);
                subStage.show();
                selectedPos = -1;
            }
        });
        Button editSelectedBtn= new Button("Edit Selected Tokimon");
        editSelectedBtn.setOnAction(actionEvent -> {
            if (selectedPos == -1) {
                System.out.println("No selected Tokimon");
            }
            else {
                Stage subStage = new Stage();
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Tokimon selectedTokimon = tokimons.get(selectedPos);

                subStage.setTitle("Edit Tokimon");
                Label addMsg = new Label("Edit an existing Tokimon by completing all fields below:");
                Label nameMsg = new Label("Tokimon Name:");
                TextField nameInput = new TextField();
                nameInput.setText(selectedTokimon.getName());
                Label weightMsg = new Label("Tokimon Weight (kg):");
                Slider sliderWeight = new Slider(0.0, 100, 50);
                sliderWeight.setValue(selectedTokimon.getWeight());
                Label weightValue = new Label(String.valueOf(selectedTokimon.getWeight()));
                sliderWeight.setMajorTickUnit(10);
                sliderWeight.setMinorTickCount(9);
                sliderWeight.setShowTickMarks(true);
                sliderWeight.setShowTickLabels(true);
                sliderWeight.valueProperty().addListener((observableValue, number, newNumber) -> {
                    weightValue.setText(String.valueOf(Constant.round(newNumber.doubleValue(), 1)));
                });

                Label heightMsg = new Label("Tokimon Height (m):");
                Slider sliderHeight = new Slider(0.0, 100, 50);
                sliderHeight.setValue(selectedTokimon.getHeight());
                Label heightValue = new Label(String.valueOf(selectedTokimon.getHeight()));
                sliderHeight.setMajorTickUnit(10);
                sliderHeight.setMinorTickCount(9);
                sliderHeight.setShowTickMarks(true);
                sliderHeight.setShowTickLabels(true);
                sliderHeight.valueProperty().addListener((observableValue, number, newNumber) -> heightValue.setText(String.valueOf(Constant.round(newNumber.doubleValue(), 1))));

                Label typeMsg = new Label("Tokimon Type:");
                ComboBox<String> typeBox = new ComboBox<>();
                typeBox.getItems().addAll(Constant.types);
                typeBox.setValue(selectedTokimon.getType());
                Label strengthMsg = new Label("Tokimon Strength (0-100)");
                Slider sliderStrength = new Slider(0, 100, 50);
                sliderStrength.setValue(selectedTokimon.getStrength());
                Label strengthValue = new Label(String.valueOf((int)sliderStrength.getValue()));
                sliderStrength.setMajorTickUnit(10);
                sliderStrength.setMinorTickCount(9);
                sliderStrength.setShowTickMarks(true);
                sliderStrength.setShowTickLabels(true);
                sliderStrength.valueProperty().addListener((observableValue, number, newNumber) -> strengthValue.setText(String.valueOf(newNumber.intValue())));
                Label colorMsg = new Label("Tokimon Color");
                ComboBox<String> colorBox = new ComboBox<>();
                colorBox.getItems().addAll(Constant.COLORS);
                colorBox.setValue(selectedTokimon.getColor());
                Label errorMsg = new Label();

                Button updateBtn = new Button("Update");
                updateBtn.setOnAction(actionEvent1 -> {
                    String name = nameInput.getText();
                    double weight = sliderWeight.getValue();
                    double height = sliderHeight.getValue();
                    String type = typeBox.getValue();
                    int strength = (int) sliderStrength.getValue();
                    String color = colorBox.getValue();
                    restRequest.editTokimonRequest(selectedTokimon.getTokimonId(),name,weight,height,type,strength,color);
                    if(!restRequest.getRequestResponseMsg().equals("")){
                        errorMsg.setText(restRequest.getRequestResponseMsg());
                    }
                    updateTokimonGrid();
                    subStage.close();
                });
                Button cancelBtn = new Button("Cancel");
                cancelBtn.setOnAction(actionEvent1 -> subStage.close());
                HBox hBox = new HBox(10, updateBtn, cancelBtn);
                hBox.setAlignment(Pos.CENTER);

                VBox vBox = new VBox(addMsg, nameMsg, nameInput, weightMsg, sliderWeight, weightValue, heightMsg,sliderHeight, heightValue, typeMsg,typeBox, strengthMsg, sliderStrength, strengthValue, colorMsg, colorBox, errorMsg, hBox);
                vBox.setAlignment(Pos.CENTER);

                Scene subSence = new Scene(vBox, 1000, 500);
                subStage.setScene(subSence);
                subStage.show();
                selectedPos = -1;
            }
        });

        Button deleteSelectedBtn = new Button("Delete Selected Tokimon");
        deleteSelectedBtn.setOnAction(actionEvent -> {
            if(selectedPos == -1){
                System.out.println("No selected Tokimon");
            }
            else{
                Stage subStage = new Stage();
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Tokimon selectedTokimon = tokimons.get(selectedPos);
                Label deleteMsg = new Label("Are you sure you like to delete " + selectedTokimon.getName() + "?");
                Label errorMsg = new Label();
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(actionEvent1 -> {

                    restRequest.deleteTokimonRequest(selectedTokimon.getTokimonId());
                    if(!restRequest.getRequestResponseMsg().equals("")){
                        errorMsg.setText(restRequest.getRequestResponseMsg());
                    }
                    else {
                        updateTokimonGrid();
                    }
                    subStage.close();
                });
                Button cancelBtn = new Button("Cancel");
                cancelBtn.setOnAction(actionEvent1 -> {
                    subStage.close();
                });
                HBox hBox = new HBox(20, deleteBtn, cancelBtn);
                hBox.setAlignment(Pos.CENTER);
                VBox vBox = new VBox(deleteMsg, errorMsg, hBox);
                vBox.setAlignment(Pos.CENTER);
                Scene scene = new Scene(vBox);
                subStage.setScene(scene);
                subStage.show();
                selectedPos = -1;
            }
        });
        HBox hBox = new HBox(10, viewSelectedBtn, editSelectedBtn, deleteSelectedBtn);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(topMsg, addTokimonBtn, scrollPane, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        primaryStage.setTitle("Tokimon Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void initTokimonGrid() {
        int count = 0;

        if(tokimons == null){
            return;
        }

        while(count < tokimons.size()){
            int row = count/COLUMN;
            int col = count%COLUMN;
            ImageView tokimonImg = new ImageView(KIRBY);
            Button tokimonBtn = new Button();
            int finalCount = count;
            tokimonBtn.setOnAction(actionEvent -> {
                selectedPos = finalCount;
                System.out.println("Selected == " + selectedPos);
            });
            Tokimon curToki = tokimons.get(count);
            tokimonBtn.setGraphic(tokimonImg);
            tokimonImg.setFitWidth(100*curToki.getWeight()/100+20);
            tokimonImg.setFitHeight(100*curToki.getHeight()/100+20);
            Label tokimonName = new Label(curToki.getName());
            tokimonName.setStyle(Constant.pickColor(curToki.getColor()));

            VBox vBox = new VBox(10, tokimonName, tokimonBtn);
            vBox.setMaxHeight(150);
            vBox.setMinHeight(150);
            vBox.setMaxWidth(150);
            vBox.setMinWidth(150);
            vBox.setAlignment(Pos.CENTER);
            tokimonGrid.add(vBox, col, row);
            count++;
        }
    }

    public void updateTokimonGrid(){
        tokimonGrid.getChildren().clear();
        tokimons.clear();
        tokimons.addAll(restRequest.readTokimonFromRemote());
        initTokimonGrid();
        selectedPos = -1;
    }
}


