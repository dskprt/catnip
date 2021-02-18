package com.github.dskprt.catnip.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class StartupController {

    private int stage = 0;
    private int stages = 1;

    @FXML
    public Label loadingStage;

    @FXML
    public Label loadingInfo;

    @FXML
    public ProgressBar progressBar;

    public void setStage(int stage) {
        this.stage = stage;

        progressBar.setProgress((double) stages / stage);
    }

    public void setStages(int stages) {
        this.stages = stages;
    }

    public void incrementStage() {
        stage++;

        progressBar.setProgress(progressBar.getProgress() + (1d / stages));
        loadingStage.setText(String.format("Loading... (%d/%d)", stage, stages));
    }
}
