package com.kinnara.kecakplugins.multiparticipant;

import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.workflow.model.DefaultParticipantPlugin;
import org.joget.workflow.model.ParticipantPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author aristo
 *
 * Collect participant from multiple Participant Plugins
 */
public class MultiParticipant extends DefaultParticipantPlugin {
    @Override
    public String getName() {
        return "Multi Participant";
    }

    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
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
        return getName();
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/MultiParticipant.json", null, false, "/messages/MultiParticipant");
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
