package ru.PVV;

import java.io.IOException;
//import java.io.File;
import java.util.ArrayList;

import javafx.scene.input.DataFormat;
import ru.PVV.Enum.*;


//import java.util.concurrent.Callable;

//import com.aspose.cells.LoadOptions;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.aspose.cells.Workbook;
import com.aspose.cells.Cells;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


public class ControllerRiso {

    private class Controller_state{
        //<editor-fold desc="поля...">
        // Макет
        private double costOfMakingALayout; // стоимость работ по изготовлению макета
        // Тираж
        private int edition; // количество отпечатанных листов
        private double cost; // стоимость отпечатанного листа
        private double totalCost; // полная стоимость
        //Тип цен
        private TypeOfPrices typeOfPrices; // Тип цены
        // Вид оплаты
        private FormOfPayment formOfPayment; // Форма оплаты
        // Способ печати
        private TypeOfPrint typeOfPrint; // способ печати
        // Цветность
        private char chromaticity; // цветность (программно это просто номер кнопки на форме)
        private double costOfFirstColourImprints110; // Цена первого цветного проката тираж 1-10
        private double costOfFirstColourImprints1050; // Цена первого цветного проката тираж 10-50
        private double costOfFirstColourImprintsMost50; // Цена первого цветного проката тираж > 50

