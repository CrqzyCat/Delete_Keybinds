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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.screen.option.ControlsListWidget$KeyBindingEntry")
public abstract class DeleteKeybindsClient {

    @Shadow @Final
    private KeyBinding binding;

    @Unique
    private ButtonWidget unbindButton;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(KeyBinding binding, Text bindingName, CallbackInfo ci) {
        this.unbindButton = ButtonWidget.builder(
                Text.literal("§c×"),
                btn -> {
                    this.binding.setBoundKey(InputUtil.UNKNOWN_KEY);
                    KeyBinding.updateKeysByCode();
                }
        ).dimensions(0, 0, 20, 20).build();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/DrawContext;IIIIIIIZF)V",
            at = @At("TAIL")
    )
    private void onRender(
            DrawContext context,
            int index,
            int y,
            int x,
            int entryWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (this.unbindButton != null) {
            this.unbindButton.setX(x + 165);
            this.unbindButton.setY(y);
            this.unbindButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    @Inject(
            method = "mouseClicked(DDI)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.unbindButton != null && this.unbindButton.mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }
}