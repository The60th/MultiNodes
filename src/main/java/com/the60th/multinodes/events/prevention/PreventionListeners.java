package com.the60th.multinodes.events.prevention;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.events.RegistrableListener;

public class PreventionListeners extends RegistrableListener {
    private final MultiNodes plugin;
    public PreventionListeners(MultiNodes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    //Break events
    public void onPlayerBreakEvent(){}

    //Place events
    public void onPlayerPlaceEvent(){}

    //Tnt etc
    public void onPlayerItemUseEvent(){}

    //Interact events, chests doors
    public void onPlayerInteractEvent(){}

}

