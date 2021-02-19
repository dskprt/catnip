package com.github.dskprt.catnip.module;

import com.github.dskprt.catnip.settings.Settings;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.controllers.StartupController;
import javafx.application.Platform;
import org.reflections.Reflections;

import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();

        StartupController controller = JfxUI.getController();
        Platform.runLater(controller::incrementStage);

        new Reflections("com.github.dskprt.catnip.module.modules").getSubTypesOf(Module.class)
                .forEach(m -> {
                    Module module;

                    try {
                        module = m.newInstance();
                    } catch(InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }

                    Platform.runLater(() -> controller.loadingInfo.setText("Registering module " + module.getName()));
                    modules.add(module);

                    Platform.runLater(() -> controller.loadingInfo.setText("Loading settings for module " + module.getName()));
                    Settings.load(module);
                });
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
