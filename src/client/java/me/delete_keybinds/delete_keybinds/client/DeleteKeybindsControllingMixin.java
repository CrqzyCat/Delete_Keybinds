package me.delete_keybinds.delete_keybinds.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.blamejared.controlling.client.NewKeyBindsList$KeyEntry", remap = false)
public abstract class DeleteKeybindsControllingMixin {

    // Getter statt direktem @Shadow auf private Felder
    @Shadow
    public abstract ButtonWidget getBtnResetKeyBinding();

    @Shadow
    public abstract KeyBinding getKey();

    @Unique
    private ButtonWidget unbindButton;

    @Inject(method = "render", at = @At("TAIL"), require = 0, remap = false)
    private void onRender(
            DrawContext context,
            int mouseX,
            int mouseY,
            boolean hovered,
            float tickDelta,
            CallbackInfo ci
    ) {
        ButtonWidget resetBtn = this.getBtnResetKeyBinding();
        if (resetBtn == null) return;

        if (this.unbindButton == null) {
            this.unbindButton = ButtonWidget.builder(
                    Text.literal("§c×"),
                    btn -> {
                        this.getKey().setBoundKey(InputUtil.UNKNOWN_KEY);
                        KeyBinding.updateKeysByCode();
                    }
            ).dimensions(0, 0, 20, 20).build();
        }

        this.unbindButton.setX(resetBtn.getX() - this.unbindButton.getWidth() - 5);
        this.unbindButton.setY(resetBtn.getY());
        this.unbindButton.render(context, mouseX, mouseY, tickDelta);
    }
}
