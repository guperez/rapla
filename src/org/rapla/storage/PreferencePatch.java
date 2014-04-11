package org.rapla.storage;

import java.util.LinkedHashSet;
import java.util.Set;

import org.rapla.entities.configuration.internal.RaplaMapImpl;

public class PreferencePatch extends RaplaMapImpl {
    String userId;
    Set<String> removedEntries = new LinkedHashSet<String>();
    public void addRemove(String role) {
        removedEntries.add( role);
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public Set<String> getRemovedEntries() 
    {
        return removedEntries;
    }
    
    @Override
    public String toString() {
        return "Patch for " + userId + " " + super.toString() + " Removed " + removedEntries.toString(); 
    }
    
    
    
}
