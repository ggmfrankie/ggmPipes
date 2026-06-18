package de.ggmfrankie.ggmpipes.gui.widget;

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

    protected ToggleButton(int x, int y, int width, int height, boolean initial, ToggleAction action) {
        super(x, y, width, height, Component.empty());
        state = initial;
        this.action = action;
        updateMessage();
    }

    @Override
    @NullMarked
    protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
        this.extractDefaultSprite(guiGraphicsExtractor);

    }

    @Override
    @NullMarked
    public void onPress(InputWithModifiers input) {
        action.accept(state, this);
        state = !state;
        updateMessage();
    }

    @Override
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
