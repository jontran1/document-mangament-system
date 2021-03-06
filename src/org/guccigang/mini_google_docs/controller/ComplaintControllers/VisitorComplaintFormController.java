package org.guccigang.mini_google_docs.controller.ComplaintControllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.guccigang.mini_google_docs.model.*;

import java.sql.*;
import java.util.Calendar;

public class VisitorComplaintFormController {

    @FXML
    TextArea complaintText;

    private DocumentFile docFile;

    private UserObject currentUser;

    public VisitorComplaintFormController(){
        this(null);
    }

    public VisitorComplaintFormController(DocumentFile docFile) {
        this.docFile = docFile;
    }

    public void submitComplaintToDB(ActionEvent event) throws SQLException {
        int tempDocId = docFile.getID();
        int result = 0;



        String userNameInput = "";

        if(currentUser == null){
            userNameInput = "visitor";
        }
        else {
            userNameInput = currentUser.getUserName();
        }

        try {
            String sql = "INSERT INTO complaints(DocId,version,owner,complainer,message) VALUES ('" + tempDocId + "','" + getVersionOfDocument(tempDocId) + "', '" + docFile.getOwner() + "', '" + userNameInput + "','" + complaintText.getText() + "')";

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GucciGangDB", "root", "password");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeUpdate(sql);


            if (result == 0) {
                GuiUtil.createAlertWindow(Alert.AlertType.ERROR, null, "Something went wrong. Please try again", "error");
            } else {
                GuiUtil.createAlertWindow(Alert.AlertType.CONFIRMATION, "Complaint has been sent!",
                        "Complaint submitted successfully!,", "Confirmation");
                GuiUtil.closeWindow(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitComplaint(ActionEvent actionEvent){
        if(currentUser == null){
            String SQLstatement = "INSERT INTO complaints (DocID, owner, complainer, message)values(?,?,?,?)";
            DbUtil.executeUpdateDB(SQLstatement,String.valueOf(docFile.getiD()),docFile.getOwner(),currentUser.getUserName(),complaintText.getText());
        }else {
            String SQLstatement = "INSERT INTO complaints (DocID, owner, complainer, message)values(?,?,?,?)";
            DbUtil.executeUpdateDB(SQLstatement,String.valueOf(docFile.getiD()),docFile.getOwner(),"Visitor",complaintText.getText());
        }

    }

    private int getVersionOfDocument(Integer docID) throws SQLException {
       int version = 0;

        String sqlStatement = "Select version FROM revisions WHERE docID = ? ";
        ResultSet rs =DbUtil.processQuery(sqlStatement,docID.toString());
        if(rs.next()){
             version = rs.getInt("version");
        }

        return version;
    }

    private boolean isTaken(String userName) {
        ResultSet resultSet = DbUtil.processQuery("SELECT * FROM users WHERE username = ?", userName);
        try {
            if(!resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
