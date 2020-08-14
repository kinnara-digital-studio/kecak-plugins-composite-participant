package com.kinnara.kecakplugins.compositeparticipant;

import org.joget.workflow.model.DefaultParticipantPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author aristo
 *
 * Nobody
 *
 */
public class NobodyParticipant extends DefaultParticipantPlugin {
    @Override
    public String getName() {
        return "Nobody";
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
        return Collections.emptyList();
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
        return "";
    }
}
