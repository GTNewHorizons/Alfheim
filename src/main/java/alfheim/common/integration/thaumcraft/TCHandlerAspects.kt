@file:Suppress("LocalVariableName")

package alfheim.common.integration.thaumcraft

import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.AlfheimBlocks.alfStorage
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
import alfheim.common.block.AlfheimBlocks.livingcobble
import alfheim.common.block.AlfheimBlocks.manaAccelerator
import alfheim.common.block.AlfheimBlocks.manaInfuser
import alfheim.common.block.AlfheimBlocks.poisonIce
import alfheim.common.block.AlfheimBlocks.redFlame
import alfheim.common.block.AlfheimBlocks.tradePortal
import alfheim.common.core.handler.AlfheimConfigHandler
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
import alfheim.common.item.AlfheimItems.elementalLeggings
import alfheim.common.item.AlfheimItems.elementiumHoe
import alfheim.common.item.AlfheimItems.elfFirePendant
import alfheim.common.item.AlfheimItems.elfIcePendant
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.elvoriumBoots
import alfheim.common.item.AlfheimItems.elvoriumChestplate
import alfheim.common.item.AlfheimItems.elvoriumHelmet
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealing
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
import alfheim.common.item.material.ElvenResourcesMetas
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
		ThaumcraftApi.registerObjectTag(ItemStack(alfStorage, 1, 0), AspectList().add(Aspect.getAspect("alienis"), 6).add(Aspect.getAspect("lucrum"), 20).add(Aspect.getAspect("metallum"), 27).add(Aspect.getAspect("praecantatio"), 54))
		ThaumcraftApi.registerObjectTag(ItemStack(alfStorage, 1, 1), AspectList().add(Aspect.getAspect("auram"), 64).add(Aspect.getAspect("alienis"), 64).add(Aspect.getAspect("lucrum"), 64).add(Aspect.getAspect("metallum"), 54).add(Aspect.getAspect("potentia"), 64).add(Aspect.getAspect("praecantatio"), 64))
		ThaumcraftApi.registerObjectTag(ItemStack(anyavil), AspectList().add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 46).add(Aspect.getAspect("praecantatio"), 52))
		ThaumcraftApi.registerObjectTag(ItemStack(animatedTorch), AspectList().add(Aspect.getAspect("motus"), 1).add(Aspect.getAspect("potentia"), 1).add(Aspect.getAspect("machina"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(manaAccelerator), AspectList().add(Aspect.getAspect("terra"), 4).add(Aspect.getAspect("potentia"), 2))
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

		elementalHelmetRevealing?.let {
			ThaumcraftApi.registerObjectTag(ItemStack(it), AspectList().add(Aspect.getAspect("metallum"), 15).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("tutamen"), 5).add(Aspect.getAspect("aqua"), 8).add(Aspect.getAspect("sensus"), 4))
		}

		ThaumcraftApi.registerObjectTag(ItemStack(elementalChestplate), AspectList().add(Aspect.getAspect("metallum"), 24).add(Aspect.getAspect("praecantatio"), 18).add(Aspect.getAspect("tutamen"), 8).add(Aspect.getAspect("terra"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalLeggings), AspectList().add(Aspect.getAspect("metallum"), 21).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("tutamen"), 7).add(Aspect.getAspect("ignis"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalBoots), AspectList().add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 10).add(Aspect.getAspect("tutamen"), 4).add(Aspect.getAspect("aer"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumHelmet), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))

		elvoriumHelmetRevealing?.let {
			ThaumcraftApi.registerObjectTag(ItemStack(it), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		}

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
	
	fun wildStack(i: Block): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	fun wildStack(i: Item): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	
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
		ThaumcraftApi.registerObjectTag(wildStack(flower), list)
		
		list = AspectList().a(Aspect.EARTH, 8).a(Aspect.CRAFT, 4)
		ThaumcraftApi.registerObjectTag(wildStack(altar), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(wildStack(livingrock), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(wildStack(livingwood), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.MAGIC, 2).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(wildStack(specialFlower), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 0), list) // Spreader
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.MECHANISM)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 1), list) // Pulse
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 2), list) // Dreamwood
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.MOTION, 2).a(Aspect.ELDRITCH).a(COLOR).a(PRIDE)
		ThaumcraftApi.registerObjectTag(ItemStack(spreader, 1, 3), list) // Gaia
		
		list = AspectList().a(Aspect.EARTH, 8).a(Aspect.MAGIC, 2).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(wildStack(pool), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.CRAFT, 4).a(Aspect.EARTH, 4)
		ThaumcraftApi.registerObjectTag(wildStack(runeAltar), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.ENTROPY, 2).add(COLOR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(unstableBlock), list)
		
		list = AspectList().a(Aspect.MAGIC, 4).a(Aspect.GREED, 4).a(Aspect.METAL, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 0), list) // Mana Pylon
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.EARTH, 2).a(Aspect.METAL, 2).a(Aspect.ELDRITCH, 2).a(Aspect.CRYSTAL, 2).a(ENVY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 1), list) // Natura Pylon
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.METAL, 2).a(Aspect.ELDRITCH, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(pylon, 1, 2), list) // Gaia Pylon
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.MECHANISM, 4).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(wildStack(pistonRelay), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MOTION, 2).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(distributor), list)
		
		list = AspectList().a(Aspect.MAGIC, 2).a(Aspect.ENTROPY, 2).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(manaBeacon), list)
		
		list = AspectList().a(Aspect.VOID, 6).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(manaVoid), list)
		
		list = AspectList().a(Aspect.MECHANISM, 6).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(manaDetector), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.CRAFT, 4).a(Aspect.SENSES, 2)
		ThaumcraftApi.registerObjectTag(wildStack(enchanter), list)
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.MECHANISM, 4)
		ThaumcraftApi.registerObjectTag(wildStack(turntable), list)
		
		list = AspectList().a(Aspect.EARTH, 64).a(Aspect.HUNGER, 12).a(Aspect.MAGIC, 2) // It's REALLY heavy.
		ThaumcraftApi.registerObjectTag(wildStack(ModBlocks.tinyPlanet), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.EXCHANGE, 8)
		ThaumcraftApi.registerObjectTag(wildStack(alchemyCatalyst), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(wildStack(openCrate), list)
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.MECHANISM, 2)
		ThaumcraftApi.registerObjectTag(wildStack(forestEye), list)
		
		list = AspectList().add(Aspect.HARVEST, 12)
		ThaumcraftApi.registerObjectTag(wildStack(forestDrum), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(COLOR).a(Aspect.LIGHT)
		ThaumcraftApi.registerObjectTag(wildStack(shinyFlower), list)
		
		list = AspectList().a(Aspect.TREE, 2).a(Aspect.SOUL, 2).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(ItemStack(platform, 1, 0), list)
		ThaumcraftApi.registerObjectTag(ItemStack(platform, 1, 1), list)
		// Nothing for the Infrangible Platform.
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.MECHANISM, 4).a(Aspect.TRAVEL, 4)
		ThaumcraftApi.registerObjectTag(wildStack(alfPortal), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(wildStack(dreamwood), list)
		
		list = AspectList().a(Aspect.MAGIC, 8).a(Aspect.ORDER, 8).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(wildStack(conjurationCatalyst), list)
		
		list = AspectList().a(COLOR, 8)
		ThaumcraftApi.registerObjectTag(wildStack(bifrost), list)
		
		list = AspectList().a(Aspect.PLANT, 1)
		ThaumcraftApi.registerObjectTag(wildStack(solidVines), list)
		
		list = AspectList().a(Aspect.PLANT, 1).a(COLOR, 1)
		ThaumcraftApi.registerObjectTag(wildStack(buriedPetals), list)
		
		list = AspectList().a(Aspect.WATER, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(wildStack(prismarine), list)
		
		list = AspectList().a(Aspect.WATER, 2).a(Aspect.CRYSTAL, 2).a(Aspect.LIGHT, 2)
		ThaumcraftApi.registerObjectTag(wildStack(seaLamp), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(Aspect.LIGHT).a(Aspect.AIR).a(Aspect.EARTH)
		ThaumcraftApi.registerObjectTag(wildStack(floatingFlower), list)
		
		list = AspectList().a(Aspect.LIFE, 2).a(Aspect.CROP, 2)
		ThaumcraftApi.registerObjectTag(wildStack(tinyPotato), list)
		
		list = AspectList().a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(wildStack(spawnerClaw), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(NETHER)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 0), list) // Hellish Brick
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.SOUL)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 1), list) // Soul Brick
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.COLD)
		ThaumcraftApi.registerObjectTag(ItemStack(customBrick, 1, 2), list) // Frosty Brick
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.MECHANISM, 2).a(ENVY, 2)
		ThaumcraftApi.registerObjectTag(wildStack(enderEye), list)
		
		list = AspectList().a(Aspect.LIGHT, 8).a(Aspect.METAL, 4)
		ThaumcraftApi.registerObjectTag(wildStack(starfield), list)
		
		list = AspectList().a(Aspect.MECHANISM, 16).a(Aspect.ENERGY, 8).a(SLOTH, 8).a(GLUTTONY, 4) // If you can't tell, I don't like RF.
		ThaumcraftApi.registerObjectTag(wildStack(rfGenerator), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.ELDRITCH)
		ThaumcraftApi.registerObjectTag(wildStack(elfGlass), list)
		
		list = AspectList(ItemStack(Blocks.brewing_stand)).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(brewery), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(wildStack(manaGlass), list)
		
		list = AspectList().a(Aspect.CRAFT, 4).a(Aspect.FIRE, 2).a(Aspect.WATER, 2).a(Aspect.EARTH, 2).a(Aspect.AIR, 2).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(terraPlate), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(wildStack(redStringContainer), list)
		ThaumcraftApi.registerObjectTag(wildStack(redStringDispenser), list)
		ThaumcraftApi.registerObjectTag(wildStack(redStringFertilizer), list)
		ThaumcraftApi.registerObjectTag(wildStack(redStringComparator), list)
		ThaumcraftApi.registerObjectTag(wildStack(redStringRelay), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.MAGIC, 2).a(Aspect.LIFE).a(COLOR).a(Aspect.LIGHT).a(Aspect.AIR).a(Aspect.EARTH) // So many aspects
		ThaumcraftApi.registerObjectTag(wildStack(floatingSpecialFlower), list)
		
		list = AspectList().a(Aspect.LIGHT, 2).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(manaFlame), list)
		
		list = AspectList().a(Aspect.SOUL, 2).a(Aspect.CRYSTAL, 2)
		ThaumcraftApi.registerObjectTag(wildStack(prism), list)
		
		list = AspectList(ItemStack(Blocks.dirt))
		ThaumcraftApi.registerObjectTag(wildStack(dirtPath), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(wildStack(enchantedSoil), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(wildStack(corporeaIndex), list)
		ThaumcraftApi.registerObjectTag(wildStack(corporeaFunnel), list)
		
		list = AspectList(ItemStack(Blocks.brown_mushroom)).a(COLOR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(mushroom), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.MOTION, 2).a(Aspect.MAGIC, 2).a(Aspect.WATER, 2).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(wildStack(pump), list)
		
		list = AspectList().a(Aspect.PLANT, 2).a(Aspect.LIFE).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(doubleFlower1), list)
		ThaumcraftApi.registerObjectTag(wildStack(doubleFlower2), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2)
		ThaumcraftApi.registerObjectTag(wildStack(corporeaInterceptor), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2).a(Aspect.SENSES)
		ThaumcraftApi.registerObjectTag(wildStack(corporeaCrystalCube), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.FIRE, 2)
		ThaumcraftApi.registerObjectTag(wildStack(incensePlate), list)
		
		list = AspectList(ItemStack(Items.clock)).a(Aspect.CRYSTAL).a(Aspect.MECHANISM)
		ThaumcraftApi.registerObjectTag(wildStack(hourglass), list)
		
		list = AspectList(ItemStack(Blocks.rail)).a(Aspect.SOUL)
		ThaumcraftApi.registerObjectTag(wildStack(ghostRail), list)
		
		list = AspectList().a(Aspect.MECHANISM, 3).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(wildStack(sparkChanger), list)
		
		list = AspectList().a(Aspect.TREE, 4).a(Aspect.LIFE)
		ThaumcraftApi.registerObjectTag(wildStack(root), list)
		
		list = AspectList(ItemStack(Blocks.pumpkin)).a(Aspect.DARKNESS, 2)
		ThaumcraftApi.registerObjectTag(wildStack(felPumpkin), list)
		
		list = AspectList().a(Aspect.LIFE, 4).a(Aspect.BEAST, 8)
		ThaumcraftApi.registerObjectTag(wildStack(cocoon), list)
		
		list = AspectList().a(Aspect.TRAVEL, 3).a(Aspect.MOTION, 2).a(Aspect.LIGHT, 2)
		ThaumcraftApi.registerObjectTag(wildStack(lightRelay), list)
		
		list = AspectList().a(Aspect.TRAVEL, 3).a(Aspect.MECHANISM, 2)
		ThaumcraftApi.registerObjectTag(wildStack(lightLauncher), list)
		
		list = AspectList().a(Aspect.ENTROPY, 16).a(WRATH, 8).a(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(manaBomb), list)
		
		list = AspectList(ItemStack(Blocks.noteblock)).a(Aspect.GREED, 2).a(Aspect.AIR, 3)
		ThaumcraftApi.registerObjectTag(wildStack(ModBlocks.cacophonium), list)
		
		list = AspectList().a(Aspect.MOTION, 2).a(Aspect.AIR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(bellows), list)
		
		list = AspectList().a(COLOR, 8)
		ThaumcraftApi.registerObjectTag(ItemStack(bifrostPerm), list)
		
		list = AspectList().a(Aspect.PLANT, 4).a(Aspect.LIFE, 4)
		ThaumcraftApi.registerObjectTag(wildStack(cellBlock), list)
		
		list = AspectList().a(Aspect.MECHANISM, 4).a(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(wildStack(redStringInterceptor), list)
		
		list = AspectList(ItemStack(Items.skull, 1, 3)).a(Aspect.ELDRITCH, 4).a(Aspect.EARTH, 4)
		ThaumcraftApi.registerObjectTag(wildStack(ModBlocks.gaiaHead), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 4).a(Aspect.VOID, 2).a(Aspect.MIND, 2)
		ThaumcraftApi.registerObjectTag(wildStack(corporeaRetainer), list)
		
		list = AspectList().a(Aspect.WEATHER, 2)
		ThaumcraftApi.registerObjectTag(wildStack(teruTeruBozu), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(shimmerrock), list)
		
		list = AspectList().a(Aspect.TREE).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(shimmerwoodPlanks), list)
		
		list = AspectList().a(Aspect.MAN, 4).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(avatar), list)
		
		list = AspectList().a(Aspect.EARTH).a(Aspect.PLANT).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(altGrass), list)
		
		list = AspectList(ItemStack(Blocks.quartz_block))
		if (darkQuartz != null)
			ThaumcraftApi.registerObjectTag(wildStack(darkQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(manaQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(blazeQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(lavenderQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(redQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(elfQuartz), list)
		ThaumcraftApi.registerObjectTag(wildStack(sunnyQuartz), list)
		
		list = AspectList().a(Aspect.EARTH, 2)
		ThaumcraftApi.registerObjectTag(wildStack(biomeStoneA), list)
		ThaumcraftApi.registerObjectTag(wildStack(biomeStoneB), list)
		ThaumcraftApi.registerObjectTag(wildStack(stone), list)
		
		list = AspectList().a(Aspect.EARTH, 2).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(pavement), list)
		
		/////// ITEMS!
		
		list = AspectList(ItemStack(Items.book)).a(Aspect.PLANT)
		ThaumcraftApi.registerObjectTag(wildStack(lexicon), list)
		
		list = AspectList().a(Aspect.PLANT).a(COLOR)
		ThaumcraftApi.registerObjectTag(wildStack(petal), list)
		ThaumcraftApi.registerObjectTag(wildStack(dye), list)
		
		list = AspectList(ItemStack(twigWand)).a(Aspect.TOOL, 3)
		ThaumcraftApi.registerObjectTag(wildStack(twigWand), list)
		
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
		ThaumcraftApi.registerObjectTag(wildStack(manaTablet), list)
		
		list = AspectList().a(Aspect.HUNGER, 64).a(Aspect.CROP, 64) // why did i do this
		ThaumcraftApi.registerObjectTag(wildStack(manaCookie), list)
		
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
		ThaumcraftApi.registerObjectTag(wildStack(manaMirror), list)
		
		list = AspectList(ItemStack(manasteelHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(wildStack(manasteelHelmRevealing), list)
		
		list = AspectList(ItemStack(terrasteelHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(wildStack(terrasteelHelmRevealing), list)
		
		list = AspectList().a(Aspect.EARTH, 12).a(Aspect.HUNGER, 12).a(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(ModItems.tinyPlanet), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8).a(Aspect.METAL, 8)
		ThaumcraftApi.registerObjectTag(wildStack(manaRing), list)
		
		list = AspectList().a(Aspect.VOID, 2).a(Aspect.MAGIC, 8).a(Aspect.METAL, 8)
		ThaumcraftApi.registerObjectTag(wildStack(manaRingGreater), list)
		
		list = AspectList().a(Aspect.CLOTH, 4).a(Aspect.TRAVEL, 8)
		ThaumcraftApi.registerObjectTag(wildStack(travelBelt), list)
		
		list = AspectList(ItemStack(Items.quartz))
		ThaumcraftApi.registerObjectTag(wildStack(quartz), list)
		
		list = AspectList(ItemStack(elementiumHelm)).a(Aspect.SENSES, 4)
		ThaumcraftApi.registerObjectTag(wildStack(elementiumHelmRevealing), list)
		
		list = AspectList().a(Aspect.METAL, 4).a(Aspect.VOID, 8)
		ThaumcraftApi.registerObjectTag(wildStack(openBucket), list)
		
		list = AspectList().a(Aspect.LIFE, 16).a(Aspect.MAGIC, 8).a(Aspect.TRAVEL, 4)
		ThaumcraftApi.registerObjectTag(wildStack(spawnerMover), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.ENTROPY, 8).a(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(manaBottle), list)
		
		list = AspectList().a(Aspect.METAL, 15).a(Aspect.GREED, 3).a(Aspect.SENSES, 3)
		ThaumcraftApi.registerObjectTag(wildStack(itemFinder), list)
		
		list = AspectList().a(Aspect.FLIGHT).a(Aspect.MAGIC)
		ThaumcraftApi.registerObjectTag(wildStack(manaInkwell), list)
		
		list = AspectList().a(Aspect.CRYSTAL)
		ThaumcraftApi.registerObjectTag(wildStack(vial), list)
		ThaumcraftApi.registerObjectTag(wildStack(brewVial), list)
		
		list = AspectList().a(Aspect.CRYSTAL).a(Aspect.MAGIC, 8)
		ThaumcraftApi.registerObjectTag(wildStack(brewFlask), list)
		
		list = AspectList().a(Aspect.CRAFT, 4).a(Aspect.ELDRITCH, 4)
		ThaumcraftApi.registerObjectTag(wildStack(craftingHalo), list)
		
		list = AspectList().a(Aspect.MAGIC, 16).a(Aspect.ELDRITCH, 4).a(Aspect.ORDER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(blackLotus, 1, 0), list)
		
		list = AspectList().a(Aspect.MAGIC, 20).a(Aspect.ELDRITCH, 8).a(Aspect.ORDER, 6)
		ThaumcraftApi.registerObjectTag(ItemStack(blackLotus, 1, 1), list)
		
		list = AspectList().a(Aspect.SENSES, 8).a(Aspect.CRYSTAL, 4)
		ThaumcraftApi.registerObjectTag(wildStack(monocle), list)
		
		list = AspectList().a(Aspect.TRAVEL, 16)
		ThaumcraftApi.registerObjectTag(wildStack(worldSeed), list)
		
		list = AspectList().a(Aspect.PLANT, 8).a(Aspect.METAL, 4).a(Aspect.WEAPON, 4).a(WRATH, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(thornChakram, 1, 0), list)
		
		list = AspectList().a(Aspect.PLANT, 8).a(Aspect.METAL, 4).a(Aspect.WEAPON, 4).a(WRATH, 4).a(Aspect.FIRE, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(thornChakram, 1, 1), list)
		
		list = AspectList().a(Aspect.LIFE, 32).a(Aspect.MAGIC, 16)
		ThaumcraftApi.registerObjectTag(wildStack(overgrowthSeed), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 64).a(Aspect.MIND, 32).a(Aspect.SOUL, 32)
		ThaumcraftApi.registerObjectTag(wildStack(ancientWill), list)
		
		list = AspectList().a(Aspect.VOID)
		ThaumcraftApi.registerObjectTag(wildStack(phantomInk), list)
		
		list = AspectList().a(COLOR, 32).a(Aspect.LIFE, 16).a(Aspect.HEAL, 16)
		ThaumcraftApi.registerObjectTag(wildStack(pinkinator), list)
		
		list = AspectList().a(Aspect.VOID, 64)
		ThaumcraftApi.registerObjectTag(wildStack(blackHoleTalisman), list)
		
		list = AspectList().a(Aspect.SENSES, 4).a(Aspect.AIR, 4).a(Aspect.GREED, 4).a(COLOR, 2).a(PRIDE, 4)
		ThaumcraftApi.registerObjectTag(wildStack(recordGaia1), list)
		ThaumcraftApi.registerObjectTag(wildStack(recordGaia2), list)
		
		list = AspectList(ItemStack(terraAxe)).a(Aspect.TOOL, 3)
		ThaumcraftApi.registerObjectTag(wildStack(terraAxe), list)
		
		list = AspectList().a(Aspect.TREE).a(Aspect.WATER)
		ThaumcraftApi.registerObjectTag(wildStack(waterBowl), list)
		
		list = AspectList().a(Aspect.ELDRITCH, 16).a(Aspect.WEAPON, 4)
		ThaumcraftApi.registerObjectTag(wildStack(starSword), list)
		
		ThaumcraftApi.registerObjectTag(wildStack(exchangeRod), list)
		
		list = AspectList().a(Aspect.WEATHER, 16).a(Aspect.WEAPON, 4)
		ThaumcraftApi.registerObjectTag(wildStack(thunderSword), list)
		
		list = AspectList(ItemStack(ModBlocks.gaiaHead))
		ThaumcraftApi.registerObjectTag(wildStack(ModItems.gaiaHead), list)
		
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
	
	fun wildStack(i: Block): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	fun wildStack(i: Item): ItemStack = ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)
	
	fun forMeta(cap: Int, lambda: (Int) -> Unit) {
		for (i in 0..cap) {
			lambda.invoke(i)
		}
	}
	
	var COLOR: Aspect? = null
	
	fun initAspects() {
		if (AlfheimConfigHandler.addAspectsToBotania)
			BotaniaTCAspects.initAspects()
		
		if (AlfheimConfigHandler.addTincturemAspect)
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
		if (AlfheimConfigHandler.addAspectsToBotania)
			BotaniaTCAspects.addAspects()
		if (AlfheimConfigHandler.addTincturemAspect)
			overrideVanillaAspects()
		
		val NETHER: Aspect? = getAspect("ForbiddenMagic", "infernus")
		val PRIDE: Aspect? = getAspect("ForbiddenMagic", "superbia")
		val WRATH: Aspect? = getAspect("ForbiddenMagic", "ira")
		val SLOTH: Aspect? = getAspect("ForbiddenMagic", "desidia")
		val forbidden = Loader.isModLoaded("ForbiddenMagic")
		val hellAspect = if (forbidden) NETHER else Aspect.FIRE
		val colorAspect = if (AlfheimConfigHandler.addTincturemAspect) COLOR else Aspect.SENSES
		
		val splinterlist = AspectList().add(Aspect.TREE, 1).add(Aspect.ENTROPY, 1)
		
		var list = AspectList().add(Aspect.EARTH, 2).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.irisDirt), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.rainbowDirt), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisSapling), list)
		
		list = AspectList().add(Aspect.PLANT, 1).add(Aspect.AIR, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.irisGrass), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.rainbowGrass), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.irisTallGrass0), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.irisTallGrass1), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.rainbowTallGrass), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(colorAspect, 1)
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisWood0, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisWood1, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisWood2, 1, it), list) }
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisWood3, 1, it), list) }
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.rainbowWood), list)
		forMeta(4) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.altWood0, 1, it), list) }
		forMeta(2) { ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.altWood1, 1, it), list) }
		
		list = AspectList().add(Aspect.TREE, 1).add(Aspect.METAL, 1)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.itemDisplay), list)
		
		list = AspectList().add(Aspect.TREE, 8).add(Aspect.MAGIC, 8).add(colorAspect, 2).add(Aspect.CRAFT, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.treeCrafterBlock), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.treeCrafterBlockRB), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(Aspect.WEATHER, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.lightningSapling), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(Aspect.WEATHER, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.lightningWood), list)
		
		list = AspectList().add(Aspect.TREE, 20).add(Aspect.MECHANISM, 1).add(Aspect.EXCHANGE, 1).add(Aspect.VOID, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.livingwoodFunnel), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(hellAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.netherSapling), list)
		list = AspectList().add(Aspect.TREE, 4).add(hellAspect, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.netherWood), list)
		
		list = AspectList().add(Aspect.CLOTH, 4).add(Aspect.FIRE, 2).add(Aspect.MAGIC, 2)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.kindling), list)
		
		list = AspectList().add(Aspect.PLANT, 1).add(Aspect.EXCHANGE, 1).add(colorAspect, 1)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.irisSeeds), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.EARTH, 4).add(Aspect.AIR, 2).add(colorAspect, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.rodColorfulSkyDirt), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.LIGHT, 6).add(colorAspect, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.rodPrismatic), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.WEATHER, 8).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.rodLightning), list)
		
		list = if (forbidden) AspectList().add(Aspect.TOOL, 8).add(Aspect.AIR, 4).add(Aspect.WEATHER, 2).add(PRIDE, 2).add(Aspect.MAGIC, 4)
		else AspectList().add(Aspect.TOOL, 8).add(Aspect.AIR, 6).add(Aspect.WEATHER, 2).add(Aspect.MAGIC, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.rodInterdiction), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.WEATHER, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.emblem, 1, 0), list)
		list = AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.EARTH, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.emblem, 1, 1), list)
		list = if (forbidden) AspectList().add(Aspect.ELDRITCH, 5).add(PRIDE, 16)
		else AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.AIR, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.emblem, 1, 2), list)
		list = AspectList().add(Aspect.ELDRITCH, 5).add(hellAspect, 16)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.emblem, 1, 3), list)
		
		list = AspectList().add(Aspect.CLOTH, 4).add(colorAspect, 2)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.coatOfArms), list)
		
		list = AspectList().add(colorAspect, 8).add(Aspect.METAL, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.colorOverride), list)
		
		list = AspectList().add(Aspect.CLOTH, 2).add(Aspect.GREED, 2).add(Aspect.ELDRITCH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.attributionBauble, 1, 0), list)
		
		list = AspectList().add(Aspect.CLOTH, 2).add(Aspect.GREED, 2).add(Aspect.ELDRITCH, 2).add(Aspect.CROP, 64).add(Aspect.BEAST, 64) // memes
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.attributionBauble, 1, 1), list)
		
		list = AspectList().add(Aspect.MAGIC, 16).add(Aspect.TAINT, 4).add(Aspect.ENTROPY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.wiltedLotus, 1, 0), list)
		
		list = AspectList().add(Aspect.MAGIC, 20).add(Aspect.DEATH, 4).add(Aspect.ELDRITCH, 4).add(Aspect.TAINT, 4).add(Aspect.ENTROPY, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.wiltedLotus, 1, 1), list)
		
		list = AspectList().add(Aspect.TOOL, 2).add(Aspect.TREE, 2).add(Aspect.WEATHER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.ThunderwoodTwig), list)
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.ThunderwoodSplinters), splinterlist)
		
		list = AspectList().add(Aspect.TOOL, 2).add(Aspect.TREE, 2).add(hellAspect, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodTwig), list)
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodSplinters), splinterlist)
		
		list = AspectList().add(Aspect.FIRE, 4).add(Aspect.ENERGY, 2)
		if (forbidden) list.add(NETHER, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal), list)
		
		list = AspectList().add(Aspect.TOOL, 3).add(Aspect.VOID, 12).add(Aspect.CLOTH, 4).add(Aspect.GREED, 2)
		if (forbidden) list.add(SLOTH, 2)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.toolbelt), list)
		
		list = AspectList(ItemStack(manaFlame))
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.rainbowFlame), list)
		
		list = AspectList(ItemStack(manaFlame)).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.invisibleFlame), list)
		
		list = AspectList(ItemStack(lens, 1, 17)).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.invisibleFlameLens), list)
		
		list = AspectList().add(Aspect.LIGHT, 4).add(Aspect.MECHANISM, 2).add(colorAspect, 4)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.irisLamp), list)
		
		list = AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.sealingWood), list)
		
		list = AspectList().add(Aspect.PLANT, 2).add(Aspect.TREE, 1).add(Aspect.VOID, 1)
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimBlocks.sealingSapling), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 2).add(Aspect.LIGHT, 2).add(COLOR, 2)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.starPlacer), list)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.starBlock), list)
		
		list = AspectList().add(Aspect.TOOL, 8).add(Aspect.FIRE, 4).add(hellAspect, 4)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.rodFlameStar), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 9).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 10).add(Aspect.ENTROPY, 5)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.wireAxe), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 10).add(Aspect.WEAPON, 1).add(Aspect.ORDER, 5)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.trisDagger), list)
		
		list = AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 10).add(Aspect.WEAPON, 1).add(Aspect.ORDER, 5)
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimItems.trisDagger), list)
		
		list = AspectList(ItemStack(Blocks.quartz_block))
		ThaumcraftApi.registerObjectTag(wildStack(AlfheimBlocks.shimmerQuartz), list)
		
		list = AspectList(ItemStack(Items.quartz))
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowQuartz), list)
		
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
			replaceAspect(wildStack(Blocks.stained_hardened_clay), Aspect.SENSES, COLOR!!)
			replaceAspect(wildStack(Blocks.red_flower), Aspect.SENSES, COLOR!!)
			replaceAspect(wildStack(Blocks.yellow_flower), Aspect.SENSES, COLOR!!)
			
			for (i in LibOreDict.DYES)
				replaceAspect(i, Aspect.SENSES, COLOR!!)
			
			replaceAspect("oreLapis", Aspect.SENSES, COLOR!!)
		}
	}
}