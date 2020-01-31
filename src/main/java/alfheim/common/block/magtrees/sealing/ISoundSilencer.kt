package alfheim.common.block.magtrees.sealing

import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent17

/**
 * @author WireSegal
 * Created at 9:00 AM on 1/27/16.
 *
 * Defines a block which can silence sounds.
 */
interface ISoundSilencer {
	
	/**
	 * Checks if the block can silence the given sound.
	 * The maximum distance is 16.
	 *
	 * @param world - The current world.
	 * @param x - The x position of the block.
	 * @param y - The y position of the block.
	 * @param z - The z position of the block.
	 * @param dist - The distance to the block from the sound.
	 * @param soundEvent - The event being processed.
	 * @return - Whether the block should silence the given sound.
	 */
	fun canSilence(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17): Boolean
	
	/**
	 * Gets the volume multiplier that the block should apply.
	 *
	 * @param world - The current world.
	 * @param x - The x position of the block.
	 * @param y - The y position of the block.
	 * @param z - The z position of the block.
	 * @param dist - The distance to the block from the sound.
	 * @param soundEvent - The event being processed.
	 * @return - The volume multiplier of the given block.
	 */
	fun getVolumeMultiplier(world: World, x: Int, y: Int, z: Int, dist: Double, soundEvent: PlaySoundEvent17): Float
}