        private double costOfColourImprints; // цена цветного проката
        private int numberOfUpperColourImprints; // количество цветных прокатов сверху на А3 лист
        private int numberOfLowerColourImprints; // количество цветных прокатов снизу на А3 лист
        private double costOfFirstBlackImprints; // Цена первого черно-белого проката
        private double costOfBlackImprints; // цена черно-белого проката
        // Черно-белые прокаты относятся и к печати ризографом и к печати принтером
        private int numberOfUpperBlackImprints; // количество черно-белых прокатов сверху на А3 лист
        private int numberOfLowerBlackImprints; // количество черно-белых прокатов снизу на А3 лист
        // ризограф
        private double costOfFirstOneSidedRisoImprints; // цена первого проката ризографа при односторонней печати
        private double costOfFirstDuplexRisoImprints; // цена первого проката ризографа при двусторонней печати
        private double costOfRisoImprints; // цена проката ризографа
        // Формат
        private int format; // формат (количество на листе А3)
        // Бумага
        private TypePaper paper; // Бумага (название и цена А3 листа)
        // Нумерация
        private boolean needNumeration;
        private int countNumbersOnSheet;
        private double costOfNumber;
        // Листоподборка
        private boolean needCollating;
        private int numberInSet;
        private double costCollatingOneSheet;
        // Подложки
        private boolean needLayer;
        private TypePaper upperLayer;
        private TypePaper lowerLayer;
        // Проклейка
        private boolean needGluing;
        private double costGluingOneSet;
        // Скобы
        private boolean needBrackets;
        private double costOneBracket;
        private int countBracketsOnSet;
        // Пружина
        private boolean needSpring;
        private boolean needShortSpring;
        private double costOfBindingWithA6MetalSpringOnEitherSide;
        private double costOfBindingWithA5MetalSpringOnTheLongSide;
        private double costOfBindingWithA4MetalSpringOnTheLongSide;
        // Листов А3
        private int countA3;
        // Блоков
        private int countSets;
        //</editor-fold>
        private double loadCostOfNumber(String url) throws Exception {
            return (new Workbook(url+"\\data.xls")).getWorksheets().get("тарифы").getCells().get(30,0).getDoubleValue();
        }
        private void loadPaperAndFormatsAndTariffs(String url) throws Exception {
            // Подключение к хранилищу
            Workbook workbook = new Workbook(url+"\\data.xls");

            Cells cellsCostPaper = workbook.getWorksheets().get("Цены").getCells();
            ArrayList<TypePaper> basePapersList = new ArrayList<>(31),
                    printPapersList = new ArrayList<>(31),
                    upperLayerPapersList = new ArrayList<>(32),
                    lowerLayerPapersList = new ArrayList<>(32);
            String valueFirstCellPaperName =cellsCostPaper.get(0,0).getValue().toString(),
                    valueCellPaperName=cellsCostPaper.get(1,0).getValue().toString();
            for(int i=1;
                i<=100 && !valueFirstCellPaperName.equals(valueCellPaperName);
                valueCellPaperName=cellsCostPaper.get(++i,0).getValue().toString())
            {
                basePapersList.add(new TypePaper(valueCellPaperName,cellsCostPaper.get(i,2).getDoubleValue()));
            }
            printPapersList.addAll(basePapersList);
            printPapersList.add(new TypePaper("Произвольная стоимость",5.));
            comboBoxPaper.setItems(FXCollections.observableArrayList(printPapersList));
            cellsCostPaper = workbook.getWorksheets().get("тарифы").getCells();
            // Цветность
            costOfFirstColourImprints110 = cellsCostPaper.get(52,0).getDoubleValue(); // Цена первого цветного проката тираж 1-10
            costOfFirstColourImprints1050 = cellsCostPaper.get(53,0).getDoubleValue(); // Цена первого цветного проката тираж 10-50
            costOfFirstColourImprintsMost50 = cellsCostPaper.get(54,0).getDoubleValue(); // Цена первого цветного проката тираж > 50
            //costOfColourImprints = cellsCostPaper.get(55,0).getDoubleValue(); // цена цветного проката
            costOfFirstBlackImprints = cellsCostPaper.get(11,0).getDoubleValue(); // Цена первого черно-белого проката
            costOfBlackImprints = cellsCostPaper.get(56,0).getDoubleValue(); // цена черно-белого проката
            // ризограф
            costOfFirstOneSidedRisoImprints = cellsCostPaper.get(3,0).getDoubleValue(); // цена первого проката ризографа при односторонней печати
            costOfFirstDuplexRisoImprints = cellsCostPaper.get(4,0).getDoubleValue(); // цена первого проката ризографа при двусторонней печати
            costOfRisoImprints = cellsCostPaper.get(5,0).getDoubleValue(); // цена проката ризографа
            // Формат

            // Нумерация
            costOfNumber = cellsCostPaper.get(30,0).getDoubleValue();
            // Листоподборка
            costCollatingOneSheet = cellsCostPaper.get(49,0).getDoubleValue();
            // Подложки
            upperLayerPapersList.add(new TypePaper("Без верхней подложки",0.0));
            upperLayerPapersList.addAll(basePapersList);
            upperLayerPapersList.add(new TypePaper("Произвольная стоимость",5.));
            comboBoxUpperLayer.setItems(FXCollections.observableArrayList(upperLayerPapersList));
            lowerLayerPapersList.add(new TypePaper("Без нижней подложки",0.0));
            lowerLayerPapersList.addAll(basePapersList);
            lowerLayerPapersList.add(new TypePaper("Произвольная стоимость",5.));
            comboBoxLowerLayer.setItems(FXCollections.observableArrayList(lowerLayerPapersList));
            // Проклейка
            costGluingOneSet = cellsCostPaper.get(33,0).getDoubleValue();
            // Скобы
            costOneBracket = cellsCostPaper.get(34,0).getDoubleValue();
            // Пружина
            costOfBindingWithA6MetalSpringOnEitherSide = cellsCostPaper.get(40,0).getDoubleValue();
            costOfBindingWithA5MetalSpringOnTheLongSide = cellsCostPaper.get(41,0).getDoubleValue();
            costOfBindingWithA4MetalSpringOnTheLongSide = cellsCostPaper.get(42,0).getDoubleValue();
        }
        private void activateListeners(){
        // Макет
            // Следим за снятием фокуса с поля стоимости изготовления макета
            textFieldCostOfMakingALayout.focusedProperty().addListener((o,of,nf)->{
                if((of!=nf)&(!nf))try {
                    costOfMakingALayout = Math.round(
                            Double.parseDouble(
                                    textFieldCostOfMakingALayout.getText().replaceAll(",",".")
                            )*100.0
                    )/100.0;
                }catch (NumberFormatException e){

                }finally {
                    textFieldCostOfMakingALayout.setText(doubleToString(costOfMakingALayout));
                    calculate();
                }
            });
        // Тираж
            // Следим за снятием фокуса с поля величины тиража
            textFieldEdition.focusedProperty().addListener((o,of,nf)->{
                if((of!=nf)&(!nf))try {
                    edition = Math.abs(
                            Integer.parseInt(
                                    textFieldEdition.getText()
                            ));
                }catch (NumberFormatException e){
                }finally {
                    if(edition == 0)edition = 1;
                    textFieldEdition.setText(Integer.toString(edition));
                    if(numberInSet >= edition) {
                        numberInSet = edition;
                        needCollating = false;
                        toggleButtonCollatingTrue.setSelected(false);
                        toggleButtonCollatingFalse.setSelected(true);
                        labelNumberInSet.setVisible(false);
                        textFieldNumberInSet.setVisible(false);
                    }
                    textFieldNumberInSet.setText(Integer.toString(numberInSet));
                    calculate();
                }
            });
        // Формат
            // Следим за изменением ручной установки количества на листе A3
            textFieldNumberOnTheSheet.focusedProperty().addListener((x,ov,nv)->{
                if((ov!=nv)&&(!nv))try {
                    format = Math.abs(
                            Integer.parseInt(
                                    textFieldNumberOnTheSheet.getText()
                            )
                    );
                }catch (NumberFormatException e) {

                } finally {
                    textFieldNumberOnTheSheet.setText(Integer.toString(format));
                    calculate();
                }
            });
        // Бумага
            // Следим за появлением бумаги "Произвольная стоимость"
            comboBoxPaper.getSelectionModel().selectedItemProperty().addListener((x,ov,nv)->{
                if(ov!=nv){
                    if("Произвольная стоимость".equals(nv.getName())){
                        labelCostOfASheet.setVisible(true);
                        textFieldCostOfASheet.setVisible(true);
                        textFieldCostOfASheet.setText(doubleToString(nv.getCost()));
                    }else{
                        if("Произвольная стоимость".equals(ov.getName())){
                            labelCostOfASheet.setVisible(false);
                            textFieldCostOfASheet.setVisible(false);
                        }
                    }
                    paper = nv;
                    calculate();
                }
            });
            // Следим за изменением ручной установки цены листа A3
            textFieldCostOfASheet.focusedProperty().addListener((x,ov,nv)->{
                if((ov!=nv)&&(!nv))try {
                    paper.setCost(
                            Math.round(
                                    Double.parseDouble(
                                            textFieldCostOfASheet.getText().replaceAll(",",".")
                                    )*100.0
                            )/100.0);
                }catch (NumberFormatException e){

                }finally {
                    textFieldCostOfASheet.setText(doubleToString(paper.getCost()));
                    calculate();
                }
            });
        // Нумерация
            // Следим за снятием фокуса с поля количества номеров на лист
            textFieldCountNumbersOnSheet.focusedProperty().addListener((o,of,nf)->{
                if((of!=nf)&(!nf))try {
                    countNumbersOnSheet = Math.abs(
                            Integer.parseInt(
                                    textFieldCountNumbersOnSheet.getText()
                            ));
                }catch (NumberFormatException e){
                }finally {
                    textFieldCountNumbersOnSheet.setText(Integer.toString(countNumbersOnSheet));
                    if(countNumbersOnSheet == 0) buttonNumeration.fire();
                    else calculate();
                }
            });
            // Следим за изменением цены за номер
            textFieldCostOfNumber.focusedProperty().addListener((x,ov,nv)->{
                if((ov!=nv)&&(!nv))try {
                    costOfNumber = Math.round(
                            Double.parseDouble(
                                    textFieldCostOfNumber.getText().replaceAll(",",".")
                            )*100.0
                    )/100.0;
                }catch (NumberFormatException e){

                }finally {
                    if(costOfNumber == 0) {
                        controller_state.needNumeration = false;
                        labelCountNumbersOnSheet.setVisible(false);
                        textFieldCountNumbersOnSheet.setText("0");
                        textFieldCountNumbersOnSheet.setVisible(false);
                        countNumbersOnSheet = 0;
                        labelCostOfNumber.setVisible(false);
                        textFieldCostOfNumber.setVisible(false);
                        buttonNumeration.setText("Без нумерации");
                        try {
                            costOfNumber = loadCostOfNumber("C:\\Users\\Husim\\IdeaProjects\\TypographicCalculator\\src\\main\\resources\\ru\\pvv");
                        } catch (Exception e) {
                            e.printStackTrace();
                            costOfNumber = 0.5;
                        }
                    }
                    textFieldCostOfNumber.setText(doubleToString(controller_state.costOfNumber));
                    calculate();
                }
            });
        // Листоподборка
            // Следим за снятием фокуса с "Количество в комплекте"
            textFieldNumberInSet.focusedProperty().addListener((o,of,nf)->{
                if((of!=nf)&(!nf))try {
                    numberInSet = Math.abs(
                            Integer.parseInt(
                                    textFieldNumberInSet.getText()
                            ));
                }catch (NumberFormatException e){
                }finally {
                    if(numberInSet == 0)numberInSet = 1;
                    if(numberInSet >= edition) {
                        numberInSet = edition;
                        needCollating = false;
                        toggleButtonCollatingTrue.setSelected(false);
                        toggleButtonCollatingFalse.setSelected(true);
                        labelNumberInSet.setVisible(false);
                        textFieldNumberInSet.setVisible(false);
                    }
                    textFieldNumberInSet.setText(Integer.toString(numberInSet));
                    controller_state.calculate();
                }
            });
        // Подложки
            // Следим за изменением верхней подложки
            comboBoxUpperLayer.getSelectionModel().selectedItemProperty().addListener((x,ov,nv)->{
                if(ov!=nv){
                    if("Произвольная стоимость".equals(nv.getName())){
                        labelCostOfUpperLayer.setVisible(true);
                        textFieldCostOfUpperLayer.setVisible(true);
                        textFieldCostOfUpperLayer.setText(doubleToString(nv.getCost()));
                    }else{
                        if("Произвольная стоимость".equals(ov.getName())){
                            labelCostOfUpperLayer.setVisible(false);
                            textFieldCostOfUpperLayer.setVisible(false);
                        }
                    }
                    upperLayer = nv; // lowerLayer
                    calculate();
                }
            });
            textFieldCostOfUpperLayer.focusedProperty().addListener((x,ov,nv)->{
                if((ov!=nv)&&(!nv))try {
                    upperLayer.setCost(
                            Math.round(
                                    Double.parseDouble(
                                            textFieldCostOfUpperLayer.getText().replaceAll(",",".")
                                    )*100.0
                            )/100.0);
                }catch (NumberFormatException e){

                }finally {
                    textFieldCostOfUpperLayer.setText(doubleToString(upperLayer.getCost()));
                    calculate();
                }
            });
            // Следим за изменением цены за нижней подложи
            comboBoxLowerLayer.getSelectionModel().selectedItemProperty().addListener((x,ov,nv)->{
                if(ov!=nv){
                    if("Произвольная стоимость".equals(nv.getName())){
                        labelCostOfLowerLayer.setVisible(true);
                        textFieldCostOfLowerLayer.setVisible(true);
                        textFieldCostOfLowerLayer.setText(doubleToString(nv.getCost()));
                    }else{
                        if("Произвольная стоимость".equals(ov.getName())){
                            labelCostOfLowerLayer.setVisible(false);
                            textFieldCostOfLowerLayer.setVisible(false);
                        }
                    }
                    lowerLayer = nv; // lowerLayer
                    calculate();
                }
            });
            textFieldCostOfLowerLayer.focusedProperty().addListener((x,ov,nv)->{
                if((ov!=nv)&&(!nv))try {
                    lowerLayer.setCost(
                            Math.round(
                                    Double.parseDouble(
                                            textFieldCostOfLowerLayer.getText().replaceAll(",",".")
                                    )*100.0
                            )/100.0);
                }catch (NumberFormatException e){

                }finally {
                    textFieldCostOfLowerLayer.setText(doubleToString(lowerLayer.getCost()));
                    calculate();
                }
            });
        // Проклейка

        // Скобы
            // Следим за снятием фокуса с "Скоб на комплект"
            textFieldNumberOfBracket.focusedProperty().addListener((o,of,nf)->{
                if((of!=nf)&(!nf))try {
                    countBracketsOnSet = Math.abs(
                            Integer.parseInt(
                                    textFieldNumberOfBracket.getText()
                            ));
                }catch (NumberFormatException e){
                }finally {
                    if(countBracketsOnSet == 0) {
                        toggleButtonBracketFalse.fire();
                    }
                    else{
                        textFieldNumberOfBracket.setText(Integer.toString(countBracketsOnSet));
                        calculate();
                    }
                }
            });
        }
        private double getCostSpring(){
            if(!needSpring)return 0.0;
            if(format>=8)return costOfBindingWithA6MetalSpringOnEitherSide;
            if(format>=4) {
                if (needShortSpring) return costOfBindingWithA6MetalSpringOnEitherSide;
                else return costOfBindingWithA5MetalSpringOnTheLongSide;
            }
            if(format>=2 && needShortSpring)return costOfBindingWithA5MetalSpringOnTheLongSide;
            return costOfBindingWithA4MetalSpringOnTheLongSide;
        }
        private void calculate(){
            countA3 = edition / format;
            if((edition % format)>0)countA3++;
            textFieldCountA3.setText(Integer.toString(countA3));
            if(!needCollating) {
                numberInSet = edition;
                textFieldNumberInSet.setText(Integer.toString(numberInSet));
            }
            countSets = edition / numberInSet;
            if((edition % numberInSet)>0)countSets++;
            textFieldCountSets.setText(Integer.toString(countSets));
            double t1,
                    t2,
                    t3,
                    print, // C61
                    gluing, // D61
                    brackets, // E61
                    numbering, // F61
                    springs, // G61
                    collating, // H61
                    layer; // I61
            // =A64+A66*C13+paper.getCost()*C13
            // =countA3*A64/ВПР(countA3;тираж_ризограф;1;1)+A66*countA3+paper.getCost()*countA3
            if(countA3<10){
                t1 = costOfFirstColourImprints110; // Цена первого цветного проката тираж 1-10
            }else{
                if(countA3<50){
                    t1 = costOfFirstColourImprints1050; // Цена первого цветного проката тираж 10-50
                }else{
                    t1 = costOfFirstColourImprintsMost50;
                }
            }
            if(countA3<25)t2=1;
            else if(countA3<50)t2=25;
            else if(countA3<75)t2=50;
            else if(countA3<100)t2=75;
            else if(countA3<125)t2=100;
            else if(countA3<150)t2=125;
            else if(countA3<200)t2=150;
            else if(countA3<250)t2=200;
            else if(countA3<300)t2=250;
            else if(countA3<350)t2=300;
            else if(countA3<400)t2=350;
            else if(countA3<450)t2=400;
            else if(countA3<500)t2=450;
            else if(countA3<750)t2=500;
            else if(countA3<1000)t2=750;
            else if(countA3<1250)t2=1000;
            else if(countA3<1500)t2=1250;
            else if(countA3<2000)t2=1500;
            else if(countA3<2500)t2=2000;
            else t2=2500;
            t3=paper.getCost();
            if(countA3<50) t3=Math.ceil(3.03*t3)/2.0; // 0,5*ОКРУГЛВВЕРХ(1,01*1,5*C2/0,5;0)
            else t3=Math.ceil(153.0*t3)/100; // =ОКРУГЛВВЕРХ(C2*1,5*(1+$J$1/100);1) // 100*1.5*1.02 = 153
            print=countA3*t3;
            print+=switch (typeOfPrint){
                case KONICA->switch (chromaticity){
                        case 1->0.5 * costOfFirstBlackImprints + costOfBlackImprints * countA3;
                        case 2->costOfFirstBlackImprints + 2 * costOfBlackImprints * countA3;
                        case 3->costOfFirstBlackImprints + countA3 * t1;
                        case 4->2.0 * costOfFirstBlackImprints + 2.0 * countA3 * t1;
                        default->1.5 * costOfFirstBlackImprints + countA3 * (t1 + costOfBlackImprints);
                    }; // =A64+A66*C13+A60*C13
                default->switch (chromaticity){
                        case 1->countA3 * (costOfFirstOneSidedRisoImprints / t2 + costOfRisoImprints);
                        case 2->countA3 * (costOfFirstDuplexRisoImprints / t2 + 2.0 * costOfRisoImprints);
                        case 3->countA3 * ((costOfFirstOneSidedRisoImprints + costOfFirstDuplexRisoImprints) / t2 + 3.0 * costOfRisoImprints);
                        default->countA3 *( 2 * costOfFirstDuplexRisoImprints / t2 + 4.0 * costOfRisoImprints);
                    }; // =C13*A64/ВПР(C13;тираж_ризограф;1;1)+A66*C13+A60*C13
            };
            if (needNumeration){
                numbering = costOfNumber * countNumbersOnSheet * edition;
            }else numbering=0.0;
            if (needCollating){
                collating = countA3 * costCollatingOneSheet;
            }else collating = 0.0;
            if (needLayer){
                layer = Math.ceil(100.0*(lowerLayer.getCost() + upperLayer.getCost()) / format) * countSets / 100.0;
            }else layer=0.0;
            if (needGluing){
                gluing = 1.18 * costGluingOneSet * countSets;
            }else gluing=0.0;
            if (needBrackets){
                brackets = 1.18 * costOneBracket * countBracketsOnSet * countSets;
            }else brackets=0.0;
            if (needSpring){
                springs = getCostSpring() * countSets;
            }else springs=0.0;
            /*System.out.println("Макет " + costOfMakingALayout + "цена листа " + paper.getCost() + "первый" + 0.5 * costOfFirstBlackImprints + "оттиск" + costOfBlackImprints);
            System.out.println("печать " + print);
            System.out.println("проклейка " + gluing);
            System.out.println("скобы " + brackets);
            System.out.println("нумерация " + numbering);
            System.out.println("пружины " + springs);
            System.out.println("листоподбор " + collating);
            System.out.println("подложки " + layer);
            System.out.println(doubleToString(0.0));*/
            cost =1.2*(costOfMakingALayout+print+gluing+brackets+numbering+springs+collating+layer)/edition;
            // опт 0,8 розница 1,0
            cost = switch (typeOfPrices){
                case RETAIL->Math.ceil(100*cost)/100;
                case WHOLESALE->Math.ceil(0.8*100*cost)/100;
            };
            cost = switch (formOfPayment){
                case CASH -> cost;
                case NON_CASH->Math.ceil(100.0*cost*1.06)/100.0;
                case NON_CASH_WITH_VAT->Math.ceil(cost*1.12/0.06)*0.06;
            };
            textFieldCost.setText(doubleToString(cost));
            totalCost=cost*edition;
            textFieldTotalCost.setText(doubleToString(totalCost));
        }
    }
    final private Controller_state controller_state = new Controller_state();
    //<editor-fold desc="привязки к элементам формы ofset.fxml...">
    @FXML
    private TextField textFieldCostOfMakingALayout = new TextField();
    @FXML
    private TextField textFieldEdition = new TextField();
    @FXML
    private TextField textFieldCost = new TextField();
    @FXML
    private TextField textFieldTotalCost = new TextField();
    @FXML
    private RadioButton radioButton1 = new RadioButton();
    @FXML
    private RadioButton radioButton2 = new RadioButton();
    @FXML
    private RadioButton radioButton3 = new RadioButton();
    @FXML
    private RadioButton radioButton4 = new RadioButton();
    @FXML
    private RadioButton radioButton5 = new RadioButton();
    @FXML
    private ToggleButton toggleButtonA3 = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonA4 = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonA5 = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonEuro = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonA6 = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonA7 = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonHandleNumberOnTheSheet = new ToggleButton();
    @FXML
    private TextField textFieldNumberOnTheSheet = new TextField();
    @FXML
    private ComboBox<TypePaper> comboBoxPaper = new ComboBox<>();
    @FXML
    private Label labelCostOfASheet = new Label();
    @FXML
    private TextField textFieldCostOfASheet = new TextField();
    @FXML
    private Label labelCountNumbersOnSheet = new Label();
    @FXML
    private TextField textFieldCountNumbersOnSheet = new TextField();
    @FXML
    private Button buttonNumeration = new Button();
    @FXML
    private Label labelCostOfNumber = new Label();
    @FXML
    private TextField textFieldCostOfNumber = new TextField();
    @FXML
    private ToggleButton toggleButtonCollatingTrue = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonCollatingFalse = new ToggleButton();
    @FXML
    private Label labelNumberInSet = new Label();
    @FXML
    private TextField textFieldNumberInSet = new TextField();
    @FXML
    private ComboBox<TypePaper> comboBoxUpperLayer = new ComboBox<>();
    @FXML
    private Label labelCostOfUpperLayer = new Label();
    @FXML
    private TextField textFieldCostOfUpperLayer = new TextField();
    @FXML
    private ComboBox<TypePaper> comboBoxLowerLayer = new ComboBox<>();
    @FXML
    private Label labelCostOfLowerLayer = new Label();
    @FXML
    private TextField textFieldCostOfLowerLayer = new TextField();
    @FXML
    private ToggleButton toggleButtonBracketTrue = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonBracketFalse = new ToggleButton();
    @FXML
    private Label labelNumberOfBracket = new Label();
    @FXML
    private TextField textFieldNumberOfBracket = new TextField();
    @FXML
    private ToggleButton toggleButtonWithoutSprings = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonShortSpring = new ToggleButton();
    @FXML
    private ToggleButton toggleButtonLongSpring = new ToggleButton();
    @FXML
    private TextField textFieldCountA3 = new TextField();
    @FXML
    private TextField textFieldCountSets = new TextField();
    //</editor-fold
    //<editor-fold desc="обработчики событий элементов формы ofset.fxml...">
    // Вернуться в главное меню
    // Кнопка "Вернуться в главное меню"
    @FXML
    private void switchToPrimary() throws IOException {App.setRoot("primary");}
    // Макет
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
    // Тираж
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
            if(controller_state.edition == 0) controller_state.edition = 1;
            textFieldEdition.setText(Integer.toString(controller_state.edition));
            controller_state.totalCost = controller_state.cost*controller_state.edition;
            textFieldTotalCost.setText(doubleToString(controller_state.totalCost));

