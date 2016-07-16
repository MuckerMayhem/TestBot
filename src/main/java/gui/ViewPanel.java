package gui;

import java.awt.*;

/**
 * Represents the middle panel in the BotGui
 * Contains two panels:<ul>
 *     <li>An info panel that shows the information of the currently selected guild</li>
 *     <li>A settings panel that shows the settings of the currently selected guild</li>
 * </ul>
 */
public class ViewPanel extends AbstractBotPanel{

    private InfoPanel infoPanel;
    private SettingsPanel settingsPanel;

    public ViewPanel(BotGui gui, LayoutManager layout){
        super(gui, layout);

        this.infoPanel = new InfoPanel(gui);
        add(this.infoPanel);
        
        this.settingsPanel = new SettingsPanel(gui);
        add(this.settingsPanel);
    }

    public InfoPanel getInfoPanel(){
        return this.infoPanel;
    }
    
    public SettingsPanel getSettingsPanel(){
        return this.settingsPanel;
    }
    
    @Override
    protected void onUpdate(){
        this.infoPanel.update();
        this.settingsPanel.update();
    }
}
