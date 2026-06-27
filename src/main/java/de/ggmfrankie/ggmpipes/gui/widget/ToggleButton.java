package de.ggmfrankie.ggmpipes.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NullMarked;

import java.awt.*;
import java.util.function.Consumer;


public class ToggleButton extends AbstractButton {

    private boolean state;
    private final Consumer<ToggleButton> action;
    private final Font font;

    public ToggleButton(int x, int y, int width, int height, boolean initial, String label ,Consumer<ToggleButton> action) {
        super(x, y, width, height, Component.empty());
        state = initial;
        this.action = action;
        this.font = Minecraft.getInstance().font;
        setMessage(Component.literal(label));
    }

    @Override
    @NullMarked
    protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
        this.extractDefaultSprite(guiGraphicsExtractor);
        int textColor = state ? ARGB.color(20, 200, 20) : ARGB.color(200, 20, 20);

        Component message = this.getMessage();
        guiGraphicsExtractor.text(font, message,
                this.getX() + (this.width - this.font.width(message)) / 2,
                this.getY() + (this.height - 8) / 2,
                textColor
        );
    }

    @Override
    @NullMarked
    public void onPress(InputWithModifiers input) {
        state = !state;
        action.accept(this);
        this.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    @NullMarked
    public void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    private void updateMessage() {

    }

    public boolean isToggled() {
        return state;
    }

}
