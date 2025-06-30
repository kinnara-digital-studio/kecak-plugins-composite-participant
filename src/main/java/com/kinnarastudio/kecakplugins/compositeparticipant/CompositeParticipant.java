package com.kinnarastudio.kecakplugins.compositeparticipant;

import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.workflow.model.DefaultParticipantPlugin;
import org.joget.workflow.model.ParticipantPlugin;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aristo
 *
 * Collect participant from multiple Participant Plugins
 */
public class CompositeParticipant extends DefaultParticipantPlugin {
    public final static String LABEL = "Composite Participant";

    @Override
    public String getName() {
        return LABEL;
    }

    @Override
    public String getVersion() {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        ResourceBundle resourceBundle = pluginManager.getPluginMessageBundle(getClassName(), "/messages/BuildNumber");
        String buildNumber = resourceBundle.getString("buildNumber");
        return buildNumber;
    }

    @Override
    public String getDescription() {
        return getClass().getPackage().getImplementationTitle();
    }

    @Override
    public Collection<String> getActivityAssignments(Map map) {
        boolean collectBoth = "collectBoth".equalsIgnoreCase(getPropertyString("collectMode"));
        ParticipantPlugin participantPlugin1 = getPluginObject((Map<String, Object>) getProperty("participant1"), map);
        ParticipantPlugin participantPlugin2 = getPluginObject((Map<String, Object>) getProperty("participant2"), map);

        @Nonnull final Collection<String> activityAssignments = Optional.ofNullable(participantPlugin1).map(p -> p.getActivityAssignments(p.getProperties())).orElseGet(ArrayList::new);
        if(activityAssignments.isEmpty() || collectBoth) {
            activityAssignments.addAll(Optional.ofNullable(participantPlugin2).map(p -> p.getActivityAssignments(p.getProperties())).orElseGet(ArrayList::new));
        }

        return activityAssignments.stream().sorted().distinct().collect(Collectors.toList());
    }

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/CompositeParticipant.json", null, false, "/messages/Composite");
    }

    /**
     * Generate plugins
     * @param elementSelect
     * @param <T>
     * @return
     */
    private  <T> T getPluginObject(Map<String, Object> elementSelect, Map multiParticipantMap) {
        if(elementSelect == null)
            return null;

        AppDefinition appDef = AppUtil.getCurrentAppDefinition();
        PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");

        String className = (String)elementSelect.get("className");
        Map<String, Object> properties = (Map<String, Object>)elementSelect.get("properties");

        T  plugin = (T) pluginManager.getPlugin(className);
        if(plugin == null) {
            LogUtil.warn(getClassName(), "Error generating plugin [" + className + "]");
            return null;
        }

        if(properties != null && plugin instanceof PropertyEditable) {
            properties.put("pluginManager", multiParticipantMap.get("pluginManager"));
            properties.put("workflowActivity", multiParticipantMap.get("workflowActivity"));
            ((PropertyEditable) plugin).setProperties(properties);
        }

        return plugin;
    }
}
