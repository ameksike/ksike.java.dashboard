package ksike.dashboard.home;

import java.awt.Component;
import java.awt.event.MouseEvent;
import ksike.dashboard.home.view.Home;
import ksike.mvc.KsModule;
import ksike.plugin.KsMetadata;
import ksike.ui.base.KsMouseListener;
import ksike.ui.bite.KsDashboard;

/**
 * @author Antonio Membrides Espinosa
 * @made 19/04/2019
 * @version 1.0
 */
public class Main extends KsModule {

    public KsDashboard gui;
    public Home viewHome;

    @Override
    public void onInit() {

    }

    @Override
    public void onLoad() {
        this.gui = (KsDashboard) this.helper.lib.get("gui");
        this.init();
    }

    @Override
    public KsMetadata getMetadata() {
        return new Metadata();
    }

    public void init() {
        this.viewHome = new Home();
        this.gui.center.add(viewHome);
        this.gui.west.add("fa-home", "Home", new KsMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gui.center.add(viewHome);
            }
        });
    }
}
