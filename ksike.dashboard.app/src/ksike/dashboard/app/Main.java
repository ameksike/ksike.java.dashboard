package ksike.dashboard.app;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ksike.mvc.KsModule;
import ksike.secretary.EntityManager;
import ksike.plugin.KsFodm;
import ksike.plugin.KsHelper;
import ksike.plugin.KsLoader;
import ksike.plugin.KsLoaderSubscribtor;
import ksike.plugin.KsPluginException;
import ksike.ui.bite.KsDashboard;

/**
 * @author Antonio Membrides Espinosa
 * @made 19/04/2019
 * @version 1.0
 */
public class Main implements KsLoaderSubscribtor {

    public JFrame gui;
    private final String mainclass;
    private final String extension;
    private final String pathMod;
    private final String pathLib;
    private final String pathData;
    private final String pathCfg;
    private final String title;
    private final int width;
    private final int height;

    private KsDashboard dashboard;
    private final KsLoader loader;
    private final KsHelper helper;

    public Main() throws IOException, URISyntaxException {
        this.pathMod = KsFodm.self().getFilePath(this.getClass()) + "./plugin/";
        this.pathLib = KsFodm.self().getFilePath(this.getClass()) + "./lib/";
        this.pathData = KsFodm.self().getFilePath(this.getClass()) + "./data/";
        this.pathCfg = KsFodm.self().getFilePath(this.getClass()) + "./cfg/";

        this.extension = "jar";
        this.mainclass = "Main";
        this.title = "Demo app";

        this.width = 1024;
        this.height = 768;
        this.loader = KsLoader.self();
        this.helper = KsHelper.self();
        this.loader.setMin(3);
    }

    public void build() {
        this.gui = new JFrame(this.title);
        this.loader.setPath(this.pathMod);
        this.loader.setMainclass(this.mainclass);
        this.loader.setExtension(this.extension);
        this.loader.subscribe(this);
        try {
            this.dashboard = new KsDashboard();
            this.helper.lib.put("gui", this.dashboard);
            this.helper.lib.put("db", this.dbUser());
            this.gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.loader.send("init", "ksike.porter", "porter");

        } catch (KsPluginException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            this.dashboard.south.add(new JLabel(ex.getMessage()));
        } finally {
            this.gui.setVisible(true);
            this.gui.setSize(this.width, this.height);
            this.gui.add(this.dashboard);
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        Main app;
        try {
            app = new Main();
            app.build();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private EntityManager dbUser() {
        EntityManager dm = new EntityManager();
        dm.setPath(this.pathData + "model.accdb");
        return dm;
    }

    private EntityManager dbSys() {
        EntityManager dm = new EntityManager();
        dm.setPath(this.pathCfg + "sim.accdb");
        return dm;
    }

    @Override
    public void onEvent(String event, KsModule mod, Object handler, Object[] params) {
        switch (event) {
            case "preload":
                if (mod.getClass().getName().equals("ksike.porter.Main") ){
                    mod.setDependency(this.dbSys());
                }
                break;
            case "posload":

                break;
            case "exception":
                this.dashboard.log("Error messages", "Se ha detectado un error en " + mod.getClass().getName(), ((Exception)params[0]).getMessage());
                break;
        }
    }

    @Override
    public boolean checkPlugin(String name, KsModule mod, KsLoader handler) {
        this.dashboard.log("Plugin messages", "Se ha cargado el plugin " + name, "satisfactoriamente");
        return true;
    }
}