            if(controller_state.numberInSet >= controller_state.edition) {
                controller_state.numberInSet = controller_state.edition;
                controller_state.needCollating = false;
                toggleButtonCollatingTrue.setSelected(false);
                toggleButtonCollatingFalse.setSelected(true);
                labelNumberInSet.setVisible(false);
                textFieldNumberInSet.setVisible(false);
            }
            textFieldNumberInSet.setText(Integer.toString(controller_state.numberInSet));
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
    // Тип цен
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
    // Вид оплаты
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
    // Способ печати
    @FXML
    private void radioButtonTypeOfPrintRISO(){
        controller_state.typeOfPrint = TypeOfPrint.RISO;
        radioButton1.setText("1+0");
        radioButton2.setText("1+1, 2+0");
        radioButton2.setPrefWidth(90);
        radioButton3.setText("3+0, 2+1");
        radioButton3.setPrefWidth(90);
        radioButton4.setText("4+0, 2+2, 3+1");
        radioButton4.setPrefWidth(110);
        controller_state.numberOfUpperColourImprints = 0; // количество цветных прокатов сверху на А3 лист
        controller_state.numberOfLowerColourImprints = 0; // количество цветных прокатов снизу на А3 лист
        switch (controller_state.chromaticity){
            case 1->{
                controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
            }
            case 2->{
                controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 1; // количество черно-белых прокатов снизу на А3 лист
            }
            case 3->{
                controller_state.numberOfUpperBlackImprints = 2; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 1; // количество черно-белых прокатов снизу на А3 лист
            }
            case 4->{
                controller_state.numberOfUpperBlackImprints = 2; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 2; // количество черно-белых прокатов снизу на А3 лист
            }
            case 5->{
                controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
                controller_state.chromaticity=1;
                radioButton5.setSelected(false);
                radioButton1.setSelected(true);
                radioButton5.setVisible(false);
            }
        }
        controller_state.calculate();
    }
    @FXML
    private void radioButtonTypeOfPrintKONICA(){
        controller_state.typeOfPrint = TypeOfPrint.KONICA;
        radioButton1.setText("1+0");
        radioButton2.setText("1+1");
        radioButton2.setPrefWidth(60);
        radioButton3.setText("4+0");
        radioButton3.setPrefWidth(60);
        radioButton4.setText("4+4");
        radioButton4.setPrefWidth(60);
        radioButton5.setVisible(true);
        switch (controller_state.chromaticity){
            case 1->{
                controller_state.numberOfUpperColourImprints = 0; // количество цветных прокатов сверху на А3 лист
                controller_state.numberOfLowerColourImprints = 0; // количество цветных прокатов снизу на А3 лист
                controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
            }
            case 2->{
                controller_state.numberOfUpperColourImprints = 0; // количество цветных прокатов сверху на А3 лист
                controller_state.numberOfLowerColourImprints = 0; // количество цветных прокатов снизу на А3 лист
                controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 1; // количество черно-белых прокатов снизу на А3 лист
            }
            case 3->{
                controller_state.numberOfUpperColourImprints = 1; // количество цветных прокатов сверху на А3 лист
                controller_state.numberOfLowerColourImprints = 0; // количество цветных прокатов снизу на А3 лист
                controller_state.numberOfUpperBlackImprints = 0; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
            }
            case 4->{
                controller_state.numberOfUpperColourImprints = 1; // количество цветных прокатов сверху на А3 лист
                controller_state.numberOfLowerColourImprints = 1; // количество цветных прокатов снизу на А3 лист
                controller_state.numberOfUpperBlackImprints = 0; // количество черно-белых прокатов сверху на А3 лист
                controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
            }

        }
        controller_state.calculate();
    }
    // Цветность
    @FXML
    private void radioButton1Switch(){
        controller_state.chromaticity = 1;
        controller_state.calculate();
    }
    @FXML
    private void radioButton2Switch(){
        controller_state.chromaticity = 2;
        controller_state.calculate();
    }
    @FXML
    private void radioButton3Switch(){
        controller_state.chromaticity = 3;
        controller_state.calculate();
    }
    @FXML
    private void radioButton4Switch(){
        controller_state.chromaticity = 4;
        controller_state.calculate();
    }
    @FXML
    private void radioButton5Switch(){
        controller_state.chromaticity = 5;
        controller_state.calculate();
    }
    // Формат
    @FXML
    private void toggleButtonA3Switch(){
        controller_state.format = 1;
        textFieldNumberOnTheSheet.setText("1");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonA4Switch(){
        controller_state.format = 2;
        textFieldNumberOnTheSheet.setText("2");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonA5Switch(){
        controller_state.format = 4;
        textFieldNumberOnTheSheet.setText("4");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonAEuroSwitch(){
        controller_state.format = 6;
        textFieldNumberOnTheSheet.setText("6");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonA6Switch(){
        controller_state.format = 8;
        textFieldNumberOnTheSheet.setText("8");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonA7Switch(){
        controller_state.format = 16;
        textFieldNumberOnTheSheet.setText("16");
        textFieldNumberOnTheSheet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void toggleButtonHandleNumberOnTheSheetSwitch(){
        textFieldNumberOnTheSheet.setVisible(true);
    }
    // Поля ручного ввода количества на листе
    @FXML
    private void textFieldNumberOnTheSheetTextChange() {
        try {
            controller_state.format = Math.abs(
                    Integer.parseInt(
                            textFieldNumberOnTheSheet.getText()
                    )
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldNumberOnTheSheet.setText(Integer.toString(controller_state.format));
            controller_state.calculate();
        }
    }
    // Бумага
    // Поля ручного ввода цены бумаги
    @FXML
    private void textFieldCostOfASheetTextChange() {
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
    // Нумерация
    @FXML
    private void buttonNumerationClick() throws Exception {
        if('Б' == buttonNumeration.getText().charAt(0)){
            controller_state.needNumeration = true;
            labelCountNumbersOnSheet.setVisible(true);
            textFieldCountNumbersOnSheet.setText("1");
            textFieldCountNumbersOnSheet.setVisible(true);
            controller_state.countNumbersOnSheet = 1;
            labelCostOfNumber.setVisible(true);
            textFieldCostOfNumber.setText(doubleToString(controller_state.costOfNumber));
            textFieldCostOfNumber.setVisible(true);
            buttonNumeration.setText("Нумеруем");
        }else{
            controller_state.needNumeration = false;
            labelCountNumbersOnSheet.setVisible(false);
            textFieldCountNumbersOnSheet.setText("0");
            textFieldCountNumbersOnSheet.setVisible(false);
            controller_state.countNumbersOnSheet = 0;
            labelCostOfNumber.setVisible(false);
            textFieldCostOfNumber.setVisible(false);
            buttonNumeration.setText("Без нумерации");
            controller_state.costOfNumber = controller_state.loadCostOfNumber("C:\\Users\\yahus\\IdeaProjects\\TypographicCalculator\\src\\main\\resources\\ru\\pvv");
            textFieldCostOfNumber.setText(Double.toString(controller_state.costOfNumber).replaceAll(".",","));
        }
        controller_state.calculate();
    }
    @FXML
    private void textFieldCountNumbersOnSheetTextChange() {
        try {
            controller_state.countNumbersOnSheet = Math.abs(
                    Integer.parseInt(
                            textFieldCountNumbersOnSheet.getText()
                    )
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldCountNumbersOnSheet.setText(Integer.toString(controller_state.countNumbersOnSheet));
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldCostOfNumberTextChange() {
        try {
            controller_state.costOfNumber = Math.round(
                    Double.parseDouble(
                            textFieldCostOfNumber.getText().replaceAll(",",".")
                    )*100.0
            )/100.0;
        }catch (NumberFormatException e){
        }finally {
            textFieldCostOfNumber.setText(doubleToString(controller_state.costOfNumber));
            controller_state.calculate();
        }
    }
    // Листоподборка
    @FXML
    private void collatingTrue(){
        if(controller_state.edition!=1) {
            controller_state.needCollating = true;
            labelNumberInSet.setVisible(true);
            textFieldNumberInSet.setVisible(true);
            if (controller_state.edition > 100) {
                controller_state.numberInSet = 100;
            } else {
                controller_state.numberInSet = 1;
            }
            textFieldNumberInSet.setText(Integer.toString(controller_state.numberInSet));
            controller_state.calculate();
        }else{
            toggleButtonCollatingTrue.setSelected(false);
            toggleButtonCollatingFalse.setSelected(true);
        }
    }
    @FXML
    private void collatingFalse(){
        controller_state.needCollating = false;
        labelNumberInSet.setVisible(false);
        textFieldNumberInSet.setVisible(false);
        controller_state.calculate();
    }
    @FXML
    private void textFieldNumberInSetTextChange(){
        try {
            controller_state.numberInSet = Math.abs(
                    Integer.parseInt(
                            textFieldNumberInSet.getText()
                    )
            );
        }catch (NumberFormatException e){
        }finally {
            if(controller_state.numberInSet == 0)controller_state.numberInSet = 1;
            if(controller_state.numberInSet >= controller_state.edition) {
                controller_state.numberInSet = controller_state.edition;
                controller_state.needCollating = false;
                toggleButtonCollatingTrue.setSelected(false);
                toggleButtonCollatingFalse.setSelected(true);
                labelNumberInSet.setVisible(false);
                textFieldNumberInSet.setVisible(false);
            }
            textFieldNumberInSet.setText(Integer.toString(controller_state.numberInSet));
            controller_state.calculate();
        }
    }
    // Подложки
    @FXML
    private void layerTrue(){
        controller_state.needLayer = true;
        comboBoxUpperLayer.setVisible(true);
        comboBoxLowerLayer.setVisible(true);
        controller_state.calculate();
    }
    @FXML
    private void layerFalse(){
        controller_state.needLayer = false;

        comboBoxUpperLayer.getSelectionModel().select(0);
        controller_state.upperLayer = comboBoxUpperLayer.getValue();
        comboBoxUpperLayer.setVisible(false);
        labelCostOfUpperLayer.setVisible(false);
        textFieldCostOfUpperLayer.setVisible(false);

        comboBoxLowerLayer.getSelectionModel().select(0);
        controller_state.lowerLayer = comboBoxLowerLayer.getValue();
        comboBoxLowerLayer.setVisible(false);
        labelCostOfLowerLayer.setVisible(false);
        textFieldCostOfLowerLayer.setVisible(false);

        controller_state.calculate();
    }
    @FXML
    private void textFieldCostOfUpperLayerTextChange() {
        try {
            controller_state.upperLayer.setCost(
                    Math.round(
                            Double.parseDouble(
                                    textFieldCostOfUpperLayer.getText().replaceAll(",",".")
                            )*100.0
                    )/100.0
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldCostOfUpperLayer.setText(doubleToString(controller_state.upperLayer.getCost()));
            controller_state.calculate();
        }
    }
    @FXML
    private void textFieldCostOfLowerLayerTextChange() {
        try {
            controller_state.lowerLayer.setCost(
                    Math.round(
                            Double.parseDouble(
                                    textFieldCostOfLowerLayer.getText().replaceAll(",",".")
                            )*100.0
                    )/100.0
            );
        }catch (NumberFormatException e){
        }finally {
            textFieldCostOfLowerLayer.setText(doubleToString(controller_state.lowerLayer.getCost()));
            controller_state.calculate();
        }
    }
    // Проклейка
    @FXML
    private void gluingTrue(){
        controller_state.needGluing = true;
        controller_state.calculate();
    }
    @FXML
    private void gluingFalse(){
        controller_state.needGluing = false;
        controller_state.calculate();
    }
    // Скобы
    @FXML
    private void bracketTrue(){
        controller_state.needBrackets = true;
        labelNumberOfBracket.setVisible(true);
        textFieldNumberOfBracket.setVisible(true);
        textFieldNumberOfBracket.setText("1");
        controller_state.countBracketsOnSet = 1;
        controller_state.calculate();
    }
    @FXML
    private void bracketFalse(){
        controller_state.needBrackets = false;
        labelNumberOfBracket.setVisible(false);
        textFieldNumberOfBracket.setVisible(false);
        textFieldNumberOfBracket.setText("0");
        controller_state.countBracketsOnSet = 0;
        controller_state.calculate();
    }
    @FXML
    private void textFieldNumberOfBracketTextChange(){
        try {
            controller_state.countBracketsOnSet = Math.abs(
                    Integer.parseInt(
                            textFieldNumberOfBracket.getText()
                    )
            );
        }catch (NumberFormatException e){
        }finally {
            if(controller_state.countBracketsOnSet == 0) {
                toggleButtonBracketFalse.fire();
            }
            else{
                textFieldNumberOfBracket.setText(Integer.toString(controller_state.countBracketsOnSet));
                controller_state.calculate();
            }
        }
    }
    // Пружина
    @FXML
    private void withoutSprings() {
        controller_state.needSpring = false;
        controller_state.calculate();
    }
    @FXML
    private void longSpring() {
        controller_state.needSpring = true;
        controller_state.needShortSpring = false;
        if(controller_state.format == 1)toggleButtonShortSpring.fire();
        else controller_state.calculate();
    }
    @FXML
    private void shortSpring() {
        controller_state.needSpring = true;
        controller_state.needShortSpring = true;
        controller_state.calculate();
    }
    //</editor-fold desc="обработчики событий элементов формы ofset.fxml...">
    @FXML
    private void initialize () throws Exception {
        controller_state.loadPaperAndFormatsAndTariffs("C:\\Users\\yahus\\IdeaProjects\\TypographicCalculator\\src\\main\\resources\\ru\\pvv");
    // Макет
        textFieldCostOfMakingALayout.setText("0,00");
        controller_state.costOfMakingALayout = 0;
    // Тираж
        textFieldEdition.setText("1000");
        controller_state.edition = 1000;
    // Тип цен
        controller_state.typeOfPrices = TypeOfPrices.RETAIL;
    // Вид оплаты
        controller_state.formOfPayment = FormOfPayment.NON_CASH_WITH_VAT;
    // Способ печати
        controller_state.typeOfPrint = TypeOfPrint.RISO;
    // Цветность
        controller_state.chromaticity = 1; // цветность (программно это просто номер кнопки на форме)
        radioButton1.setText("1+0");
        radioButton1.setSelected(true);
        radioButton2.setText("1+1, 2+0");
        radioButton3.setText("3+0, 2+1");
        radioButton4.setText("4+0, 2+2, 3+1");
        radioButton5.setVisible(false);
        controller_state.numberOfUpperColourImprints = 0; // количество цветных прокатов сверху на А3 лист
        controller_state.numberOfLowerColourImprints = 0; // количество цветных прокатов снизу на А3 лист
        // Черно-белые прокаты относятся и к печати ризографом и к печати принтером
        controller_state.numberOfUpperBlackImprints = 1; // количество черно-белых прокатов сверху на А3 лист
        controller_state.numberOfLowerBlackImprints = 0; // количество черно-белых прокатов снизу на А3 лист
    // Формат
        toggleButtonA4.setSelected(true);
        textFieldNumberOnTheSheet.setText("2");
        controller_state.format=2;
        textFieldNumberOnTheSheet.setVisible(false);
    // Бумага
        comboBoxPaper.getSelectionModel().select(0);
        controller_state.paper=comboBoxPaper.getSelectionModel().getSelectedItem();
        textFieldCostOfASheet.setText(doubleToString(controller_state.paper.getCost()));
        labelCostOfASheet.setVisible(false);
        textFieldCostOfASheet.setVisible(false);
    // Нумерация
        controller_state.needNumeration = false;
        labelCountNumbersOnSheet.setVisible(false);
        textFieldCountNumbersOnSheet.setText("0");
        textFieldCountNumbersOnSheet.setVisible(false);
        controller_state.countNumbersOnSheet = 0;
        labelCostOfNumber.setVisible(false);
        textFieldCostOfNumber.setText(doubleToString(controller_state.costOfNumber));
        textFieldCostOfNumber.setVisible(false);
    // Листоподборка
        controller_state.needCollating = false;
        labelNumberInSet.setVisible(false);
        controller_state.numberInSet = controller_state.edition;
        textFieldNumberInSet.setText(Integer.toString(controller_state.edition));
        textFieldNumberInSet.setVisible(false);
    // Подложки
        controller_state.needLayer = false;
        comboBoxUpperLayer.getSelectionModel().select(0);
        controller_state.upperLayer = comboBoxUpperLayer.getValue();
        comboBoxUpperLayer.setVisible(false);
        labelCostOfUpperLayer.setVisible(false);
        textFieldCostOfUpperLayer.setText(doubleToString((comboBoxUpperLayer.getItems().get(comboBoxUpperLayer.getItems().size()-1)).getCost()));
        textFieldCostOfUpperLayer.setVisible(false);
        comboBoxLowerLayer.getSelectionModel().select(0);
        controller_state.lowerLayer = comboBoxLowerLayer.getValue();
        comboBoxLowerLayer.setVisible(false);
        labelCostOfLowerLayer.setVisible(false);
        textFieldCostOfLowerLayer.setText(doubleToString((comboBoxLowerLayer.getItems().get(comboBoxLowerLayer.getItems().size()-1)).getCost()));
        textFieldCostOfLowerLayer.setVisible(false);
    // Проклейка
        controller_state.needGluing = false;
    // Скобы
        controller_state.needBrackets = false;
        labelNumberOfBracket.setVisible(false);
        controller_state.countBracketsOnSet = 0;
        textFieldNumberOfBracket.setText("0");
        textFieldNumberOfBracket.setVisible(false);
    // Пружина
        controller_state.needSpring = false;
        controller_state.needShortSpring = true;
    // Активация реакции на некоторые изменения и первоначальный расчет
        controller_state.activateListeners();
        controller_state.calculate();

    }
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