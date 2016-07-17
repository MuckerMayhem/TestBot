package gui.window.main.view;

import gui.AbstractBotPanel;
import gui.BotGui;
import gui.window.main.view.features.FeaturesPanel;
import gui.window.main.view.info.InfoPanel;
import gui.window.main.view.settings.SettingsPanel;

import java.awt.*;

/**
 * Represents the middle panel in the BotGui
 * Contains two panels:<ul>
 *     <li>An {@link InfoPanel} that shows the information of the currently selected guild</li>
 *     <li>A {@link SettingsPanel} that shows the settings of the currently selected guild</li>
 * </ul>
 */
public class ViewPanel extends AbstractBotPanel{

    private InfoPanel infoPanel;
    private FeaturesPanel featuresPanel;
    private SettingsPanel settingsPanel;

    public ViewPanel(BotGui gui){
        super(gui, new GridLayout(3, 3));

        this.infoPanel = new InfoPanel(gui);
        add(this.infoPanel);
        
        this.featuresPanel = new FeaturesPanel(gui);
        add(this.featuresPanel);
        
        this.settingsPanel = new SettingsPanel(gui);
        add(this.settingsPanel);
    }

    public InfoPanel getInfoPanel(){
        return this.infoPanel;
    }
    
    public FeaturesPanel getFeaturesPanel(){
        return this.featuresPanel;
    }
    
    public SettingsPanel getSettingsPanel(){
        return this.settingsPanel;
    }
    
    @Override
    protected void onUpdate(){
        this.infoPanel.update();
        this.featuresPanel.update();
        this.settingsPanel.update();
    }
}
