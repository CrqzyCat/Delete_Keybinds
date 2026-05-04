package me.delete_keybinds.delete_keybinds.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.blamejared.controlling.client.NewKeyBindsList$KeyBindEntry", require = 0)
public abstract class DeleteKeybindsControllingMixin {

    @Shadow @Final
    private KeyBinding binding;

    @Shadow @Final
    private ButtonWidget resetButton;

    @Shadow
    protected abstract void update();

    @Unique
    private ButtonWidget unbindButton;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(CallbackInfo ci) {
        this.unbindButton = ButtonWidget.builder(
                Text.literal("§c×"),
                btn -> {
                    this.binding.setBoundKey(InputUtil.UNKNOWN_KEY);
                    KeyBinding.updateKeysByCode();
                    this.update();
                }
        ).dimensions(0, 0, 20, 20).build();
    }

    @Inject(method = "render", at = @At("TAIL"), remap = false)
    private void onRender(
            DrawContext context,
            int mouseX,
            int mouseY,
            boolean hovered,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (this.unbindButton != null) {
            this.unbindButton.setX(this.resetButton.getX() - this.unbindButton.getWidth() - 5);
            this.unbindButton.setY(this.resetButton.getY());
            this.unbindButton.render(context, mouseX, mouseY, tickDelta);
        }
    }
}
