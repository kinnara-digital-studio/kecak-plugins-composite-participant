package com.kinnarastudio.kecakplugins.compositeparticipant;

import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.PluginManager;
import org.joget.workflow.model.DefaultParticipantPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author aristo
 *
 * Nobody
 *
 */
public class NobodyParticipant extends DefaultParticipantPlugin {
    public final static String LABEL = "Nobody";

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
        return Collections.emptyList();
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
        return "";
    }
}
