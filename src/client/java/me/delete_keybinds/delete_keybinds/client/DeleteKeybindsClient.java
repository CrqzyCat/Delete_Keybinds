package me.delete_keybinds.delete_keybinds.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
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
import com.google.common.collect.ImmutableList;
import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.screen.option.ControlsListWidget$KeyBindingEntry")
public abstract class DeleteKeybindsClient {

    @Shadow @Final
    private KeyBinding binding;

    @Shadow @Final
    private ButtonWidget resetButton;

    @Unique
    private ButtonWidget unbindButton;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(
            ControlsListWidget outer,
            KeyBinding binding,
            Text bindingName,
            CallbackInfo ci
    ) {
        this.unbindButton = ButtonWidget.builder(
                Text.literal("§c×"),
                btn -> {
                    this.binding.setBoundKey(InputUtil.UNKNOWN_KEY);
                    KeyBinding.updateKeysByCode();
                }
        ).dimensions(0, 0, 20, 20).build();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(
            DrawContext context,
            int mouseX,
            int mouseY,
            boolean hovered,
            float deltaTicks,
            CallbackInfo ci
    ) {
        if (this.unbindButton != null) {
            this.unbindButton.setX(this.resetButton.getX() - this.unbindButton.getWidth() - 5);
            this.unbindButton.setY(this.resetButton.getY());
            this.unbindButton.render(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Inject(method = "children()Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void onChildren(CallbackInfoReturnable<List<Element>> cir) {
        if (this.unbindButton != null) {
            ImmutableList.Builder<Element> list = ImmutableList.builder();
            list.add(this.unbindButton);
            list.addAll(cir.getReturnValue());
            cir.setReturnValue(list.build());
        }
    }
}