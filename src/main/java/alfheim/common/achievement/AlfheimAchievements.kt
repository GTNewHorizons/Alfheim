package alfheim.common.achievement

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimAchievementHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import net.minecraft.block.Block
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.stats.*
import net.minecraftforge.common.AchievementPage
import vazkii.botania.api.item.IRelic
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import java.util.*

object AlfheimAchievements {
	
	val achievements: MutableList<Achievement> = ArrayList()
	
	val alfheim: Achievement // go to alfheim					A
	val infuser: Achievement // build up infuser				I
	val wingedHussar: Achievement // become winged hussar		W
	
	// relics
	val akashic: Achievement //									K
	val excaliber: Achievement //								E
	val flugelSoul: Achievement //								L
	val gjallarhorn: Achievement //								j
	val gleipnir: Achievement //								g
	val gungnir: Achievement //									G
	val mask: Achievement //									M
	val mjolnir: Achievement // 								J
	val moonlightBow: Achievement //							B
	val ringHeimdall: Achievement //							H
	val ringNjord: Achievement //								N
	val ringSif: Achievement //									S
	val subspace: Achievement //								U
	
	val divineMarksman: Achievement // show your marksman skill D
	val newChance: Achievement // remove race with akashic		C
	val rosaBomb: Achievement // bomb 'em all					R
	
	val flugelHardKill: Achievement // 							F
	val outstander: Achievement // 								O
	
	val ragnarok: Achievement
	val theEND: Achievement
	
	val firework: Achievement //								Y
	
	// ---------------> X
	// |    W     H N S
	// |      I  r D C
	// |          B K g
	// |     A L M J G
	// |        F U E j
	// |           R
	// |  Y      O
	// Y
	
	init {
		alfheim = AlfheimAchievement("alfheim", 0, 0, ItemStack(AlfheimBlocks.alfheimPortal, 1, 1), null)
		infuser = AlfheimAchievement("infuser", 1, -2, AlfheimBlocks.manaInfuser, alfheim)
		
		wingedHussar = AlfheimAchievement("wingedHussaurs", -2, -2, AlfheimItems.elvoriumHelmet, infuser).setSpecial()
		
		flugelSoul = AlfheimAchievement("flugelSoul", 2, 0, AlfheimItems.flugelSoul, null)
		
		flugelHardKill = AlfheimAchievement("flugelKill", 3, 1, ModItems.flightTiara, flugelSoul)
		mask = AlfheimAchievement("mask", 4, 0, AlfheimItems.mask, flugelSoul)
		outstander = AlfheimAchievement("outstander", 4, 3, Items.diamond_chestplate, mask).setSpecial()
		
		ragnarok = if (AlfheimCore.ENABLE_RAGNAROK) AlfheimAchievement("ragnarok", 32, 32, AlfheimItems.ragnarokEmblem, null).setSpecial() else AchievementList.openInventory
		theEND = if (AlfheimCore.ENABLE_RAGNAROK) AlfheimAchievement("theEND", 34, 32, ItemStack(AlfheimItems.ragnarokEmblem).apply { ItemNBTHelper.setBoolean(this, ItemRagnarokEmblem.TAG_GEM_FLAG, true) }, ragnarok).setSpecial() else AchievementList.openInventory
		
		akashic = AlfheimAchievement("akashic", 7, -1, AlfheimItems.akashicRecords, mask)
		excaliber = AlfheimAchievement("excaliber", 7, 1, AlfheimItems.excaliber, mask)
		gjallarhorn = AlfheimAchievement("gjallarhorn", 7, 3, AlfheimItems.gjallarhorn, mask)
		gleipnir = AlfheimAchievement("gleipnir", 9, -1, AlfheimItems.gleipnir, mask)
		gungnir = AlfheimAchievement("gungnir", 8, 0, AlfheimItems.gungnir, mask)
		mjolnir = AlfheimAchievement("mjolnir", 6, 0, AlfheimItems.mjolnir, mask)
		moonlightBow = AlfheimAchievement("moonlightBow", 5, -1, AlfheimItems.moonlightBow, mask)
		ringHeimdall = AlfheimAchievement("ringHeimdall", 5, -3, AlfheimItems.priestRingHeimdall, mask)
		ringNjord = AlfheimAchievement("ringNjord", 7, -3, AlfheimItems.priestRingNjord, mask)
		ringSif = AlfheimAchievement("ringSif", 9, -3, AlfheimItems.priestRingSif, mask)
		subspace = AlfheimAchievement("subspace", 5, 1, AlfheimItems.subspaceSpear, mask)
		
		newChance = AlfheimAchievement("newChance", 8, -2, Items.spawn_egg, akashic)
		divineMarksman = AlfheimAchievement("divineMarksman", 6, -2, ItemStack(Blocks.red_flower, 1, 2), moonlightBow).setSpecial()
		rosaBomb = AlfheimAchievement("rosaBomb", 6, 2, Blocks.red_flower, subspace).setSpecial()
		
		firework = AlfheimAchievement("firework", -3, 3, Items.fireworks, null).setSpecial()
		
		AchievementPage.registerAchievementPage(AchievementPage(ModInfo.MODID.capitalize(), *achievements.toTypedArray()))
		
		AlfheimAchievementHandler
	}
}

class AlfheimAchievement(name: String, x: Int, y: Int, icon: ItemStack, parent: Achievement?): Achievement("achievement.$name.name", name, x, y, icon, parent) {
	
	init {
		AlfheimAchievements.achievements.add(this)
		registerStat()
		
		if (icon.item is IRelic) (icon.item as IRelic).bindAchievement = this
	}
	
	constructor(name: String, x: Int, y: Int, icon: Item, parent: Achievement?): this(name, x, y, ItemStack(icon), parent)
	
	constructor(name: String, x: Int, y: Int, icon: Block, parent: Achievement?): this(name, x, y, ItemStack(icon), parent)
}