package alfheim.common.integration.thaumcraft

import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks.alfheimPortal
import alfheim.common.block.AlfheimBlocks.alfheimPylon
import alfheim.common.block.AlfheimBlocks.animatedTorch
import alfheim.common.block.AlfheimBlocks.anomaly
import alfheim.common.block.AlfheimBlocks.anyavil
import alfheim.common.block.AlfheimBlocks.dreamLeaves
import alfheim.common.block.AlfheimBlocks.dreamLog
import alfheim.common.block.AlfheimBlocks.dreamSapling
import alfheim.common.block.AlfheimBlocks.elvenOres
import alfheim.common.block.AlfheimBlocks.elvenSand
import alfheim.common.block.AlfheimBlocks.elvoriumBlock
import alfheim.common.block.AlfheimBlocks.itemHolder
import alfheim.common.block.AlfheimBlocks.livingcobble
import alfheim.common.block.AlfheimBlocks.manaInfuser
import alfheim.common.block.AlfheimBlocks.mauftriumBlock
import alfheim.common.block.AlfheimBlocks.poisonIce
import alfheim.common.block.AlfheimBlocks.redFlame
import alfheim.common.block.AlfheimBlocks.tradePortal
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule.alfheimThaumOre
import alfheim.common.item.*
import alfheim.common.item.AlfheimItems.astrolabe
import alfheim.common.item.AlfheimItems.auraRingElven
import alfheim.common.item.AlfheimItems.auraRingGod
import alfheim.common.item.AlfheimItems.cloudPendant
import alfheim.common.item.AlfheimItems.cloudPendantSuper
import alfheim.common.item.AlfheimItems.creativeReachPendant
import alfheim.common.item.AlfheimItems.dodgeRing
import alfheim.common.item.AlfheimItems.elementalBoots
import alfheim.common.item.AlfheimItems.elementalChestplate
import alfheim.common.item.AlfheimItems.elementalHelmet
import alfheim.common.item.AlfheimItems.elementalHelmetRevealing
import alfheim.common.item.AlfheimItems.elementalHelmetRevealingIsInitialized
import alfheim.common.item.AlfheimItems.elementalLeggings
import alfheim.common.item.AlfheimItems.elementiumHoe
import alfheim.common.item.AlfheimItems.elfFirePendant
import alfheim.common.item.AlfheimItems.elfIcePendant
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.elvoriumBoots
import alfheim.common.item.AlfheimItems.elvoriumChestplate
import alfheim.common.item.AlfheimItems.elvoriumHelmet
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealing
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealingIsInitialized
import alfheim.common.item.AlfheimItems.elvoriumLeggings
import alfheim.common.item.AlfheimItems.excaliber
import alfheim.common.item.AlfheimItems.flugelDisc
import alfheim.common.item.AlfheimItems.flugelSoul
import alfheim.common.item.AlfheimItems.invisibilityCloak
import alfheim.common.item.AlfheimItems.livingrockPickaxe
import alfheim.common.item.AlfheimItems.lootInterceptor
import alfheim.common.item.AlfheimItems.manaRingElven
import alfheim.common.item.AlfheimItems.manaRingGod
import alfheim.common.item.AlfheimItems.manaStone
import alfheim.common.item.AlfheimItems.manaStoneGreater
import alfheim.common.item.AlfheimItems.manasteelHoe
import alfheim.common.item.AlfheimItems.mask
import alfheim.common.item.AlfheimItems.paperBreak
import alfheim.common.item.AlfheimItems.peacePipe
import alfheim.common.item.AlfheimItems.pixieAttractor
import alfheim.common.item.AlfheimItems.realitySword
import alfheim.common.item.AlfheimItems.rodFire
import alfheim.common.item.AlfheimItems.rodGrass
import alfheim.common.item.AlfheimItems.rodIce
import cpw.mods.fml.common.Loader
import net.minecraft.block.Block
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreDictionary
import org.lwjgl.opengl.GL11
import thaumcraft.api.ThaumcraftApi
import thaumcraft.api.aspects.*
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.ModBlocks.*
import vazkii.botania.common.block.ModFluffBlocks.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.ModItems.*
import vazkii.botania.common.lib.LibEntityNames

object TCHandlerAlfheimAspects {
	
