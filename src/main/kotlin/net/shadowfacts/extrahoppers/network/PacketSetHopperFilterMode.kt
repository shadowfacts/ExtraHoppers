package net.shadowfacts.extrahoppers.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.shadowfacts.extrahoppers.block.base.TileEntityHopperBase
import net.shadowfacts.extrahoppers.util.filter.FilterMode

/**
 * @author shadowfacts
 */
class PacketSetHopperFilterMode(): IMessage {

	var dim: Int = 0
	lateinit var pos: BlockPos
	lateinit var mode: FilterMode

	constructor(dim: Int, pos: BlockPos, mode: FilterMode): this() {
		this.dim = dim
		this.pos = pos
		this.mode = mode
	}

	constructor(tile: TileEntityHopperBase<*>): this(tile.world.provider.dimension, tile.pos, tile.filterMode)

	override fun toBytes(buf: ByteBuf) {
		buf.writeInt(dim)
		buf.writeLong(pos.toLong())
		buf.writeInt(mode.ordinal)
	}

	override fun fromBytes(buf: ByteBuf) {
		dim = buf.readInt()
		pos = BlockPos.fromLong(buf.readLong())
		mode = FilterMode.values()[buf.readInt()]
	}

	object Handler: IMessageHandler<PacketSetHopperFilterMode, IMessage> {

		override fun onMessage(message: PacketSetHopperFilterMode, ctx: MessageContext): IMessage? {
			val world = FMLCommonHandler.instance().minecraftServerInstance.getWorld(message.dim)
			val tile = world.getTileEntity(message.pos)
			if (tile is TileEntityHopperBase<*>) {
				tile.filterMode = message.mode
				tile.markDirty()
			}
			return null
		}

	}

}