package com.example.inventorymanagementsystem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable{

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button addProducts_btn;

    @FXML
    private Button orders_btn;

    @FXML
    private FontAwesomeIcon logout;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_numberOrder;

    @FXML
    private Label home_totalincome;

    @FXML
    private Label home_availableProducts;

    @FXML
    private AreaChart<String, Number> home_incomeChart;

    @FXML
    private BarChart<String, Number> home_orderChart;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private ImageView addProducts_imageView;

    @FXML
    private Button addProducts_importBtn;

    @FXML
    private TextField addProducts_productid;

    @FXML
    private ComboBox<String> addProducts_productType;

    @FXML
    private TextField addProducts_brand;

    @FXML
    private TextField addProducts_productName;

    @FXML
    private ComboBox<?> addProducts_status;

    @FXML
    private Spinner<Integer> addProducts_quantity;

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button addProducts_resetBtn;

    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private TextField addProducts_price;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<productData> addProducts_tableView;

    @FXML
    private TableColumn<productData, String> addProducts_col_productid;

    @FXML
    private TableColumn<productData, String> addProducts_col_type;

    @FXML
    private TableColumn<productData, String> addProducts_col_brand;

    @FXML
    private TableColumn<productData, String> addProducts_col_productName;

    @FXML
    private TableColumn<productData, String> addProducts_col_price;

    @FXML
    private TableColumn<productData, String> addProducts_col_status;

    @FXML
    private TableColumn<productData, String> addProducts_col_quantity;

    @FXML
    private TextField addProducts_search;

    @FXML
    private AnchorPane orders_form;

    @FXML
    private TableView<customerData> orders_tableView;

    @FXML
    private TableColumn<customerData, String> orders_col_type;

    @FXML
    private TableColumn<customerData, String> orders_col_brand;

    @FXML
    private TableColumn<customerData, String> orders_col_productName;

    @FXML
    private TableColumn<customerData, String> orders_col_quantity;

    @FXML
    private TableColumn<customerData, String> orders_col_price;

    @FXML
    private ComboBox<String> orders_productType;

    @FXML
    private ComboBox<String> orders_brand;

    @FXML
    private ComboBox<String> orders_productName;

    @FXML
    private Spinner<Integer> orders_quantity;

    @FXML
    private Button orders_addBtn;

    @FXML
    private Label orders_total;

    @FXML
    private TextField orders_amount;

    @FXML
    private Label orders_balance;

    @FXML
    private Button orders_payBtn;

    @FXML
    private Button orders_deleteBtn;

    @FXML
    private Button orders_updateBtn;
    private Connection connect;
    private PreparedStatement prepear;
    private Statement statement;
    private ResultSet result;
    private Image image;
    private static final int ITEMS_PER_PAGE = 17;
    public void countAvailableProducts() {
        int totalAvailableProducts = 0;
        String query = "SELECT COUNT(*) AS totalAvailable FROM product WHERE status = 'Available'";

        try {
            connect = database.connectDb();
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            if (result.next()) {
                totalAvailableProducts = result.getInt("totalAvailable");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeDatabaseResources();
        }


        home_availableProducts.setText(String.valueOf(totalAvailableProducts));
    }
    public void calculateTotalIncome() {
        double totalIncome = 0.0;
        String query = "SELECT SUM(paid) AS totalIncome FROM customer_receipt";

        try {
            connect = database.connectDb();
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            if (result.next()) {
                totalIncome = result.getDouble("totalIncome");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        home_totalincome.setText("$"+String.valueOf(totalIncome));
    }

    public void calculateTotalOrdersToday() {
        int totalOrders = 0;
        String query = "SELECT COUNT(*) AS totalOrders FROM customer_receipt WHERE DATE(date) = CURRENT_DATE";

        try {
            connect = database.connectDb();
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            if (result.next()) {
                totalOrders = result.getInt("totalOrders");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        home_numberOrder.setText(String.valueOf(totalOrders));
    }
    public void homeIncomeChart() {
        home_incomeChart.getData().clear();  // Clear existing data

        // SQL query to get total income for the previous 30 days
        String query = "SELECT DATE(date) AS order_date, SUM(total) AS total " +
                "FROM customer_receipt " +
                "WHERE date >= CURDATE() - INTERVAL 30 DAY " +
                "GROUP BY DATE(date) " +
                "ORDER BY DATE(date) ASC";

        connect = database.connectDb();
        try {
            XYChart.Series<String, Number> chart = new XYChart.Series<>();
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            // Iterate through each result row and add data to the chart
            while (result.next()) {
                String date = result.getString("order_date");  // Retrieve date as a String
                Integer totalIncome = result.getInt("total");  // Retrieve total income for the date

                // Add data point to the series
                chart.getData().add(new XYChart.Data<>(date, totalIncome));
            }

            // Add the populated series to the area chart
            chart.setName("Income (Last 30 Days)");
            home_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }


    public void homeOrderChart() {
        // Clear existing data in the chart
        home_orderChart.getData().clear();

        // SQL Query to count total orders per day for the current month
        String query = "SELECT DATE(date) AS order_date, COUNT(*) AS total_orders " +
                "FROM customer_receipt " +
                "WHERE MONTH(date) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                "GROUP BY DATE(date) " +
                "ORDER BY DATE(date) ASC";

        connect = database.connectDb(); // Assuming you have a method to connect to DB

        try {
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Orders");

            while (result.next()) {
                String orderDate = result.getString("order_date");
                int totalOrders = result.getInt("total_orders");

                // Add the data (orderDate as X-axis, totalOrders as Y-axis)
                series.getData().add(new XYChart.Data<>(orderDate, totalOrders));
            }

            // Update the BarChart with the new data
            home_orderChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources(); // Assuming you have this method to close DB resources
        }
    }
    private void initializeProductId() {
        String sql = "SELECT MAX(CAST(product_id AS UNSIGNED)) AS maxId FROM product";
        connect = database.connectDb();

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            int newProductId = 1; // Default value if there are no products
            if (result.next()) {
                String maxId = result.getString("maxId");
                if (maxId != null) {
                    newProductId = Integer.parseInt(maxId) + 1;
                }
            }

            // Set the new product ID in the text field
            addProducts_productid.setText(String.valueOf(newProductId));
            addProducts_productid.setEditable(false); // Make the field non-editable
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProduct() {
        String sql = "INSERT INTO product (product_id, type, brand, productName, price, status, image, date, quantity)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        connect = database.connectDb();

        try {
            Alert alert;

            // Check for empty fields
            if (addProducts_productid.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty() || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || (Integer) addProducts_quantity.getValue() == 0) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields or change your quantity");
                alert.showAndWait();

            } else {
                // Check if product name or product ID already exists
                String checkData = "SELECT * FROM product WHERE productName = '" + addProducts_productName.getText()
                        + "' AND type = '" + addProducts_productType.getSelectionModel().getSelectedItem()
                        + "' OR product_id = '" + addProducts_productid.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product name or ID: " + addProducts_productid.getText() + " already exists.");
                    alert.showAndWait();
                } else {
                    // Handle image path, use default if image path is not set
                    String defaultImagePath = "D:/project/InventoryManagementsystem/src/main/resources/default.png";
                    String imagePath = (getData.path != null && !getData.path.isEmpty())
                            ? getData.path.replace("\\", "/")
                            : defaultImagePath;

                    // Prepare statement
                    prepear = connect.prepareStatement(sql);
                    prepear.setString(1, addProducts_productid.getText());
                    prepear.setString(2, (String) addProducts_productType.getSelectionModel().getSelectedItem());
                    prepear.setString(3, addProducts_brand.getText());
                    prepear.setString(4, addProducts_productName.getText());
                    prepear.setString(5, addProducts_price.getText());
                    prepear.setString(6, (String) addProducts_status.getSelectionModel().getSelectedItem());
                    prepear.setString(7, imagePath);

                    // Set date
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepear.setString(8, String.valueOf(sqlDate));

                    // Set quantity
                    prepear.setString(9, String.valueOf(addProducts_quantity.getValueFactory().getValue()));

                    // Execute update
                    prepear.executeUpdate();

                    // Refresh UI data
                    addProductShowData();
                    addProductsReset();
                    initializeProductId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void addProductsSearch() {
        FilteredList<productData> filter = new FilteredList<>(addProductList, e -> true);
        addProducts_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateProductData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateProductData.getProductId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });

            // Update pagination based on the filtered list
            updatePagination(filter);
        });

        // Initialize pagination with the full list
        updatePagination(filter);
    }

    private void updatePagination(FilteredList<productData> filter) {
        SortedList<productData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());

        int totalPages = (int) Math.ceil((double) filter.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(totalPages);

        pagination.setPageFactory(pageIndex -> {
            int start = pageIndex * ITEMS_PER_PAGE;
            int end = Math.min(start + ITEMS_PER_PAGE, filter.size());

            ObservableList<productData> pageData = FXCollections.observableArrayList(
                    sortList.subList(start, end)
            );

            addProducts_tableView.setItems(pageData);
            return addProducts_tableView;
        });
    }


    public void addProductsUpdate() {
        String uri = getData.path != null ? getData.path.replace("\\", "\\\\") : null;
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

        // Build SQL query with or without image
        String sql = "UPDATE product SET type='" + addProducts_productType.getSelectionModel().getSelectedItem()
                + "', brand='" + addProducts_brand.getText()
                + "', productName='" + addProducts_productName.getText()
                + "', price='" + addProducts_price.getText()
                + "', status='" + addProducts_status.getSelectionModel().getSelectedItem()
                + "', date='" + sqlDate
                + "', quantity='" + addProducts_quantity.getValueFactory().getValue() + "'";
        if (uri != null && !uri.isEmpty()) {
            sql += ", image='" + uri + "'";
        }
        sql += " WHERE product_id='" + addProducts_productid.getText() + "'";

        connect = database.connectDb();
        try {
            Alert alert;
            // Check for empty fields
            if (addProducts_productid.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty() || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || (Integer) addProducts_quantity.getValue() == 0) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields or change your quantity");
                alert.showAndWait();

            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you Sure to Update product ID:" + addProducts_productid.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                    addProductShowData();
                    addProductsReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addProductsDelete() {
        String sql = "DELETE FROM product WHERE product_id='" + addProducts_productid.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;

            // Check if the product ID is empty
            if (addProducts_productid.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a Product ID to delete.");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete product ID: " + addProducts_productid.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product successfully deleted!");
                    alert.showAndWait();

                    addProductShowData();
                    addProductsReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addProductsReset(){
        initializeProductId();
        addProducts_brand.setText("");
        addProducts_productName.setText("");
        addProducts_price.setText("");
        addProducts_status.setValue(null);
        addProducts_quantity.getValueFactory().setValue(0);
        getData.path="";
        addProducts_imageView.setImage(null);
        addProducts_productType.setValue(null);
    }

    public void addProductImportImage(){
        FileChooser open=new FileChooser();
        open.setTitle("Open Image file");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*jpg","*png","*jpeg"));
        File file=open.showOpenDialog(main_form.getScene().getWindow());
        if (file!=null){
            getData.path=file.getAbsolutePath();
            image =new Image(file.toURI().toString(),115,128,false,true);
            addProducts_imageView.setImage(image);
        }
    }

    private String[] listType={"Cosmetics","Drinks","Snacks","Crockery","others"};
    public  void  addProductsListType(){
        List<String> list=new ArrayList<>();
        for (String data:listType){
            list.add(data);
        }
        ObservableList listData=FXCollections.observableArrayList(list);
        addProducts_productType.setItems(listData);
    }
    private String[] listStatus={"Available","Not Available"};
    public void addProductsListStatus(){
        List<String> list=new ArrayList<>();
        for (String status:listStatus){
            list.add(status);
        }
        ObservableList listData=FXCollections.observableArrayList(list);
        addProducts_status.setItems(listData);
    }
    public ObservableList<productData> addProductsListData(){
        ObservableList<productData> productList= FXCollections.observableArrayList();
        String sql="SELECT * FROM product";
        connect=database.connectDb();
        try{
            prepear=connect.prepareStatement(sql);
            result=prepear.executeQuery();
            productData prodD;
            while (result.next()){
                prodD=new productData(result.getInt("product_id"),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"),
                        result.getInt("quantity"));
                productList.add(prodD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }
    private  ObservableList<productData> addProductList;
    public void addProductShowData() {
        addProductList = addProductsListData(); // Fetch the complete product list

        // Ensure columns are set up (only do this once)
        setupTableColumns();

        // Set up pagination
        int totalPages = (int) Math.ceil((double) addProductList.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(totalPages);

        // Define the page factory for pagination
        pagination.setPageFactory(this::createPage);
    }

    private void setupTableColumns() {
        // Bind TableColumns to properties in productData
        addProducts_col_productid.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addProducts_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProducts_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addProducts_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


    private TableView<productData> createPage(int pageIndex) {
        // Calculate the start and end index for the current page
        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, addProductList.size());

        // Get the data for the current page
        ObservableList<productData> pageData = FXCollections.observableArrayList(
                addProductList.subList(start, end)
        );

        // Update the TableView with the current page's data
        addProducts_tableView.setItems(pageData);

        // Return the TableView (required by the PageFactory)
        return addProducts_tableView;
    }



    public void addProductsSelect(){
        productData prodD=addProducts_tableView.getSelectionModel().getSelectedItem();
        int num=addProducts_tableView.getSelectionModel().getSelectedIndex();
        if((num-1)<-1){
            return;
        }
        addProducts_productid.setText(String.valueOf(prodD.getProductId()));
        addProducts_productid.setEditable(false);
        addProducts_brand.setText(prodD.getBrand());
        addProducts_productName.setText(prodD.getProductName());
        addProducts_price.setText(String.valueOf(prodD.getPrice()));
        String uri ="file:"+prodD.getImage();
        image=new Image(uri,115,128,false,true);
        addProducts_imageView.setImage(image);
        addProducts_quantity.getValueFactory().setValue(prodD.getQuantity());
        addProducts_quantity.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int enteredValue = Integer.parseInt(newValue);
                if (enteredValue < 0) {
                    addProducts_quantity.getEditor().setText("0");
                } else if (enteredValue > 100) {
                    addProducts_quantity.getEditor().setText("100");
                }
            } catch (NumberFormatException e) {
                addProducts_quantity.getEditor().setText(oldValue); // Revert to previous value if input is invalid
            }
        });
    }

    // Class variables for customer and order management

    private static int customerId;// static ID generator for unique customer IDs
    private int currentCustomerId;  // current customer ID for the session
    private double totalOrderPrice = 0.0;  // to track the total price of the current order

    // Product type array
    private String[] orderlistType = {"Cosmetics", "Drinks", "Snacks", "Crockery", "Others"};

    // Method to add an order to the customer table
    public void addOrderToCustomerTable() {
        String selectedType = orders_productType.getSelectionModel().getSelectedItem();
        String selectedBrand = orders_brand.getSelectionModel().getSelectedItem();
        String selectedProductName = orders_productName.getSelectionModel().getSelectedItem();
        Integer selectedQuantity = orders_quantity.getValue();

        if (selectedType == null || selectedBrand == null || selectedProductName == null || selectedQuantity == null || selectedQuantity <= 0) {
            showAlert("Error", "Please select valid product details and quantity.");
            return;
        }

        int generatedCustomerId = currentCustomerId;  // Use the current customer ID
        String productQuery = "SELECT price, quantity FROM product WHERE type = ? AND brand = ? AND productName = ?";
        String insertOrderQuery = "INSERT INTO customer (customer_id, type, brand, productName, quantity, price, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String calculateTotalOrderPriceQuery = "SELECT SUM(price) AS totalOrderPrice FROM customer WHERE customer_id = ?";
        String updateProductQuery;

        connect = database.connectDb();

        try {
            // 1. Fetch the product price and current quantity
            prepear = connect.prepareStatement(productQuery);
            prepear.setString(1, selectedType);
            prepear.setString(2, selectedBrand);
            prepear.setString(3, selectedProductName);
            result = prepear.executeQuery();

            Double productPrice = null;
            int currentQuantity = 0;

            if (result.next()) {
                productPrice = result.getDouble("price");
                currentQuantity = result.getInt("quantity");
            }

            if (productPrice == null) {
                showAlert("Error", "Could not find the price for the selected product.");
                return;
            }

            if (currentQuantity < selectedQuantity) {
                showAlert("Error", "Not enough stock available for the selected product.");
                return;
            }

            // 2. Insert the order into the customer table
            prepear = connect.prepareStatement(insertOrderQuery);
            prepear.setInt(1, generatedCustomerId);  // current customer ID
            prepear.setString(2, selectedType);
            prepear.setString(3, selectedBrand);
            prepear.setString(4, selectedProductName);
            prepear.setInt(5, selectedQuantity);
            prepear.setDouble(6, Math.round(productPrice * selectedQuantity * 100.0)/100.0);
            prepear.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
            prepear.executeUpdate();

            // 3. Update the product quantity in the product table
            int newQuantity = currentQuantity - selectedQuantity;
            if (newQuantity <= 0) {
                updateProductQuery = "UPDATE product SET quantity = 0, status = 'Not Available' WHERE type = ? AND brand = ? AND productName = ?";
            } else {
                updateProductQuery = "UPDATE product SET quantity = ? WHERE type = ? AND brand = ? AND productName = ?";
            }

            prepear = connect.prepareStatement(updateProductQuery);

            if (newQuantity > 0) {
                prepear.setInt(1, newQuantity);
                prepear.setString(2, selectedType);
                prepear.setString(3, selectedBrand);
                prepear.setString(4, selectedProductName);
            } else {
                prepear.setString(1, selectedType);
                prepear.setString(2, selectedBrand);
                prepear.setString(3, selectedProductName);
            }

            prepear.executeUpdate();

            // 4. Calculate the total order price using SQL SUM
            prepear = connect.prepareStatement(calculateTotalOrderPriceQuery);
            prepear.setInt(1, generatedCustomerId);
            result = prepear.executeQuery();

            if (result.next()) {
                totalOrderPrice = result.getDouble("totalOrderPrice");
                orders_total.setText(String.format("$%.2f", totalOrderPrice)); // Display the updated total order price
            }

            // Show the updated orders list for the current customer
            ordersShowListData();
            orders_quantity.getValueFactory().setValue(0);
            orders_productType.getSelectionModel().clearSelection();
            orders_brand.getSelectionModel().clearSelection();
            orders_productName.getSelectionModel().clearSelection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    // Method to handle payment and reset the state for a new order
    public void getBalance() {
        String paidAmount = orders_amount.getText();
        if (paidAmount == null || paidAmount.trim().isEmpty()) {
            showAlert("Invalid Amount", "Please enter an amount.");
            return;
        }

        try {
            double enteredAmount = Double.parseDouble(paidAmount);
            double balance = enteredAmount - totalOrderPrice;
            orders_balance.setText(String.format("$%.2f", balance));

            if (enteredAmount < totalOrderPrice) {
                showAlert("Invalid Amount", "Amount is less than total.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Enter a valid number.");
        }
    }
    public void orderReceipt(){
        HashMap hash=new HashMap();
        hash.put("inventoryP",currentCustomerId);
        try{
            JasperDesign jDesign= JRXmlLoader.load("D:\\project\\InventoryManagementsystem\\src\\main\\java\\com\\example\\inventorymanagementsystem\\report.jrxml");
            JasperReport jReport= JasperCompileManager.compileReport(jDesign);
            JasperPrint jprint= JasperFillManager.fillReport(jReport,hash,connect);
            JasperPrintManager.printReport(jprint, false);
        }catch (Exception e){
            e.printStackTrace();
            showAlert("Error", "Failed to generate or print receipt.");
        }

    }
    public void pay() {
        String balanceText = orders_balance.getText();
        double balance = Double.parseDouble(balanceText.replace("$", "").trim());

        if (balance >= 0 && totalOrderPrice >0) {
            // Get the necessary data for the receipt
            int customerId = currentCustomerId;  // Assume you have this from the current session
            double totalPrice = totalOrderPrice; // Total price of the order
            double paid = Double.parseDouble(orders_amount.getText());

            // Insert data into customer_receipt table
            String insertReceiptQuery = "INSERT INTO customer_receipt (customer_id, total, paid, balance, date) VALUES (?, ?, ?, ?, ?)";
            connect = database.connectDb();

            try {
                prepear = connect.prepareStatement(insertReceiptQuery);
                prepear.setInt(1, customerId);
                prepear.setDouble(2, totalPrice);
                prepear.setDouble(3, paid);
                prepear.setDouble(4, balance);
                prepear.setDate(5, new java.sql.Date(new java.util.Date().getTime()));  // Current date

                prepear.executeUpdate();
                orderReceipt();

                // Clear the orders table and reset customer ID after payment
                clearOrderTableAndResetCustomerId();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while saving the receipt.");
            } finally {
                closeDatabaseResources();
            }

        } else if (totalOrderPrice == 0) {
            showAlert("Invalid Order", "Please select a product to Order.");

        } else {
            showAlert("Invalid balance", "Please pay the full amount");
        }
    }

    // Method to clear the order table and reset for the next customer
    private void clearOrderTableAndResetCustomerId() {
        orders_tableView.getItems().clear();  // Clear current orders
        currentCustomerId = generateCustomerId();  // Generate a new customer ID
        totalOrderPrice = 0.0;  // Reset total order price
        orders_total.setText(String.format("$%.2f", totalOrderPrice));
        orders_balance.setText("$0.0");
        orders_amount.clear();// Update the total order price label
        orders_productType.getSelectionModel().clearSelection();
        orders_brand.getSelectionModel().clearSelection();
        orders_productName.getSelectionModel().clearSelection();
    }

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to close database resources
    private void closeDatabaseResources() {
        try {
            if (result != null) result.close();
            if (prepear != null) prepear.close();
            if (connect != null) connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initializeCustomerId() {
        String query = "SELECT MAX(customer_id) FROM customer_receipt";
        try {
            connect = database.connectDb();
            prepear = connect.prepareStatement(query);
            result = prepear.executeQuery();

            if (result.next()) {
                customerId = result.getInt(1) + 1;
                System.out.println("Customer ID initialized to: " + customerId);  // Debugging line
            } else {
                customerId = 1;
                System.out.println("No customer records found. Starting from ID 1.");  // Debugging line
            }
        } catch (Exception e) {
            e.printStackTrace();
            customerId = 1;
            System.out.println("Error initializing customer ID. Fallback to ID 1.");  // Debugging line
        } finally {
            closeDatabaseResources();
        }
    }


    // Method to generate a unique customer ID
    public int generateCustomerId() {
        return customerId++;  // Increment customer ID for next customer
    }
    public void deleteOrderAndUpdateInventory() {
        customerData selectedOrder = orders_tableView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert("Error", "Please select an order to delete.");
            return;
        }

        int selectedCustomerId = selectedOrder.getCustomerId();
        String selectedType = selectedOrder.getType();
        String selectedBrand = selectedOrder.getBrand();
        String selectedProductName = selectedOrder.getProductName();
        int selectedQuantity = selectedOrder.getQuantity();

        String deleteOrderQuery = "DELETE FROM customer WHERE customer_id = ? AND type = ? AND brand = ? AND productName = ?";
        String updateProductQuery = "UPDATE product SET quantity = quantity + ? WHERE type = ? AND brand = ? AND productName = ?";
        String updateProductStatusQuery = "UPDATE product SET status = 'Available' WHERE quantity > 0 AND type = ? AND brand = ? AND productName = ?";
        String calculateTotalOrderPriceQuery = "SELECT SUM(price) AS totalOrderPrice FROM customer WHERE customer_id = ?";

        connect = database.connectDb();

        try {
            // Delete the order from the customer table
            prepear = connect.prepareStatement(deleteOrderQuery);
            prepear.setInt(1, selectedCustomerId);
            prepear.setString(2, selectedType);
            prepear.setString(3, selectedBrand);
            prepear.setString(4, selectedProductName);
            prepear.executeUpdate();

            // Update the product quantity in the product table
            prepear = connect.prepareStatement(updateProductQuery);
            prepear.setInt(1, selectedQuantity);
            prepear.setString(2, selectedType);
            prepear.setString(3, selectedBrand);
            prepear.setString(4, selectedProductName);
            prepear.executeUpdate();

            // 3. Update the product status if quantity is now greater than zero
            prepear = connect.prepareStatement(updateProductStatusQuery);
            prepear.setString(1, selectedType);
            prepear.setString(2, selectedBrand);
            prepear.setString(3, selectedProductName);
            prepear.executeUpdate();

            // Recalculate and update the total order price
            prepear = connect.prepareStatement(calculateTotalOrderPriceQuery);
            prepear.setInt(1, selectedCustomerId);
            result = prepear.executeQuery();

            if (result.next()) {
                totalOrderPrice = result.getDouble("totalOrderPrice");
                orders_total.setText(String.format("$%.2f", totalOrderPrice));
            }

            // Refresh the orders table view
            ordersShowListData();
            showAlert("Success", "Order deleted and inventory updated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the order.");
        } finally {
            closeDatabaseResources();
        }
    }

    public void selectOrder() {
        customerData selectedOrder = orders_tableView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert("Error", "Please select an order.");
            return;
        }

        // Set the current quantity in the spinner
        orders_quantity.getValueFactory().setValue(selectedOrder.getQuantity());
    }

    public void updateOrderQuantity() {
        customerData selectedOrder = orders_tableView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert("Error", "Please select an order to update.");
            return;
        }

        int selectedCustomerId = selectedOrder.getCustomerId();
        String selectedType = selectedOrder.getType();
        String selectedBrand = selectedOrder.getBrand();
        String selectedProductName = selectedOrder.getProductName();
        int previousQuantity = selectedOrder.getQuantity();
        double totalPrice = selectedOrder.getPrice();
        double pricePerUnit = totalPrice / previousQuantity;

        Integer newQuantity = orders_quantity.getValue();
        if (newQuantity == null || newQuantity <= 0) {
            showAlert("Error", "Please enter a valid quantity.");
            return;
        }

        int quantityDifference = newQuantity - previousQuantity;

        String productQuery = "SELECT quantity FROM product WHERE type = ? AND brand = ? AND productName = ?";
        String updateOrderQuery = "UPDATE customer SET quantity = ?, price = ? WHERE customer_id = ? AND type = ? AND brand = ? AND productName = ?";
        String updateProductQuery = "UPDATE product SET quantity = ?, status = ? WHERE type = ? AND brand = ? AND productName = ?";
        String calculateTotalOrderPriceQuery = "SELECT SUM(price) AS totalOrderPrice FROM customer WHERE customer_id = ?";

        connect = database.connectDb();

        try {
            // Check product stock availability
            prepear = connect.prepareStatement(productQuery);
            prepear.setString(1, selectedType);
            prepear.setString(2, selectedBrand);
            prepear.setString(3, selectedProductName);
            result = prepear.executeQuery();

            int currentInventoryQuantity = 0;
            if (result.next()) {
                currentInventoryQuantity = result.getInt("quantity");
            }

            if (quantityDifference > 0 && currentInventoryQuantity < quantityDifference) {
                showAlert("Error", "Not enough stock available. Please reduce the quantity.");
                return;
            }

            // Update the order quantity and price
            prepear = connect.prepareStatement(updateOrderQuery);
            prepear.setInt(1, newQuantity);
            prepear.setDouble(2, Math.round(pricePerUnit * newQuantity*100.0)/100.0);
            prepear.setInt(3, selectedCustomerId);
            prepear.setString(4, selectedType);
            prepear.setString(5, selectedBrand);
            prepear.setString(6, selectedProductName);
            prepear.executeUpdate();

            // Update the product quantity in inventory
            // Update the product quantity in inventory
            int newInventoryQuantity = currentInventoryQuantity - quantityDifference;
            String newStatus = newInventoryQuantity > 0 ? "Available" : "Not Available";

            prepear = connect.prepareStatement(updateProductQuery);
            prepear.setInt(1, Math.max(newInventoryQuantity, 0));  // Ensure non-negative quantity
            prepear.setString(2, newStatus);
            prepear.setString(3, selectedType);
            prepear.setString(4, selectedBrand);
            prepear.setString(5, selectedProductName);
            prepear.executeUpdate();
            // Recalculate and update the total order price using SUM
            prepear = connect.prepareStatement(calculateTotalOrderPriceQuery);
            prepear.setInt(1, selectedCustomerId);
            result = prepear.executeQuery();

            if (result.next()) {
                totalOrderPrice = result.getDouble("totalOrderPrice");
                orders_total.setText(String.format("$%.2f", totalOrderPrice));
            }

            // Refresh the orders table view
            ordersShowListData();
            orders_tableView.getSelectionModel().clearSelection();
            orders_quantity.getValueFactory().setValue(0);
            orders_productType.getSelectionModel().clearSelection();
            orders_brand.getSelectionModel().clearSelection();
            orders_productName.getSelectionModel().clearSelection();

            showAlert("Success", "Order updated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the order.");
        } finally {
            closeDatabaseResources();
        }
    }





    // Method to populate the product type ComboBox
    public void orderListType() {
        ObservableList<String> listData = FXCollections.observableArrayList(orderlistType);
        orders_productType.setItems(listData);

        orders_productType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ordersListBrand();  // Load brands based on selected product type
            }
        });
    }

    // Method to update brands based on selected product type
    public void ordersListBrand() {
        String selectedType = orders_productType.getSelectionModel().getSelectedItem();

        if (selectedType == null) {
            return;
        }

        String sql = "SELECT * FROM product WHERE type = ? AND status = 'Available'";
        connect = database.connectDb();

        try {
            prepear = connect.prepareStatement(sql);
            prepear.setString(1, selectedType);
            result = prepear.executeQuery();
            ObservableList<String> listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("brand"));
            }

            orders_brand.setItems(listData);

            orders_brand.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    ordersListProductName();  // Load product names based on selected brand
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update product names based on selected brand
    public void ordersListProductName() {
        String selectedBrand = orders_brand.getSelectionModel().getSelectedItem();

        if (selectedBrand == null) {
            return;
        }

        String sql = "SELECT * FROM product WHERE brand = ?";
        connect = database.connectDb();

        try {
            prepear = connect.prepareStatement(sql);
            prepear.setString(1, selectedBrand);
            result = prepear.executeQuery();
            ObservableList<String> listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("productName"));
            }

            orders_productName.setItems(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetches order list data for the current customer
    public ObservableList<customerData> orderListdata() {
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        connect = database.connectDb();

        try {
            prepear = connect.prepareStatement(sql);
            prepear.setInt(1, currentCustomerId);  // Use current customer ID
            result = prepear.executeQuery();

            while (result.next()) {
                customerData customerD = new customerData(
                        result.getInt("customer_id"),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getDate("date")
                );
                listData.add(customerD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return listData;
    }

    // Displays the order list in the TableView
    private ObservableList<customerData> ordersList;
    public void ordersShowListData() {
        ordersList = orderListdata();  // Get order list for the current customer
        orders_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        orders_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        orders_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orders_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        orders_tableView.setItems(ordersList);
    }



    public void switchForm(ActionEvent event){
        if(event.getSource() == home_btn) {
            calculateTotalOrdersToday();
            calculateTotalIncome();
            countAvailableProducts();
            homeIncomeChart();
            homeOrderChart();
            home_form.setVisible(true);
            addProducts_form.setVisible(false);
            orders_form.setVisible(false);
            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right,#25a473 ,#89892b);");
            addProducts_btn.setStyle("-fx-background-color: Transparent;");
            orders_btn.setStyle("-fx-background-color: Transparent;");
        } else if (event.getSource() == addProducts_btn) {
            addProducts_form.setVisible(true);
            home_form.setVisible(false);
            orders_form.setVisible(false);
            addProducts_btn.setStyle("-fx-background-color:linear-gradient(to bottom right,#25a473,#89892b);");
            home_btn.setStyle("-fx-background-color: Transparent;");
            orders_btn.setStyle("-fx-background-color: Transparent;");
            addProductShowData();
            addProductsListStatus();
            addProductsListType();
            addProductsSearch();
        }
    }
    public void logout(){
        try{
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure?");
            Optional<ButtonType> option=alert.showAndWait();
            if(option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage=new Stage();
                stage.getIcons().add(new Image(getClass().getResource("/logo.png").toExternalForm()));
                Scene scene=new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }else return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayUsername(){
        username.setText(getData.username);
    }
    public void defaultnav(){
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right,#25a473 ,#89892b);");
    }
    public void maximize(){
        Stage stage=(Stage)main_form.getScene().getWindow();
        if(stage.isMaximized()){
            stage.setMaximized(false);
        }
        else {
            stage.setMaximized(true);
        }
    }
    public void minimize(){
        Stage stage=(Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close(){
        System.exit(0);
    }
    String role=getData.role;
    public void roleFunctions(){
        if (role.equals("admin")){
            orders_btn.setDisable(true);
            defaultnav();
            calculateTotalOrdersToday();
            calculateTotalIncome();
            countAvailableProducts();
            homeIncomeChart();
            homeOrderChart();
            addProductShowData();
            initializeProductId();
            addProductsListStatus();
            addProductsListType();
            addProductsSearch();
        }
        else {
            home_btn.setDisable(true);
            addProducts_btn.setDisable(true);
            initializeCustomerId();
            currentCustomerId=generateCustomerId();
            orders_btn.setStyle("-fx-background-color:linear-gradient(to bottom right,#25a473 ,#89892b);");
            orders_form.setVisible(true);
            home_form.setVisible(false);
            addProducts_form.setVisible(false);
            orderListType();
            ordersShowListData();
            ordersListBrand();
            ordersListProductName();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        roleFunctions();
        addProducts_quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        orders_quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 15, 0));
        addProducts_quantity.setEditable(true);
        orders_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectOrder();
            }
        });
    }
}

