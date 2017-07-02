package net.shadowfacts.extrahoppers.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.shadowmc.util.RedstoneMode

/**
 * @author shadowfacts
 */
class PacketSetHopperRedstoneMode(): IMessage {

	var dim: Int = 0
	lateinit var pos: BlockPos
	lateinit var mode: RedstoneMode

	constructor(dim: Int, pos: BlockPos, mode: RedstoneMode): this() {
		this.dim = dim
		this.pos = pos
		this.mode = mode
	}

	constructor(tile: TileEntityHopperBase<*>): this(tile.world.provider.dimension, tile.pos, tile.mode)

	override fun toBytes(buf: ByteBuf) {
		buf.writeInt(dim)
		buf.writeLong(pos.toLong())
		buf.writeInt(mode.ordinal)
	}

	override fun fromBytes(buf: ByteBuf) {
		dim = buf.readInt()
		pos = BlockPos.fromLong(buf.readLong())
		mode = RedstoneMode.values()[buf.readInt()]
	}

	object Handler: IMessageHandler<PacketSetHopperRedstoneMode, IMessage> {

		override fun onMessage(message: PacketSetHopperRedstoneMode, ctx: MessageContext): IMessage? {
			val world = FMLCommonHandler.instance().minecraftServerInstance.getWorld(message.dim)
			val tile = world.getTileEntity(message.pos)
			if (tile is TileEntityHopperBase<*>) {
				tile.mode = message.mode
				tile.markDirty()
			}
			return null
		}

	}

}