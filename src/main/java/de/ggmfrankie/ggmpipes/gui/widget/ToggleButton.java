package de.ggmfrankie.ggmpipes.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;


public class ToggleButton extends AbstractButton {

    private boolean state;
    private final ToggleAction action;
    private final Font font;

    public ToggleButton(int x, int y, int width, int height, boolean initial, ToggleAction action, Font font) {
        super(x, y, width, height, Component.empty());
        state = initial;
        this.action = action;
        this.font = font;
        updateMessage();
    }

    @Override
    @NullMarked
    protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
        this.extractDefaultSprite(guiGraphicsExtractor);
        int textColor = 0xFFFFFF;
        if (!this.active) textColor = 0xA0A0A0;
        guiGraphicsExtractor.text(font, "this.getMessage()",
                this.getX() + (this.width - this.font.width("this.getMessage()")) / 2,
                this.getY() + (this.height - 8) / 2,
                textColor
        );
    }

    @Override
    @NullMarked
    public void onPress(InputWithModifiers input) {
        state = !state;
        action.accept(state, this);
        updateMessage();
        this.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    @NullMarked
    public void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    private void updateMessage() {
        this.setMessage(Component.literal(state ? "ON" : "OFF"));
    }

    public boolean isToggled() {
        return state;
    }

    @FunctionalInterface
    public interface ToggleAction{
        void accept(boolean state, ToggleButton button);
    }
}
