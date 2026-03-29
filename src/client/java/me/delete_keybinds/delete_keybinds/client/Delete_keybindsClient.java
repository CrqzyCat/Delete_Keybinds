package me.delete_keybinds.delete_keybinds.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.screen.option.ControlsListWidget$KeyEntry")
public abstract class Delete_keybindsClient {

    @Shadow @Final private KeyBinding binding;

    private ButtonWidget unbindButton;

    // Konstruktor-Injection: Erstellt den Button
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(Object controlsListWidget, KeyBinding binding, Text bindingName, CallbackInfo ci) {
        this.unbindButton = ButtonWidget.builder(Text.literal("§c×"), (button) -> {
            // Setzt die Taste auf "Nicht belegt"
            this.binding.setBoundKey(InputUtil.UNKNOWN_KEY);
            KeyBinding.updateKeysByCode();
        }).dimensions(0, 0, 20, 20).build();
    }

    // Render-Injection: Zeichnet den Button in der Liste
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (this.unbindButton != null) {
            // x + 175 positioniert ihn links neben den Reset-Button
            this.unbindButton.setX(x + 175);
            this.unbindButton.setY(y);
            this.unbindButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    // Klick-Injection: Macht den Button funktionsfähig
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.unbindButton != null && this.unbindButton.mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }
}