package ru.PVV;

import java.io.IOException;
//import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import javafx.scene.input.DataFormat;
import ru.PVV.Enum.*;
//import java.util.concurrent.Callable;

//import com.aspose.cells.LoadOptions;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.aspose.cells.Workbook;
import com.aspose.cells.Cells;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


public class ControllerOfset {

    private class Controller_state{
        //<editor-fold desc="поля...">
        private double costOfMakingALayout; // стоимость работ по изготовлению макета
        private int edition; // количество отпечатанных листов
        private double cost; // стоимость отпечатанного листа
        private double totalCost; // полная стоимость
        private TypeOfPrices typeOfPrices; // Тип цены
        private FormOfPayment formOfPayment; // Форма оплаты
        private PaperFormat format; // формат бумаги (название и количество на листе А3)
        private TypePaper paper; // Бумага (название и цена А3 листа)
        private int numberOfUpperColourImprints; // количество цветных прокатов сверху на А3 лист
        private int numberOfUpperBlackImprints; // количество черно-белых прокатов сверху на А3 лист
        private int numberOfLowerColourImprints; // количество цветных прокатов снизу на А3 лист
        private int numberOfLowerBlackImprints; // количество черно-белых прокатов снизу на А3 лист
        private int numberOfPrintingPlates; // количество печатных пластин
        // картинки на кнопках
        private ImageInput imageInputUpperColour;
        private ImageInput imageInputUpperBlack;
        private ImageInput imageInputLowerColour;
        private ImageInput imageInputLowerBlack;
        // Tariffs:
        private double costOfAPlates; // Цена печатной пластины
        private double costOfColourImprints; // цена цветного проката
        private double costOfBlackImprints; // цена черно-белого проката
        //</editor-fold>

