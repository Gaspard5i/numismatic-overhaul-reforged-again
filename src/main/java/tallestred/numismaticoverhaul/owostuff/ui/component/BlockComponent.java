package tallestred.numismaticoverhaul.owostuff.ui.component;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import tallestred.numismaticoverhaul.mixin.owomixins.ui.BlockEntityAccessor;
import tallestred.numismaticoverhaul.owostuff.ui.base.BaseComponent;
import tallestred.numismaticoverhaul.owostuff.ui.util.Drawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BlockComponent extends BaseComponent {

    private final Minecraft client = Minecraft.getInstance();

    private final BlockState state;
    private final @Nullable BlockEntity entity;

    protected BlockComponent(BlockState state, @Nullable BlockEntity entity) {
        this.state = state;
        this.entity = entity;
    }

    @Override
    @SuppressWarnings("NonAsciiCharacters")
    public void draw(Drawer matrices, int mouseX, int mouseY, float partialTicks, float delta) {
        matrices.pose().pushPose();

        matrices.pose().translate(x + this.width / 2f, y + this.height / 2f, 100);
        matrices.pose().scale(40 * this.width / 64f, -40 * this.height / 64f, 40);

        matrices.pose().mulPose(Axis.XP.rotationDegrees(30));
        matrices.pose().mulPose(Axis.YP.rotationDegrees(45 + 180));

        matrices.pose().translate(-.5, -.5, -.5);

        RenderSystem.runAsFancy(() -> {
            final var vertexConsumers = client.renderBuffers().bufferSource();
            if (this.state.getRenderShape() != RenderShape.ENTITYBLOCK_ANIMATED) {
                this.client.getBlockRenderer().renderSingleBlock(
                        this.state, matrices.pose(), vertexConsumers,
                        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY
                );
            }

            if (this.entity != null) {
                var медведь = this.client.getBlockEntityRenderDispatcher().getRenderer(this.entity);
                if (медведь != null) {
                    медведь.render(entity, partialTicks, matrices.pose(), vertexConsumers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                }
            }

            RenderSystem.setShaderLights(new Vector3f(-1.5f, -.5f, 0), new Vector3f(0, -1, 0));
            vertexConsumers.endBatch();
            Lighting.setupFor3DItems();
        });

        matrices.pose().popPose();
    }

    protected static void prepareBlockEntity(BlockState state, BlockEntity blockEntity, @Nullable CompoundTag nbt) {
        if (blockEntity == null) return;

        ((BlockEntityAccessor) blockEntity).owo$setCachedState(state);
        blockEntity.setLevel(Minecraft.getInstance().level);

        if (nbt == null) return;

        final var nbtCopy = nbt.copy();

        nbtCopy.putInt("x", 0);
        nbtCopy.putInt("y", 0);
        nbtCopy.putInt("z", 0);

        blockEntity.load(nbtCopy);
    }
}
