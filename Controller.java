package sample;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {
    private ObservableList<Product> ls = FXCollections.observableArrayList();
    @FXML
    private TableView<Product> myTable;

    @FXML
    private TableColumn<Product, Integer> c1;

    @FXML
    private TableColumn<Product, String> c2;

    @FXML
    private TableColumn<Product, Double> c3;

    @FXML
    private TableColumn<Product, String> c4;

    @FXML
    private TableColumn<Product, Integer> c5;

    @FXML
    private TextField id_text;

    @FXML
    private TextField title_text;

    @FXML
    private TextField price_text;

    @FXML
    private TextField discount_text;

    @FXML
    private TextField search_txt;

    @FXML
    private DatePicker time_text;

    @FXML
    void delete(ActionEvent event) {
        delete();
        search();
        id_text.clear();
        title_text.clear();
        price_text.clear();
        discount_text.clear();
        time_text.setValue(null);
    }

    @FXML
    void insert(ActionEvent event) {
        insert();
        search();
        id_text.clear();
        title_text.clear();
        price_text.clear();
        discount_text.clear();
        time_text.setValue(null);
    }

    @FXML
    void update(ActionEvent event) {
        update();
        search();
        id_text.clear();
        title_text.clear();
        price_text.clear();
        discount_text.clear();
        time_text.setValue(null);
    }

    @FXML
    void dd() {
        Product pr = myTable.getSelectionModel().getSelectedItem();
        id_text.setText(""+pr.getProductId());
        title_text.setText(pr.getTitle());
        price_text.setText(""+pr.getPrice());
        discount_text.setText(""+pr.getDiscount());
    }

    public void refresh(ActionEvent actionEvent) {
        id_text.clear();
        title_text.clear();
        price_text.clear();
        discount_text.clear();
        time_text.setValue(null);
    }


    @FXML
    void search() {
        c1.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
        c2.setCellValueFactory(new PropertyValueFactory<>("Title"));
        c3.setCellValueFactory(new PropertyValueFactory<>("Price"));
        c4.setCellValueFactory(new PropertyValueFactory<>("UpdatedDate"));
        c5.setCellValueFactory(new PropertyValueFactory<>("Discount"));
            ls = getProduct();
            myTable.setItems(ls);
            FilteredList<Product> filteredData = new FilteredList<>(ls, b -> true);
            search_txt.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (product.getTitle().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                        return true;
                    } else if (String.valueOf(product.getPrice()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }else if (String.valueOf(product.getDiscount()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else if (String.valueOf(product.getProductId()).indexOf(lowerCaseFilter)!=-1)
                        return true;
                    else
                        return false;
                });
            });
            SortedList<Product> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(myTable.comparatorProperty());
            myTable.setItems(sortedData);
        }

     Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://TIKOW\\SQLSERVER:1433;databaseName=dbms1As3;integratedSecurity=true";
            System.out.println("Connection created!");
            Connection conn = DriverManager.getConnection(url);
            return conn;
        }
        catch(SQLException e) {
            System.out.println("Error!! No connection! " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Product> getProduct() {
        ObservableList<Product> ol = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String q = "SELECT * FROM Product";
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.createStatement();
            resultSet = statement.executeQuery(q);
            Product product;
            while (resultSet.next()) {
                product = new Product(resultSet.getInt("ProductId"), resultSet.getString("Title"),
                        resultSet.getDouble("Price"), resultSet.getString("UpdatedDate"),
                        resultSet.getInt("Discount"));
                ol.add(product);
            }
        }catch (Exception e) {
            System.out.println("Try again!");
        }
        return ol;
    }

    public void printProduct() {
        search();
        ObservableList<Product> oll = getProduct();
        c1.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
        c2.setCellValueFactory(new PropertyValueFactory<>("Title"));
        c3.setCellValueFactory(new PropertyValueFactory<>("Price"));
        c4.setCellValueFactory(new PropertyValueFactory<>("UpdatedDate"));
        c5.setCellValueFactory(new PropertyValueFactory<>("Discount"));
        myTable.setItems(oll);
    }

    public void insert() {
        String q = "exec InsertIntoProdu " +  id_text.getText() + ", '" + title_text.getText() + "', " +  price_text.getText() +
               ", '" +  time_text.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +  "', " + discount_text.getText();
        executing(q);
        printProduct();
    }

    public void update() {
        String q = "";
        q = "exec updateProduc " + id_text.getText() + ", '" + title_text.getText() + "', '" + price_text.getText() + "', '" +
                time_text.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "', '" + discount_text.getText() + "'";
        executing(q);
        printProduct();
    }

    public void delete() {
        String q = "exec deleteFromProduct " + id_text.getText() + ";";
        executing(q);
        printProduct();
    }

    public void executing(String q) {
        Connection conn = getConnection();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(q);
        }catch (Exception e) {
            System.out.println("Try again!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        printProduct();
        search();
    }

}
