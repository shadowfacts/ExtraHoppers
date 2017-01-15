package net.shadowfacts.extrahoppers.block.fluid

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.VertexBuffer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.FluidStack
import net.shadowfacts.extrahoppers.block.fluid.TileEntityFluidHopper
import net.shadowfacts.shadowmc.util.RenderHelper
import org.lwjgl.opengl.GL11

/**
 * @author shadowfacts
 */
object TESRFluidHopper: TileEntitySpecialRenderer<TileEntityFluidHopper>() {

	override fun renderTileEntityAt(te: TileEntityFluidHopper, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
		if (te.tank.fluid != null && !te.world.getBlockState(te.pos.up()).isSideSolid(te.world, te.pos.up(), EnumFacing.DOWN)) {
			val fluid = te.tank.fluid

			val tessellator = Tessellator.getInstance()
			val renderer = tessellator.buffer
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
			val color = fluid!!.fluid.getColor(fluid)
			val brightness = Minecraft.getMinecraft().world.getCombinedLight(te.pos, fluid.fluid.luminosity)

			GlStateManager.pushMatrix()

			GlStateManager.disableLighting()
			GlStateManager.enableBlend()
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

			if (Minecraft.isAmbientOcclusionEnabled()) {
				GL11.glShadeModel(GL11.GL_SMOOTH)
			} else {
				GL11.glShadeModel(GL11.GL_FLAT)
			}

			GlStateManager.translate(x, y, z)

			val still = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(fluid.fluid.getStill(fluid).toString())

			RenderHelper.putTexturedQuad(renderer, still, 2 / 16.0, 14 / 16.0, 2 / 16.0, 12 / 16.0, 0.0, 12 / 16.0, EnumFacing.UP, color, brightness)

			tessellator.draw()

			GlStateManager.disableBlend()
			GlStateManager.enableLighting()
			GlStateManager.popMatrix()
		}
	}

}
