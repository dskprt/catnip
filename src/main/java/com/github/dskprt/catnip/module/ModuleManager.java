package com.github.dskprt.catnip.module;

import com.github.dskprt.catnip.module.modules.Test;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.controllers.StartupController;
import javafx.application.Platform;

import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();

        StartupController controller = JfxUI.getController();

        Platform.runLater(() -> {
            controller.incrementStage();
            controller.loadingInfo.setText("Registering modules");
        });

        modules.add(new Test());
    }

    public Module getModuleById(String id) {
        return modules.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Module[] getModulesInCategory(Module.Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .toArray(Module[]::new);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
