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

    @Shadow
    private ButtonWidget btnResetKeyBinding;

    @Shadow
    private KeyBinding key;

    @Unique
    private ButtonWidget unbindButton;

    // Kein direkter Import von Controlling nötig - Object als Typ für unbekannte Parameter
    @Inject(method = "<init>", at = @At("TAIL"), require = 0, remap = false)
    private void onInit(Object list, KeyBinding key, Text text, CallbackInfo ci) {
        this.unbindButton = ButtonWidget.builder(
                Text.literal("§c×"),
                btn -> {
                    this.key.setBoundKey(InputUtil.UNKNOWN_KEY);
                    KeyBinding.updateKeysByCode();
                }
        ).dimensions(0, 0, 20, 20).build();
    }

    @Inject(method = "render", at = @At("TAIL"), require = 0, remap = false)
    private void onRender(
            DrawContext context,
            int mouseX,
            int mouseY,
            boolean hovered,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (this.unbindButton != null && this.btnResetKeyBinding != null) {
            this.unbindButton.setX(this.btnResetKeyBinding.getX() - this.unbindButton.getWidth() - 5);
            this.unbindButton.setY(this.btnResetKeyBinding.getY());
            this.unbindButton.render(context, mouseX, mouseY, tickDelta);
        }
    }
}