        private void loadPaperAndFormatsAndTariffs(String url) throws Exception {
            Workbook workbook = new Workbook(url+"\\data.xls");
            Cells cellsCostPaper = workbook.getWorksheets().get("Цены").getCells();
            ArrayList<TypePaper> papersList = new ArrayList<>(31);
            String valueFirstCellPaperName =cellsCostPaper.get(0,0).getValue().toString(),
                    valueCellPaperName=cellsCostPaper.get(1,0).getValue().toString();
            for(int i=1;
                i<=100 && !valueFirstCellPaperName.equals(valueCellPaperName);
                valueCellPaperName=cellsCostPaper.get(++i,0).getValue().toString())
            {
                papersList.add(new TypePaper(valueCellPaperName,cellsCostPaper.get(i,2).getDoubleValue()));
            }
            papersList.add(new TypePaper("Установить стоимость вручную",5.));
            comboBoxPaper.setItems(FXCollections.observableArrayList(papersList));
            comboBoxPaper.getSelectionModel().select(0);
            paper=comboBoxPaper.getSelectionModel().getSelectedItem();
            ArrayList<PaperFormat> papersFormats = new ArrayList<>(8);
            papersFormats.add(new PaperFormat("A3",1));
            papersFormats.add(new PaperFormat("A4",2));
            papersFormats.add(new PaperFormat("A5",4));
            papersFormats.add(new PaperFormat("Euro",6));
            papersFormats.add(new PaperFormat("A6",8));
            papersFormats.add(new PaperFormat("A7",16));
            papersFormats.add(new PaperFormat("A8",32));
            papersFormats.add(new PaperFormat("Установить количество вручную",1));
            comboBoxFormat.setItems(FXCollections.observableArrayList(papersFormats));
            comboBoxFormat.getSelectionModel().select(1);
            controller_state.format=comboBoxFormat.getSelectionModel().getSelectedItem();
            cellsCostPaper = workbook.getWorksheets().get("тарифы").getCells();
            controller_state.costOfAPlates=cellsCostPaper.get(0,0).getDoubleValue();
            controller_state.costOfColourImprints=cellsCostPaper.get(1,0).getDoubleValue();
            controller_state.costOfBlackImprints=cellsCostPaper.get(2,0).getDoubleValue();
            imageInputUpperColour = new ImageInput();
            imageInputUpperColour.setSource(new Image("file:\\"+url+"\\UpperColour.png"));
            imageInputUpperBlack = new ImageInput();
            imageInputUpperBlack.setSource(new Image("file:\\"+url+"\\UpperBlack.png"));
            imageInputLowerColour = new ImageInput();
            imageInputLowerColour.setSource(new Image("file:\\"+url+"\\LowerColour.png"));
            imageInputLowerBlack = new ImageInput();
            imageInputLowerBlack.setSource(new Image("file:\\"+url+"\\LowerBlack.png"));
        }
        private void chekPrintingPlate(){
            int sum = controller_state.numberOfLowerColourImprints
                    + controller_state.numberOfLowerBlackImprints
                    + controller_state.numberOfUpperColourImprints
                    + controller_state.numberOfUpperBlackImprints;
            if(controller_state.numberOfPrintingPlates==0) {
                if(sum!=0) {
                    controller_state.numberOfPrintingPlates = 1;
                    textFieldNumberOfPrintingPlates.setText("1");
                }
            }else
                if(controller_state.numberOfPrintingPlates>sum) {
                    controller_state.numberOfPrintingPlates = sum;
                    textFieldNumberOfPrintingPlates.setText(String.valueOf(sum));
                }
        }
        private void calculate(){
            int countA3 = edition / format.getCount();
            if((edition % format.getCount())>0)countA3++;
            //System.out.println(countA3);
            int sum = numberOfUpperColourImprints + numberOfUpperBlackImprints +
                    numberOfLowerColourImprints + numberOfLowerBlackImprints;
            double costOfWork=countA3;
            if(countA3<5000) {
                if(countA3<500) costOfWork = 500.;
                countA3 += 50;
                costOfWork+=50;
                if(sum>1) {
                    countA3 += 50;
                    costOfWork+=50;
                }
            }else if(countA3<10000){
                countA3+=75;
                if(sum>1) countA3 += 75;
                costOfWork=countA3;
            }else if(countA3<100000){
                countA3+=100;
                if(sum>1) countA3 += 100;
                costOfWork=countA3;
            }else if(countA3<200000){
                countA3+=150;
                if(sum>1) countA3 += 150;
                costOfWork=countA3;
            }else{
                countA3*=0.0025; // тут возможна несущественная потеря точности
                if(sum>1) countA3*=2;
                costOfWork=countA3;
            }
            //System.out.println(countA3);
            costOfWork *= ((numberOfLowerBlackImprints + numberOfUpperBlackImprints) * costOfBlackImprints +
                    (numberOfLowerColourImprints + numberOfUpperColourImprints) * costOfColourImprints);
            //System.out.println(costOfColourImprints);
            double coefficientRW = switch (typeOfPrices){
                case RETAIL -> 1.5;
                case WHOLESALE -> 1.3;
            };
            double coefficientCNV = switch (formOfPayment){
                case CASH -> 1.0;
                case NON_CASH -> 1.06;
                case NON_CASH_WITH_VAT -> 1.12;
            };
            //System.out.println(costOfWork);
            cost=(50. + costOfMakingALayout + coefficientRW * (costOfWork + numberOfPrintingPlates * costOfAPlates + countA3 * paper.getCost()))/edition;
            cost=Math.ceil(cost*100)/100;
            cost=Math.ceil(coefficientCNV*cost*100)/100;
            textFieldCost.setText(doubleToString(cost));
            totalCost=cost*edition;
            textFieldTotalCost.setText(doubleToString(totalCost));
        }
    }
    final private Controller_state controller_state = new Controller_state();
    //<editor-fold desc="привязки к полям формы ofset.fxml...">
    @FXML
    private TextField textFieldCostOfMakingALayout = new TextField();
    @FXML
    private TextField textFieldEdition = new TextField();
    @FXML
    private TextField textFieldCost = new TextField();
    @FXML
    private TextField textFieldTotalCost = new TextField();
    @FXML
    private ToggleGroup groupTypeOfPrices = new ToggleGroup();
    @FXML
    private RadioButton radioButtonTypeOfPricesRetail = new RadioButton();
    @FXML
    private ToggleGroup groupFormOfPayment = new ToggleGroup();
    @FXML
    private RadioButton radioButtonFormOfPaymentNon_cash_with_vat = new RadioButton();
    @FXML
    private ComboBox<PaperFormat> comboBoxFormat = new ComboBox<>();
    @FXML
    private Label labelNumberOnTheSheet = new Label();
    @FXML
    private TextField textFieldNumberOnTheSheet = new TextField();
    @FXML
    private ComboBox<TypePaper> comboBoxPaper = new ComboBox<>();
    @FXML
    private Label labelCostOfASheet = new Label();
    @FXML
    private TextField textFieldCostOfASheet = new TextField();
    @FXML
    private Button buttonUpperImprints = new Button();
    @FXML
    private Button buttonLowerImprints = new Button();
    @FXML
    private TextField textFieldUpperColourImprints = new TextField();
    @FXML
    private TextField textFieldUpperBlackImprints = new TextField();
    @FXML
    private TextField textFieldLowerColourImprints = new TextField();
    @FXML
    private TextField textFieldLowerBlackImprints = new TextField();
    @FXML
    private TextField textFieldNumberOfPrintingPlates = new TextField();
    //</editor-fold
    @FXML
    private void initialize () throws Exception {
        textFieldCostOfMakingALayout.setText("0,00");
        controller_state.costOfMakingALayout=0;
        textFieldEdition.setText("1000");
        controller_state.edition=1000;
        // Следим за снятием фокуса с поля стоимости изготовления макета
        textFieldCostOfMakingALayout.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)&(!nf))try {
                controller_state.costOfMakingALayout = Math.round(
                        Double.parseDouble(
                                textFieldCostOfMakingALayout.getText().replaceAll(",",".")
                        )*100.0
                )/100.0;
            }catch (NumberFormatException e){

            }finally {
                textFieldCostOfMakingALayout.setText(doubleToString(controller_state.costOfMakingALayout));
                controller_state.calculate();
            }
        });
        // Следим за снятием фокуса с поля величины тиража
        textFieldEdition.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)&(!nf))try {
                controller_state.edition = Math.abs(
                        Integer.parseInt(
                                textFieldEdition.getText()
                        ));
            }catch (NumberFormatException e){
            }finally {
                textFieldEdition.setText(Integer.toString(controller_state.edition));
                controller_state.totalCost = controller_state.cost*controller_state.edition;
                textFieldTotalCost.setText(doubleToString(controller_state.totalCost));
                controller_state.calculate();
            }
        });
        controller_state.cost=Double.parseDouble(textFieldCost.getText().replaceAll(",","."));
        textFieldCost.setText(doubleToString(controller_state.cost));
        controller_state.totalCost=controller_state.cost*controller_state.edition;
        textFieldTotalCost.setText(doubleToString(controller_state.totalCost));
        //radioButtonTypeOfPricesRetail.fire();
        controller_state.typeOfPrices=TypeOfPrices.RETAIL;
        //radioButtonFormOfPaymentNon_cash_with_vat.fire();
        controller_state.formOfPayment=FormOfPayment.NON_CASH_WITH_VAT;
        labelNumberOnTheSheet.setVisible(false);
        textFieldNumberOnTheSheet.setVisible(false);
        labelCostOfASheet.setVisible(false);
        textFieldCostOfASheet.setVisible(false);
        controller_state.loadPaperAndFormatsAndTariffs("C:\\Users\\yahus\\IdeaProjects\\TypographicCalculator\\src\\main\\resources\\ru\\pvv");
        controller_state.paper=comboBoxPaper.getSelectionModel().getSelectedItem();
        // Следим за изменением цены за лист A3
        comboBoxPaper.getSelectionModel().selectedItemProperty().addListener((x,ov,nv)->{
            if(ov!=nv){
                if("Установить стоимость вручную".equals(nv.getName())){
                    labelCostOfASheet.setVisible(true);
                    textFieldCostOfASheet.setVisible(true);
                    textFieldCostOfASheet.setText(doubleToString(nv.getCost()));
                }else{
                    if("Установить стоимость вручную".equals(ov.getName())){
                        labelCostOfASheet.setVisible(false);
                        textFieldCostOfASheet.setVisible(false);
                    }
                }
                controller_state.paper = nv;
                controller_state.calculate();
            }
        });
        // Следим за изменением ручной установки цены листа A3
        textFieldCostOfASheet.focusedProperty().addListener((x,ov,nv)->{
            if((ov!=nv)&&(!nv))try {
                controller_state.paper.setCost(
                        Math.round(
                                Double.parseDouble(
                                        textFieldCostOfASheet.getText().replaceAll(",",".")
                                )*100.0
                        )/100.0);
            }catch (NumberFormatException e){

            }finally {
                textFieldCostOfASheet.setText(doubleToString(controller_state.paper.getCost()));
                controller_state.calculate();
            }
        });
        controller_state.format = comboBoxFormat.getSelectionModel().getSelectedItem();
        // Следим за изменением формата бумаги
        comboBoxFormat.getSelectionModel().selectedItemProperty().addListener((x,ov,nv)->{
            if(ov!=nv){
                if("Установить количество вручную".equals(nv.getName())){
                    labelNumberOnTheSheet.setVisible(true);
                    textFieldNumberOnTheSheet.setVisible(true);
                    textFieldNumberOnTheSheet.setText(String.valueOf(nv.getCount()));
                }else{
                    if("Установить количество вручную".equals(ov.getName())){
                        labelNumberOnTheSheet.setVisible(false);
                        textFieldNumberOnTheSheet.setVisible(false);
                    }
                }
                controller_state.format = nv;
                controller_state.calculate();
            }
        });
        // Следим за изменением ручной установки количества на листе A3
        textFieldNumberOnTheSheet.focusedProperty().addListener((x,ov,nv)->{
            if((ov!=nv)&&(!nv))try {
                controller_state.format.setCount(
                        Math.abs(
                                Integer.parseInt(
                                        textFieldNumberOnTheSheet.getText()
                                )
                        )
                );
            }catch (NumberFormatException e) {

            } finally {
                textFieldNumberOnTheSheet.setText(Integer.toString(controller_state.format.getCount()));
                controller_state.calculate();
            }
        });
        buttonUpperImprints.setEffect(controller_state.imageInputUpperColour);
        buttonLowerImprints.setEffect(controller_state.imageInputLowerColour);
        controller_state.numberOfUpperColourImprints=4;
        textFieldUpperColourImprints.setText("4");
        // Следим за снятием фокуса с поля количества цветных прокатов сверху листа
        textFieldUpperColourImprints.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)) {
                if(!nf)try {
                    controller_state.numberOfUpperColourImprints = Math.abs(
                            Integer.parseInt(
                                    textFieldUpperColourImprints.getText()
                            ));
                } catch (NumberFormatException e) {
                } finally {
                    textFieldUpperColourImprints.setText(Integer.toString(controller_state.numberOfUpperColourImprints));
                    if(controller_state.numberOfUpperColourImprints>0){
                        if (controller_state.numberOfUpperBlackImprints != 0) {
                            controller_state.numberOfUpperBlackImprints = 0;
                            textFieldUpperBlackImprints.setText("0");
                        }
                        if (controller_state.numberOfLowerBlackImprints != 0) {
                            controller_state.numberOfLowerBlackImprints = 0;
                            textFieldLowerBlackImprints.setText("0");
                        }
                    }
                    controller_state.calculate();
                }
                buttonUpperImprints.setEffect(controller_state.imageInputUpperColour);
            }
        });
        controller_state.numberOfUpperBlackImprints=0;
        textFieldUpperBlackImprints.setText("0");
        // Следим за снятием фокуса с поля количества черно белых прокатов сверху листа
        textFieldUpperBlackImprints.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)) {
                if(!nf)try {
                    controller_state.numberOfUpperBlackImprints = Math.abs(
                            Integer.parseInt(
                                    textFieldUpperBlackImprints.getText()
                            ));
                } catch (NumberFormatException e) {
                } finally {
                    textFieldUpperBlackImprints.setText(Integer.toString(controller_state.numberOfUpperBlackImprints));
                    if(controller_state.numberOfUpperBlackImprints>0){
                        if (controller_state.numberOfUpperColourImprints != 0) {
                            controller_state.numberOfUpperColourImprints = 0;
                            textFieldUpperColourImprints.setText("0");
                        }
                        if (controller_state.numberOfLowerColourImprints != 0) {
                            controller_state.numberOfLowerColourImprints = 0;
                            textFieldLowerColourImprints.setText("0");
                        }
                    }
                    controller_state.calculate();
                }
                buttonUpperImprints.setEffect(controller_state.imageInputUpperBlack);
            }
        });
        controller_state.numberOfLowerColourImprints=4;
        textFieldLowerColourImprints.setText("4");
        // Следим за снятием фокуса с поля количества цветных прокатов снизу листа
        textFieldLowerColourImprints.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)) {
                if(!nf)try {
                    controller_state.numberOfLowerColourImprints = Math.abs(
                            Integer.parseInt(
                                    textFieldLowerColourImprints.getText()
                            ));
                } catch (NumberFormatException e) {
                } finally {
                    textFieldLowerColourImprints.setText(Integer.toString(controller_state.numberOfLowerColourImprints));
                    if(controller_state.numberOfLowerColourImprints>0){
                        if (controller_state.numberOfLowerBlackImprints != 0) {
                            controller_state.numberOfLowerBlackImprints = 0;
                            textFieldLowerBlackImprints.setText("0");
                        }
                        if (controller_state.numberOfUpperBlackImprints != 0) {
                            controller_state.numberOfUpperBlackImprints = 0;
                            textFieldUpperBlackImprints.setText("0");
                        }
                    }
                    controller_state.calculate();
                }
                buttonLowerImprints.setEffect(controller_state.imageInputLowerColour);
            }
        });
        controller_state.numberOfLowerBlackImprints=0;
        textFieldLowerBlackImprints.setText("0");
        // Следим за снятием фокуса с поля количества черно белых прокатов снизу листа
        textFieldLowerBlackImprints.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)) {
                if(!nf)try {
                    controller_state.numberOfLowerBlackImprints = Math.abs(
                            Integer.parseInt(
                                    textFieldLowerBlackImprints.getText()
                            ));
                } catch (NumberFormatException e) {
                } finally {
                    textFieldLowerBlackImprints.setText(Integer.toString(controller_state.numberOfLowerBlackImprints));
                    if(controller_state.numberOfLowerBlackImprints>0){
                        if (controller_state.numberOfLowerColourImprints != 0) {
                            controller_state.numberOfLowerColourImprints = 0;
                            textFieldLowerColourImprints.setText("0");
                        }
                        if (controller_state.numberOfUpperColourImprints != 0) {
                            controller_state.numberOfUpperColourImprints = 0;
                            textFieldUpperColourImprints.setText("0");
                        }
                    }
                    controller_state.calculate();
                }
                buttonLowerImprints.setEffect(controller_state.imageInputLowerBlack);
            }
        });
        controller_state.numberOfPrintingPlates=8;
        textFieldNumberOfPrintingPlates.setText("8");
        // Следим за снятием фокуса с поля количества печатных пластин
        textFieldNumberOfPrintingPlates.focusedProperty().addListener((o,of,nf)->{
            if((of!=nf)&(!nf))try {
                controller_state.numberOfPrintingPlates = Math.abs(
                        Integer.parseInt(
                                textFieldNumberOfPrintingPlates.getText()
                        ));
            }catch (NumberFormatException e){
            }finally {
                controller_state.chekPrintingPlate();
                textFieldNumberOfPrintingPlates.setText(Integer.toString(controller_state.numberOfPrintingPlates));
                controller_state.calculate();
            }
        });
        controller_state.calculate();

    }
    //<editor-fold desc="обработчики событий элементов формы ofset.fxml...">
    // Кнопка "Вернуться в главное меню"
    @FXML
    private void switchToPrimary() throws IOException {App.setRoot("primary");}
    // Поле стоимости изготовления макета
    @FXML
    private void textFieldCostOfMakingALayoutTextChange() throws NumberFormatException {
        try {
            controller_state.costOfMakingALayout = Math.round(
                    Double.parseDouble(
                            textFieldCostOfMakingALayout.getText().replaceAll(",",".")
                    )*100.0
            )/100.0;
        }catch (NumberFormatException e){
        }finally {
            textFieldCostOfMakingALayout.setText(doubleToString(controller_state.costOfMakingALayout));
            controller_state.calculate();
        }
    }
    // Кнопка "Калькуляция макета прокатами"
    @FXML
    private void calculateCostOfLayout(){
        controller_state.calculate();
    }
    // Поле тиража
    @FXML
    private void textFieldEditionTextChange() throws NumberFormatException {
        try {
            controller_state.edition = Math.abs(
                    Integer.parseInt(
                            textFieldEdition.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            textFieldEdition.setText(Integer.toString(controller_state.edition));
            controller_state.totalCost = controller_state.cost*controller_state.edition;
            textFieldTotalCost.setText(doubleToString(controller_state.totalCost));
            controller_state.calculate();
        }
    }
    // Поле цены
    @FXML
    private void setTextFieldCostToClipboard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(textFieldCost.getText());
        clipboard.setContent(content);
    }
    // Переключатели "Тип цен"
    @FXML
    private void radioButtonTypeOfPricesRetailSwitch(){
        controller_state.typeOfPrices = TypeOfPrices.RETAIL;
        controller_state.calculate();
    }
    @FXML
    private void radioButtonTypeOfPricesWholesaleSwitch(){
        controller_state.typeOfPrices = TypeOfPrices.WHOLESALE;
        controller_state.calculate();
    }
    // Переключатели "Форма оплаты"
    @FXML
    private void radioButtonFormOfPaymentCash(){
        controller_state.formOfPayment = FormOfPayment.CASH;
        controller_state.calculate();
    }
    @FXML
    private void radioButtonFormOfPaymentNon_Cash(){
        controller_state.formOfPayment = FormOfPayment.NON_CASH;
        controller_state.calculate();
    }
    @FXML
    private void radioButtonFormOfPaymentNon_cash_with_vat(){
        controller_state.formOfPayment = FormOfPayment.NON_CASH_WITH_VAT;
        controller_state.calculate();
    }
    // Поля ручного ввода цены бумаги и количества на листе
    @FXML
    private void textFieldCostOfASheetTextChange() throws NumberFormatException {
        try {
            controller_state.paper.setCost(
                    Math.round(
                            Double.parseDouble(
                                    textFieldCostOfASheet.getText().replaceAll(",",".")
                            )*100.0
                    )/100.0
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldCostOfASheet.setText(doubleToString(controller_state.paper.getCost()));
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldNumberOnTheSheetTextChange() throws NumberFormatException {
        try {
            controller_state.format.setCount(
                    Math.abs(
                            Integer.parseInt(
                                    textFieldNumberOnTheSheet.getText()
                            )
                    )
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldNumberOnTheSheet.setText(Integer.toString(controller_state.format.getCount()));
            controller_state.calculate();
        }
    }
    // Группа кнопок Прокатов
    @FXML
    private void textFieldUpperColourImprintsTextChange() throws NumberFormatException {
        try {
            controller_state.numberOfUpperColourImprints = Math.abs(
                    Integer.parseInt(
                            textFieldUpperColourImprints.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            textFieldUpperColourImprints.setText(Integer.toString(controller_state.numberOfUpperColourImprints));
            if(controller_state.numberOfUpperColourImprints>0){
                if (controller_state.numberOfUpperBlackImprints != 0) {
                    controller_state.numberOfUpperBlackImprints = 0;
                    textFieldUpperBlackImprints.setText("0");
                }
                if (controller_state.numberOfLowerBlackImprints != 0) {
                    controller_state.numberOfLowerBlackImprints = 0;
                    textFieldLowerBlackImprints.setText("0");
                }
                controller_state.chekPrintingPlate();
            }
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldUpperBlackImprintsTextChange() throws NumberFormatException {
        try {
            controller_state.numberOfUpperBlackImprints = Math.abs(
                    Integer.parseInt(
                            textFieldUpperBlackImprints.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            textFieldUpperBlackImprints.setText(Integer.toString(controller_state.numberOfUpperBlackImprints));
            if(controller_state.numberOfUpperBlackImprints>0){
                if (controller_state.numberOfUpperColourImprints != 0) {
                    controller_state.numberOfUpperColourImprints = 0;
                    textFieldUpperColourImprints.setText("0");
                }
                if (controller_state.numberOfLowerColourImprints != 0) {
                    controller_state.numberOfLowerColourImprints = 0;
                    textFieldLowerColourImprints.setText("0");
                }
                controller_state.chekPrintingPlate();
            }
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldLowerColourImprintsTextChange() throws NumberFormatException {
        try {
            controller_state.numberOfLowerColourImprints = Math.abs(
                    Integer.parseInt(
                            textFieldLowerColourImprints.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            textFieldLowerColourImprints.setText(Integer.toString(controller_state.numberOfLowerColourImprints));
            if(controller_state.numberOfLowerColourImprints>0){
                if (controller_state.numberOfLowerBlackImprints != 0) {
                    controller_state.numberOfLowerBlackImprints = 0;
                    textFieldLowerBlackImprints.setText("0");
                }
                if (controller_state.numberOfUpperBlackImprints != 0) {
                    controller_state.numberOfUpperBlackImprints = 0;
                    textFieldUpperBlackImprints.setText("0");
                }
                controller_state.chekPrintingPlate();
            }
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldLowerBlackImprintsTextChange() throws NumberFormatException {
        try {
            controller_state.numberOfLowerBlackImprints = Math.abs(
                    Integer.parseInt(
                            textFieldLowerBlackImprints.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            textFieldLowerBlackImprints.setText(Integer.toString(controller_state.numberOfLowerBlackImprints));
            if(controller_state.numberOfLowerBlackImprints>0){
                if (controller_state.numberOfLowerColourImprints != 0) {
                    controller_state.numberOfLowerColourImprints = 0;
                    textFieldLowerColourImprints.setText("0");
                }
                if (controller_state.numberOfUpperColourImprints != 0) {
                    controller_state.numberOfUpperColourImprints = 0;
                    textFieldUpperColourImprints.setText("0");
                }
            }
            controller_state.chekPrintingPlate();
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldNumberOfPrintingPlatesTextChange() throws NumberFormatException {
        try {
            controller_state.numberOfPrintingPlates = Math.abs(
                    Integer.parseInt(
                            textFieldNumberOfPrintingPlates.getText()
                    ));
        }catch (NumberFormatException e){
        }finally {
            controller_state.chekPrintingPlate();
            textFieldNumberOfPrintingPlates.setText(Integer.toString(controller_state.numberOfPrintingPlates));
            controller_state.calculate();
        }
    }
    @FXML
    private void clickUpperImprints(){
        String s = ((ImageInput)buttonUpperImprints.getEffect()).getSource().getUrl();
        switch (s.charAt(s.length() - 5)){
            case 'r'-> {
                if (controller_state.numberOfUpperColourImprints > 0)
                    textFieldUpperColourImprints.setText(String.valueOf(--controller_state.numberOfUpperColourImprints));
            }
            case 'k'->{
                if (controller_state.numberOfUpperBlackImprints > 0)
                    textFieldUpperBlackImprints.setText(String.valueOf(--controller_state.numberOfUpperBlackImprints));
            }
        }
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    @FXML
    private void clickLowerImprints(){
        String s = ((ImageInput)buttonLowerImprints.getEffect()).getSource().getUrl();
        switch (s.charAt(s.length() - 5)){
            case 'r'-> {
                if (controller_state.numberOfLowerColourImprints > 0)
                    textFieldLowerColourImprints.setText(String.valueOf(--controller_state.numberOfLowerColourImprints));
            }
            case 'k'->{
                if (controller_state.numberOfLowerBlackImprints > 0)
                    textFieldLowerBlackImprints.setText(String.valueOf(--controller_state.numberOfLowerBlackImprints));
            }
        }
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    @FXML
    private void clickUpperColour(){
        if(controller_state.numberOfUpperBlackImprints!=0){
            controller_state.numberOfUpperBlackImprints=0;
            textFieldUpperBlackImprints.setText("0");
        }
        if(controller_state.numberOfLowerBlackImprints!=0){
            controller_state.numberOfLowerBlackImprints=0;
            textFieldLowerBlackImprints.setText("0");
        }
        controller_state.numberOfUpperColourImprints=Integer.parseInt(textFieldUpperColourImprints.getText())+1;
        textFieldUpperColourImprints.setText(String.valueOf(controller_state.numberOfUpperColourImprints));
        buttonUpperImprints.setEffect(controller_state.imageInputUpperColour);
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    @FXML
    private void clickUpperBlack(){
        if(controller_state.numberOfUpperColourImprints!=0){
            controller_state.numberOfUpperColourImprints=0;
            textFieldUpperColourImprints.setText("0");
        }
        if(controller_state.numberOfLowerColourImprints!=0){
            controller_state.numberOfLowerColourImprints=0;
            textFieldLowerColourImprints.setText("0");
        }
        controller_state.numberOfUpperBlackImprints=Integer.parseInt(textFieldUpperBlackImprints.getText())+1;
        textFieldUpperBlackImprints.setText(String.valueOf(controller_state.numberOfUpperBlackImprints));
        buttonUpperImprints.setEffect(controller_state.imageInputUpperBlack);
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    @FXML
    private void clickLowerColour(){
        if(controller_state.numberOfLowerBlackImprints!=0){
            controller_state.numberOfLowerBlackImprints=0;
            textFieldLowerBlackImprints.setText("0");
        }
        if(controller_state.numberOfUpperBlackImprints!=0){
            controller_state.numberOfUpperBlackImprints=0;
            textFieldUpperBlackImprints.setText("0");
        }
        controller_state.numberOfLowerColourImprints=Integer.parseInt(textFieldLowerColourImprints.getText())+1;
        textFieldLowerColourImprints.setText(String.valueOf(controller_state.numberOfLowerColourImprints));
        buttonLowerImprints.setEffect(controller_state.imageInputLowerColour);
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    @FXML
    private void clickLowerBlack(){
        if(controller_state.numberOfLowerColourImprints!=0){
            controller_state.numberOfLowerColourImprints=0;
            textFieldLowerColourImprints.setText("0");
        }
        if(controller_state.numberOfUpperColourImprints!=0){
            controller_state.numberOfUpperColourImprints=0;
            textFieldUpperColourImprints.setText("0");
        }
        controller_state.numberOfLowerBlackImprints=Integer.parseInt(textFieldLowerBlackImprints.getText())+1;
        textFieldLowerBlackImprints.setText(String.valueOf(controller_state.numberOfLowerBlackImprints));
        buttonLowerImprints.setEffect(controller_state.imageInputLowerBlack);
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    // Группа кнопок Пластин
    @FXML
    private void clickDecreaseNumberOfPlates(){
        if(controller_state.numberOfPrintingPlates>0) {
            --controller_state.numberOfPrintingPlates;
            controller_state.chekPrintingPlate();
            textFieldNumberOfPrintingPlates.setText(String.valueOf(controller_state.numberOfPrintingPlates));
            controller_state.calculate();
        }
    }
    @FXML
    private void clickIncreaseNumberOfPlates(){
        textFieldNumberOfPrintingPlates.setText(String.valueOf(++controller_state.numberOfPrintingPlates));
        controller_state.chekPrintingPlate();
        controller_state.calculate();
    }
    //</editor-fold>
    @org.jetbrains.annotations.NotNull
    private String doubleToString(double c){
        long l = Math.round(Math.ceil(c*100));
        String s = Long.toString(l);
        if(s.length()<3){

            if(s.length()==1)if(s=="0") {
                return s;
            }else {
                return "0,0" + s;
            }
            if(s.length()==2)return "0,"+s;
        }
        return s.substring(0,s.length()-2)+","+s.substring(s.length()-2,s.length());
    }
}