package org.guccigang.mini_google_docs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.guccigang.mini_google_docs.model.GuiUtil;
import org.guccigang.mini_google_docs.model.TabooWordDAO;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.TextField;

public class OriginalAndVisitorReportTabooWordController {


    @FXML
    TextField userNameBar;

    @FXML
    TextField tabooWordBar;


    private void tabooSuggestionToDB() throws IOException, SQLException {
        TabooWordDAO.sendTabooSuggestion(userNameBar.getText(),tabooWordBar.getText());
    }
    public void submitAction(ActionEvent event) {
        try {
            tabooSuggestionToDB();
            GuiUtil.closeWindow(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}