	fun addAspects() {
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 0), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("metallum"), 2).add(Aspect.getAspect("permutatio"), 2).add(Aspect.getAspect("venenum"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 7), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("vinculum"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 1), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("aer"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 2), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("ignis"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 3), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("aqua"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 4), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("terra"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 5), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("ordo"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 6), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("perditio"), 3).add(Aspect.getAspect("vitreus"), 2))
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 0), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("vitreus"), 3))    // dragonstone
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 1), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("metallum"), 3))                                                // elementium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 2), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("vitreus"), 3))                                                // quartz
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 3), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("lucrum"), 1).add(Aspect.getAspect("metallum"), 2))    // gold
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 4), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("sensus"), 3))                                                // iffesal
		ThaumcraftApi.registerObjectTag(ItemStack(livingcobble), AspectList().add(Aspect.getAspect("perditio"), 1).add(Aspect.getAspect("terra"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(elvenSand), AspectList().add(Aspect.getAspect("perditio"), 1).add(Aspect.getAspect("terra"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamLog), AspectList().add(Aspect.getAspect("arbor"), 4).add(Aspect.getAspect("herba"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamLeaves), AspectList().add(Aspect.getAspect("herba"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamSapling), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("herba"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(redFlame), AspectList().add(Aspect.getAspect("ignis"), 16).add(Aspect.getAspect("perditio"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(poisonIce), AspectList().add(Aspect.getAspect("gelum"), 16).add(Aspect.getAspect("ordo"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(manaInfuser), AspectList().add(Aspect.getAspect("fabrico"), 5).add(Aspect.getAspect("metallum"), 10).add(Aspect.getAspect("ordo"), 4).add(Aspect.getAspect("permutatio"), 4).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPortal), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("arbor"), 8).add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(tradePortal), AspectList().add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 4).add(Aspect.getAspect("terra"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumBlock), AspectList().add(Aspect.getAspect("alienis"), 6).add(Aspect.getAspect("lucrum"), 20).add(Aspect.getAspect("metallum"), 27).add(Aspect.getAspect("praecantatio"), 54))
		ThaumcraftApi.registerObjectTag(ItemStack(mauftriumBlock), AspectList().add(Aspect.getAspect("auram"), 64).add(Aspect.getAspect("alienis"), 64).add(Aspect.getAspect("lucrum"), 64).add(Aspect.getAspect("metallum"), 54).add(Aspect.getAspect("potentia"), 64).add(Aspect.getAspect("praecantatio"), 64))
		ThaumcraftApi.registerObjectTag(ItemStack(anyavil), AspectList().add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 46).add(Aspect.getAspect("praecantatio"), 52))
		ThaumcraftApi.registerObjectTag(ItemStack(animatedTorch), AspectList().add(Aspect.getAspect("motus"), 1).add(Aspect.getAspect("potentia"), 1).add(Aspect.getAspect("machina"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(itemHolder), AspectList().add(Aspect.getAspect("terra"), 4).add(Aspect.getAspect("potentia"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(anomaly), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("perditio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 0), AspectList().add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 1), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 2), AspectList().add(Aspect.getAspect("spiritus"), 2).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 6).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4).add(Aspect.getAspect("alienis"), 3))
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 0), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("lux"), 8).add(Aspect.getAspect("ordo"), 8).add(Aspect.getAspect("praecantatio"), 8))                                                // Intercore
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 1), AspectList().add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 6))                                                                                            // Infuscore
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 2), AspectList().add(Aspect.getAspect("alienis"), 1).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 8))                                                                                            // Elvorium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 3), AspectList().add(Aspect.getAspect("auram"), 20).add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("lucrum"), 20).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("praecantatio"), 50).add(Aspect.getAspect("potentia"), 16))    // Mauftrium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 4), AspectList().add(Aspect.getAspect("auram"), 10).add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("ignis"), 24))    // Fire ingot
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 5), AspectList().add(Aspect.getAspect("auram"), 10).add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("gelum"), 24))    // Ice ingot
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 6), AspectList().add(Aspect.getAspect("lucrum"), 1).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 2))                                                                                                                                        // Elv nugg
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 7), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("alienis"), 2).add(Aspect.getAspect("lucrum"), 2).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("potentia"), 2))    // Mau nugg
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 8), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("alienis"), 3).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("ignis"), 8))                                                // Fire ess
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 9), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("alienis"), 3).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("gelum"), 8))                                                // Ice ess
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 10), AspectList().add(Aspect.getAspect("auram"), 1).add(Aspect.getAspect("potentia"), 1).add(Aspect.getAspect("praecantatio"), 1))                                                                                                                                        // Iffesal dust
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 11), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("lucrum"), 6).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("terra"), 8))                                                                                            // Prim rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 12), AspectList().add(Aspect.getAspect("auram"), 3).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 2).add(Aspect.getAspect("ignis"), 12))                                                                                            // Fire rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 13), AspectList().add(Aspect.getAspect("auram"), 3).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 2).add(Aspect.getAspect("gelum"), 12))                                                                                            // Ice rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 14), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("praecantatio"), 2))                                                                                            // Twig
		ThaumcraftApi.registerObjectTag(ItemStack(elementalHelmet), AspectList().add(Aspect.getAspect("metallum"), 15).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("tutamen"), 5).add(Aspect.getAspect("aqua"), 8))
		if (elementalHelmetRevealingIsInitialized())
		ThaumcraftApi.registerObjectTag(ItemStack(elementalHelmetRevealing), AspectList().add(Aspect.getAspect("metallum"), 15).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("tutamen"), 5).add(Aspect.getAspect("aqua"), 8).add(Aspect.getAspect("sensus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalChestplate), AspectList().add(Aspect.getAspect("metallum"), 24).add(Aspect.getAspect("praecantatio"), 18).add(Aspect.getAspect("tutamen"), 8).add(Aspect.getAspect("terra"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalLeggings), AspectList().add(Aspect.getAspect("metallum"), 21).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("tutamen"), 7).add(Aspect.getAspect("ignis"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalBoots), AspectList().add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 10).add(Aspect.getAspect("tutamen"), 4).add(Aspect.getAspect("aer"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumHelmet), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		if (elvoriumHelmetRevealingIsInitialized())
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumHelmetRevealing), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumChestplate), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 56).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 24).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumLeggings), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 49).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 21).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumBoots), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 28).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 12).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(realitySword), AspectList().add(Aspect.getAspect("auram"), 54).add(Aspect.getAspect("lucrum"), 49).add(Aspect.getAspect("metallum"), 18).add(Aspect.getAspect("potentia"), 36).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("telum"), 12))
		ThaumcraftApi.registerObjectTag(ItemStack(livingrockPickaxe), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("perditio"), 2).add(Aspect.getAspect("perfodio"), 2).add(Aspect.getAspect("terra"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(manasteelHoe), AspectList().add(Aspect.getAspect("meto"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(elementiumHoe), AspectList().add(Aspect.getAspect("meto"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(rodFire), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("instrumentum"), 8).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("ignis"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(rodIce), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("instrumentum"), 8).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("gelum"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(excaliber), AspectList().add(Aspect.getAspect("motus"), 16).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("telum"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(mask), AspectList().add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("fames"), 16).add(Aspect.getAspect("sano"), 16).add(Aspect.getAspect("tenebrae"), 16).add(Aspect.getAspect("tutamen"), 16).add(Aspect.getAspect("vacuos"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(flugelSoul), AspectList().add(Aspect.getAspect("auram"), 16).add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("iter"), 16).add(Aspect.getAspect("ordo"), 16).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("spiritus"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(flugelDisc), AspectList().add(Aspect.getAspect("aer"), 4).add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("sensus"), 4).add(Aspect.getAspect("lux"), 2).add(Aspect.getAspect("auram"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(elfFirePendant), AspectList().add(Aspect.getAspect("praecantatio"), 24).add(Aspect.getAspect("auram"), 8).add(Aspect.getAspect("tutamen"), 6).add(Aspect.getAspect("ignis"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elfIcePendant), AspectList().add(Aspect.getAspect("praecantatio"), 24).add(Aspect.getAspect("auram"), 8).add(Aspect.getAspect("tutamen"), 6).add(Aspect.getAspect("gelum"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(pixieAttractor), AspectList().add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("vitreus"), 4).add(Aspect.getAspect("fames"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(creativeReachPendant), AspectList().add(Aspect.getAspect("aer"), 64).add(Aspect.getAspect("aqua"), 64).add(Aspect.getAspect("ignis"), 64).add(Aspect.getAspect("ordo"), 64).add(Aspect.getAspect("perditio"), 64).add(Aspect.getAspect("terra"), 64))
		ThaumcraftApi.registerObjectTag(ItemStack(manaStone), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 10).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaStoneGreater), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaRingElven), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 30).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaRingGod), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("metallum"), 24).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(auraRingElven), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(auraRingGod), AspectList().add(Aspect.getAspect("auram"), 16).add(Aspect.getAspect("alienis"), 4).add(Aspect.getAspect("lucrum"), 8).add(Aspect.getAspect("metallum"), 16).add(Aspect.getAspect("potentia"), 12).add(Aspect.getAspect("praecantatio"), 40))
		ThaumcraftApi.registerObjectTag(ItemStack(peacePipe), AspectList().add(Aspect.getAspect("spiritus"), 4).add(Aspect.getAspect("arbor"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(paperBreak), AspectList().add(Aspect.getAspect("spiritus"), 4).add(Aspect.getAspect("sensus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.flugelHead), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("humanus"), 4).add(Aspect.getAspect("mortuus"), 8).add(Aspect.getAspect("spiritus"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(astrolabe), AspectList().add(Aspect.getAspect("instrumentum"), 4).add(Aspect.getAspect("iter"), 2).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("praecantatio"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(invisibilityCloak), AspectList().add(Aspect.getAspect("pannus"), 6).add(Aspect.getAspect("fabrico"), 1).add(Aspect.getAspect("sensus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(dodgeRing), AspectList().add(Aspect.getAspect("vitreus"), 3).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("motus"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(lootInterceptor), AspectList().add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("fames"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(rodGrass), AspectList().add(Aspect.getAspect("herba"), 7).add(Aspect.getAspect("terra"), 3).add(Aspect.getAspect("sano"), 2).add(Aspect.getAspect("praecantatio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(cloudPendant), AspectList().add(Aspect.getAspect("tempestas"), 2).add(Aspect.getAspect("volatus"), 4).add(Aspect.getAspect("motus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(cloudPendantSuper), AspectList().add(Aspect.getAspect("tempestas"), 2).add(Aspect.getAspect("volatus"), 6).add(Aspect.getAspect("motus"), 8))
		
		// For transmutation recipe
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 7), AspectList().add(Aspect.getAspect("metallum"), 3).add(Aspect.getAspect("praecantatio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 19), AspectList().add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 1))
	}
}

/**
 * @author WireSegal
 * Created at 4:07 PM on 1/19/16.
 */
object BotaniaTCAspects {
	
	fun WildStack(i: Block): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	fun WildStack(i: Item): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	
	fun initAspects() {
	
	}
	
	fun getAspect(mod: String, tag: String): Aspect? {
		if (Loader.isModLoaded(mod)) {
			try {
				return Aspect.getAspect(tag)
			} catch (e: Exception) {
			}
		}
		return null
	}
	
	fun AspectList.a(asp: Aspect?, n: Int = 1): AspectList {
		if (asp != null)
			this.add(asp, n)
		return this
	}
	
	fun addAspects() {
		val NETHER = if (Aspect.aspects.containsKey("infernus")) getAspect("ForbiddenMagic", "infernus") else Aspect.FIRE
		val LUST: Aspect? = getAspect("ForbiddenMagic", "luxuria")
		val PRIDE: Aspect? = getAspect("ForbiddenMagic", "superbia")
		val GLUTTONY: Aspect? = getAspect("ForbiddenMagic", "gula")
		val ENVY: Aspect? = getAspect("ForbiddenMagic", "invidia")
		val WRATH: Aspect? = getAspect("ForbiddenMagic", "ira")
		val SLOTH: Aspect? = getAspect("ForbiddenMagic", "desidia")
		val COLOR = if (TCHandlerShadowFoxAspects.COLOR == null) Aspect.SENSES else TCHandlerShadowFoxAspects.COLOR
		val forbidden = Loader.isModLoaded("ForbiddenMagic")
		
		var list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(flower), list)
		
		list = AspectList().a(Aspect.EARTH, 8).a(Aspect.CRAFT, 4)
		ThaumcraftApi.registerObjectTag(WildStack(altar), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(WildStack(livingrock), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(WildStack(livingwood), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.MAGIC, 2).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(WildStack(specialFlower), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 0), list) // Spreader
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.MECHANISM)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 1), list) // Pulse
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 2), list) // Dreamwood
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.ELDRITCH).a(COLOR).a(PRIDE)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 3), list) // Gaia
		
		list = AspectList().a(Aspect.EARTH, 8).a(Aspect.MAGIC, 2).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(WildStack(pool), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.CRAFT, 4).a(Aspect.EARTH, 4)
		ThaumcraftApi.registerObjectTag(WildStack(runeAltar), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.ENTROPY, 2).add(COLOR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(unstableBlock), list)
		
		list = AspectList().a(Aspect.MAGIC, 4).a(Aspect.GREED, 4).a(Aspect.METAL, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 0), list) // Mana Pylon
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.EARTH, 2).a(Aspect.METAL, 2).a(Aspect.ELDRITCH, 2).a(Aspect.CRYSTAL, 2).a(ENVY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 1), list) // Natura Pylon
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.METAL, 2).a(Aspect.ELDRITCH, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 2), list) // Gaia Pylon
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.MECHANISM, 4).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(WildStack(pistonRelay), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MOTION, 2).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(distributor), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.ENTROPY, 2).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(manaBeacon), list)
		
		list = AspectList().a(Aspect.VOID, 6).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(manaVoid), list)
		
		list = AspectList().a(Aspect.MECHANISM, 6).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(manaDetector), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.CRAFT, 4).a(Aspect.SENSES, 2)
		ThaumcraftApi.registerObjectTag(WildStack(enchanter), list)
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.MECHANISM, 4)
		ThaumcraftApi.registerObjectTag(WildStack(turntable), list)
		
		list = AspectList().a(Aspect.EARTH, 64).a(Aspect.HUNGER, 12).a(Aspect.MAGIC, 2) // It's REALLY heavy.
		ThaumcraftApi.registerObjectTag(WildStack(ModBlocks.tinyPlanet), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.EXCHANGE, 8)
		ThaumcraftApi.registerObjectTag(WildStack(alchemyCatalyst), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(WildStack(openCrate), list)
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.MECHANISM, 2)
		ThaumcraftApi.registerObjectTag(WildStack(forestEye), list)
		
		list = AspectList().add(Aspect.HARVEST, 12)
		ThaumcraftApi.registerObjectTag(WildStack(forestDrum), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(COLOR).a(Aspect.LIGHT)
		ThaumcraftApi.registerObjectTag(WildStack(shinyFlower), list)
		
		list = AspectList().a(Aspect.TREE, 2).a(Aspect.SOUL, 2).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(ItemStack(platform, 1, 0), list)
		ThaumcraftApi.registerObjectTag(ItemStack(platform, 1, 1), list)
		// Nothing for the Infrangible Platform.
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.MECHANISM, 4).a(Aspect.TRAVEL, 4)
		ThaumcraftApi.registerObjectTag(WildStack(alfPortal), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(WildStack(dreamwood), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.ORDER, 8).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(WildStack(conjurationCatalyst), list)
		
		list = AspectList().a(COLOR, 8)
		ThaumcraftApi.registerObjectTag(WildStack(bifrost), list)
		
		list = AspectList().a(Aspect.PLANT, 1)
		ThaumcraftApi.registerObjectTag(WildStack(solidVines), list)
		
		list = AspectList().a(Aspect.PLANT, 1).a(COLOR, 1)
		ThaumcraftApi.registerObjectTag(WildStack(buriedPetals), list)
		
		list = AspectList().a(Aspect.WATER, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(WildStack(prismarine), list)
		
		list = AspectList().a(Aspect.WATER, 2).a(Aspect.CRYSTAL, 2).a(Aspect.LIGHT, 2)
		ThaumcraftApi.registerObjectTag(WildStack(seaLamp), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(Aspect.LIGHT).a(Aspect.AIR).a(Aspect.EARTH)
		ThaumcraftApi.registerObjectTag(WildStack(floatingFlower), list)
		
		list = AspectList().a(Aspect.LIFE, 2).a(Aspect.CROP, 2)
		ThaumcraftApi.registerObjectTag(WildStack(tinyPotato), list)
		
		list = AspectList().a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(WildStack(spawnerClaw), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(NETHER)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 0), list) // Hellish Brick
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.SOUL)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 1), list) // Soul Brick
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.COLD)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 2), list) // Frosty Brick
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.MECHANISM, 2).a(ENVY, 2)
		ThaumcraftApi.registerObjectTag(WildStack(enderEye), list)
		
		list = AspectList().a(Aspect.LIGHT, 8).a(Aspect.METAL, 4)
		ThaumcraftApi.registerObjectTag(WildStack(starfield), list)
		
		list = AspectList().a(Aspect.MECHANISM, 16).a(Aspect.ENERGY, 8).a(SLOTH, 8).a(GLUTTONY, 4) // If you can't tell, I don't like RF.
		ThaumcraftApi.registerObjectTag(WildStack(rfGenerator), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(WildStack(elfGlass), list)
		
		list = AspectList(ItemStack(Blocks.brewing_stand)).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(brewery), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(WildStack(manaGlass), list)
		
		list = AspectList().a(Aspect.CRAFT, 4).a(Aspect.FIRE, 2).a(Aspect.WATER, 2).a(Aspect.EARTH, 2).a(Aspect.AIR, 2).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(terraPlate), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(WildStack(redStringContainer), list)
		ThaumcraftApi.registerObjectTag(WildStack(redStringDispenser), list)
		ThaumcraftApi.registerObjectTag(WildStack(redStringFertilizer), list)
		ThaumcraftApi.registerObjectTag(WildStack(redStringComparator), list)
		ThaumcraftApi.registerObjectTag(WildStack(redStringRelay), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.MAGIC, 2).a(Aspect.LIFE).a(COLOR).a(Aspect.LIGHT).a(Aspect.AIR).a(Aspect.EARTH) // So many aspects
		ThaumcraftApi.registerObjectTag(WildStack(floatingSpecialFlower), list)
		
		list = AspectList().a(Aspect.LIGHT, 2).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(manaFlame), list)
		
		list = AspectList().a(Aspect.SOUL, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(WildStack(prism), list)
		
		list = AspectList(ItemStack(Blocks.dirt))
		ThaumcraftApi.registerObjectTag(WildStack(dirtPath), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(WildStack(enchantedSoil), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(WildStack(corporeaIndex), list)
		ThaumcraftApi.registerObjectTag(WildStack(corporeaFunnel), list)
		
		list = AspectList(ItemStack(Blocks.brown_mushroom)).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(mushroom), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.MOTION, 2).a(Aspect.MAGIC, 2).a(Aspect.WATER, 2).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(WildStack(pump), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(doubleFlower1), list)
		ThaumcraftApi.registerObjectTag(WildStack(doubleFlower2), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(WildStack(corporeaInterceptor), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2).a(Aspect.SENSES)
		ThaumcraftApi.registerObjectTag(WildStack(corporeaCrystalCube), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.FIRE, 2)
		ThaumcraftApi.registerObjectTag(WildStack(incensePlate), list)
		
		list = AspectList(ItemStack(Items.clock)).a(Aspect.CRYSTAL).a(Aspect.MECHANISM)
		ThaumcraftApi.registerObjectTag(WildStack(hourglass), list)
		
		list = AspectList(ItemStack(Blocks.rail)).a(Aspect.SOUL)
		ThaumcraftApi.registerObjectTag(WildStack(ghostRail), list)
		
		list = AspectList().a(Aspect.MECHANISM, 3).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(WildStack(sparkChanger), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(WildStack(root), list)
		
		list = AspectList(ItemStack(Blocks.pumpkin)).a(Aspect.DARKNESS, 2)
		ThaumcraftApi.registerObjectTag(WildStack(felPumpkin), list)
		
		list = AspectList().a(Aspect.LIFE, 4).a(Aspect.BEAST, 8)
		ThaumcraftApi.registerObjectTag(WildStack(cocoon), list)
		
		list = AspectList().a(Aspect.TRAVEL, 3).a(Aspect.MOTION, 2).a(Aspect.LIGHT, 2)
		ThaumcraftApi.registerObjectTag(WildStack(lightRelay), list)
		
		list = AspectList().a(Aspect.TRAVEL, 3).a(Aspect.MECHANISM, 2)
		ThaumcraftApi.registerObjectTag(WildStack(lightLauncher), list)
		
		list = AspectList().a(Aspect.ENTROPY, 16).a(WRATH, 8).a(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(manaBomb), list)
		
		list = AspectList(ItemStack(Blocks.noteblock)).a(Aspect.GREED, 2).a(Aspect.AIR, 3)
		ThaumcraftApi.registerObjectTag(WildStack(ModBlocks.cacophonium), list)
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.AIR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(bellows), list)
		
		list = AspectList().a(COLOR, 8)
		ThaumcraftApi.registerObjectTag(ItemStack(bifrostPerm), list)
		
		list = AspectList().a(Aspect.PLANT, 4).a(Aspect.LIFE, 4)
		ThaumcraftApi.registerObjectTag(WildStack(cellBlock), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(WildStack(redStringInterceptor), list)
		
		list = AspectList(ItemStack(Items.skull, 1, 3)).a(Aspect.ELDRITCH, 4).a(Aspect.EARTH, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ModBlocks.gaiaHead), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2).a(Aspect.MIND, 2)
		ThaumcraftApi.registerObjectTag(WildStack(corporeaRetainer), list)
		
		list = AspectList().a(Aspect.WEATHER, 2)
		ThaumcraftApi.registerObjectTag(WildStack(teruTeruBozu), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(shimmerrock), list)
		
		list = AspectList().a(Aspect.TREE).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(shimmerwoodPlanks), list)
		
		list = AspectList().a(Aspect.MAN, 4).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(avatar), list)
		
		list = AspectList().a(Aspect.EARTH).a(Aspect.PLANT).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(altGrass), list)
		
		list = AspectList(ItemStack(Blocks.quartz_block))
		if (darkQuartz != null)
			ThaumcraftApi.registerObjectTag(WildStack(darkQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(manaQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(blazeQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(lavenderQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(redQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(elfQuartz), list)
		ThaumcraftApi.registerObjectTag(WildStack(sunnyQuartz), list)
		
		list = AspectList().a(Aspect.EARTH, 2)
		ThaumcraftApi.registerObjectTag(WildStack(biomeStoneA), list)
		ThaumcraftApi.registerObjectTag(WildStack(biomeStoneB), list)
		ThaumcraftApi.registerObjectTag(WildStack(stone), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(pavement), list)
		
		
		
		
		
		
		/////// ITEMS!
		
		list = AspectList(ItemStack(Items.book)).a(Aspect.PLANT)
		ThaumcraftApi.registerObjectTag(WildStack(lexicon), list)
		
		list = AspectList().a(Aspect.PLANT).a(COLOR)
		ThaumcraftApi.registerObjectTag(WildStack(petal), list)
		ThaumcraftApi.registerObjectTag(WildStack(dye), list)
		
		list = AspectList(ItemStack(twigWand)).a(Aspect.TOOL, 3)
		ThaumcraftApi.registerObjectTag(WildStack(twigWand), list)
		
		list = AspectList().a(Aspect.METAL, 4).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 0), list) // Manasteel
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.MAGIC, 6).a(Aspect.TRAVEL, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 1), list) // Manapearl
		
		list = AspectList().a(Aspect.CRYSTAL, 4).a(Aspect.GREED, 4).a(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 2), list) // Manadiamond
		
		list = AspectList().a(Aspect.METAL, 4).a(Aspect.MAGIC, 2).a(Aspect.EARTH, 2).a(Aspect.ELDRITCH, 2).a(Aspect.GREED, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 4), list) // Terrasteel
		
		list = AspectList().a(Aspect.ELDRITCH, 16).a(PRIDE, 8).a(Aspect.LIFE, 4).a(COLOR, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 5), list) // Gaia Spirit
		
		list = AspectList().a(Aspect.METAL, 4).a(Aspect.ELDRITCH, 1).a(Aspect.MAGIC, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 7), list) // Elementium
		
		list = AspectList().a(Aspect.ELDRITCH, 6).a(Aspect.MAGIC, 6)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 8), list) // Pixie Dust
		
		list = AspectList().a(Aspect.CRYSTAL, 4).a(Aspect.GREED, 4).a(Aspect.MAGIC, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 9), list) // Dragonstone
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.WATER)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 10), list) // Prismarine
		
		list = AspectList().a(Aspect.CRAFT)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 11), list) // Crafting Placeholder
		
		list = AspectList().a(Aspect.ELDRITCH, 2).a(Aspect.CLOTH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 12), list) // Red String
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 15), list) // Ender Air
		
		list = AspectList().a(Aspect.CLOTH).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 16), list) // Mana String
		
		list = AspectList().a(Aspect.METAL)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 17), list) // Manasteel Nugget
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 18), list) // Terrasteel Nugget
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 19), list) // Elementium Nugget
		
		list = AspectList().a(Aspect.TREE)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 20), list) // Livingroot
		
		list = AspectList().a(Aspect.EARTH)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 21), list) // Pebble
		
		list = AspectList().a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 23), list) // Mana Powder
		
		list = AspectList().a(Aspect.WATER, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 0), list) // Rune of Water
		
		list = AspectList().a(Aspect.FIRE, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 1), list) // Rune of Fire
		
		list = AspectList().a(Aspect.EARTH, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 2), list) // Rune of Earth
		
		list = AspectList().a(Aspect.AIR, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 3), list) // Rune of Air
		
		list = AspectList().a(Aspect.LIFE, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 4), list) // Rune of Spring
		
		list = AspectList().a(Aspect.CROP, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 5), list) // Rune of Summer
		
		list = AspectList().a(Aspect.HARVEST, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 6), list) // Rune of Autumn
		
		list = AspectList().a(Aspect.COLD, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 7), list) // Rune of Winter
		
		list = AspectList().a(Aspect.AURA, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 8), list) // Rune of Mana
		
		list = if (forbidden) AspectList().a(LUST, 16)
		else AspectList().a(Aspect.FLESH, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 9), list) // Rune of Lust
		
		list = if (forbidden) AspectList().a(GLUTTONY, 16)
		else AspectList().a(Aspect.HUNGER, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 10), list) // Rune of Gluttony
		
		list = AspectList().a(Aspect.GREED, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 11), list) // Rune of Greed
		
		list = if (forbidden) AspectList().a(SLOTH, 16)
		else AspectList().a(Aspect.CLOTH, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 12), list) // Rune of Sloth
		
		list = if (forbidden) AspectList().a(WRATH, 16)
		else AspectList().a(Aspect.WEAPON, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 13), list) // Rune of Wrath
		
		list = if (forbidden) AspectList().a(ENVY, 16)
		else AspectList().a(Aspect.TRAP, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 14), list) // Rune of Envy
		
		list = if (forbidden) AspectList().a(PRIDE, 16)
		else AspectList().a(COLOR, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(rune, 1, 15), list) // Rune of Pride
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(WildStack(manaTablet), list)
		
		list = AspectList().a(Aspect.HUNGER, 64).a(Aspect.CROP, 64) // why did i do this
		ThaumcraftApi.registerObjectTag(WildStack(manaCookie), list)
		
		list = AspectList().a(Aspect.PLANT).a(Aspect.EXCHANGE)
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 0), list) // grass
		
		list = AspectList().a(Aspect.EARTH).a(Aspect.EXCHANGE)
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 1), list) // podzol
		
		list = AspectList().a(Aspect.DARKNESS).a(Aspect.EXCHANGE)
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 2), list) // mycelium
		
		list = AspectList().a(Aspect.PLANT).a(Aspect.EXCHANGE).a(COLOR)
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 3), list) // dry
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 4), list) // golden
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 5), list) // vivid
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 6), list) // scorched
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 7), list) // infused
		ThaumcraftApi.registerObjectTag(ItemStack(grassSeeds, 1, 8), list) // mutated
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8).a(Aspect.ELDRITCH, 8)
		ThaumcraftApi.registerObjectTag(WildStack(manaMirror), list)
		
		list = AspectList(ItemStack(manasteelHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(WildStack(manasteelHelmRevealing), list)
		
		list = AspectList(ItemStack(terrasteelHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(WildStack(terrasteelHelmRevealing), list)
		
		list = AspectList().a(Aspect.EARTH, 12).a(Aspect.HUNGER, 12).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(ModItems.tinyPlanet), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8).a(Aspect.METAL, 8)
		ThaumcraftApi.registerObjectTag(WildStack(manaRing), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8).a(Aspect.METAL, 8)
		ThaumcraftApi.registerObjectTag(WildStack(manaRingGreater), list)
		
		list = AspectList().a(Aspect.CLOTH, 4).a(Aspect.TRAVEL, 8)
		ThaumcraftApi.registerObjectTag(WildStack(travelBelt), list)
		
		list = AspectList(ItemStack(Items.quartz))
		ThaumcraftApi.registerObjectTag(WildStack(quartz), list)
		
		list = AspectList(ItemStack(elementiumHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(WildStack(elementiumHelmRevealing), list)
		
		list = AspectList().a(Aspect.METAL, 4).a(Aspect.VOID, 8)
		ThaumcraftApi.registerObjectTag(WildStack(openBucket), list)
		
		list = AspectList().a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8).a(Aspect.TRAVEL, 4)
		ThaumcraftApi.registerObjectTag(WildStack(spawnerMover), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.ENTROPY, 8).a(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(manaBottle), list)
		
		list = AspectList().a(Aspect.METAL, 15).a(Aspect.GREED, 3).a(Aspect.SENSES, 3)
		ThaumcraftApi.registerObjectTag(WildStack(itemFinder), list)
		
		list = AspectList().a(Aspect.FLIGHT).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(WildStack(manaInkwell), list)
		
		list = AspectList().a(Aspect.CRYSTAL)
		ThaumcraftApi.registerObjectTag(WildStack(vial), list)
		ThaumcraftApi.registerObjectTag(WildStack(brewVial), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(WildStack(brewFlask), list)
		
		list = AspectList().a(Aspect.CRAFT, 4).a(Aspect.ELDRITCH, 4)
		ThaumcraftApi.registerObjectTag(WildStack(craftingHalo), list)
		
		list = AspectList().a(Aspect.MAGIC, 16).a(Aspect.ELDRITCH, 4).a(Aspect.ORDER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(blackLotus, 1, 0), list)
		
		list = AspectList().a(Aspect.MAGIC, 20).a(Aspect.ELDRITCH, 8).a(Aspect.ORDER, 6)
		ThaumcraftApi.registerObjectTag(ItemStack(blackLotus, 1, 1), list)
		
		list = AspectList().a(Aspect.SENSES, 8).a(Aspect.CRYSTAL, 4)
		ThaumcraftApi.registerObjectTag(WildStack(monocle), list)
		
		list = AspectList().a(Aspect.TRAVEL, 16)
		ThaumcraftApi.registerObjectTag(WildStack(worldSeed), list)
		
		list = AspectList().a(Aspect.PLANT, 8).a(Aspect.METAL, 4).a(Aspect.WEAPON, 4).a(WRATH, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(thornChakram, 1, 0), list)
		
		list = AspectList().a(Aspect.PLANT, 8).a(Aspect.METAL, 4).a(Aspect.WEAPON, 4).a(WRATH, 4).a(Aspect.FIRE, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(thornChakram, 1, 1), list)
		
		list = AspectList().a(Aspect.LIFE, 32).a(Aspect.MAGIC, 16)
		ThaumcraftApi.registerObjectTag(WildStack(overgrowthSeed), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 64).a(Aspect.MIND, 32).a(Aspect.SOUL, 32)
		ThaumcraftApi.registerObjectTag(WildStack(ancientWill), list)
		
		list = AspectList().a(Aspect.VOID)
		ThaumcraftApi.registerObjectTag(WildStack(phantomInk), list)
		
		list = AspectList().a(COLOR, 32).a(Aspect.LIFE, 16).a(Aspect.HEAL, 16)
		ThaumcraftApi.registerObjectTag(WildStack(pinkinator), list)
		
		list = AspectList().a(Aspect.VOID, 64)
		ThaumcraftApi.registerObjectTag(WildStack(blackHoleTalisman), list)
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.AIR, 4).a(Aspect.GREED, 4).a(COLOR, 2).a(PRIDE, 4)
		ThaumcraftApi.registerObjectTag(WildStack(recordGaia1), list)
		ThaumcraftApi.registerObjectTag(WildStack(recordGaia2), list)
		
		list = AspectList(ItemStack(terraAxe)).a(Aspect.TOOL, 3)
		ThaumcraftApi.registerObjectTag(WildStack(terraAxe), list)
		
		list = AspectList().a(Aspect.TREE).a(Aspect.WATER)
		ThaumcraftApi.registerObjectTag(WildStack(waterBowl), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 16).a(Aspect.WEAPON, 4)
		ThaumcraftApi.registerObjectTag(WildStack(starSword), list)
		
		ThaumcraftApi.registerObjectTag(WildStack(exchangeRod), list)
		
		list = AspectList().a(Aspect.WEATHER, 16).a(Aspect.WEAPON, 4)
		ThaumcraftApi.registerObjectTag(WildStack(thunderSword), list)
		
		list = AspectList(ItemStack(ModBlocks.gaiaHead))
		ThaumcraftApi.registerObjectTag(WildStack(ModItems.gaiaHead), list)
		
		
		
		
		/// ENTITIES!
		
		list = AspectList().a(Aspect.MAGIC, 3).a(Aspect.AURA, 3)
		ThaumcraftApi.registerEntityTag(LibEntityNames.MANA_BURST, list)
		
		list = AspectList().a(Aspect.MAGIC, 3).a(Aspect.LIGHT, 3).a(COLOR, 3)
		ThaumcraftApi.registerEntityTag(LibEntityNames.SIGNAL_FLARE, list)
		
		list = AspectList().a(Aspect.LIGHT, 2).a(Aspect.FLIGHT, 2).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerEntityTag(LibEntityNames.PIXIE, list)
		
		list = AspectList().a(Aspect.FIRE, 8)
		ThaumcraftApi.registerEntityTag(LibEntityNames.FLAME_RING, list)
		
		list = AspectList().a(Aspect.PLANT, 7)
		ThaumcraftApi.registerEntityTag(LibEntityNames.VINE_BALL, list)
		
		list = AspectList().a(Aspect.MAN, 16).a(Aspect.EARTH, 8).a(Aspect.ELDRITCH, 8).a(Aspect.DARKNESS, 4)
		ThaumcraftApi.registerEntityTag(LibEntityNames.DOPPLEGANGER, list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.TRAP, 2).a(Aspect.POISON, 2)
		ThaumcraftApi.registerEntityTag(LibEntityNames.MAGIC_LANDMINE, list)
		
		list = AspectList(ItemStack(spark))
		ThaumcraftApi.registerEntityTag(LibEntityNames.SPARK, list)
		
		list = AspectList().a(Aspect.MOTION, 4)
		ThaumcraftApi.registerEntityTag(LibEntityNames.THROWN_ITEM, list)
		
		list = AspectList().a(Aspect.WEAPON, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerEntityTag(LibEntityNames.MAGIC_MISSILE, list)
		
		list = AspectList(ItemStack(thornChakram))
		ThaumcraftApi.registerEntityTag(LibEntityNames.THORN_CHAKRAM, list)
		
		list = AspectList(ItemStack(corporeaSpark))
		ThaumcraftApi.registerEntityTag(LibEntityNames.CORPOREA_SPARK, list)
		
		list = AspectList(ItemStack(manaResource, 1, 15))
		ThaumcraftApi.registerEntityTag(LibEntityNames.ENDER_AIR_BOTTLE, list)
		
		list = AspectList(ItemStack(poolMinecart))
		ThaumcraftApi.registerEntityTag(LibEntityNames.POOL_MINECART, list)
		
		list = AspectList().a(Aspect.UNDEAD, 20).a(Aspect.ORDER, 20).a(Aspect.FIRE, 15).a(Aspect.HEAL, 15)
		ThaumcraftApi.registerEntityTag(LibEntityNames.PINK_WITHER, list)
		
		list = AspectList().a(Aspect.MOTION, 4).a(Aspect.TRAVEL, 4)
		ThaumcraftApi.registerEntityTag(LibEntityNames.PLAYER_MOVER, list)
		
		list = AspectList().a(Aspect.ENTROPY, 32).a(WRATH, 16)
		ThaumcraftApi.registerEntityTag(LibEntityNames.MANA_STORM, list)
		
		list = AspectList().a(Aspect.ELDRITCH, 16).a(Aspect.WEAPON, 16)
		ThaumcraftApi.registerEntityTag(LibEntityNames.BABYLON_WEAPON, list)
		
		list = AspectList().a(Aspect.ELDRITCH, 8)
		ThaumcraftApi.registerEntityTag(LibEntityNames.FALLING_STAR, list)
	}
}

object TCHandlerShadowFoxAspects {
	
	class RainbowAspect(name: String, components: Array<Aspect>, texture: ResourceLocation, blend: Int): Aspect(name, 0xFFFFFF, components, texture, blend) {
		override fun getColor(): Int = ItemIridescent.rainbowColor()
	}
	
	fun WildStack(i: Block): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	fun WildStack(i: Item): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	
	fun forMeta(cap: Int, lambda: (Int) -> Unit) {
		for (i in 0..cap) {
			lambda.invoke(i)
		}
	}
	
	var COLOR: Aspect? = null
	
	fun initAspects() {
		if (AlfheimConfig.addAspectsToBotania)
			BotaniaTCAspects.initAspects()
		
		if (AlfheimConfig.addTincturemAspect)
			COLOR = RainbowAspect("tincturem", arrayOf(Aspect.LIGHT, Aspect.ORDER), ResourceLocation(ModInfo.MODID, "textures/misc/tincturem.png"), GL11.GL_ONE_MINUS_SRC_ALPHA)
	}
	
	fun getAspect(mod: String, tag: String): Aspect? {
		if (Loader.isModLoaded(mod)) {
			try {
				return Aspect.getAspect(tag)
			} catch (e: Exception) {
			}
		}
		return null
	}
	
	fun replaceAspect(stack: ItemStack, a1: Aspect, a2: Aspect) {
		val list = AspectList(stack)
		val amount = list.getAmount(a1)
		list.remove(a1)
		list.add(a2, amount)
		ThaumcraftApi.registerObjectTag(stack, list)
	}
	
	fun replaceAspect(key: String, a1: Aspect, a2: Aspect) {
		val list = AspectList(OreDictionary.getOres(key)[0])
		val amount = list.getAmount(a1)
		list.remove(a1)
		list.add(a2, amount)
		ThaumcraftApi.registerObjectTag(key, list)
	}
	
	fun addAspects() {
		if (AlfheimConfig.addAspectsToBotania)
			BotaniaTCAspects.addAspects()
		if (AlfheimConfig.addTincturemAspect)
			overrideVanillaAspects()
		
		val NETHER: Aspect? = getAspect("ForbiddenMagic", "infernus")
		val PRIDE: Aspect? = getAspect("ForbiddenMagic", "superbia")
		val WRATH: Aspect? = getAspect("ForbiddenMagic", "ira")
		val SLOTH: Aspect? = getAspect("ForbiddenMagic", "desidia")
		val forbidden = Loader.isModLoaded("ForbiddenMagic")
		val hellAspect = if (forbidden) NETHER else Aspect.FIRE
		val colorAspect = if (AlfheimConfig.addTincturemAspect) COLOR else Aspect.SENSES
		
		val splinterlist = AspectList().add(Aspect.TREE, 1).add(Aspect.ENTROPY, 1)
		
		var list = AspectList().add(Aspect.EARTH, 2).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.coloredDirtBlock), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.rainbowDirtBlock), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisSapling), list)
		
		list = AspectList().add(Aspect.PLANT, 1).add(Aspect.AIR, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.irisGrass), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.rainbowGrass), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.irisTallGrass0), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.irisTallGrass1), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.rainbowTallGrass), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(colorAspect, 1)
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisWood0, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisWood1, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisWood2, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisWood3, 1, it), list) }
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.rainbowWood), list)
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.altWood0, 1, it), list) }
		forMeta(2) { ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.altWood1, 1, it), list) }
		
		list = AspectList().add(Aspect.TREE, 1).add(Aspect.METAL, 1)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.itemDisplay), list)
		
		list = AspectList().add(Aspect.TREE, 8).add(Aspect.MAGIC, 8).add(colorAspect, 2).add(Aspect.CRAFT, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.treeCrafterBlock), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.treeCrafterBlockRB), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(Aspect.WEATHER, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.lightningSapling), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(Aspect.WEATHER, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.lightningWood), list)
		
		list = AspectList().add(Aspect.TREE, 20).add(Aspect.MECHANISM, 1).add(Aspect.EXCHANGE, 1).add(Aspect.VOID, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.livingwoodFunnel), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(hellAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.netherSapling), list)
		list = AspectList().add(Aspect.TREE, 4).add(hellAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.netherWood), list)
		
		list = AspectList().add(Aspect.CLOTH, 4).add(Aspect.FIRE, 2).add(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.kindling), list)
		
		list = AspectList().add(Aspect.PLANT, 1).add(Aspect.EXCHANGE, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.irisSeeds), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.EARTH, 4).add(Aspect.AIR, 2).add(colorAspect, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.colorfulSkyDirtRod), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.LIGHT, 6).add(colorAspect, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.prismaticRod), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.WEATHER, 8).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.lightningRod), list)
		
		list = if (forbidden) AspectList().add(Aspect.TOOL, 8).add(Aspect.AIR, 4).add(Aspect.WEATHER, 2).add(PRIDE, 2).add(Aspect.MAGIC, 4)
		else AspectList().add(Aspect.TOOL, 8).add(Aspect.AIR, 6).add(Aspect.WEATHER, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.interdictionRod), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.WEATHER, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.emblem, 1, 0), list)
		list = AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.EARTH, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.emblem, 1, 1), list)
		list = if (forbidden) AspectList().add(Aspect.ELDRITCH, 5).add(PRIDE, 16)
		else AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.AIR, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.emblem, 1, 2), list)
		list = AspectList().add(Aspect.ELDRITCH, 5).add(hellAspect, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.emblem, 1, 3), list)
		
		list = AspectList().add(Aspect.CLOTH, 4).add(colorAspect, 2)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.coatOfArms), list)
		
		list = AspectList().add(colorAspect, 8).add(Aspect.METAL, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.colorOverride), list)
		
		list = AspectList().add(Aspect.CLOTH, 2).add(Aspect.GREED, 2).add(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.attributionBauble, 1, 0), list)
		
		list = AspectList().add(Aspect.CLOTH, 2).add(Aspect.GREED, 2).add(Aspect.ELDRITCH, 2).add(Aspect.CROP, 64).add(Aspect.BEAST, 64) // memes
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.attributionBauble, 1, 1), list)
		
		list = AspectList().add(Aspect.MAGIC, 16).add(Aspect.TAINT, 4).add(Aspect.ENTROPY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.wiltedLotus, 1, 0), list)
		
		list = AspectList().add(Aspect.MAGIC, 20).add(Aspect.DEATH, 4).add(Aspect.ELDRITCH, 4).add(Aspect.TAINT, 4).add(Aspect.ENTROPY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.wiltedLotus, 1, 1), list)
		
		list = AspectList().add(Aspect.TOOL, 2).add(Aspect.TREE, 2).add(Aspect.WEATHER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 0), list)
		
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 1), splinterlist)
		
		list = AspectList().add(Aspect.TOOL, 2).add(Aspect.TREE, 2).add(hellAspect, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 2), list)
		
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 3), splinterlist)
		
		list = AspectList().add(Aspect.FIRE, 4).add(Aspect.ENERGY, 2)
		if (forbidden) list.add(NETHER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 4), list)
		
		list = AspectList().add(Aspect.TOOL, 3).add(Aspect.VOID, 12).add(Aspect.CLOTH, 4).add(Aspect.GREED, 2)
		if (forbidden) list.add(SLOTH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.toolbelt), list)
		
		list = AspectList(ItemStack(manaFlame))
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.rainbowFlame), list)
		
		list = AspectList(ItemStack(manaFlame)).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.invisibleFlame), list)
		
		list = AspectList(ItemStack(lens, 1, 17)).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.invisibleFlameLens), list)
		
		list = AspectList().add(Aspect.LIGHT, 4).add(Aspect.MECHANISM, 2).add(colorAspect, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.irisLamp), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.sealingWood), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxBlocks.sealingSapling), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 2).add(Aspect.LIGHT, 2).add(COLOR, 2)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.star), list)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.star), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.FIRE, 4).add(hellAspect, 4)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.flameRod), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 9).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 10).add(Aspect.ENTROPY, 5)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.wireAxe), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 10).add(Aspect.WEAPON, 1).add(Aspect.ORDER, 5)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.trisDagger), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 10).add(Aspect.WEAPON, 1).add(Aspect.ORDER, 5)
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxItems.trisDagger), list)
		
		list = AspectList(ItemStack(Blocks.quartz_block))
		ThaumcraftApi.registerObjectTag(WildStack(ShadowFoxBlocks.shimmerQuartz), list)
		
		list = AspectList(ItemStack(Items.quartz))
		ThaumcraftApi.registerObjectTag(ItemStack(ShadowFoxItems.resource, 1, 5), list)
		
		list = AspectList().add(COLOR, 1)
		ThaumcraftApi.registerObjectTag(LibOreDict.DYES[16], list)
		
		list = AspectList().add(Aspect.TAINT, 2).add(Aspect.ENTROPY, 2).add(Aspect.PLANT, 2)
		if (forbidden) list.add(WRATH, 2)
		ThaumcraftApi.registerEntityTag("${ModInfo.MODID}:voidCreeper", list)
		
		list = AspectList().add(Aspect.ENTROPY, 16).add(Aspect.PLANT, 2) // don't fool around with these guys
		if (forbidden) list.add(WRATH, 8)                                // they will mess. you. up.
		ThaumcraftApi.registerEntityTag("${ModInfo.MODID}:grieferCreeper", list)
	}
	
	fun overrideVanillaAspects() {
		if (COLOR != null) {
			replaceAspect(WildStack(Blocks.stained_hardened_clay), Aspect.SENSES, COLOR!!)
			replaceAspect(WildStack(Blocks.red_flower), Aspect.SENSES, COLOR!!)
			replaceAspect(WildStack(Blocks.yellow_flower), Aspect.SENSES, COLOR!!)
			
			for (i in LibOreDict.DYES)
				replaceAspect(i, Aspect.SENSES, COLOR!!)
			
			replaceAspect("oreLapis", Aspect.SENSES, COLOR!!)
		}
	}
}